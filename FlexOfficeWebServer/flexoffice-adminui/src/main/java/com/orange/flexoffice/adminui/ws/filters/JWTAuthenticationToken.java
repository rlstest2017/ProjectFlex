package com.orange.flexoffice.adminui.ws.filters;

import java.text.ParseException;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.nimbusds.jwt.JWTParser;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {
	
    private static final long serialVersionUID = 1L;
    //private final Object principal;
    private Object principal;
    private Object details;
 
    Collection  authorities;
    
    public JWTAuthenticationToken( String jwtToken) {
        super(null);
        super.setAuthenticated(true); // must use super, as we override
    
        // JWTParser parser = new JWTParser(jwtToken);
        // this.principal=parser.getSub();
    	try {
			this.principal=JWTParser.parse(jwtToken);
			this.setDetailsAuthorities();
	
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
    private void setDetailsAuthorities() {
        String username = principal.toString();
        //SpringUserDetailsAdapter adapter = new SpringUserDetailsAdapter(username);
        //details=adapter;
        //authorities=(Collection) adapter.getAuthorities();
         
    }
 
    @Override
    public Collection getAuthorities() {
        return authorities;
    }
}