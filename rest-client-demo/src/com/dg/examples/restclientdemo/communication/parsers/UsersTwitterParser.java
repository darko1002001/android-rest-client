package com.dg.examples.restclientdemo.communication.parsers;

import java.io.InputStream;

import com.dg.examples.restclientdemo.domain.UserModel;
import com.dg.libs.rest.parsers.BaseJacksonMapperResponseParser;


public class UsersTwitterParser extends BaseJacksonMapperResponseParser<UserModel> {

    public static final String TAG = UsersTwitterParser.class.getSimpleName();

    @Override
    public UserModel parse(InputStream instream) throws Exception {
        return mapper.readValue(instream, UserModel.class);
    }
}
