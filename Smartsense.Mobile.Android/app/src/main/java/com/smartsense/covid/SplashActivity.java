package com.smartsense.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.api.model.requests.RefreshTokenRequest;
import com.smartsense.covid.api.model.responses.QuarantineStatusResponse;
import com.smartsense.covid.api.model.responses.RefreshTokenResponse;
import com.smartsense.covid.api.service.RetrofitClient;
import com.smartsense.covid.ui.home.HomeFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private PrefManager prefManager;
    private static final String TAG = "SplashActivity";
    private ApiConstantText apiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefManager = new PrefManager(getApplicationContext());
        apiText = new ApiConstantText(getApplicationContext());
    }

    private void splashWait() {
        if (prefManager.isLoggedIn()) {
            if (prefManager.getEmailVerified()) {
                if (prefManager.getUserRole() != MyConstant.PATIENT_ROLE) {
                    Handler handler2 = new Handler();
                    handler2.postDelayed(() -> {
                        Intent intent = new Intent(SplashActivity.this, WebViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }, 500);
                } else {
                    if (isConnectionHas()) {
                        refreshToken();
                    } else {
                        Handler handler2 = new Handler();
                        handler2.postDelayed(() -> {
                            Intent intent = new Intent(SplashActivity.this, CovidMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }, 500);
                    }
                }
            } else {
                Handler handler2 = new Handler();
                handler2.postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }, 500);
            }
        } else {
            Handler handler2 = new Handler();
            handler2.postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }, 500);
        }


       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefManager.isLoggedIn()) {
                    if (prefManager.getEmailVerified()) {
                        if (isConnectionHas()) {
                            refreshToken();
                        } else {
                            Intent intent = new Intent(SplashActivity.this, CovidMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, 500);*/

    }

    private boolean isConnectionHas() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //region Server Methods
    private void refreshToken() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setEmail(prefManager.getUserEmail());
        request.setPassword(prefManager.getUserPass());
        request.setRemember(true);

        Call<RefreshTokenResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).refreshToken(request);

        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, "refreshToken: " + response.body().getToken());
                                prefManager.setAuthToken(response.body().getToken());
                                getQuarantineStatus();
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                                getQuarantineStatus();
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 311");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 312");
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
                            getShortToast(getString(R.string.occurred_error) + " 313");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 314");
                    }
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 315");
            }
        });
    }


    private void getQuarantineStatus() {
        Call<QuarantineStatusResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).quarantineStatus();

        call.enqueue(new Callback<QuarantineStatusResponse>() {
            @Override
            public void onResponse(Call<QuarantineStatusResponse> call, Response<QuarantineStatusResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                prefManager.setIsLocationCanChange(!response.body().isQuarantineStatus());
                                CovidMainActivity.isHomeSetClicked = true;
                                Log.i(TAG, "getQuarantineStatus: " + response.body().getCode());


                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 291");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 292");
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
                            getShortToast(getString(R.string.occurred_error) + " 295");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 296");
                    }
                }

                Intent intent = new Intent(SplashActivity.this, CovidMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

            @Override
            public void onFailure(Call<QuarantineStatusResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 293");
            }
        });
    }
    //endregion Server methods

    private void getShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        splashWait();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}