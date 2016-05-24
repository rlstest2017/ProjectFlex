package com.orange.flexoffice.business.adminui.stat;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

/**
 * ExportMeetingRoomStatFlatFileWriter
 * @author oab
 *
 */
public class ExportMeetingRoomStatFlatFileWriter implements FlatFileHeaderCallback {

	@Override
	public void writeHeader(Writer writer) throws IOException {
        writer.write("country name; region name; city name; building name; meeting room name; meeting room floor; meeting room type; date begin occupancy; date end occupancy");
	}
}
