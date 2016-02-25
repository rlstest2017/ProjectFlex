package com.orange.flexoffice.business.adminui.stat;

//import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import com.orange.flexoffice.dao.common.model.object.ExportStatDto;

/**
 * ExportStatItemProcessor
 * This class is not used in this version of export stat.
 * Maybe later when we will have some process data to do.
 * @author oab
 *
 */
public class ExportStatItemProcessor implements ItemProcessor<ExportStatDto, ExportStatDto>{

//	private static final Logger LOGGER = Logger.getLogger(ExportStatItemProcessor.class);
	
	@Override
	public ExportStatDto process(ExportStatDto result) throws Exception {
		/*
		 * Only return results which have begin & end occupancy dates
		 * 
		 */
//		if ((result.getBeginOccupancyDate() == null) || (result.getEndOccupancyDate() == null)) {
//			LOGGER.info("result room name:"+result.getRoomName()+" is refused");
//			return null;
//		}
		
		return result;
	}

}
