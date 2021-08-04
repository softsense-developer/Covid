package com.smartsense.covid.bluetoothlegatt;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.smartsense.covid.CovidMainActivity;
import com.smartsense.covid.R;
import com.smartsense.covid.bluetoothlegatt.DerpData;

import java.util.ArrayList;

import static android.bluetooth.BluetoothDevice.EXTRA_DEVICE;

public class BluetoothScanActivity extends AppCompatActivity implements com.smartsense.covid.bluetoothlegatt.BluetoothAdapter.ItemClickCallback {

    private final String TAG = "Smartsense";
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Snackbar snackbar;
    private final int MY_PERMISSIONS = 123;
    private boolean mScanning;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 5000;
    private Handler mHandler;
    private ArrayList listData;
    private RecyclerView recyclerView;
    private com.smartsense.covid.bluetoothlegatt.BluetoothAdapter adapter;
    private ProgressBar bluetoothScanProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
            finish();
        }

        snackbar = Snackbar.make(findViewById(android.R.id.content),
                getString(R.string.give_permission), Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.WHITE);

        mHandler = new Handler();
        bluetoothScanProgress = findViewById(R.id.bluetoothScanProgress);

        recyclerView = findViewById(R.id.bluetoothScanRecView);
        listData = (ArrayList) DerpData.getListData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new com.smartsense.covid.bluetoothlegatt.BluetoothAdapter(listData, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.setItemClickCallback(this);
        adapter.setListData(listData);
    }

    @Override
    public void onItemClick(int p) {
        BluetoothDevice item = (BluetoothDevice) listData.get(p);
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }

        Log.i(TAG, (item.getAddress() + " " + item.getName()));


        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DEVICE, item);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

        /*Intent intent = new Intent(this, CovidMainActivity.class);
        intent.putExtra(EXTRA_DEVICE, item);
        startActivity(intent);
         */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bluetooth_menu, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_scan).setTitle(getString(R.string.menu_scan));
            bluetoothScanProgress.setVisibility(View.INVISIBLE);
        } else {
            menu.findItem(R.id.menu_scan).setTitle(getString(R.string.menu_stop));
            bluetoothScanProgress.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                if (!mScanning) {
                    scanLeDevice(true);
                } else {
                    scanLeDevice(false);
                }
                break;
        }
        return true;
    }

    private void scanDevices() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        scanLeDevice(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    private void scanLeDevice(boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            (device, rssi, scanRecord) -> runOnUiThread(() -> {
                try {
                    String deviceName = device.getName();
                    if (deviceName.contains("")) {
                        addDevice(device);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    //skip if null
                }
            });


    public void addDevice(BluetoothDevice device) {
        if (!listData.contains(device)) {
            listData.add(device);
        }
    }

    public void mRequestPermissions() {
        try {
            String[] permissionRequested = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH};
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
                if (snackbar != null) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                }
                scanDevices();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionRequested, MY_PERMISSIONS);
                }
            }
        } catch (Exception e) {
            Log.w("SmartSense", e.getMessage());
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
                    scanDevices();
                } else {
                    snackbar.setAction(getString(R.string.location_permission_enable), v -> {
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.setData(Uri.parse("package:" + getPackageName()));
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(i);
                    }).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listData.clear();
        mRequestPermissions();
    }


}