package com.orange.flexoffice.adminui.ws.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.orange.flexoffice.adminui.ws.model.ErrorModel;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;

/**
 * ErrorMessageHandler
 * @author oab
 *
 */
public class ErrorMessageHandler {
    
	private final ObjectFactory factory = new ObjectFactory();
	
	/** Create error message for exception
	 * 
	 * @param error
	 * @param status
	 * @return message
	 */
	public Response createErrorMessage(final EnumErrorModel error, Status status) {
		ErrorModel errorModel = factory.createErrorModel();
		errorModel.setCode(error.code());
		errorModel.setMessage(error.value());

		/**
		 * Les requêtes en erreurs (GET, DELETE, PUT, POST) ont un paramètre "Origin". 
		 * Donc, elles passent par le filtre CustomTokenAuthenticationFilter.java
		 * qui ajoute dans leurs réponses les paramètres :
		 * response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Credentials", "true");
              response.addHeader("Access-Control-Allow-Headers",
                    request.getHeader("Access-Control-Request-Headers"));
         *  Dans request.getHeader("Access-Control-Request-Headers") il n y a rien donc
         *  le paramètres "Access-Control-Allow-Headers" n'est pas ajouté
         *  C'est pour cela on l'ajoute ici ;)           
		 */
		return Response.ok(errorModel).status(status)
		        .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		        .build();
	}
	
}
