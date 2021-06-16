package com.smartsense.covid.ui.datasent;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.opencsv.CSVWriter;
import com.smartsense.covid.MyConstant;
import com.smartsense.covid.R;
import com.smartsense.covid.model.Covid;
import com.smartsense.covid.adapters.selectBackupAdapter.BackupItem;
import com.smartsense.covid.adapters.selectBackupAdapter.DerpAdapter;
import com.smartsense.covid.adapters.selectBackupAdapter.DerpData;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DataSentFragment extends Fragment {

    private DataSentViewModel mViewModel;
    private MaterialCardView backupAndSentButton, selectBackupAndSentButton;
    private final int MY_PERMISSIONS = 136;
    private Snackbar snackbar;
    private Dialog backupDialog;
    boolean isBackupAndSentClick = true;
    private ArrayList listData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(DataSentViewModel.class);
        View root = inflater.inflate(R.layout.data_sent_fragment, container, false);

        backupAndSentButton = root.findViewById(R.id.backupAndSentButton);
        selectBackupAndSentButton = root.findViewById(R.id.selectBackupAndSentButton);


        backupAndSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    backupAndSent();
                } else {
                    isBackupAndSentClick = true;
                    mRequestPermissions();
                }
            }
        });

        selectBackupAndSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    getSelectBackupDialog();

                } else {
                    isBackupAndSentClick = false;
                    mRequestPermissions();
                }
            }
        });

        return root;
    }

    private void backupAndSent() {
        try {
            getBackupDialog(true);

            File file;

            File directory = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Log.i("SmartSense", directory.getPath());

            long millisecond = new Date().getTime();
            DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            df.setTimeZone(TimeZone.getDefault());

            String fileName = getString(R.string.app_name_no_space) + "_" + df.format(new Date(millisecond)) + ".csv";

            file = new File(directory, fileName);
            if (file.exists()) {
                file.delete();
            }

            Log.i("SmartSense", file.getPath());

            CSVWriter writer = new CSVWriter(new FileWriter(file), ';'
                    , CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);


            List<String[]> data = new ArrayList<>();
            List<Covid> allData = mViewModel.getAllData();

            if (allData.size() > 0) {
                String[] headerRecord = {getString(R.string.csv_data), getString(R.string.csv_data_type)
                        , getString(R.string.csv_save_type), getString(R.string.csv_save_type)};
                writer.writeNext(headerRecord);

                for (int i = 0; i < allData.size(); i++) {
                    if (allData.get(i).getDataType() == MyConstant.TEMP) {
                        data.add(new String[]{String.format(Locale.getDefault(), "%.2f", allData.get(i).getData())
                                , getDataType(allData.get(i).getDataType()), getSaveType(allData.get(i).getSaveType())
                                , getDate(allData.get(i).getTime())});
                    } else {
                        data.add(new String[]{String.valueOf((int)allData.get(i).getData())
                                , getDataType(allData.get(i).getDataType()), getSaveType(allData.get(i).getSaveType())
                                , getDate(allData.get(i).getTime())});
                    }
                }


                writer.writeAll(data);
                writer.close();

                Toast.makeText(getContext(), getString(R.string.back_up_done), Toast.LENGTH_SHORT).show();

                shareFile(file.getName());
            }

            if (backupDialog != null) {
                backupDialog.dismiss();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            Log.i("settingsEr", "Error:" + e.getLocalizedMessage());
            if (backupDialog != null) {
                backupDialog.dismiss();
            }
        }
    }

    private void getBackupDialog(boolean isBackup) {
        backupDialog = new Dialog(getActivity());
        backupDialog.setContentView(R.layout.dialog_backup);
        backupDialog.setCancelable(false);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        backupDialog.getWindow().setLayout(dm.widthPixels - 100, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView loadingText = backupDialog.findViewById(R.id.backupRestoreLoadingText);
        if (!isBackup) {
            loadingText.setText(getString(R.string.restore_dot));
        } else {
            loadingText.setText(getString(R.string.backup_dot));
        }

        backupDialog.show();

    }

    private String getDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp * 1000L);
        return android.text.format.DateFormat.format("dd/MM/yyyy - HH:mm:ss", cal).toString();
    }

    private void getSelectBackupDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_select_backup);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        dialog.getWindow().setLayout(dm.widthPixels - 100, LinearLayout.LayoutParams.WRAP_CONTENT);
        RecyclerView recyclerView = dialog.findViewById(R.id.dialogSelectBackupRecView);

        listData = (ArrayList) DerpData.getListData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DerpAdapter adapter = new DerpAdapter(listData, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setListData(listData);
        adapter.notifyDataSetChanged();
        adapter.setItemClickCallback(new DerpAdapter.ItemClickCallback() {
            @Override
            public void onItemClick(int p) {
                BackupItem item = (BackupItem) listData.get(p);
                shareFile(item.getBackupName());
                dialog.dismiss();
            }
        });

        File file[] = null;
        File f = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/");
        file = f.listFiles();


        if (file != null) {
            for (int i = 0; i < file.length; i++) {
                //here populate your listview
                Log.d("Files", "FileName: " + file[i].getName());
                Log.d("Files", "FilePath:" + file[i].getAbsolutePath());
                BackupItem item = new BackupItem();
                item.setBackupName(file[i].getName());
                item.setPath(file[i].getPath());
                listData.add(item);
                adapter.notifyItemInserted(listData.indexOf(item));
            }
            dialog.show();
        } else {
            Toast.makeText(getContext(), getString(R.string.not_found_backup), Toast.LENGTH_LONG).show();
        }


    }

    private void shareFile(String fileName) {
        //OLD CODE
        /*Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(path);

        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("text/*");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, fileName);
            intentShareFile.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name));

            startActivity(Intent.createChooser(intentShareFile, getString(R.string.select_app_to_sent)));
        }

         */
        Context context = getContext();
        File filelocation = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        Uri path = FileProvider.getUriForFile(context, "com.smartsense.covid.fileprovider", filelocation);
        Intent fileIntent = new Intent(Intent.ACTION_SEND);
        fileIntent.setType("text/csv");
        fileIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " Data");
        fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileIntent.putExtra(Intent.EXTRA_STREAM, path);
        startActivity(Intent.createChooser(fileIntent, getString(R.string.select_app_to_sent)));
    }

    private String getDataType(int type) {
        if (type == MyConstant.HEART) {
            return getString(R.string.heart);
        } else if (type == MyConstant.SPO2) {
            return getString(R.string.spo2);
        } else {
            return getString(R.string.temperature);
        }
    }

    private String getSaveType(int type) {
        if (type == MyConstant.MANUEL_SAVE) {
            return getString(R.string.manuel_save);
        } else {
            return getString(R.string.auto_save);
        }
    }


    private int getDataType(String type) {
        if (type.equals(getString(R.string.heart))) {
            return MyConstant.HEART;
        } else if (type.equals(getString(R.string.spo2))) {
            return MyConstant.SPO2;
        } else {
            return MyConstant.TEMP;
        }
    }

    private int getSaveType(String type) {
        if (type.equals(getString(R.string.manuel_save))) {
            return MyConstant.MANUEL_SAVE;
        } else {
            return MyConstant.AUTO_SAVE;
        }
    }

    public void mRequestPermissions() {
        try {
            String[] permissionRequested = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (snackbar != null) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                }
                if (isBackupAndSentClick) {
                    backupAndSent();
                } else {
                    getSelectBackupDialog();
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionRequested, MY_PERMISSIONS);
                }
            }
        } catch (Exception e) {
            Log.w("settingsWarning", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (snackbar != null) {
                        if (snackbar.isShown()) {
                            snackbar.dismiss();
                        }
                    }
                    if (isBackupAndSentClick) {
                        backupAndSent();
                    } else {
                        getSelectBackupDialog();
                    }
                } else {
                    snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            getString(R.string.write_external_permission), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.setAction(getString(R.string.give_permission), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }
                    }).show();
                }
                break;
        }
    }

}