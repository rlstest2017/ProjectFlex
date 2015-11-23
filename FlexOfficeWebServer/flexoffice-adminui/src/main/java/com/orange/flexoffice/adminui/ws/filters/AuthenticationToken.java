package com.orange.flexoffice.adminui.ws.filters;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.orange.flexoffice.business.common.service.data.SystemManager;
import com.orange.flexoffice.business.common.service.data.impl.SystemManagerImpl;

public class AuthenticationToken extends AbstractAuthenticationToken {
	
    private static final long serialVersionUID = 1L;
    private Object principal;
    
    public AuthenticationToken( String jwtToken) {
        super(null);
        super.setAuthenticated(true);
    }
    
    public Boolean checkToken(String token) {
    	SystemManager systemManager = new SystemManagerImpl();
        Boolean isValidToken = systemManager.checkToken(token);
        if (isValidToken) {
        	return true;
        } else {
        	return false;
        }
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