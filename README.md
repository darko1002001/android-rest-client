[Follow to public page for more info](http://darko1002001.github.com/android-rest-client/)

android-rest-client
===================

A simple rest API client library


used libraries
================

Uses OKHTTP from square
https://github.com/square/okhttp

Uses Jackson from FasterXML to parse JSON responses
https://github.com/FasterXML/jackson

jackson-core
jackson-annotations
jackson-databind




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

Look at the demo project for details.

Also this demo sample project
https://github.com/darko1002001/sync-notes-android


## Steps to setup the library

1. Import the lib project into your workspace and add it as a library project
2. Look into the library project manifest and copy the definitions
3. Define a Class extending application and init the Authorization class


## Adding just the JAR files

The library can be added inside /libs as a JAR. The dependencies include a version of Jackson for parsing JSON requests. you can use the regular jar
and add your own parsers if the responses aren't JSON or you want to use a different library for parsing them.

## Manifest Declarations

      <uses-permission android:name="android.permission.INTERNET" />
    
      <uses-sdk android:minSdkVersion="14" />
    
      <application>
        <service android:name="com.dg.libs.rest.services.HTTPRequestExecutorService" >
        </service>
      </application>


## Calling requests (From the demo project)

    ```

    //----------------------------------
    
    TwitterService.getUsersTwitterRequest(getApplicationContext(), "android", new HttpCallbackImplementation())
        .executeAsync();
    
    //----------------------------------


    // Callback returning your defined type

    private final class HttpCallbackImplementation implements HttpCallback<UserModel> {

        @Override
        public void onSuccess(UserModel responseData) {
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
https://github.com/chute/Chute-SDK/tree/master/Android



## Use of Authentication Provider

Look at the demo project to see how the Authentication Provider can be set. Use this if you need to define a custom logic
(Parameters and/or headers that will be appended on each request). You can also specify a provider for individual requests.


# Changelog:

## Release 1.3.0

   Added Entity body requests which are now the base class for the String body request.
   Opens options to add FileEntity body which enables you to upload streams from files.
   File Requests also include a progress listener which you can set if you want to track the progress.
   Added File Parser which save the response stream to a specified file location.

## Release 1.1.0
   Reworked the HttpRequestParser to return an InputStream instead of a String.
   Use StringResponseParser (extend or use directly) to get the input stream into a String
   

   
# TODO

Split the Jackson support inside a new library so the HTTP library is cleaner. convert to parent/child maven project.
