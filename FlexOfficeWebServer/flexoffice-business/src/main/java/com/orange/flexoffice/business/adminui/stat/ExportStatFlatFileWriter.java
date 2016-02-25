package com.orange.flexoffice.business.adminui.stat;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

/**
 * ExportStatFlatFileWriter
 * @author oab
 *
 */
public class ExportStatFlatFileWriter implements FlatFileHeaderCallback {

	@Override
	public void writeHeader(Writer writer) throws IOException {
        writer.write("Room name, Room type, Date begin occupancy, Date end occupancy");
		
	}
}
