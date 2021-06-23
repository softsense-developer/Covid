package com.smartsense.covid.ui.request;

import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.smartsense.covid.CovidMainActivity;
import com.smartsense.covid.LoginActivity;
import com.smartsense.covid.MyConstant;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;
import com.smartsense.covid.SplashActivity;
import com.smartsense.covid.adapters.requestAdapter.DerpAdapter;
import com.smartsense.covid.adapters.requestAdapter.DerpData;
import com.smartsense.covid.adapters.requestAdapter.Promotion;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.api.model.requests.PatientConnectionRequest;
import com.smartsense.covid.api.model.responses.GetPromotionsResponse;
import com.smartsense.covid.api.model.responses.PatientConnectionResponse;
import com.smartsense.covid.api.service.RetrofitClient;
import com.smartsense.covid.settings.SettingsActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFragment extends Fragment implements DerpAdapter.ItemClickCallback {

    private static final String TAG = "RequestFragment";
    private RequestViewModel mViewModel;
    private RecyclerView recView;
    private LinearLayoutManager layoutManager;
    private DerpAdapter adapter;
    private ArrayList listData;
    private TextView noRequestText;
    private Dialog loadingDialog;
    private ApiConstantText apiText;
    private PrefManager prefManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request, container, false);
        mViewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        recView = root.findViewById(R.id.requestItemRecView);
        noRequestText = root.findViewById(R.id.noRequestText);

        listData = (ArrayList) DerpData.getListData();
        layoutManager = new LinearLayoutManager(getContext());
        recView.setLayoutManager(layoutManager);
        recView.setHasFixedSize(true);
        adapter = new DerpAdapter(listData, getContext());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        prefManager = new PrefManager(getContext());
        apiText = new ApiConstantText(getContext());

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        getRequests();

        return root;
    }



    private void patientConnectionAcceptRefuse(Promotion promotion, long id, boolean isAccepted) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }

        PatientConnectionRequest request = new PatientConnectionRequest();
        request.setId(id);
        request.setAccept(isAccepted);

        Call<PatientConnectionResponse> call = RetrofitClient.getInstance()
                .getApi(getContext()).getPatientConnectionAcceptRefuse(request);

        call.enqueue(new Callback<PatientConnectionResponse>() {
            @Override
            public void onResponse(Call<PatientConnectionResponse> call, Response<PatientConnectionResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                listData.remove(promotion);
                                adapter.notifyDataSetChanged();
                                if (isAccepted) {
                                    prefManager.setUserRole(MyConstant.DOCTOR_ROLE);
                                    getLongToast(getString(R.string.request_role_doctor));

                                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    try {
                                        ((CovidMainActivity)getContext()).setNotPatientNavigationDrawer();
                                    }catch (Exception e){
                                        Log.e(TAG, "onCreateView: "+ e.getMessage());
                                    }
                                } else {
                                    getShortToast(getString(R.string.request_refuse));
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
                            getShortToast(getString(R.string.occurred_error) + " 411");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 412");
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
                            getShortToast(getString(R.string.occurred_error) + " 415");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 416");
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PatientConnectionResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.occurred_error) + " 413", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void getRequests() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }

        Call<GetPromotionsResponse> call = RetrofitClient.getInstance()
                .getApi(getContext()).getPatientPromotions();

        call.enqueue(new Callback<GetPromotionsResponse>() {
            @Override
            public void onResponse(Call<GetPromotionsResponse> call, Response<GetPromotionsResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                for (Promotion promotions : response.body().connectionRequests) {
                                    listData.add(promotions);
                                }
                                adapter.notifyDataSetChanged();

                                if (listData.size() > 0) {
                                    recView.setVisibility(View.VISIBLE);
                                    noRequestText.setVisibility(View.GONE);
                                } else {
                                    recView.setVisibility(View.GONE);
                                    noRequestText.setVisibility(View.VISIBLE);
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
                            getShortToast(getString(R.string.occurred_error) + " 381");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 382");
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
                            getShortToast(getString(R.string.occurred_error) + " 385");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 386");
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetPromotionsResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.occurred_error) + " 383", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onAcceptClick(int p) {
        Promotion promotion = (Promotion) listData.get(p);
        patientConnectionAcceptRefuse(promotion, promotion.getId(), true);
    }

    @Override
    public void onRefuseClick(int p) {
        Promotion promotion = (Promotion) listData.get(p);
        patientConnectionAcceptRefuse(promotion, promotion.getId(), false);
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
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}