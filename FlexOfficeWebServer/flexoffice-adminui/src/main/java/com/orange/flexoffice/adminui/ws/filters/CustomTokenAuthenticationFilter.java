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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.orange.flexoffice.adminui.ws.auth.AuthenticationToken;
import com.orange.flexoffice.adminui.ws.auth.NoOpAuthenticationManager;
import com.orange.flexoffice.adminui.ws.auth.TokenSimpleUrlAuthenticationSuccessHandler;

public class CustomTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
     
    static final String ORIGIN = "Origin";
    @Autowired
    AuthenticationToken authToken;
    
    public CustomTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(new NoOpAuthenticationManager());
        setAuthenticationSuccessHandler(new TokenSimpleUrlAuthenticationSuccessHandler());
    }
   
    public static final String HEADER_SECURITY_TOKEN = "x-auth-token";
 
 
    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers 
     */
    @SuppressWarnings("static-access")
	@Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    	if (request.getHeader(ORIGIN) != null) {
//            String origin = request.getHeader(ORIGIN);
//            response.addHeader("Access-Control-Allow-Origin", origin);
//            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//            response.addHeader("Access-Control-Allow-Credentials", "true");
              response.addHeader("Access-Control-Allow-Headers",
                    request.getHeader("Access-Control-Request-Headers"));
        }
    	if ("OPTIONS".equals(request.getMethod())) {
            response.getWriter().print("OK");
            response.getWriter().flush();
            response.setStatus(response.SC_OK);
    	}   
    	
    	String token = request.getHeader(HEADER_SECURITY_TOKEN);
        logger.debug("token found:"+token);
        AbstractAuthenticationToken userAuthenticationToken = authUserByToken(token);
        if(userAuthenticationToken == null) { 
        	throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "Bad Token"));
        }	
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
        Boolean isValidToken = authToken.checkToken(token);
        if (isValidToken) {
        	return authToken;
        } else {
        	return null;
        }
    }
 
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }


	
 
}

