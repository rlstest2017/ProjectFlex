package com.orange.flexoffice.adminui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orange.flexoffice.business.dto.Message;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.orange.flexoffice.business.handler.MessageHandler;
import com.orange.flexoffice.business.facade.SystemManageFacade;
import com.orange.flexoffice.adminui.enums.MethodsCalledByHmi;
import com.orange.flexoffice.adminui.handler.JsonResponseHandler;

/**
 * Servlet implementation class SystemManageServlet
 * @author oab
 */
public class SystemManageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_ZERO_VALUE = "0";

    /**
     * WebApplicationContext 
     *                  springContext parameter
     */
    private transient WebApplicationContext springContext;

    /**
     * JsonResponseHandler
     *                  responseHandler parameter
     */
    private transient JsonResponseHandler responseHandler;

    /**
     * MessageHandler
     *                  messageHandler parameter
     */
    private transient MessageHandler messageHandler;

    /**
     * SystemManageFacade
     *                  systemManageFacade parameter
     */
    private transient SystemManageFacade systemManageFacade;

    /**
     * Messages to Log in MessageHandler
     */
    private List<Message> messageLogList = new ArrayList<Message>();

    /**
     * The logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger( SystemManageServlet.class );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SystemManageServlet() {
        super();
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.springContext = WebApplicationContextUtils.getWebApplicationContext( this.getServletContext() );
        this.responseHandler = (JsonResponseHandler) springContext.getBean("responseHandler");
        this.messageHandler = (MessageHandler) springContext.getBean("messageHandler");
        this.systemManageFacade = (SystemManageFacade) springContext.getBean("systemManageFacade");
    }

    /**
     * @see Servlet#destroy()
     */
    public void destroy() {
        messageLogList.clear();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding( "utf8" );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "utf8" );

        String pathInfo = request.getPathInfo();
        
        LOGGER.info( "Begin call doGet method for SystemManageServlet at: " + new Date() );

        if (pathInfo == null) pathInfo = "/system";  
        	
        MethodsCalledByHmi methodsCalledByHmiObject = MethodsCalledByHmi.fromValue(pathInfo);

        switch(methodsCalledByHmiObject.code()) {

        case 0: // /v1/system
        	LOGGER.debug( " request is /v1/system ");
            break;
            
        case 1: // /v1/system/login
        	LOGGER.debug( " request is /v1/system/login ");
            break;
            
        case 2: // /v1/system/logout
        	LOGGER.debug( " request is /v1/system/logout ");
            break;
            
        case 3: // /v1/system/teachin
        	LOGGER.debug( " request is /v1/system/teachin ");
            break;
        
        default:

        }

        LOGGER.info( "End call doGet method for SystemManageServlet at: " + new Date() );

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding( "utf8" );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "utf8" );

        final String pathInfo = request.getPathInfo();
        

        LOGGER.info( "Begin call doPost method for SystemManageServlet at: " + new Date() );


        MethodsCalledByHmi methodsCalledByHmiObject = MethodsCalledByHmi.fromValue(pathInfo);

        switch(methodsCalledByHmiObject.code()) {

        case 4: // /v1/system/teachin/init?roomId= 
        	
        	String roomId = request.getParameter("roomId");
        	
        	LOGGER.debug( " request is /v1/system/teachin/init?roomId=" + roomId);
            break;
        
        case 5: // /v1/system/teachin/submit
        	LOGGER.debug( " request is /v1/system/teachin/submit ");
            break;
        
        case 6: // /v1/system/teachin/cancel
        	LOGGER.debug( " request is /v1/system/teachin/cancel ");
            break;
            
        default:

        }

        LOGGER.info( "End call doPost method for SystemManageServlet at: " + new Date() );

    }
       

}
