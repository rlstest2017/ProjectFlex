package com.orange.flexoffice.adminui.ws.endPoint.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger LOGGER = Logger.getLogger(RestAuthenticationEntryPoint.class);
    
	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException ) throws IOException, ServletException {
        String contentType = request.getContentType();
        LOGGER.info(contentType);
        response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
    }
 
}