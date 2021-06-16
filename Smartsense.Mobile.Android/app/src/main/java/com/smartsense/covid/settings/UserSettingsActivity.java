package com.smartsense.covid.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;
import com.smartsense.covid.api.model.requests.AddInfoRequest;
import com.smartsense.covid.api.model.responses.AddInfoResponse;
import com.smartsense.covid.api.model.responses.PatientInfoResponse;
import com.smartsense.covid.api.model.requests.UserInfoRequest;
import com.smartsense.covid.api.model.responses.UserInfoResponse;
import com.smartsense.covid.api.service.RetrofitClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserSettingsActivity extends AppCompatActivity {


    private TextInputLayout userName, userSurname, userPhone, userIdentity, userBirthDate;
    private PrefManager prefManager;
    private MaterialButton userDataSave, patientDataSave;
    private boolean isSave = true;
    private String userNameText, userSurnameText, userPhoneText, identityNumberText, dateOfBirthText;
    private ApiConstantText apiText;
    private String TAG = "Smartsense";
    private Dialog loadingDialog;
    private Spinner genderSpinner, contactSpinner;
    private int contactType = 0, genderType = 0;
    private int birthdayYear, birthdayMonth, birthdayDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        userName = findViewById(R.id.userNameTI);
        userSurname = findViewById(R.id.userSurnameTI);
        userPhone = findViewById(R.id.userPhoneTI);
        userIdentity = findViewById(R.id.userIdentityTIL);
        userBirthDate = findViewById(R.id.userBirthDateTIL);
        genderSpinner = findViewById(R.id.userGenderSpinner);
        contactSpinner = findViewById(R.id.contactSpinner);
        userDataSave = findViewById(R.id.userDataSaveButton);
        patientDataSave = findViewById(R.id.patientDataSaveButton);

        prefManager = new PrefManager(getApplicationContext());
        apiText = new ApiConstantText(getApplicationContext());

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (prefManager.getName() != null) {
            userName.getEditText().setText(prefManager.getName());
            isSave = false;
        }
        if (prefManager.getSurname() != null) {
            userSurname.getEditText().setText(prefManager.getSurname());
            isSave = false;
        }
        if (prefManager.getUserPhone() != null) {
            userPhone.getEditText().setText(prefManager.getUserPhone());
            isSave = false;
        }
        if (prefManager.getUserIdentity() != null) {
            userIdentity.getEditText().setText(prefManager.getUserIdentity());
            isSave = false;
            patientDataSave.setText(getString(R.string.update));
        }
        if (prefManager.getUserBirthday() != -1) {
            userBirthDate.getEditText().setText(convertTimestampToDate(prefManager.getUserBirthday()));
            isSave = false;
        }


        if (!isSave) {
            userDataSave.setText(getString(R.string.update));
        }


        if (savedInstanceState != null) {
            userName.getEditText().setText(savedInstanceState.getString("userName"));
            userSurname.getEditText().setText(savedInstanceState.getString("userSurname"));
            userPhone.getEditText().setText(savedInstanceState.getString("userPhone"));
            userIdentity.getEditText().setText(savedInstanceState.getString("userIdentity"));
            userBirthDate.getEditText().setText(savedInstanceState.getString("userBirthDate"));
            genderSpinner.setSelection(savedInstanceState.getInt("userGender"));
            contactSpinner.setSelection(savedInstanceState.getInt("userContact"));
        }

        ArrayAdapter genderAdapter = new ArrayAdapter(this, R.layout.spinner_item, getResources().getStringArray(R.array.gender));
        genderSpinner.setAdapter(genderAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter contactArray = new ArrayAdapter(this, R.layout.spinner_item, getResources().getStringArray(R.array.contact_array));
        contactSpinner.setAdapter(contactArray);

        contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contactType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (prefManager.getUserGender() != -1) {
            genderSpinner.setSelection(prefManager.getUserGender());
            genderType = prefManager.getUserGender();
        }

        if (prefManager.getUserTrackingType() != -1) {
            contactSpinner.setSelection(prefManager.getUserTrackingType());
            contactType = prefManager.getUserTrackingType();
        }

        userBirthDate.getEditText().setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            birthdayYear = calendar.get(Calendar.YEAR);
            birthdayMonth = calendar.get(Calendar.MONTH);
            birthdayDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(this,
                    (view1, year, month, dayOfMonth) -> {
                        month += 1;
                        userBirthDate.getEditText().setText(dayOfMonth + "/" + month + "/" + year);


                    }, birthdayYear, birthdayMonth, birthdayDay);

            dpd.setButton(TimePickerDialog.BUTTON_POSITIVE, getString(R.string.select), dpd);
            dpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), dpd);
            dpd.show();
        });


        userDataSave.setOnClickListener(view -> {
            userName.setError(null);
            userPhone.setError(null);
            userSurname.setError(null);

            userNameText = userName.getEditText().getText().toString().trim();
            userSurnameText = userSurname.getEditText().getText().toString().trim();
            userPhoneText = userPhone.getEditText().getText().toString().trim();

            if (!userNameText.isEmpty() && !userSurnameText.isEmpty() && !userPhoneText.isEmpty()) {
                if (userPhoneText.length() == 10) {
                    if (isConnectionHas()) {
                        if (loadingDialog != null && !loadingDialog.isShowing()) {
                            loadingDialog.show();
                        }
                        UserInfoRequest request = new UserInfoRequest();
                        request.setName(userNameText);
                        request.setSurname(userSurnameText);
                        request.setPhone(userPhoneText);
                        updateProfileInfo(request);
                    } else {
                        Toast.makeText(UserSettingsActivity.this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                    }

                    hideKeyboard(UserSettingsActivity.this);
                } else {
                    if (userPhoneText.length() != 10) {
                        userPhone.setError(getString(R.string.phone_must_length));
                    }

                    errorNull();
                }

            } else {
                if (userNameText.isEmpty()) {
                    userName.setError(getString(R.string.not_empty));
                }
                if (userSurnameText.isEmpty()) {
                    userSurname.setError(getString(R.string.not_empty));
                }
                if (userPhoneText.isEmpty()) {
                    userPhone.setError(getString(R.string.not_empty));
                }
                errorNull();
            }
        });

        patientDataSave.setOnClickListener(view -> {
            userIdentity.setError(null);
            userBirthDate.setError(null);

            identityNumberText = userIdentity.getEditText().getText().toString().trim();
            dateOfBirthText = userBirthDate.getEditText().getText().toString();

            if (!identityNumberText.isEmpty() && !dateOfBirthText.isEmpty()) {
                if (identityNumberText.length() == 11) {
                    if (isConnectionHas()) {
                        if (loadingDialog != null && !loadingDialog.isShowing()) {
                            loadingDialog.show();
                        }
                        AddInfoRequest addInfoRequest = new AddInfoRequest();
                        addInfoRequest.setIdentityNumber(identityNumberText);
                        addInfoRequest.setGender(genderType);
                        addInfoRequest.setDateOfBirth(convertTimestampToIso8601(convertDateToTimestamp(dateOfBirthText)));
                        addInfoRequest.setUserStatus(contactType);
                        addInfoRequest.setDiagnosis("0");

                        if (prefManager.getUserIdentity() != null) {
                            updatePatientInfo(addInfoRequest);
                        } else {
                            addPatientInfo(addInfoRequest);
                        }
                    } else {
                        Toast.makeText(UserSettingsActivity.this, getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                    }

                    hideKeyboard(UserSettingsActivity.this);
                } else {
                    if (identityNumberText.length() != 11) {
                        userIdentity.setError(getString(R.string.identity_must_length));
                    }
                    errorNull();
                }

            } else {
                if (identityNumberText.isEmpty()) {
                    userIdentity.setError(getString(R.string.not_empty));
                }
                if (dateOfBirthText.isEmpty()) {
                    userBirthDate.setError(getString(R.string.not_empty));
                }
                errorNull();
            }
        });


        getUserInfo();
        if(prefManager.getUserIdentity()!=null){
            getPatientInfo();
        }

    }

    private boolean isConnectionHas() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void getUserInfo() {
        Call<UserInfoResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).getProfileInfo();

        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                prefManager.setName(response.body().getName());
                                prefManager.setSurname(response.body().getSurname());
                                prefManager.setUserPhone(response.body().getPhone());

                                userName.getEditText().setText(response.body().getName());
                                userSurname.getEditText().setText(response.body().getSurname());
                                userPhone.getEditText().setText(response.body().getPhone());
                                Log.i(TAG, "User data updated.");
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
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
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 263", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getPatientInfo() {
        Call<PatientInfoResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).getPatientInfo();

        call.enqueue(new Callback<PatientInfoResponse>() {
            @Override
            public void onResponse(Call<PatientInfoResponse> call, Response<PatientInfoResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
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

                                genderType = response.body().getGender();
                                contactType = response.body().getUserStatus();

                                userName.getEditText().setText(response.body().getName());
                                userSurname.getEditText().setText(response.body().getSurname());
                                userIdentity.getEditText().setText(response.body().getIdentityNumber());
                                userBirthDate.getEditText().setText(convertTimestampToDate(convertIso8601DateToTimestamp(response.body().getDateOfBirth())));
                                genderSpinner.setSelection(response.body().getGender());
                                contactSpinner.setSelection(response.body().getUserStatus());
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
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
            }

            @Override
            public void onFailure(Call<PatientInfoResponse> call, Throwable t) {
                Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 263", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileInfo(UserInfoRequest request) {
        Call<UserInfoResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).updateProfileInfo(request);

        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                prefManager.setName(response.body().getName());
                                prefManager.setSurname(response.body().getSurname());
                                prefManager.setUserPhone(response.body().getPhone());

                                getShortToast(apiText.getText(ApiConstant.USER_UPDATE));
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
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
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 263", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void addPatientInfo(AddInfoRequest request) {
        Call<AddInfoResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).addPatientInfo(request);

        call.enqueue(new Callback<AddInfoResponse>() {
            @Override
            public void onResponse(Call<AddInfoResponse> call, Response<AddInfoResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                patientDataSave.setText(getString(R.string.update));
                                prefManager.setUserIdentity(request.getIdentityNumber());
                                prefManager.setUserGender(request.getGender());
                                prefManager.setUserBirthday(convertIso8601DateToTimestamp(request.getDateOfBirth()));
                                prefManager.setUserTrackingType(request.getUserStatus());
                                prefManager.setUserDiagnosis(request.getDiagnosis());
                                genderType = request.getGender();
                                contactType = request.getUserStatus();

                                getShortToast(apiText.getText(ApiConstant.USER_UPDATE));
                                Log.i(TAG, "User data updated.");
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 261");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 262");
                    }
                } else {
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

                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AddInfoResponse> call, Throwable t) {
                Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 263", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: ");
                loadingDialog.dismiss();
            }
        });
    }


    private void updatePatientInfo(AddInfoRequest request) {
        Log.i(TAG, "updatePatientInfo: ");
        Call<AddInfoResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).updatePatientInfo(request);

        call.enqueue(new Callback<AddInfoResponse>() {
            @Override
            public void onResponse(Call<AddInfoResponse> call, Response<AddInfoResponse> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "onResponse: 200");
                    if (response.isSuccessful()) {
                        Log.i(TAG, "onResponse: successful");
                        if (response.body() != null) {
                            Log.i(TAG, "onResponse: body!=null");
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, "updatePatientInfo: 200");
                                Log.i(TAG, "updatePatientInfo: " + request.toString());

                                prefManager.setUserIdentity(request.getIdentityNumber());
                                prefManager.setUserGender(request.getGender());
                                prefManager.setUserBirthday(convertIso8601DateToTimestamp(request.getDateOfBirth()));
                                prefManager.setUserTrackingType(request.getUserStatus());
                                prefManager.setUserDiagnosis(request.getDiagnosis());

                                genderType = request.getGender();
                                contactType = request.getUserStatus();

                                getShortToast(apiText.getText(ApiConstant.USER_UPDATE));
                                Log.i(TAG, "User data updated.");

                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                Log.i(TAG, "updatePatientInfo: 400");
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 261");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 262");
                    }
                } else {
                    Log.i(TAG, "else: " + response.code());
                    Log.i(TAG, "else: " + response.errorBody().toString());

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

                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AddInfoResponse> call, Throwable t) {
                Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 263", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: ");
                loadingDialog.dismiss();
            }
        });
    }

    //TODO: Old api
    /*
    private void updateUserData(String name, String surname, String phone) {
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).updateUser(
                        prefManager.getUserEmail(), name, surname, phone);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "response.code() == 200");
                    if (response.isSuccessful()) {
                        Log.i(TAG, "response.isSuccessful()");
                        if (response.body() != null) {
                            Log.i(TAG, "response.body() != null");
                            prefManager.setName(name);
                            prefManager.setSurname(surname);
                            prefManager.setUserPhone(phone);
                            Toast.makeText(UserSettingsActivity.this, apiText.getText(150), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 128", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 121", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 125", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(UserSettingsActivity.this, apiText.getText(ApiConstant.UNAUTHORIZED), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 124", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UserSettingsActivity.this, getString(R.string.occurred_error) + " 123", Toast.LENGTH_SHORT).show();
                Log.i(TAG, call.request().toString());
                Log.i(TAG, t.getLocalizedMessage());
                loadingDialog.dismiss();
            }
        });
    }
*/

    private long convertIso8601DateToTimestamp(String date) {
        try {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date result1 = df1.parse(date);
            return result1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String convertTimestampToIso8601(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return android.text.format.DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", cal).toString();
    }

    private String convertTimestampToDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return android.text.format.DateFormat.format("dd/MM/yyyy", cal).toString();
    }

    private long convertDateToTimestamp(String date) {
        try {
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date result1 = df1.parse(date);
            return result1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean emailCheck(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void errorNull() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                userName.setError(null);
                userPhone.setError(null);
                userSurname.setError(null);
                userIdentity.setError(null);
                userBirthDate.setError(null);
            }
        }, 4000);
    }

    private void getShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("userName", userName.getEditText().getText().toString().trim());
        outState.putString("userSurname", userSurname.getEditText().getText().toString().trim());
        outState.putString("userPhone", userPhone.getEditText().getText().toString().trim());
        outState.putString("userIdentity", userIdentity.getEditText().getText().toString().trim());
        outState.putString("userBirthDate", userBirthDate.getEditText().getText().toString().trim());
        outState.putInt("userGender", genderType);
        outState.putInt("userContact", contactType);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}