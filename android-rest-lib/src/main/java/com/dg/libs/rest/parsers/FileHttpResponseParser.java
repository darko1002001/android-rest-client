package com.dg.libs.rest.parsers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

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
