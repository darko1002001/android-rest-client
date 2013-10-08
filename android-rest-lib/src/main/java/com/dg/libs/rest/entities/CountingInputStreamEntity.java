package com.dg.libs.rest.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.InputStreamEntity;

public class CountingInputStreamEntity extends InputStreamEntity {

  public interface UploadListener {

    public void onChange(long current);
  }

  private UploadListener listener;

  public CountingInputStreamEntity(final InputStream instream, final long length) {
    super(instream, length);
  }

  public void setUploadListener(final UploadListener listener) {
    this.listener = listener;
  }

  @Override
  public void writeTo(final OutputStream outstream) throws IOException {
    super.writeTo(new CountingOutputStream(outstream));
  }

  class CountingOutputStream extends OutputStream {

    private long counter = 0L;
    private final OutputStream outputStream;

    public CountingOutputStream(final OutputStream outputStream) {
      this.outputStream = outputStream;
    }

    @Override
    public void write(final byte[] buffer, final int offset, final int count)
        throws IOException {
      this.outputStream.write(buffer, offset, count);
      this.counter += count;
      if (listener != null) {
        listener.onChange(counter);
      }
    }

    @Override
    public void write(final int oneByte) throws IOException {
      this.outputStream.write(oneByte);
      counter++;
      if (listener != null) {
        // int percent = (int) ((counter * 100) / length);
        listener.onChange(counter);
      }
    }
  }

}
