package com.orange.meetingroom.gui.ws.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;


/**
 * AuthenticationToken
 * @author oab
 *
 */
public class AuthenticationToken extends AbstractAuthenticationToken {
	
    private static final long serialVersionUID = 1L;
    private Object principal;
    
    public AuthenticationToken() {
        super(null);
        super.setAuthenticated(true);
    }
    
    public Boolean checkToken(String token) {
    	return true;
    }
 
    @Override
    public Object getCredentials() {
        return "";
    }
 
    @Override
    public Object getPrincipal() {
        return principal;
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Collection getAuthorities() {
    	return null;
    }
}