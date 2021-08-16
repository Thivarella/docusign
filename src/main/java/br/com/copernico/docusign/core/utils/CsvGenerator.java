package br.com.copernico.docusign.core.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.UUID;


public class CsvGenerator {


  public static File createCsv(Object header, Iterable<?> values) throws IOException {
    UUID randomName = UUID.randomUUID();
    final File out = new File("/var/files/acquisition" + randomName + ".csv");


    try (
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out, "UTF-8"), CSVFormat.EXCEL
                    .withHeader(toStringMaker(header)));
    ) {
      values.forEach(value -> {
        try {
          csvPrinter.printRecord(value == null ? "" : value);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });

      csvPrinter.flush();
    }

    return out;
  }

  private static String[] toStringMaker(Object o) {
    Class<? extends Object> c = o.getClass();
    Field[] fields = c.getDeclaredFields();
    String[] header = new String[fields.length];
    for (int i = 0; i < fields.length; i++) {
      header[i] = fields[i].getName();
    }
    return header;
  }

}
