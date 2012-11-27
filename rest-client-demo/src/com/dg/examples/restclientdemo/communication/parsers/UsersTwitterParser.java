package com.dg.examples.restclientdemo.communication.parsers;

import com.dg.examples.restclientdemo.domain.UserModel;
import com.dg.libs.rest.parsers.BaseJacksonMapperResponseParser;


public class UsersTwitterParser extends BaseJacksonMapperResponseParser<UserModel> {

    public static final String TAG = UsersTwitterParser.class.getSimpleName();

    @Override
    public UserModel parse(String responseBody) throws Exception {
        return mapper.readValue(responseBody, UserModel.class);
    }
}
