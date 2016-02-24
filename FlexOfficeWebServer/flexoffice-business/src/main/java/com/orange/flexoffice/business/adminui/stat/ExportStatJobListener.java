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
		System.out.println("ExportStat Job starts at :"+startTime);
		LOGGER.info("ExportStat Job starts at :"+startTime);
	}
	

	@Override
	public void afterJob(JobExecution jobExecution) {
		stopTime = new Date();
		System.out.println("ExportStat Job stops at :"+stopTime);
		LOGGER.info("ExportStat Job stops at :"+stopTime);
		
		System.out.println("Total time take in millis :"+getTimeInMillis(startTime , stopTime));
		LOGGER.info("Total time take in millis :"+getTimeInMillis(startTime , stopTime));
		
		if(jobExecution.getStatus() == BatchStatus.COMPLETED){
			System.out.println("ExportStat job completed successfully");
			LOGGER.info("ExportStat job completed successfully");
			//Here you can perform some other business logic like cleanup
		}else if(jobExecution.getStatus() == BatchStatus.FAILED){
			System.out.println("ExportStat job failed with following exceptions ");
			LOGGER.info("ExportStat job failed with following exceptions ");
			List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
			for(Throwable th : exceptionList){
				System.err.println("exception :" +th.getLocalizedMessage());
				LOGGER.error("exception :" +th.getLocalizedMessage());
			}
		}
	}
	
	private long getTimeInMillis(Date start, Date stop){
		return stop.getTime() - start.getTime();
	}
	
}
