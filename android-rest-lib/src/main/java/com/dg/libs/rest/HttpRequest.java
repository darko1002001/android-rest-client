package com.dg.libs.rest;

/**
 * @author darko.grozdanovski The {@link HttpRequest} interface has the
 *         responsibility to send a request object to the server which is used
 *         for data exchange with the server behind the scenes. It contains
 *         methods that run in threads.
 */
public interface HttpRequest extends Runnable {

  /**
   * This method runs asynchronously in the same thread as the application and
   * launches a service for further server communication.
   */
  public void executeAsync();

}
