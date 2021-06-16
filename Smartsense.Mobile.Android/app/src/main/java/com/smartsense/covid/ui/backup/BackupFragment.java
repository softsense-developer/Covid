package com.smartsense.covid.ui.backup;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.smartsense.covid.MyConstant;
import com.smartsense.covid.R;
import com.smartsense.covid.model.Covid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

public class BackupFragment extends Fragment {

    private BackupViewModel backupViewModel;
    private MaterialCardView backupButton, restoreButton;
    private final int MY_PERMISSIONS = 126;
    private Snackbar snackbar;
    private boolean isBackupClick = true;
    private Dialog backupDialog;
    private TextView textBackup;
    private int restoreCorrect = 0, restoreError = 0, restoreSame = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        backupViewModel =
                ViewModelProviders.of(this).get(BackupViewModel.class);
        View root = inflater.inflate(R.layout.backup_fragment, container, false);

        backupButton = root.findViewById(R.id.backupButton);
        restoreButton = root.findViewById(R.id.restoreButton);
        textBackup = root.findViewById(R.id.textBackup);

        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    backup();
                    //backupThree();
                } else {
                    isBackupClick = true;
                    mRequestPermissions();
                }
            }
        });

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent()
                            .setType("*/*")
                            .setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_backup_file)), 111);

                } else {
                    isBackupClick = false;
                    mRequestPermissions();
                }
            }
        });


        return root;
    }

    private void backup() {
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
            List<Covid> allData = backupViewModel.getAllData();

            if (allData.size() > 0) {
                String[] headerRecord = {getString(R.string.csv_data), getString(R.string.csv_data_type)
                        , getString(R.string.csv_save_type), getString(R.string.csv_save_type)};
                writer.writeNext(headerRecord);

                Log.i("Smartsense", String.valueOf(allData.size()));
                for (int i = 0; i < allData.size(); i++) {
                    if (allData.get(i).getDataType() == MyConstant.TEMP) {
                        data.add(new String[]{String.format(Locale.getDefault(), "%.2f", allData.get(i).getData())
                                , getDataType(allData.get(i).getDataType()), getSaveType(allData.get(i).getSaveType())
                                , getDate(allData.get(i).getTime())});
                    } else {
                        data.add(new String[]{String.valueOf((int) allData.get(i).getData())
                                , getDataType(allData.get(i).getDataType()), getSaveType(allData.get(i).getSaveType())
                                , getDate(allData.get(i).getTime())});
                    }

                }

                writer.writeAll(data);
                writer.close();

                Toast.makeText(getContext(), getString(R.string.back_up_done), Toast.LENGTH_SHORT).show();
                textBackup.setText((file.getPath() + " " + getString(R.string.saved_path)));
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

    private String getDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp * 1000L);
        return android.text.format.DateFormat.format("dd/MM/yyyy - HH:mm:ss", cal).toString();
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

    private void restoreData(String path) {
        try {
            getBackupDialog(false);
            restoreCorrect = 0;
            restoreError = 0;
            restoreSame = 0;


            Log.i("Smartsense", path);
            BufferedReader reader = null;
            /*try {
                Log.i("Smartsense", "İlkinde");
                File file = new File(path);
                reader = new BufferedReader(new FileReader(file));
            } catch (java.io.FileNotFoundException e) {
                try {
                    Log.i("Smartsense", "ikincide");
                    String[] paths = path.split(":");
                    File file = new File(Environment.getExternalStorageDirectory() + "/" + paths[1]);
                    reader = new BufferedReader(new FileReader(file));
                } catch (java.io.FileNotFoundException ex) {
                    Log.i("Smartsense", "ikinci hata");
                    Toast.makeText(getActivity(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
             */


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

              /*  try {
                    File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + getString(R.string.app_name_no_space) + "/" + path);
                    reader = new BufferedReader(new FileReader(file));
                } catch (java.io.FileNotFoundException ex) {
                    Toast.makeText(getActivity(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
               */


                try {
                    Log.i("Smartsense", "İlkinde");
                    File file = new File(path);
                    reader = new BufferedReader(new FileReader(file));
                } catch (java.io.FileNotFoundException e) {
                    try {
                        Log.i("Smartsense", "ikincide");
                        String[] paths = path.split(":");
                        File file = new File(Environment.getExternalStorageDirectory() + "/" + paths[1]);
                        reader = new BufferedReader(new FileReader(file));
                    } catch (java.io.FileNotFoundException ex) {
                        Log.i("Smartsense", "ikinci hata");
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
               /* try {
                    File file = new File(path);
                    reader = new BufferedReader(new FileReader(file));
                } catch (java.io.FileNotFoundException ex) {
                    Toast.makeText(getActivity(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                */

                try {
                    Log.i("Smartsense", "İlkinde");
                    File file = new File(path);
                    reader = new BufferedReader(new FileReader(file));
                } catch (java.io.FileNotFoundException e) {
                    try {
                        Log.i("Smartsense", "ikincide");
                        String[] paths = path.split(":");
                        File file = new File(Environment.getExternalStorageDirectory() + "/" + paths[1]);
                        reader = new BufferedReader(new FileReader(file));
                    } catch (java.io.FileNotFoundException ex) {
                        Log.i("Smartsense", "ikinci hata");
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }


            final CSVParser parser =
                    new CSVParserBuilder()
                            .withSeparator(';')
                            .withIgnoreQuotations(true)
                            .build();

            final CSVReader csvReader =
                    new CSVReaderBuilder(reader)
                            .withSkipLines(1)
                            .withCSVParser(parser)
                            .build();
            //CSVReader csvReader = new CSVReader(reader, ';');

            String[] data;
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            while ((data = csvReader.readNext()) != null) {
                if (!data[0].isEmpty() && !data[1].isEmpty() && !data[2].isEmpty() && !data[3].isEmpty()) {
                    Covid restore = new Covid(getTimestamp(data[3]), format.parse(data[0]).doubleValue(), getDataType(data[1]), getSaveType(data[2]));
                    if (data[0] != null && data[1] != null && data[2] != null && data[3] != null) {
                        if (!backupViewModel.isDataExist(restore.getTime())) {
                            backupViewModel.insert(restore);
                            restoreCorrect++;
                        } else {
                            restoreSame++;
                        }
                    } else {
                        restoreError++;
                    }

                } else {
                    restoreError++;
                }
            }


            Toast.makeText(getActivity(), getString(R.string.restore_done), Toast.LENGTH_SHORT).show();
            if (backupDialog != null) {
                backupDialog.dismiss();
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    getRestoredDialog(restoreCorrect, restoreSame, restoreError);
                }
            }, 500);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.getCause();
            if (backupDialog != null) {
                backupDialog.dismiss();
            }
        }
    }

    private void startBackup() {
        Intent exportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        exportIntent.addCategory(Intent.CATEGORY_OPENABLE);
        exportIntent.setType("text/csv");
        String filename = "test.csv";
        exportIntent.putExtra(Intent.EXTRA_TITLE, filename);
        startActivityForResult(exportIntent, 12345);
    }

    private void backupTwo(Uri uri) {
        try {
            Log.i("SmartSense", uri.getPath());
            CSVWriter writer = new CSVWriter(new FileWriter(uri.getPath()), ';'
                    , CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

            List<String[]> data = new ArrayList<>();
            List<Covid> allData = backupViewModel.getAllData();

            if (allData.size() > 0) {
                String[] headerRecord = {getString(R.string.csv_data), getString(R.string.csv_data_type)
                        , getString(R.string.csv_save_type), getString(R.string.csv_save_type)};
                writer.writeNext(headerRecord);

                Log.i("Smartsense", String.valueOf(allData.size()));
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
                textBackup.setText((uri.getPath() + " " + getString(R.string.saved_path)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void backupThree() {
        long millisecond = new Date().getTime();
        DateFormat df = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
        df.setTimeZone(TimeZone.getDefault());

        String fileName = getString(R.string.app_name_no_space) + "_" + df.format(new Date(millisecond)) + ".csv";

       /* ContentResolver resolver = getContext().getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, "document/csv");
        //contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Download/" + getString(R.string.app_name_no_space));
        Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentValues values = new ContentValues();
            values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.Files.FileColumns.MIME_TYPE, "text/csv");
            values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/" + getString(R.string.app_name_no_space));
            Uri uri = getContext().getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

            Log.i("Smartsense", "Three: " + uri.getPath());
            //imageOutStream = getContext().getContentResolver().openOutputStream(uri);
        } else {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
            File file = new File(path, fileName);
            //imageOutStream = new FileOutputStream(image);
            Log.i("Smartsense", "Three: " + file.getPath());
            Log.i("Smartsense", "Three: " + file.getAbsolutePath());
        }


        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("document/csv")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_backup_file)), 111);
    }

    private long getTimestamp(String dateString) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            Date date = (Date) formatter.parse(dateString);
            return date.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
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


    private void getRestoredDialog(int correct, int same, int error) {
        final Dialog restoredDialog = new Dialog(getActivity());
        restoredDialog.setContentView(R.layout.dialog_restored);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        restoredDialog.getWindow().setLayout(dm.widthPixels - 100, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView allText = restoredDialog.findViewById(R.id.restoreAllCount);
        TextView correctText = restoredDialog.findViewById(R.id.restoreCorrectCount);
        TextView sameText = restoredDialog.findViewById(R.id.restoreSameCount);
        TextView errorText = restoredDialog.findViewById(R.id.restoreErrorCount);
        MaterialButton restoreOkay = restoredDialog.findViewById(R.id.restoreOkay);

        allText.setText(String.valueOf((correct + same + error)));
        correctText.setText(String.valueOf(correct));
        sameText.setText(String.valueOf(same));
        errorText.setText(String.valueOf(error));

        restoreOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoredDialog.dismiss();
            }
        });

        restoredDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String uriString = uri.toString();
            Log.i("Smartsense", uri.getPath());
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            Log.i("Smartsense", path);
            /*String displayName = null;
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
             */

            restoreData(uri.getPath());
        } else if (requestCode == 12345 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    backupTwo(uri);
                }
            }
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
                if (isBackupClick) {
                    backup();
                } else {
                    Intent intent = new Intent()
                            .setType("*/*")
                            .setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_backup_file)), 111);

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
                    if (isBackupClick) {
                        backup();
                    } else {
                        Intent intent = new Intent()
                                .setType("*/*")
                                .setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_backup_file)), 111);
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
