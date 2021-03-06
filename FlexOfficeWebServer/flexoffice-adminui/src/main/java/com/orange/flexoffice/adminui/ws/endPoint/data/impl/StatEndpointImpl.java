package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.flexoffice.adminui.ws.endPoint.data.StatEndpoint;
import com.orange.flexoffice.adminui.ws.model.MultiStat;
import com.orange.flexoffice.adminui.ws.model.MultiStatSet;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.SimpleStat;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.service.data.StatManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.object.MultiStatDto;
import com.orange.flexoffice.dao.common.model.object.MultiStatSetDto;
import com.orange.flexoffice.dao.common.model.object.SimpleStatDto;

/**
 * StatEndpointImpl
 * @author oab
 *
 */
public class StatEndpointImpl implements StatEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(StatEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private TestManager testManager;
	@Autowired
	private StatManager statManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	private static ClassPathXmlApplicationContext context;
	
	/**
	 * properties for get export file location
	 */
	private Properties properties;

    @Autowired
    @Qualifier("appProperties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
	@Override
	public List<SimpleStat> getPopularStats() {
		try {
		LOGGER.debug( "Begin call StatEndpoint.getPopularStats at: " + new Date() );
		List<SimpleStat> simpleStatsList = new ArrayList<SimpleStat>();

		List<SimpleStatDto> statsList = statManager.getPopularStats();
		for (SimpleStatDto simpleStatDto : statsList) {
			SimpleStat simpleStat = factory.createSimpleStat();
			simpleStat.setLabel(simpleStatDto.getRoomName());
			simpleStat.setValue(String .valueOf(simpleStatDto.getRate()));
			simpleStatsList.add(simpleStat);
		}
		
		LOGGER.debug( "End call StatEndpoint.getPopularStats at: " + new Date() );
		return simpleStatsList;
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in getPopularStats() StatEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public MultiStatSet getOccupancyStats(String from, String to, String viewtype) {
		try {
		LOGGER.debug( "Begin call StatEndpoint.getOccupancyStats at: " + new Date() );
		
		MultiStatSet set = factory.createMultiStatSet();
		
		MultiStatSetDto setDto = statManager.getOccupancyStats(from, to, viewtype);
		
		set.setStartdate(setDto.getStartdate());
		set.setEnddate(setDto.getEnddate());

		// List categories
		List<String> categoriesDto = setDto.getCategories();
		for (String cat : categoriesDto) {
			set.getCategories().add(cat);
		}
		// List multiStat
		List<MultiStatDto> multiStatDtoList = setDto.getData();
		for (MultiStatDto multiStatDto : multiStatDtoList) {
			//multiStat
			MultiStat mstat = factory.createMultiStat();
			mstat.setLabel(multiStatDto.getLabel());
			List<String> multiStatValues = multiStatDto.getValues();
			for (String value : multiStatValues) {
				mstat.getValues().add(value);
			}
			set.getData().add(mstat);
		}
		
		LOGGER.debug( "End call StatEndpoint.getOccupancyStats at: " + new Date() );
		
		return factory.createMultiStatSet(set).getValue();
		
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in getOccupancyStats() StatEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}
	
	@Override
	public Response getFile() {
		 
		context = new ClassPathXmlApplicationContext("classpath:spring/spring-batch-context.xml");
		
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("exportStatJob");
	 
		try {
			jobLauncher.run(job, new JobParameters());
			File file = new File(properties.getProperty("export.file.location"));
			ResponseBuilder response = Response.ok((Object) file);
		    response.header("Content-Disposition",
		           "attachment; filename=\"export_stats.csv\"");
		    
			return response.build();
		    //Response[] table = {response.build()};
		   //return table; 
			
		} catch (JobExecutionException e) {
			LOGGER.info("Job ExportStat failed");
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			if (context != null){
				context.close(); 
			}
		}
		
	}
	
	@Override
	public boolean getTestFile() {
		
		context = new ClassPathXmlApplicationContext("classpath:spring/spring-batch-context-test.xml");
		
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("exportStatJob");
	 
		try {
			jobLauncher.run(job, new JobParameters());
			return true;
			
		} catch (JobExecutionException e) {
			LOGGER.info("Job ExportStat failed");
			return false;
		} finally {
			if (context != null){
				context.close();  
			}
		}
		
	}
	
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

	@Override
	public boolean initRoomDailyOccupancyTable() {
		return testManager.initRoomDailyOccupancyTable();
	}

	@Override
	public boolean initRoomMonthlyOccupancyTable() {
		return testManager.initRoomMonthlyOccupancyTable();
	}

	
}
