package com.smartsense.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.api.model.requests.UserLoginRequest;
import com.smartsense.covid.api.model.responses.UserLoginResponse;
import com.smartsense.covid.api.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerifyActivity extends AppCompatActivity {

    private static final String TAG = "EmailVerifyActivity";
    private LottieAnimationView emailAnim;
    private PrefManager prefManager;
    private ProgressDialog progressDialog;
    private ApiConstantText apiText;
    private MaterialButton backToLogin, emailVerifiedBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        emailAnim = findViewById(R.id.emailVerifyAnim);
        backToLogin = findViewById(R.id.backToLogin);
        emailVerifiedBt = findViewById(R.id.emailVerifiedBt);

        emailAnim.setMaxProgress(0.73f);

        prefManager = new PrefManager(this);

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCancelable(false);
        apiText = new ApiConstantText(getApplicationContext());


        emailVerifiedBt.setOnClickListener(view -> {
            UserLoginRequest request = new UserLoginRequest();
            request.setEmail(prefManager.getUserEmail());
            request.setPassword(prefManager.getUserPass());
            request.setRemember(true);
            login(request);
        });

        backToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(EmailVerifyActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

    }

    private void login(UserLoginRequest request) {
        progressDialog.setMessage(getResources().getString(R.string.sign_in_dot));
        progressDialog.show();

        Call<UserLoginResponse> call = RetrofitClient
                .getInstance().getApi(getApplicationContext()).login(request);

        call.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, response.body().getToken());
                                prefManager.setAuthToken(response.body().getToken());
                                prefManager.setEmailVerified(true);
                                prefManager.setUserID(response.body().getUserId());
                                prefManager.setName(response.body().getName());
                                prefManager.setSurname(response.body().getSurname());
                                prefManager.setUserPhone(response.body().getPhone());
                                prefManager.setUserEmail(request.getEmail());
                                prefManager.setUserPass(request.getPassword());

                                Intent intent = new Intent(EmailVerifyActivity.this, CovidMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 111");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 112");
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
                            getShortToast(getString(R.string.occurred_error) + " 115");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 116");
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                Log.i(TAG, "onFailure");
                getShortToast(getString(R.string.occurred_error) + " 114");
                progressDialog.dismiss();
            }
        });

    }

    private void getShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}