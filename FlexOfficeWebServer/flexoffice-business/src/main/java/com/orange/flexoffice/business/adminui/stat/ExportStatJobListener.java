package com.orange.flexoffice.business.adminui.stat;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * ExportStatJobListener
 * @author oab
 *
 */
public class ExportStatJobListener implements JobExecutionListener{

	private static final Logger LOGGER = Logger.getLogger(ExportStatJobListener.class);
	
	private Date startTime, stopTime;
	
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		startTime = new Date();
		LOGGER.info("ExportStat Job starts at :"+startTime);
	}
	

	@Override
	public void afterJob(JobExecution jobExecution) {
		stopTime = new Date();
		LOGGER.info("ExportStat Job stops at :"+stopTime);
		LOGGER.info("Total time take in millis :"+getTimeInMillis(startTime , stopTime));
		
		if(jobExecution.getStatus() == BatchStatus.COMPLETED){
			LOGGER.info("ExportStat job completed successfully");
			//Here you can perform some other business logic like cleanup
		}else if(jobExecution.getStatus() == BatchStatus.FAILED){
			LOGGER.info("ExportStat job failed with following exceptions ");
			List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
			for(Throwable th : exceptionList){
				LOGGER.error("exception :" +th.getLocalizedMessage());
			}
		}
	}
	
	private long getTimeInMillis(Date start, Date stop){
		return stop.getTime() - start.getTime();
	}
	
}
