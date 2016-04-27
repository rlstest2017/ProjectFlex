package com.orange.flexoffice.adminui.ws.endPoint.support;

import java.math.BigInteger;

import com.orange.flexoffice.adminui.ws.model.AgentInput;
import com.orange.flexoffice.adminui.ws.model.AgentInput2;
import com.orange.flexoffice.adminui.ws.model.DashboardInput;
import com.orange.flexoffice.adminui.ws.model.DashboardInput2;
import com.orange.flexoffice.adminui.ws.model.ECommandModel;
import com.orange.flexoffice.adminui.ws.model.GatewayInput;
import com.orange.flexoffice.adminui.ws.model.GatewayInput3;
import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.dao.common.model.data.UserDao;

public class ObjectFactory {

	private final com.orange.flexoffice.adminui.ws.model.ObjectFactory factory = new com.orange.flexoffice.adminui.ws.model.ObjectFactory();
	
    /**
     * Create an instance of {@link UserInput }
     * 
     */
    public UserInput createHmiUser(String firstName, String lastName, String email) {
    	UserInput user = factory.createUserInput();
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	
        return user;
    }
  
    /**
     * Create an instance of {@link UserDao }
     * 
     */
    public UserDao createUser(Long id, String firstName, String lastName, String email) {
    	UserDao user = new UserDao();
    	user.setId(id);
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	
        return user;
    }
    
    /**
     * Create an instance of {@link GatewayInput3 }
     * 
     */
    public GatewayInput3 createHmiGateway(String macAddress, String name, String desc) {
    	GatewayInput3 gateway = factory.createGatewayInput3();
    	gateway.setMacAddress(macAddress);
    	gateway.setName(name);
    	gateway.setDesc(desc);
    	
        return gateway;
    }
    
    /**
     * Create an instance of {@link GatewayInput3 }
     * 
     */
    public GatewayInput createHmi2Gateway(String name, String desc) {
    	GatewayInput gateway = factory.createGatewayInput();
    	gateway.setName(name);
    	gateway.setDesc(desc);
    	
        return gateway;
    }
    
    /**
     * Create an instance of {@link AgentInput }
     * 
     */
    public AgentInput createHmiAgent(String macAddress, String name, String desc) {
    	AgentInput agent = factory.createAgentInput();
    	agent.setMacAddress(macAddress);
    	agent.setName(name);
    	agent.setDesc(desc);
    	
        return agent;
    }
    
    /**
     * Create an instance of {@link AgentInput2 }
     * 
     */
    public AgentInput2 createHmi2Agent(String name, String desc, ECommandModel command) {
    	AgentInput2 agent = factory.createAgentInput2();
    	agent.setName(name);
    	agent.setDesc(desc);
    	agent.setCommand(command);
    	
        return agent;
    }
    
    /**
     * Create an instance of {@link DashboardInput }
     * 
     */
    public DashboardInput createHmiDashboard(String macAddress, String name, String desc, ECommandModel command, String cityId, String buildingId, BigInteger floor) {
    	DashboardInput dashboard = factory.createDashboardInput();
    	dashboard.setMacAddress(macAddress);
    	dashboard.setName(name);
    	dashboard.setDesc(desc);
    	dashboard.setCommand(command);
    	dashboard.setCityId(cityId);
    	dashboard.setBuildingId(buildingId);
    	dashboard.setFloor(floor);
        return dashboard;
    }
    
    /**
     * Create an instance of {@link DashboardInput2 }
     * 
     */
    public DashboardInput2 createHmi2Dashboard(String name, String desc, ECommandModel command, String cityId, String buildingId, BigInteger floor) {
    	DashboardInput2 dashboard = factory.createDashboardInput2();
    	dashboard.setName(name);
    	dashboard.setDesc(desc);
    	dashboard.setCommand(command);
    	dashboard.setCityId(cityId);
    	dashboard.setBuildingId(buildingId);
    	dashboard.setFloor(floor);
        return dashboard;
    }
}
