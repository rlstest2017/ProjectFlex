package com.orange.meetingroom.gui.ws.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * TokenSimpleUrlAuthenticationSuccessHandler
 * forward to origin request if success.
 * @author oab
 *
 */
public class TokenSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	 
    @Override
    protected String determineTargetUrl(HttpServletRequest request,
            HttpServletResponse response) {
        String context = request.getContextPath();
        String fullURL = request.getRequestURI();
        return fullURL.substring(fullURL.indexOf(context)+context.length());
    }
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String url = determineTargetUrl(request,response);
        request.getRequestDispatcher(url).forward(request, response);
    }
}    