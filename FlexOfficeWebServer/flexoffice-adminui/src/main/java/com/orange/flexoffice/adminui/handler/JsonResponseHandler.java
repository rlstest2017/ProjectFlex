package com.orange.flexoffice.adminui.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.orange.flexoffice.adminui.dto.JSONStatusResponse;

/**
 * JsonResponseHandler
 * @author oab
 *
 */
public class JsonResponseHandler {

    /**
     * The logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger( JsonResponseHandler.class );
    
    /**
     * 
     * @param returnCode  int 
     * @param response
     */
    public void handleResponse(int returnCode, HttpServletResponse response) {
        
        JSONStatusResponse statusResponse = new JSONStatusResponse();
        
         // 1. initiate jackson mapper
            ObjectMapper mapper = new ObjectMapper();
            try {
                if (returnCode ==0) {
                    statusResponse.setStatus(true);
                } else {
                    statusResponse.setStatus(false);
                }
                
                mapper.writeValue(response.getOutputStream(), statusResponse);
                
            } catch (JsonGenerationException e) {
                LOGGER.error("catch exception JsonGenerationException in JsonResponseHandler::handleResponse method" + e.getMessage());
            } catch (JsonMappingException e) {
                LOGGER.error("catch exception JsonMappingException in JsonResponseHandler::handleResponse method" + e.getMessage());
            } catch (IOException e) {
                LOGGER.error("catch exception IOException in JsonResponseHandler::handleResponse method" + e.getMessage());
            }
    
    }
   
}
