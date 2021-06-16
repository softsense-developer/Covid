package com.smartsense.covid;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.button.MaterialButton;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.api.model.VitalHistory;
import com.smartsense.covid.api.model.requests.ResetPasswordRequest;
import com.smartsense.covid.api.model.responses.LoginResponse;
import com.smartsense.covid.api.model.responses.PatientInfoResponse;
import com.smartsense.covid.api.model.responses.QuarantineStatusResponse;
import com.smartsense.covid.api.model.responses.ResetPasswordResponse;
import com.smartsense.covid.api.model.responses.SetLocationStatusResponse;
import com.smartsense.covid.api.model.User;
import com.smartsense.covid.api.model.responses.UserDataResponse;
import com.smartsense.covid.api.model.requests.UserLoginRequest;
import com.smartsense.covid.api.model.responses.UserLoginResponse;
import com.smartsense.covid.api.model.UserModel;
import com.smartsense.covid.api.model.requests.UserRegisterRequest;
import com.smartsense.covid.api.model.responses.UserRegisterResponse;
import com.smartsense.covid.api.model.responses.VitalHistoryResponse;
import com.smartsense.covid.api.service.RetrofitClient;
import com.smartsense.covid.model.Covid;
import com.smartsense.covid.repo.CovidRepository;
import com.smartsense.covid.settings.UserSettingsActivity;
import com.smartsense.covid.ui.home.HomeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView textTitle, textAccountForget, createAccountText, signUpLegalText;
    private EditText etEmail, etPassword, etName, etPhone, etSurname, etDoctorID;
    private ImageView imageEmail, imagePassword, imageName, imagePhone, imageDoctor;
    private View passDiv, emailDiv, nameDiv, phoneDiv, doctorDiv;
    private AppCompatButton loginSignUp;
    private int state = 0; //0: Sign up 1: log in 2:forget pass
    private String TAG = "Smartsense";
    private ProgressDialog progressDialog;
    private String email, pass, name, phone, surname, doctorID;
    private boolean isLogin = true;
    private ApiConstantText apiText;
    private PrefManager prefManager;
    private CovidRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        textTitle = findViewById(R.id.textLogTitle);
        textAccountForget = findViewById(R.id.loginAccountAndForget);
        createAccountText = findViewById(R.id.createAccountText);
        etEmail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        imageEmail = findViewById(R.id.imageEmail);
        imagePassword = findViewById(R.id.imagePassword);
        passDiv = findViewById(R.id.viewDividerPassword);
        emailDiv = findViewById(R.id.viewDividerEmail);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        imageName = findViewById(R.id.imageName);
        nameDiv = findViewById(R.id.viewDividerName);
        etPhone = findViewById(R.id.etPhone);
        imagePhone = findViewById(R.id.imagePhone);
        phoneDiv = findViewById(R.id.viewDividerPhone);
        signUpLegalText = findViewById(R.id.signupLegalText);

        etDoctorID = findViewById(R.id.etDoctorID);
        imageDoctor = findViewById(R.id.imageDoctor);
        doctorDiv = findViewById(R.id.viewDividerDoctorID);

        loginSignUp = findViewById(R.id.signInButton);

        prefManager = new PrefManager(getApplicationContext());

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCancelable(false);
        apiText = new ApiConstantText(getApplicationContext());

        repository = new CovidRepository(getApplication());

        textAccountForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 1) {
                    getForgetView();
                } else {
                    getLoginView();
                }
            }
        });
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSignUpView();
            }
        });

        loginSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 2) {
                    resetPassButton();
                } else {
                    signUpLoginButtonNA();
                }
            }
        });

        getSignUpView();
        signUpLegalSpanMultiply();

        //signUpUser("gd13@gd.com", "123456", "Gökhan", "Dağtekin", "04444444444");
        //loginUser(prefManager.getUsername(), prefManager.getUserPass());
        //loginUser("gd5@gmail.com", "123456");
        //getUserData("yunus12@gmail.com");
    }

    private void signUpLoginButtonNA() {
        if (connectionCheck()) {
            email = etEmail.getText().toString();
            pass = etPassword.getText().toString();
            name = etName.getText().toString();
            surname = etSurname.getText().toString();

            if (state == 0) {
                if (!name.trim().isEmpty() && !surname.trim().isEmpty() && !email.trim().isEmpty() && !pass.trim().isEmpty()) {
                    if (emailCheck(email) && passwordCheck(pass)) {
                        UserRegisterRequest registerRequest = new UserRegisterRequest();
                        registerRequest.setEmail(email);
                        registerRequest.setPassword(pass);
                        registerRequest.setName(name);
                        registerRequest.setSurname(surname);
                        register(registerRequest);

                        progressDialog.setMessage(getResources().getString(R.string.register_dot));
                        progressDialog.show();
                    } else if (!emailCheck(email) && passwordCheck(pass)) {
                        etEmail.setError(getString(R.string.error_invalid_email));
                        etEmail.requestFocus();
                    } else if (!passwordCheck(pass) && emailCheck(email)) {
                        etPassword.setError(getString(R.string.error_invalid_password));
                        etPassword.requestFocus();
                    } else {
                        etEmail.setError(getString(R.string.error_invalid_email));
                        etEmail.requestFocus();
                    }

                } else if (name.trim().isEmpty()) {
                    if (TextUtils.isEmpty(name)) {
                        etName.setError(getString(R.string.name_empty));
                        etName.requestFocus();
                    }
                } else if (surname.trim().isEmpty()) {
                    if (TextUtils.isEmpty(surname)) {
                        etSurname.setError(getString(R.string.name_empty));
                        etSurname.requestFocus();
                    }
                } else if (email.trim().isEmpty()) {
                    if (TextUtils.isEmpty(email)) {
                        etEmail.setError(getString(R.string.email_empty));
                        etEmail.requestFocus();
                    }
                } else if (pass.trim().isEmpty()) {
                    if (TextUtils.isEmpty(pass)) {
                        etPassword.setError(getString(R.string.pass_empty));
                        etPassword.requestFocus();
                    }
                }
            } else {
                if (!email.trim().isEmpty() && !pass.trim().isEmpty()) {
                    if (emailCheck(email) && passwordCheck(pass)) {
                        UserLoginRequest request = new UserLoginRequest();
                        request.setEmail(email);
                        request.setPassword(pass);
                        request.setRemember(true);
                        login(request);
                        progressDialog.setMessage(getResources().getString(R.string.sign_in_dot));
                        progressDialog.show();
                    } else if (!emailCheck(email) && passwordCheck(pass)) {
                        etEmail.setError(getString(R.string.error_invalid_email));
                        etEmail.requestFocus();
                    } else if (!passwordCheck(pass) && emailCheck(email)) {
                        etPassword.setError(getString(R.string.error_invalid_password));
                        etPassword.requestFocus();
                    } else {
                        etEmail.setError(getString(R.string.error_invalid_email));
                        etEmail.requestFocus();
                    }
                } else if (email.trim().isEmpty()) {
                    if (TextUtils.isEmpty(email)) {
                        etEmail.setError(getString(R.string.email_empty));
                        etEmail.requestFocus();
                    }
                } else if (pass.trim().isEmpty()) {
                    if (TextUtils.isEmpty(pass)) {
                        etPassword.setError(getString(R.string.pass_empty));
                        etPassword.requestFocus();
                    }
                }

            }

        } else {
            Toast.makeText(LoginActivity.this, R.string.noConnection, Toast.LENGTH_SHORT).show();
        }
    }


    private void resetPassButton() {
        if (connectionCheck()) {
            email = etEmail.getText().toString();
            if (!email.trim().isEmpty()) {
                if (emailCheck(email)) {
                    progressDialog.setMessage(getResources().getString(R.string.password_resetting));
                    progressDialog.show();
                    sendPassReset(email);
                } else if (!emailCheck(email)) {
                    etEmail.setError(getString(R.string.error_invalid_email));
                    etEmail.requestFocus();
                }
            } else if (email.trim().isEmpty()) {
                if (TextUtils.isEmpty(email)) {
                    etEmail.setError(getString(R.string.email_empty));
                    etEmail.requestFocus();
                }
            }
        } else {
            Toast.makeText(LoginActivity.this, R.string.noConnection, Toast.LENGTH_SHORT).show();
        }
    }

    private void register(UserRegisterRequest request) {
        Call<UserRegisterResponse> call = RetrofitClient
                .getInstance()
                .getApi(getApplicationContext())
                .register(request);

        call.enqueue(new Callback<UserRegisterResponse>() {
            @Override
            public void onResponse(Call<UserRegisterResponse> call, Response<UserRegisterResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, apiText.getText(ApiConstant.USER_CREATED));
                                UserModel user = new UserModel(request.getEmail(), request.getPassword(), request.getName(), request.getSurname());
                                prefManager.setUserNA(user);
                                getShortToast(apiText.getText(ApiConstant.USER_CREATED));

                                Intent intent = new Intent(LoginActivity.this, EmailVerifyActivity.class);
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
                            getShortToast(getString(R.string.occurred_error) + " 101");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 102");
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
            public void onFailure(Call<UserRegisterResponse> call, Throwable t) {
                getShortToast(getString(R.string.occurred_error) + " 104");
                Log.i(TAG, t.getLocalizedMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void login(UserLoginRequest request) {
        Call<UserLoginResponse> call = RetrofitClient
                .getInstance().getApi(getApplicationContext()).login(request);

        call.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, "logged in");
                                if(response.body().getRoleId() == MyConstant.DOCTOR_ROLE){
                                    getLongToast(getString(R.string.login_role_doctor));
                                }else if(response.body().getRoleId() == MyConstant.SUPERVISOR_ROLE){
                                    getLongToast(getString(R.string.login_role_supervisor));
                                }else if(response.body().getRoleId() == MyConstant.ADMIN_ROLE){
                                    getLongToast(getString(R.string.login_role_admin));
                                }else if(response.body().getRoleId() == MyConstant.COMPANION_ROLE){
                                    getLongToast(getString(R.string.login_role_companion));
                                }

                                prefManager.setUserRole(response.body().getRoleId());
                                prefManager.setAuthToken(response.body().getToken());
                                prefManager.setEmailVerified(true);
                                prefManager.setUserID(response.body().getUserId());
                                prefManager.setName(response.body().getName());
                                prefManager.setSurname(response.body().getSurname());
                                prefManager.setUserPhone(response.body().getPhone());
                                prefManager.setUserEmail(request.getEmail());
                                prefManager.setUserPass(request.getPassword());

                                getPatientInfo();

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

    private void getPatientInfo() {
        progressDialog.setMessage(getResources().getString(R.string.data_check));
        progressDialog.show();
        Call<PatientInfoResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).getPatientInfo();

        call.enqueue(new Callback<PatientInfoResponse>() {
            @Override
            public void onResponse(Call<PatientInfoResponse> call, Response<PatientInfoResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, "getPatientInfo: ");
                                prefManager.setName(response.body().getName());
                                prefManager.setSurname(response.body().getSurname());
                                prefManager.setUserIdentity(response.body().getIdentityNumber());
                                prefManager.setUserGender(response.body().getGender());
                                prefManager.setUserBirthday(convertIso8601DateToTimestamp(response.body().getDateOfBirth()));
                                prefManager.setUserTrackingType(response.body().getUserStatus());
                                prefManager.setUserDiagnosis(response.body().getDiagnosis());
                                prefManager.setDoctorID(response.body().getDoctorId());
                                prefManager.setDoctorNameSurname(response.body().getDoctorName());
                                prefManager.setHospitalName(response.body().getHospitalName());

                                getQuarantineStatus();
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                //getLongToast(errors.toString());

                                getQuarantineStatus();
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 261");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 262");
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
                            getShortToast(getString(R.string.occurred_error) + " 265");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 266");
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PatientInfoResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 263", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void getQuarantineStatus() {
        progressDialog.setMessage(getResources().getString(R.string.data_check));
        progressDialog.show();

        Call<QuarantineStatusResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).quarantineStatus();

        call.enqueue(new Callback<QuarantineStatusResponse>() {
            @Override
            public void onResponse(Call<QuarantineStatusResponse> call, Response<QuarantineStatusResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, "getQuarantineStatus: ");
                                prefManager.setIsLocationCanChange(!response.body().isQuarantineStatus());

                                getVitalDataHistory();
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());

                                getVitalDataHistory();
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
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<QuarantineStatusResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 293");
                progressDialog.dismiss();
            }
        });
    }

    private void getVitalDataHistory() {
        progressDialog.setMessage(getResources().getString(R.string.data_check));
        progressDialog.show();

        Call<VitalHistoryResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).getVitalHistory();

        call.enqueue(new Callback<VitalHistoryResponse>() {
            @Override
            public void onResponse(Call<VitalHistoryResponse> call, Response<VitalHistoryResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, "getVitalDataHistory: ");
                                for (VitalHistory vital : response.body().oxygen) {
                                    Covid covid = new Covid(convertIso8601DateToTimestamp(vital.getSaveDate()), vital.getDataValue(), MyConstant.SPO2, MyConstant.AUTO_SAVE);
                                    repository.insert(covid);
                                }

                                for (VitalHistory vital : response.body().pulses) {
                                    Covid covid = new Covid(convertIso8601DateToTimestamp(vital.getSaveDate()), vital.getDataValue(), MyConstant.HEART, MyConstant.AUTO_SAVE);
                                    repository.insert(covid);
                                }

                                for (VitalHistory vital : response.body().temperatures) {
                                    Covid covid = new Covid(convertIso8601DateToTimestamp(vital.getSaveDate()), vital.getDataValue(), MyConstant.TEMP, MyConstant.AUTO_SAVE);
                                    repository.insert(covid);
                                }

                                getShortToast(getString(R.string.sign_in_dot));

                                Intent intent = new Intent(LoginActivity.this, CovidMainActivity.class);
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
                            getShortToast(getString(R.string.occurred_error) + " 321");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 322");
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
                            getShortToast(getString(R.string.occurred_error) + " 325");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 326");
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<VitalHistoryResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 323");
                progressDialog.dismiss();
            }
        });
    }

    private long convertIso8601DateToTimestamp(String date) {
        try {
            java.text.DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date result1 = df1.parse(date);
            return result1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                java.text.DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date result1 = df1.parse(date);
                return result1.getTime();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }


    //TODO deprecated api
    /*
    private void signUpLoginButton() {
        if (connectionCheck()) {
            email = etEmail.getText().toString();
            pass = etPassword.getText().toString();
            name = etName.getText().toString();
            surname = etSurname.getText().toString();
            phone = etPhone.getText().toString();
            doctorID = etDoctorID.getText().toString();

            if (state == 0) {
                if (!name.trim().isEmpty() && !surname.trim().isEmpty() && !phone.trim().isEmpty() && !email.trim().isEmpty() && !pass.trim().isEmpty() && !doctorID.trim().isEmpty()) {
                    if (emailCheck(email) && passwordCheck(pass)) {
                        signUpUser(email, pass, name, surname, phone, Integer.parseInt(doctorID));
                        progressDialog.setMessage(getResources().getString(R.string.register_dot));
                        progressDialog.show();
                    } else if (!emailCheck(email) && passwordCheck(pass)) {
                        //Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                        etEmail.setError(getString(R.string.error_invalid_email));
                        etEmail.requestFocus();
                    } else if (!passwordCheck(pass) && emailCheck(email)) {
                        //Toast.makeText(this, getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();
                        etPassword.setError(getString(R.string.error_invalid_password));
                        etPassword.requestFocus();
                    } else {
                        //Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                        etEmail.setError(getString(R.string.error_invalid_email));
                        etEmail.requestFocus();
                    }

                } else if (name.trim().isEmpty()) {
                    if (TextUtils.isEmpty(name)) {
                        //Toast.makeText(LoginActivity.this, getResources().getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                        etName.setError(getString(R.string.name_empty));
                        etName.requestFocus();
                    }
                } else if (surname.trim().isEmpty()) {
                    if (TextUtils.isEmpty(surname)) {
                        //Toast.makeText(LoginActivity.this, getResources().getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                        etSurname.setError(getString(R.string.name_empty));
                        etSurname.requestFocus();
                    }
                } else if (phone.trim().isEmpty()) {
                    if (TextUtils.isEmpty(phone)) {
                        //Toast.makeText(LoginActivity.this, getResources().getString(R.string.phone_empty), Toast.LENGTH_SHORT).show();
                        etPhone.setError(getString(R.string.phone_empty));
                        etPhone.requestFocus();
                    }
                } else if (email.trim().isEmpty()) {
                    if (TextUtils.isEmpty(email)) {
                        //Toast.makeText(LoginActivity.this, getResources().getString(R.string.email_empty), Toast.LENGTH_SHORT).show();
                        etEmail.setError(getString(R.string.email_empty));
                        etEmail.requestFocus();
                    }
                } else if (pass.trim().isEmpty()) {
                    if (TextUtils.isEmpty(pass)) {
                        //Toast.makeText(LoginActivity.this, getResources().getString(R.string.pass_empty), Toast.LENGTH_SHORT).show();
                        etPassword.setError(getString(R.string.pass_empty));
                        etPassword.requestFocus();
                    }
                } else if (doctorID.trim().isEmpty()) {
                    if (TextUtils.isEmpty(doctorID)) {
                        etDoctorID.setError(getString(R.string.doctor_id_empty));
                        etDoctorID.requestFocus();
                    }
                }
            } else {
                if (!email.trim().isEmpty() && !pass.trim().isEmpty()) {
                    if (emailCheck(email) && passwordCheck(pass)) {
                        loginUser(email, pass);
                        progressDialog.setMessage(getResources().getString(R.string.sign_in_dot));
                        progressDialog.show();
                    } else if (!emailCheck(email) && passwordCheck(pass)) {
                        //Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                        etEmail.setError(getString(R.string.error_invalid_email));
                        etEmail.requestFocus();
                    } else if (!passwordCheck(pass) && emailCheck(email)) {
                        //Toast.makeText(this, getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();
                        etPassword.setError(getString(R.string.error_invalid_password));
                        etPassword.requestFocus();
                    } else {
                        //Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                        etEmail.setError(getString(R.string.error_invalid_email));
                        etEmail.requestFocus();
                    }
                } else if (email.trim().isEmpty()) {
                    if (TextUtils.isEmpty(email)) {
                        //Toast.makeText(LoginActivity.this, getResources().getString(R.string.email_empty), Toast.LENGTH_SHORT).show();
                        etEmail.setError(getString(R.string.email_empty));
                        etEmail.requestFocus();
                    }
                } else if (pass.trim().isEmpty()) {
                    if (TextUtils.isEmpty(pass)) {
                        //Toast.makeText(LoginActivity.this, getResources().getString(R.string.pass_empty), Toast.LENGTH_SHORT).show();
                        etPassword.setError(getString(R.string.pass_empty));
                        etPassword.requestFocus();
                    }
                }

            }

        } else {
            Toast.makeText(LoginActivity.this, R.string.noConnection, Toast.LENGTH_SHORT).show();
        }
    }
     */

    //TODO deprecated api
    /*
    private void loginUser(String email, String password) {
        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi(getApplicationContext()).userLogin("password", email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "response.code()==200");
                    if (response.isSuccessful()) {
                        Log.i(TAG, "İsSuccessful");
                        if (response.body() != null) {
                            Log.i(TAG, "response.body()!=null");
                            Log.i(TAG, response.body().getAccess_token());
                            prefManager.setAuthToken(response.body().getAccess_token());

                            getUserData(email, password);
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 111", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 112", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Log.i(TAG, "response.code()==400");
                    Toast.makeText(LoginActivity.this, apiText.getText(ApiConstant.USER_NOT_FOUND), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 113", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i(TAG, "onFailure");
                Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 114", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
    */

    //TODO deprecated api
    /*
    private void getUserData(String email, String password) {
        Call<UserDataResponse> call = RetrofitClient
                .getInstance().getApi(getApplicationContext()).getUserData(email);

        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "response.code()==200");
                    if (response.isSuccessful()) {
                        Log.i(TAG, "İsSuccessful");
                        if (response.body() != null) {
                            Log.i(TAG, "response.body()!=null");
                            User user = new User(email, password, response.body().getName(), response.body().getSurname(), response.body().getPhoneNumber(), response.body().getDoctorID());
                            prefManager.setUserData(user);

                            getLocationStatus();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 121", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 122", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Log.i(TAG, "response.code()==400");
                    Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 125", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Log.i(TAG, "response.code()==401");
                    Toast.makeText(LoginActivity.this, apiText.getText(ApiConstant.UNAUTHORIZED), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 123", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                Log.i(TAG, "onFailure");
                Log.i(TAG, call.request().toString());
                Log.i(TAG, t.getLocalizedMessage());
                Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 124", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
    */


    //TODO deprecated api
    /*
    private void signUpUser(String email, String password, String name, String surname, String phone, int doctorID) {

        StringBuilder username = new StringBuilder();
        if (name.contains(" ")) {
            String[] nameSurname = name.split(" ");
            for (int i = 0; i < nameSurname.length; i++) {
                username.append(nameSurname[i]);
            }
        } else {
            username.append(name);
        }
        if (surname.contains(" ")) {
            String[] nameSurname = surname.split(" ");
            for (int i = 0; i < nameSurname.length; i++) {
                username.append(nameSurname[i]);
            }
        } else {
            username.append(surname);
        }
        long timestamp = (System.currentTimeMillis() / 1000);
        username.append(getMysqlDataTimeStandart(timestamp));



        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi(getApplicationContext())
                .createUser(email, password, password, email, phone, name, surname, doctorID);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Log.i(TAG, apiText.getText(ApiConstant.USER_CREATED));

                            User user = new User(email, password, name, surname, phone, doctorID);
                            prefManager.setUserData(user);

                            Toast.makeText(LoginActivity.this, apiText.getText(ApiConstant.USER_CREATED), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, CovidMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 101", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 102", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(LoginActivity.this, apiText.getText(ApiConstant.USER_EXISTS), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 103", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 104", Toast.LENGTH_SHORT).show();
                Log.i(TAG, t.getLocalizedMessage());
                progressDialog.dismiss();
            }
        });

    }
    */

    //TODO deprecated api
    /*
    private void getLocationStatus() {
        Call<SetLocationStatusResponse> call = RetrofitClient
                .getInstance().getApi(getApplicationContext()).getUserLocationStatus(prefManager.getUserEmail());

        call.enqueue(new Callback<SetLocationStatusResponse>() {
            @Override
            public void onResponse(Call<SetLocationStatusResponse> call, Response<SetLocationStatusResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            prefManager.setIsLocationCanChange(response.body().isStatus());
                            prefManager.setHomeLocationLat((float) response.body().getLat());
                            prefManager.setHomeLocationLong((float) response.body().getLongt());

                            Toast.makeText(LoginActivity.this, apiText.getText(ApiConstant.USER_AUTHENTICATED), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, CovidMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 461", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 462", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Log.i(TAG, "getLocationStatus response.code()==400");
                    Toast.makeText(getApplicationContext(), apiText.getText(ApiConstant.BAD_REQUEST), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Log.i(TAG, "getLocationStatus response.code()==401");
                    Toast.makeText(getApplicationContext(), apiText.getText(ApiConstant.UNAUTHORIZED), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 463", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SetLocationStatusResponse> call, Throwable t) {
                Log.i(TAG, "onFailure");
                Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 164", Toast.LENGTH_SHORT).show();
            }
        });

    }
    */

    private void signUpLegalSpanMultiply() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.sign_up_legal));
        String legalText = getResources().getString(R.string.sign_up_legal);

        int startIndex = 0, endIndex = 0;
        startIndex = legalText.indexOf(getResources().getString(R.string.for_index_terms_conditions));
        endIndex = getResources().getString(R.string.for_index_terms_conditions).length() + startIndex;

        ss.setSpan(new myClickableSpan(1), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        startIndex = legalText.indexOf(getResources().getString(R.string.for_index_privacy_policy));
        endIndex = getResources().getString(R.string.for_index_privacy_policy).length() + startIndex;

        ss.setSpan(new myClickableSpan(2), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signUpLegalText.setText(ss);
        signUpLegalText.setMovementMethod(LinkMovementMethod.getInstance());
        signUpLegalText.setHighlightColor(Color.TRANSPARENT);
    }

    public class myClickableSpan extends ClickableSpan {

        int pos;

        public myClickableSpan(int position) {
            this.pos = position;
        }

        @Override
        public void onClick(View widget) {
            if (pos == 1) {
                getTermsAndCondition();
            } else if (pos == 2) {
                getPrivacyPolicy();
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    private void getPrivacyPolicy() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_privacy_terms);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        dialog.getWindow().setLayout(dm.widthPixels - 100, dm.heightPixels - 200);
        MaterialButton okay = dialog.findViewById(R.id.privacyTermsOK);
        TextView title = dialog.findViewById(R.id.privacyTermsTitle);
        TextView text = dialog.findViewById(R.id.privacyTermsText);

        title.setText(getString(R.string.privacy_policy));
        text.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));

        okay.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void getTermsAndCondition() {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_privacy_terms);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        dialog.getWindow().setLayout(dm.widthPixels - 100, dm.heightPixels - 200);
        MaterialButton okay = dialog.findViewById(R.id.privacyTermsOK);
        TextView title = dialog.findViewById(R.id.privacyTermsTitle);
        TextView text = dialog.findViewById(R.id.privacyTermsText);

        title.setText(getString(R.string.terms_of_conditions));
        text.setText(Html.fromHtml(getString(R.string.terms_of_conditions_text)));

        okay.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private String getMysqlDataTimeStandart(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp * 1000L);
        return DateFormat.format("yyyy-MM-ddHH:mm:ss", cal).toString();
    }

    private void sendPassReset(String email) {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail(email);

        Call<ResetPasswordResponse> call = RetrofitClient
                .getInstance()
                .getApi(getApplicationContext())
                .resetPassword(request);

        call.enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                getShortToast(getString(R.string.password_reset_info));
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 101");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 102");
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
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                getShortToast(getString(R.string.occurred_error) + " 104");
                Log.i(TAG, t.getLocalizedMessage());
                progressDialog.dismiss();
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
            Toast.makeText(LoginActivity.this, getString(R.string.occurred_error) + " 153", Toast.LENGTH_SHORT).show();
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

    private void getLoginView() {
        state = 1;
        etPassword.setText("");
        textTitle.setText(getString(R.string.log_in));
        textAccountForget.setText(getString(R.string.forget_password));
        textAccountForget.setVisibility(View.VISIBLE); //Şifremi unuttum kullanılmadığı için
        createAccountText.setVisibility(View.VISIBLE);
        loginSignUp.setText(getString(R.string.log_in_button));
        imagePassword.setVisibility(View.VISIBLE);
        etPassword.setVisibility(View.VISIBLE);
        etName.setVisibility(View.GONE);
        etSurname.setVisibility(View.GONE);
        imageName.setVisibility(View.GONE);
        nameDiv.setVisibility(View.GONE);
        etPhone.setVisibility(View.GONE);
        imagePhone.setVisibility(View.GONE);
        phoneDiv.setVisibility(View.GONE);
        etDoctorID.setVisibility(View.GONE);
        imageDoctor.setVisibility(View.GONE);
        doctorDiv.setVisibility(View.GONE);

        signUpLegalText.setVisibility(View.GONE);
    }

    private void getSignUpView() {
        state = 0;
        etPassword.setText("");
        textTitle.setText(getString(R.string.create_account));
        textAccountForget.setText(getString(R.string.have_account));
        textAccountForget.setVisibility(View.VISIBLE); //Şifremi unuttum kullanılmadığı için
        createAccountText.setVisibility(View.GONE);
        loginSignUp.setText(getString(R.string.sign_up_button));
        imagePassword.setVisibility(View.VISIBLE);
        etPassword.setVisibility(View.VISIBLE);
        etName.setVisibility(View.VISIBLE);
        etSurname.setVisibility(View.VISIBLE);
        imageName.setVisibility(View.VISIBLE);
        nameDiv.setVisibility(View.VISIBLE);
        etPhone.setVisibility(View.GONE);
        imagePhone.setVisibility(View.GONE);
        phoneDiv.setVisibility(View.GONE);
        etDoctorID.setVisibility(View.GONE);
        imageDoctor.setVisibility(View.GONE);
        doctorDiv.setVisibility(View.GONE);

        signUpLegalText.setVisibility(View.VISIBLE);
    }

    private void getForgetView() {
        state = 2;
        etPassword.setText("");
        textTitle.setText(getString(R.string.reset_password));
        textAccountForget.setText(getString(R.string.have_account));
        textAccountForget.setVisibility(View.VISIBLE);
        createAccountText.setVisibility(View.VISIBLE);
        loginSignUp.setText(getString(R.string.send_reset));
        imagePassword.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        etName.setVisibility(View.GONE);
        etSurname.setVisibility(View.GONE);
        imageName.setVisibility(View.GONE);
        nameDiv.setVisibility(View.GONE);
        etPhone.setVisibility(View.GONE);
        imagePhone.setVisibility(View.GONE);
        phoneDiv.setVisibility(View.GONE);
        etDoctorID.setVisibility(View.GONE);
        imageDoctor.setVisibility(View.GONE);
        doctorDiv.setVisibility(View.GONE);

        signUpLegalText.setVisibility(View.GONE);
    }

    private void getShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    public void onStart() {
        super.onStart();

        if (prefManager.isLoggedIn()) {
            if (prefManager.getEmailVerified()) {
                Intent intent = new Intent(LoginActivity.this, CovidMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}