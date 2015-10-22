package com.orange.flexoffice.business.handler;

import java.util.List;
import org.apache.log4j.Logger;
import com.orange.flexoffice.business.dto.Message;

/**
 * MessageHandler
 * @author oab
 *
 */
public class MessageHandler {
    
    /**
     * 
     * @param parameters List<Message>
     * @param LOGGER
     * @param mode GET or POST
     */
    public void handleMessage(List<Message> parameters, Logger logManager, String mode ) {
        
        synchronized(this) {
            final StringBuffer message= new StringBuffer();
            message.append( mode ); 
            message.append( " request with parameters : \n" );
            
            for (Message mess : parameters) {
                message.append( mess.getName());
                message.append(" : ");
                message.append(mess.getValue());
                message.append(" \n ");
            }
            
            logManager.info( message );
        
        }
    }
}
