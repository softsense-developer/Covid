package com.smartsense.covid.settings;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;
import com.smartsense.covid.api.model.requests.PasswordChangeRequest;
import com.smartsense.covid.api.model.responses.PasswordChangeResponse;
import com.smartsense.covid.api.service.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordChangeActivity extends AppCompatActivity {

    private TextInputLayout oldPass, newPass;
    private MaterialButton updatePass;
    private String oldPassText, newPassText;
    private PrefManager prefManager;
    private ApiConstantText apiText;
    private String TAG = "Smartsense";
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);


        oldPass = findViewById(R.id.passwordOldTI);
        newPass = findViewById(R.id.passwordNewTI);
        updatePass = findViewById(R.id.passwordUpdateButton);

        prefManager = new PrefManager(getApplicationContext());
        apiText = new ApiConstantText(getApplicationContext());

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassText = oldPass.getEditText().getText().toString().trim();
                newPassText = newPass.getEditText().getText().toString().trim();

                if (!oldPassText.isEmpty() && !newPassText.isEmpty()) {
                    if (passwordCheck(oldPassText) && passwordCheck(newPassText)) {
                        if (!oldPassText.equals(newPassText)) {
                            if (isConnectionHas()) {
                                if (loadingDialog != null && !loadingDialog.isShowing()) {
                                    loadingDialog.show();
                                }
                                PasswordChangeRequest request = new PasswordChangeRequest();
                                request.setOldPassword(oldPassText);
                                request.setNewPassword(newPassText);
                                request.setConfirmPassword(newPassText);
                                changePassword(request);
                            } else {
                                Toast.makeText(PasswordChangeActivity.this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PasswordChangeActivity.this, getString(R.string.old_pass_same_new), Toast.LENGTH_SHORT).show();
                        }
                    } else if (!passwordCheck(oldPassText)) {
                        oldPass.setError(getString(R.string.error_invalid_password));
                        errorNull();
                    } else {
                        newPass.setError(getString(R.string.error_invalid_password));
                        errorNull();
                    }
                } else {
                    if (oldPassText.isEmpty()) {
                        oldPass.setError(getString(R.string.not_empty));
                    }
                    if (newPassText.isEmpty()) {
                        newPass.setError(getString(R.string.not_empty));
                    }
                    errorNull();
                }
            }
        });
    }


    private void changePassword(PasswordChangeRequest request) {
        Call<PasswordChangeResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).passwordChange(request);

        call.enqueue(new Callback<PasswordChangeResponse>() {
            @Override
            public void onResponse(Call<PasswordChangeResponse> call, Response<PasswordChangeResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                prefManager.setUserPass(request.getNewPassword());
                                getShortToast(apiText.getText(ApiConstant.PASSWORD_CHANGED));
                                finish();
                                Log.i(TAG, "Password updated.");
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 271");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 272");
                    }
                } else {
                    try {
                        if (response.code() == ApiConstant.BAD_REQUEST) {
                            getShortToast(apiText.getText(ApiConstant.BAD_REQUEST));
                        } else if (response.code() == ApiConstant.UNAUTHORIZED) {
                            getShortToast(apiText.getText(ApiConstant.UNAUTHORIZED));
                        } else if (response.code() == ApiConstant.FORBIDDEN) {
                            getShortToast(apiText.getText(ApiConstant.FORBIDDEN));
                        } else if (response.code() == ApiConstant.INTERNAL_SERVER) {
                            getShortToast(apiText.getText(ApiConstant.INTERNAL_SERVER));
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 275");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 276");
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PasswordChangeResponse> call, Throwable t) {
                getShortToast(getString(R.string.occurred_error) + " 273");
                loadingDialog.dismiss();
            }
        });
    }

    private void getShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    //TODO Old api
    private void updatePassword(String old, String newPass) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi(getApplicationContext())
                .updatePassword(old, newPass, newPass);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 301) {
                    Log.i(TAG, "response.code() == 301");
                    prefManager.setUserPass(newPass);
                    Toast.makeText(PasswordChangeActivity.this, apiText.getText(ApiConstant.PASSWORD_CHANGED), Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code() == 400) {
                    Toast.makeText(PasswordChangeActivity.this, apiText.getText(ApiConstant.PASSWORD_DO_NOT_MATCH), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(PasswordChangeActivity.this, apiText.getText(ApiConstant.UNAUTHORIZED), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(PasswordChangeActivity.this, apiText.getText(ApiConstant.FORBIDDEN), Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, String.valueOf(response.code()));
                    Toast.makeText(PasswordChangeActivity.this, getString(R.string.occurred_error) + " 164", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PasswordChangeActivity.this, getString(R.string.occurred_error) + " 163", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void errorNull() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                oldPass.setError(null);
                newPass.setError(null);
            }
        }, 4000);
    }

    public boolean passwordCheck(String pass) {
        return pass.length() >= 6;
    }

    private boolean isConnectionHas() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}