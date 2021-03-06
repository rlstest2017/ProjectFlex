package com.orange.flexoffice.userui.ws.auth;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.orange.flexoffice.business.common.service.data.SystemManager;

public class AuthenticationToken extends AbstractAuthenticationToken {
	
    private static final long serialVersionUID = 1L;
    private Object principal;
    @Autowired
    SystemManager systemManager; 
    
    public AuthenticationToken() {
        super(null);
        super.setAuthenticated(true);
    }
    
    public Boolean checkToken(String token) {
    	return systemManager.checkToken(token);
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