package com.dg.libs.rest.parsers;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public abstract class FileHttpResponseParser implements HttpResponseParser<File> {

  private File outputFile;

  public FileHttpResponseParser(File outputFile) {
    super();
    this.outputFile = outputFile;
  }

  @Override
  public File parse(final InputStream responseStream) throws Exception {
    FileOutputStream outputStream = new FileOutputStream(outputFile);
    IOUtils.copy(responseStream, outputStream);
    return outputFile;
  }

}
