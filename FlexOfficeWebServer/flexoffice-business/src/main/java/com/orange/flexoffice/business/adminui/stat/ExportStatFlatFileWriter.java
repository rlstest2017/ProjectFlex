package com.orange.flexoffice.business.adminui.stat;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * ExportStatFlatFileWriter
 * @author oab
 *
 */
public class ExportStatFlatFileWriter extends FlatFileItemWriter<Object> {

    public ExportStatFlatFileWriter (){
        super.setHeaderCallback(new FlatFileHeaderCallback() {

            public void writeHeader(Writer writer) throws IOException {
                writer.write("Room name, Room type, Date begin occupancy, Date end occupancy");

            }
        });
   }
}
