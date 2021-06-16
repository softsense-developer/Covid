package com.smartsense.covid.api;

public class ApiConstant {

    public ApiConstant() {
    }

    public static final int USER_CREATED = 101;
    public static final int USER_EXISTS = 102;
    public static final int USER_FAILURE = 103;

    public static final int DATA_INSERTED = 110;
    public static final int DATA_NOT_INSERTED = 111;

    public static final int USER_UPDATE = 150;
    public static final int USER_UPDATE_FAILURE = 151;

    public static final int USER_AUTHENTICATED = 201;
    public static final int USER_NOT_FOUND = 202;
    public static final int USER_PASSWORD_DO_NOT_MATCH = 203;

    public static final int TOKEN_REFRESHED = 251;
    public static final int TOKEN_EXPIRED = 252;

    public static final int PASSWORD_CHANGED = 301;
    public static final int PASSWORD_DO_NOT_MATCH = 302;
    public static final int PASSWORD_NOT_CHANGED = 303;

    public static final int REQUEST_OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int INTERNAL_SERVER = 500;

}
