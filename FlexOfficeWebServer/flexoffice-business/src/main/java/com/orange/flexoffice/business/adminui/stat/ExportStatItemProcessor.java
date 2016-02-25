package com.orange.flexoffice.business.adminui.stat;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.orange.flexoffice.dao.common.model.object.ExportStatDto;

public class ExportStatItemProcessor implements ItemProcessor<ExportStatDto, ExportStatDto>{

	private static final Logger LOGGER = Logger.getLogger(ExportStatItemProcessor.class);
	
	@Override
	public ExportStatDto process(ExportStatDto result) throws Exception {
		/*
		 * Only return results which have begin & end occupancy dates
		 * 
		 */
		if ((result.getBeginOccupancyDate() == null) || (result.getEndOccupancyDate() == null)) {
			LOGGER.info("result room name:"+result.getRoomName()+" is refused");
			return null;
		}
		
		return result;
	}

}
