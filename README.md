android-rest-client
===================

A simple rest API client library


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


## Steps to setup the library

1. Import the lib project into your workspace and add it as a library project
2. Look into the library project manifest and copy the definitions
3. Define a Class extending application and init the Authorization class


## Calling requests (From the demo project)

    ```
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



