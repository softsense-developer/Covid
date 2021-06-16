package com.smartsense.covid.ui.companion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartsense.covid.CompanionActivity;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;
import com.smartsense.covid.adapters.companionAdapter.Companion;
import com.smartsense.covid.adapters.companionAdapter.DerpAdapter;
import com.smartsense.covid.adapters.companionAdapter.DerpData;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.api.model.responses.DeleteCompanionResponse;
import com.smartsense.covid.api.model.responses.GetCompanionResponse;
import com.smartsense.covid.api.service.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanionFragment extends Fragment implements DerpAdapter.ItemClickCallback {

    private static final String TAG = "CompanionFragment";
    private RecyclerView recView;
    private LinearLayoutManager layoutManager;
    private DerpAdapter adapter;
    private ArrayList listData;
    private TextView noCompanionText;
    private Dialog loadingDialog;
    private ApiConstantText apiText;
    private FloatingActionButton addCompanionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_companion, container, false);

        recView = root.findViewById(R.id.companionItemRecView);
        noCompanionText = root.findViewById(R.id.noCompanionText);
        addCompanionButton = root.findViewById(R.id.addCompanionButton);

        listData = (ArrayList) DerpData.getListData();
        layoutManager = new LinearLayoutManager(getContext());
        recView.setLayoutManager(layoutManager);
        recView.setHasFixedSize(true);
        adapter = new DerpAdapter(listData, getContext());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        apiText = new ApiConstantText(getContext());

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        addCompanionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CompanionActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private void getCompanions() {
        listData.clear();
        adapter.notifyDataSetChanged();

        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }

        Call<GetCompanionResponse> call = RetrofitClient.getInstance()
                .getApi(getContext()).getCompanions();

        call.enqueue(new Callback<GetCompanionResponse>() {
            @Override
            public void onResponse(Call<GetCompanionResponse> call, Response<GetCompanionResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                for (Companion companion : response.body().companionModels) {
                                    listData.add(companion);
                                }
                                adapter.notifyDataSetChanged();

                                if (listData.size() > 0) {
                                    recView.setVisibility(View.VISIBLE);
                                    noCompanionText.setVisibility(View.GONE);
                                } else {
                                    recView.setVisibility(View.GONE);
                                    noCompanionText.setVisibility(View.VISIBLE);
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
                            getShortToast(getString(R.string.occurred_error) + " 421");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 422");
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
                            getShortToast(getString(R.string.occurred_error) + " 423");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 424");
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetCompanionResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.occurred_error) + " 425", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }


    private void deleteCompanions(Companion companion) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }

        Call<DeleteCompanionResponse> call = RetrofitClient.getInstance()
                .getApi(getContext()).deleteCompanion(companion.getUserid());

        call.enqueue(new Callback<DeleteCompanionResponse>() {
            @Override
            public void onResponse(Call<DeleteCompanionResponse> call, Response<DeleteCompanionResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                listData.remove(companion);
                                adapter.notifyDataSetChanged();

                                if (listData.size() > 0) {
                                    recView.setVisibility(View.VISIBLE);
                                    noCompanionText.setVisibility(View.GONE);
                                } else {
                                    recView.setVisibility(View.GONE);
                                    noCompanionText.setVisibility(View.VISIBLE);
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
                            getShortToast(getString(R.string.occurred_error) + " 431");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 432");
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
                            getShortToast(getString(R.string.occurred_error) + " 433");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 434");
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DeleteCompanionResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.occurred_error) + " 435", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void getDeleteDialog(Companion companion) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delete);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(dm.widthPixels - 100, LinearLayout.LayoutParams.WRAP_CONTENT);

        MaterialButton deleteBt = dialog.findViewById(R.id.deleteYesBt);
        MaterialButton noBt = dialog.findViewById(R.id.deleteNoBt);

        deleteBt.setOnClickListener(view -> {
            deleteCompanions(companion);
            dialog.dismiss();
        });
        noBt.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }


    private void getShortToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteClick(int p) {
        Companion companion = (Companion) listData.get(p);
        getDeleteDialog(companion);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCompanions();
    }
}