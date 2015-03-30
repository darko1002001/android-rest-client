[Follow to public page for more info](http://darko1002001.github.com/android-rest-client/)

android-rest-client
===================

A simple rest API client library


used libraries
================

Main Rest client uses OKHTTP from square
https://github.com/square/okhttp

to provide HTTP layer compatibility across android OS versions and manufacturers

Additions library uses:

    <dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-core</artifactId>
			<version>2.3.0</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-annotations</artifactId>
			<version>2.3.0</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.3.0</version>
		</dependency>
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.0.1</version>
		</dependency>



Overview
================

Can be used to run synchronous or asynchronous requests towards your API.
There is a service class that handles request execution by 2 kinds of thread pools (choice can specified in the request implementation) either a single thread executor or a fixed size executor.
Each of the executors is backed by a priority queue which means each individual request can have an execution priority set.

You can also provide your own service class which will handle the requests, extend from the current ones etc...

By default authorization is handled by setting an OAuth token in the header, but can be replaced with a custom implementation.

Note: The process of getting an oauth token from the server is not a part of this library implementation and have to be set according to the specification for your webservice.


Usage:
======

Look at the demo project included in this repo for details.

There is another sample project with an older version of android-rest-client found here:
https://github.com/darko1002001/sync-notes-android


## Steps to setup the library

1. Import the lib project into your workspace and add it as a library project
2. Look into the demo project manifest and copy the definitions
3. Check the App.java class inside the Demo project on how to configure the library.


## Adding just the JAR files

The library can be added inside /libs as a JAR. The dependencies include a version of Jackson for parsing JSON requests. you can use the regular jar
and add your own parsers if the responses aren't JSON or you want to use a different library for parsing them.

## Manifest Declarations

      <uses-permission android:name="android.permission.INTERNET" />
    
      <uses-sdk android:minSdkVersion="14" />
    
      <application>
      <service android:name="com.araneaapps.android.libs.asyncrunners.services.ExecutorService"></service>
      </application>

## Configuration

The library needs to be configured in a class extending Application (look into the demo project for specifics).


    RestClientConfiguration builder = new RestClientConfiguration.ConfigurationBuilder()
        .setAuthenticationProvider(new AuthenticationProvider() {
          @Override
          public void authenticateRequest(RestClientRequest client) {
            // YOu can add parameters or headers which will be attached to each request
          }
        })
        .create();

    RestClientConfiguration.init(this, builder);

## Note - If including this library with gradle will automatically merge your manifest with the one with the library. The lib is published as AAR.


## Calling requests (From the demo project)

    ```

    //----------------------------------
    
    TwitterService.getUsersTwitterRequest("android").setCallback(new HttpCallbackImplementation())
        .executeAsync();
    
    //----------------------------------


    // Callback returning your defined type

    private final class HttpCallbackImplementation implements HttpCallback<UserModel> {

        @Override
        public void onSuccess(UserModel responseData, ResponseStatus responseCode) {
            textViewResponse.setText(responseData.toString());
        }

        @Override
        public void onHttpError(ResponseStatus responseCode) {
            Toast.makeText(getApplicationContext(),
                    responseCode.getStatusCode() + " " + responseCode.getStatusMessage(),
                    Toast.LENGTH_LONG).show();

        }
    }
    ```

Two callback methods are created each executing by default in the Android UI Thread so you don't have to implement the logic for switching back to UI from another thread.


## SDK that uses This library:
      
Chute
https://github.com/chute/Chute-SDK-V2-Android



## Use of Authentication Provider

Look at the demo project to see how the Authentication Provider can be set. Use this if you need to define a custom logic
(Parameters and/or headers that will be appended on each request). You can also specify a provider for individual requests.


# Changelog:

## Release 2.7.0

Added support for RxJava

## Release 2.2.0

Updated version of OKHTTP to 2.+
Changed the requests to RestClientRequest.class which handles all the request types.
Added the option to run requests without a parser or a callback
The callback can be set externally when creating the request

## Release 1.7.0

  Added RestClientConfiguration so all the global configuration is done through it.

## Release 1.6.0

### Breaking changes

  Added response status to success callback implementation

## Release 1.4.0

  Created a new project additions which now has some helper libraries and classes such as Jackson JSON parser, IOUtils from apache etc...
  Moved the async part (Service with executors) of the library to a new more general project on github not specific to just HTTP.
  HttpRequestStore.init(context) is now replaced with AsyncRunners.init(context) which will provide a wrapping layer around the code that should be initialized in the App class
  HttpRequest now extends Runnable interface
  Added the option to override a handleStatus method in BaseHttpRequestImpl to be able to do custom result handing based on the status (see demo for sample)

## Release 1.3.5

  Fix issue with add param with token provider
  Added socket timeout and connection timeout setters by calling `getClient().set...` inside any of the request classes. 

## Release 1.3.0

   Added Entity body requests which are now the base class for the String body request.
   Opens options to add FileEntity body which enables you to upload streams from files.
   File Requests also include a progress listener which you can set if you want to track the progress.
   Added File Parser which save the response stream to a specified file location.

## Release 1.1.0
   Reworked the HttpRequestParser to return an InputStream instead of a String.
   Use StringResponseParser (extend or use directly) to get the input stream into a String
