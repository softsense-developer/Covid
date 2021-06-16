package com.smartsense.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.smartsense.covid.adapters.companionAdapter.Companion;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.api.model.requests.AddCompanionRequest;
import com.smartsense.covid.api.model.requests.UserRegisterRequest;
import com.smartsense.covid.api.model.responses.AddCompanionResponse;
import com.smartsense.covid.api.model.responses.DeleteCompanionResponse;
import com.smartsense.covid.api.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanionActivity extends AppCompatActivity {

    private static final String TAG = "CompanionActivity";
    private TextInputLayout companionNameTI, companionSurnameTI, companionEmailTI, companionPasswordTI;
    private String companionName, companionSurname, companionEmail, companionPassword;
    private MaterialButton companionDataSaveButton;
    private ApiConstantText apiText;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companion);

        companionNameTI = findViewById(R.id.companionNameTI);
        companionSurnameTI = findViewById(R.id.companionSurnameTI);
        companionEmailTI = findViewById(R.id.companionEmailTI);
        companionPasswordTI = findViewById(R.id.companionPasswordTI);
        companionDataSaveButton = findViewById(R.id.companionDataSaveButton);

        apiText = new ApiConstantText(getApplicationContext());

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (savedInstanceState != null) {
            companionNameTI.getEditText().setText(savedInstanceState.getString("companionName"));
            companionSurnameTI.getEditText().setText(savedInstanceState.getString("companionSurname"));
            companionEmailTI.getEditText().setText(savedInstanceState.getString("companionEmail"));
            companionPasswordTI.getEditText().setText(savedInstanceState.getString("companionPassword"));
        }

        companionDataSaveButton.setOnClickListener(view -> {
            if (connectionCheck()) {
                companionNameTI.setError(null);
                companionSurnameTI.setError(null);
                companionEmailTI.setError(null);
                companionPasswordTI.setError(null);

                companionName = companionNameTI.getEditText().getText().toString().trim();
                companionSurname = companionSurnameTI.getEditText().getText().toString().trim();
                companionEmail = companionEmailTI.getEditText().getText().toString().trim();
                companionPassword = companionPasswordTI.getEditText().getText().toString().trim();

                if (!companionName.isEmpty() && !companionSurname.isEmpty() && !companionEmail.isEmpty() && !companionPassword.isEmpty()) {
                    if (emailCheck(companionEmail) && passwordCheck(companionPassword)) {
                        AddCompanionRequest request = new AddCompanionRequest();
                        request.setEmail(companionEmail);
                        request.setPassword(companionPassword);
                        request.setName(companionName);
                        request.setSurname(companionSurname);
                        addCompanion(request);
                    } else if (!emailCheck(companionEmail) && passwordCheck(companionPassword)) {
                        companionEmailTI.setError(getString(R.string.error_invalid_email));
                        companionEmailTI.requestFocus();
                    } else if (!passwordCheck(companionPassword) && emailCheck(companionEmail)) {
                        companionPasswordTI.setError(getString(R.string.error_invalid_password));
                        companionPasswordTI.requestFocus();
                    } else {
                        companionEmailTI.setError(getString(R.string.error_invalid_email));
                        companionEmailTI.requestFocus();
                    }

                } else if (companionName.trim().isEmpty()) {
                    if (TextUtils.isEmpty(companionName)) {
                        companionNameTI.setError(getString(R.string.name_empty));
                        companionNameTI.requestFocus();
                    }
                } else if (companionSurname.trim().isEmpty()) {
                    if (TextUtils.isEmpty(companionSurname)) {
                        companionSurnameTI.setError(getString(R.string.name_empty));
                        companionSurnameTI.requestFocus();
                    }
                } else if (companionEmail.trim().isEmpty()) {
                    if (TextUtils.isEmpty(companionEmail)) {
                        companionEmailTI.setError(getString(R.string.email_empty));
                        companionEmailTI.requestFocus();
                    }
                } else if (companionPassword.trim().isEmpty()) {
                    if (TextUtils.isEmpty(companionPassword)) {
                        companionPasswordTI.setError(getString(R.string.pass_empty));
                        companionPasswordTI.requestFocus();
                    }
                }
                errorNull();
            } else {
                getShortToast(getString(R.string.noConnection));
            }
        });
    }

    private void addCompanion(AddCompanionRequest request) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }

        Call<AddCompanionResponse> call = RetrofitClient.getInstance()
                .getApi(this).addCompanion(request);

        call.enqueue(new Callback<AddCompanionResponse>() {
            @Override
            public void onResponse(Call<AddCompanionResponse> call, Response<AddCompanionResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                getShortToast(getString(R.string.companion_added));
                                finish();
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 441");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 442");
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
                            getShortToast(getString(R.string.occurred_error) + " 443");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 444");
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AddCompanionResponse> call, Throwable t) {
                getShortToast(getString(R.string.occurred_error) + " 445");
                loadingDialog.dismiss();
            }
        });
    }

    public boolean connectionCheck() {
        boolean isConnected = false;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
            getShortToast(getString(R.string.occurred_error) + " 446");
        }

        return isConnected;
    }

    public boolean emailCheck(String email) {
        boolean check = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return check;
    }

    public boolean passwordCheck(String pass) {
        return pass.length() >= 6;
    }

    private void getShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void errorNull() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            companionNameTI.setError(null);
            companionSurnameTI.setError(null);
            companionEmailTI.setError(null);
            companionPasswordTI.setError(null);
        }, 4000);
    }
}