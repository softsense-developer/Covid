package com.smartsense.covid.ui.adddoctor;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.api.model.Doctor;
import com.smartsense.covid.api.model.Hospital;
import com.smartsense.covid.api.model.responses.AddDoctorResponse;
import com.smartsense.covid.api.model.responses.GetDoctorResponse;
import com.smartsense.covid.api.model.responses.GetHospitalResponse;
import com.smartsense.covid.api.service.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDoctorFragment extends Fragment {

    private AddDoctorViewModel mViewModel;
    private MaterialButton addDoctorBt;
    private Dialog loadingDialog;
    private ApiConstantText apiText;
    private PrefManager prefManager;
    private static final String TAG = "AddDoctorFragment";
    private long doctorID = -1;
    private TextView addDoctorBeforeText;
    private TextView doctorSelectView, hospitalSelectView;
    private ArrayList<Hospital> hospitalArrayList;
    private ArrayList<String> hospitalNameList;

    private ArrayList<Doctor> doctorArrayList;
    private ArrayList<String> doctorNameList;
    private Dialog dialog;
    private int hospitalPosition = -1, doctorPosition = -1;
    private TextView hospitalNameText, doctorNameText;
    private LinearLayout layoutSelectDoctor;
    private Boolean isHaveDoctor = false;
    private MaterialCardView selectedDoctorView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(AddDoctorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_doctor, container, false);

        addDoctorBt = root.findViewById(R.id.addDoctorBt);
        addDoctorBeforeText = root.findViewById(R.id.addDoctorBeforeText);
        doctorSelectView = root.findViewById(R.id.doctorSelectView);
        hospitalSelectView = root.findViewById(R.id.hospitalSelectView);
        hospitalNameText = root.findViewById(R.id.hospitalNameText);
        doctorNameText = root.findViewById(R.id.doctorNameText);
        layoutSelectDoctor = root.findViewById(R.id.layoutSelectDoctor);
        selectedDoctorView = root.findViewById(R.id.selectedDoctorView);

        prefManager = new PrefManager(getContext());
        apiText = new ApiConstantText(getContext());

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        hospitalArrayList = new ArrayList<>();
        hospitalNameList = new ArrayList<>();
        doctorArrayList = new ArrayList<>();
        doctorNameList = new ArrayList<>();

        if (prefManager.getDoctorID() != 0) {
            /*
            hospitalSelectView.setText(prefManager.getHospitalName());
            doctorSelectView.setText(prefManager.getDoctorNameSurname());
            hospitalSelectView.setEnabled(false);
            doctorSelectView.setEnabled(false);
            addDoctorBt.setEnabled(false);
            addDoctorBeforeText.setVisibility(View.GONE);
             */
            selectedDoctorView.setVisibility(View.VISIBLE);
            layoutSelectDoctor.setVisibility(View.GONE);
            hospitalNameText.setText(prefManager.getHospitalName());
            doctorNameText.setText(prefManager.getDoctorNameSurname());
            addDoctorBt.setText(getString(R.string.change_doctor));
            isHaveDoctor = true;
        }


        hospitalSelectView.setOnClickListener(v -> {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_hospital_spinner);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            DisplayMetrics dm = getResources().getDisplayMetrics();
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();

            EditText editText = dialog.findViewById(R.id.hospitalSearchET);
            ListView listView = dialog.findViewById(R.id.hospitalListView);


            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, hospitalNameList);
            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                hospitalPosition = position;
                getDoctors(hospitalArrayList.get(position).getId());
                hospitalSelectView.setText(adapter.getItem(position));
                doctorSelectView.setText(null);
                dialog.dismiss();
            });
        });

        doctorSelectView.setOnClickListener(v -> {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_doctor_spinner);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            DisplayMetrics dm = getResources().getDisplayMetrics();
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();

            EditText editText = dialog.findViewById(R.id.doctorSearchET);
            ListView listView = dialog.findViewById(R.id.doctorListView);


            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, doctorNameList);
            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                doctorPosition = position;
                doctorSelectView.setText(adapter.getItem(position));
                doctorID = doctorArrayList.get(position).getDoctorId();
                dialog.dismiss();
            });
        });




        addDoctorBt.setOnClickListener(v -> {
            if(isHaveDoctor){
                selectedDoctorView.setVisibility(View.GONE);
                layoutSelectDoctor.setVisibility(View.VISIBLE);
                hospitalNameText.setText(prefManager.getHospitalName());
                doctorNameText.setText(prefManager.getDoctorNameSurname());
                addDoctorBt.setText(getString(R.string.change_doctor));
                isHaveDoctor = false;
            }else{
                if (prefManager.getUserIdentity() != null) {
                    if (doctorID != -1) {
                        addDoctorToServer(doctorID);
                    } else {
                        getShortToast(getString(R.string.select_doctor_not_empty));
                    }
                } else {
                    getShortToast(getString(R.string.add_doctor_info_before));
                }
            }
        });

        getHospitals();

        return root;
    }

    private void getHospitals() {
        hospitalArrayList.clear();
        hospitalNameList.clear();
        hospitalPosition = -1;

        Call<GetHospitalResponse> call = RetrofitClient.getInstance()
                .getApi(getContext()).getHospitals();

        call.enqueue(new Callback<GetHospitalResponse>() {
            @Override
            public void onResponse(Call<GetHospitalResponse> call, Response<GetHospitalResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                for (Hospital hospital : response.body().hospitals) {
                                    hospitalArrayList.add(hospital);
                                    hospitalNameList.add(hospital.getHospitalName());
                                }
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 361");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 362");
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
                            getShortToast(getString(R.string.occurred_error) + " 363");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 364");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetHospitalResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 365");
            }
        });
    }

    private void getDoctors(long hospitalId) {
        doctorArrayList.clear();
        doctorNameList.clear();
        doctorPosition = -1;
        doctorID = -1;

        Call<GetDoctorResponse> call = RetrofitClient.getInstance()
                .getApi(getContext()).getDoctors(hospitalId);

        call.enqueue(new Callback<GetDoctorResponse>() {
            @Override
            public void onResponse(Call<GetDoctorResponse> call, Response<GetDoctorResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                for (Doctor doctor : response.body().doctors) {
                                    doctorArrayList.add(doctor);
                                    doctorNameList.add(doctor.getDoctorName() + " " + doctor.getDoctorSurname());
                                }

                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 371");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 372");
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
                            getShortToast(getString(R.string.occurred_error) + " 373");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 374");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDoctorResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 375");
            }
        });
    }


    private void addDoctorToServer(long id) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }

        Call<AddDoctorResponse> call = RetrofitClient.getInstance()
                .getApi(getContext()).addDoctor(id);

        call.enqueue(new Callback<AddDoctorResponse>() {
            @Override
            public void onResponse(Call<AddDoctorResponse> call, Response<AddDoctorResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                prefManager.setDoctorID(id);
                                prefManager.setDoctorNameSurname(doctorArrayList.get(doctorPosition).getDoctorName()
                                        + " " + doctorArrayList.get(doctorPosition).getDoctorSurname());
                                prefManager.setHospitalName(hospitalArrayList.get(hospitalPosition).getHospitalName());

                                layoutSelectDoctor.setVisibility(View.GONE);
                                selectedDoctorView.setVisibility(View.VISIBLE);
                                hospitalNameText.setText(prefManager.getHospitalName());
                                doctorNameText.setText(prefManager.getDoctorNameSurname());
                                addDoctorBt.setText(getString(R.string.change_doctor));
                                isHaveDoctor = true;

                                getShortToast(getString(R.string.add_doctor_toast));
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 341");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 342");
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
                            getShortToast(getString(R.string.occurred_error) + " 345");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 346");
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AddDoctorResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.occurred_error) + " 343", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void getShortToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}