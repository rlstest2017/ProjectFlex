package com.orange.flexoffice.adminui.ws.filters;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class CustomTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
     
    private static final Log logger = LogFactory.getLog(CustomTokenAuthenticationFilter.class);
    static final String ORIGIN = "Origin";
    		
    public CustomTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(new NoOpAuthenticationManager());
        setAuthenticationSuccessHandler(new TokenSimpleUrlAuthenticationSuccessHandler());
    }
   
    public final String HEADER_SECURITY_TOKEN = "x-auth-token";
 
 
    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers 
     */
    @Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    	if (request.getHeader(ORIGIN) != null) {
            String origin = request.getHeader(ORIGIN);
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Headers",
                    request.getHeader("Access-Control-Request-Headers"));
        }
    	if (request.getMethod() == "OPTIONS") {
             response.getWriter().print("OK");
             response.getWriter().flush();
         }
    	
    	String token = request.getHeader(HEADER_SECURITY_TOKEN);
        logger.info("token found:"+token);
        AbstractAuthenticationToken userAuthenticationToken = authUserByToken(token);
        if(userAuthenticationToken == null) throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "Bad Token"));
        return userAuthenticationToken;
    }
 
 
    /**
     * authenticate the user based on token
     * @return
     * @throws ParseException 
     */
    private AbstractAuthenticationToken authUserByToken(String token) {
        if(token==null) {
            return null;
        }
        AbstractAuthenticationToken authToken = new JWTAuthenticationToken(token);
        try {
            return authToken;
        } catch (Exception e) {
            logger.error("Authenticate user by token error: ", e);
        }
        return authToken;
    }
 
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }


	
 
}
