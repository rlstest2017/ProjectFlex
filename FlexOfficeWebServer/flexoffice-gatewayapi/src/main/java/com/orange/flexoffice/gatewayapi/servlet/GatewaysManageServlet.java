package com.orange.flexoffice.gatewayapi.servlet;

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

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.orange.flexoffice.business.dto.Message;
import com.orange.flexoffice.business.handler.MessageHandler;
import com.orange.flexoffice.business.facade.GatewaysManageFacade;
import com.orange.flexoffice.gatewayapi.enums.MethodsCalledByHmi;
import com.orange.flexoffice.gatewayapi.handler.JsonResponseHandler;

/**
 * Servlet implementation class GatewaysManageServlet
 * @author oab
 */
public class GatewaysManageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int GOODRETURN = 0;

    private static final int BADRETURN = -1;

    /**
     * The logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger( GatewaysManageServlet.class );

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
     * GatewaysManageFacade
     *                  gatewaysManageFacade parameter
     */
    private transient GatewaysManageFacade gatewaysManageFacade;

    /**
     * Messages to Log in MessageHandler
     */
    private List<Message> messageLogList = new ArrayList<Message>();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GatewaysManageServlet() {
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
        this.gatewaysManageFacade = (GatewaysManageFacade) springContext.getBean("gatewaysManageFacade");
    }

    /**
     * @see Servlet#destroy()
     */
    public void destroy() {

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding( "utf8" );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "utf8" );

        final String method = request.getParameter( "method" );
        String ipAdress = request.getRemoteAddr();

        LOGGER.info( "Begin call doGet method for GatewaysManageServlet at: " + new Date() );


        MethodsCalledByHmi methodsCalledByHmiObject = MethodsCalledByHmi.fromValue(method);

        switch(methodsCalledByHmiObject.code()) {

        case 81 ://get....
             break;
                    
        default:   

        }

        LOGGER.info( "End call doGet method for GatewaysManageServlet at: " + new Date() );
    }

}
