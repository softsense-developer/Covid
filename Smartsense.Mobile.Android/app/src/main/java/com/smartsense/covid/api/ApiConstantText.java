package com.smartsense.covid.api;

import android.content.Context;

import com.smartsense.covid.R;

public class ApiConstantText {
    private Context context;

    public ApiConstantText(Context context) {
        this.context = context;
    }

    public String getText(int code) {
        if (code == ApiConstant.USER_CREATED)
            return context.getString(R.string.account_created);
        else if (code == ApiConstant.USER_EXISTS)
            return context.getString(R.string.already_exists_user);
        else if (code == ApiConstant.USER_FAILURE)
            return context.getString(R.string.account_created_error);
        else if (code == ApiConstant.USER_AUTHENTICATED)
            return context.getString(R.string.login_successful);
        else if (code == ApiConstant.USER_NOT_FOUND)
            return context.getString(R.string.incorrect_email_password);
        else if (code == ApiConstant.USER_PASSWORD_DO_NOT_MATCH)
            return context.getString(R.string.error_incorrect_password);
        else if (code == ApiConstant.USER_UPDATE)
            return context.getString(R.string.data_updated);
        else if (code == ApiConstant.USER_UPDATE_FAILURE)
            return context.getString(R.string.data_update_error);
        else if (code == ApiConstant.PASSWORD_CHANGED)
            return context.getString(R.string.password_changed);
        else if (code == ApiConstant.PASSWORD_DO_NOT_MATCH)
            return context.getString(R.string.password_not_match);
        else if (code == ApiConstant.PASSWORD_NOT_CHANGED)
            return context.getString(R.string.password_not_changed);
        else if (code == ApiConstant.TOKEN_REFRESHED)
            return context.getString(R.string.token_refreshed);
        else if (code == ApiConstant.TOKEN_EXPIRED)
            return context.getString(R.string.token_expried);
        else if (code == ApiConstant.UNAUTHORIZED)
            return context.getString(R.string.unauthorized);
        else if (code == ApiConstant.FORBIDDEN)
            return context.getString(R.string.forbidden);
        else if (code == ApiConstant.DATA_INSERTED)
            return context.getString(R.string.data_inserted);
        else if (code == ApiConstant.DATA_NOT_INSERTED)
            return context.getString(R.string.data_not_inserted);
        else if (code == ApiConstant.REQUEST_OK)
            return context.getString(R.string.request_ok);
        else if (code == ApiConstant.INTERNAL_SERVER)
            return context.getString(R.string.internal_server);
        else if (code == ApiConstant.BAD_REQUEST)
            return context.getString(R.string.bad_request);
        return context.getString(R.string.occurred_error) + " 400";
    }


}
