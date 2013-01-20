package com.dg.examples.restclientdemo.domain;


public class UserModel {

    public static final String TAG = UserModel.class.getSimpleName();

    private String id;
    private String name;
    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserModel [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", location=");
        builder.append(location);
        builder.append("]");
        return builder.toString();
    }

}
