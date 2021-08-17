package com.smartsense.covid;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jstyle.blesdk1963.Util.BleSDK;
import com.jstyle.blesdk1963.callback.DataListener1963;
import com.jstyle.blesdk1963.constant.BleConst;
import com.jstyle.blesdk1963.constant.DeviceKey;
import com.smartsense.covid.api.ApiConstant;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.api.model.requests.AddQuarantineLocationRequest;
import com.smartsense.covid.api.model.requests.AddValueRequest;
import com.smartsense.covid.api.model.requests.AddWarningRequest;
import com.smartsense.covid.api.model.requests.PutNowLocationRequest;
import com.smartsense.covid.api.model.responses.AddQuarantineLocationResponse;
import com.smartsense.covid.api.model.responses.AddValueResponse;
import com.smartsense.covid.api.model.responses.AddWarningResponse;
import com.smartsense.covid.api.model.responses.PutNowLocationResponse;
import com.smartsense.covid.api.service.RetrofitClient;
import com.smartsense.covid.bluetoothlegatt.BluetoothLeService;
import com.smartsense.covid.bluetoothlegatt.GattAttributeLookupTable;
import com.smartsense.covid.dao.CovidDao;
import com.smartsense.covid.location.Common;
import com.smartsense.covid.location.MyBackgroundService;
import com.smartsense.covid.location.SendLocationToActivity;
import com.smartsense.covid.model.Covid;
import com.smartsense.covid.newBand.BleData;
import com.smartsense.covid.newBand.BleManager;
import com.smartsense.covid.newBand.BleService;
import com.smartsense.covid.newBand.RxBus;
import com.smartsense.covid.repo.CovidRepository;
import com.smartsense.covid.settings.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.bluetooth.BluetoothDevice.EXTRA_DEVICE;

public class CovidMainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, DataListener1963  /*implements BaglantiFragment.onBluetoothScanListener*/ {

    private AppBarConfiguration mAppBarConfiguration;
    private Snackbar snackbar;
    private final int MY_PERMISSIONS = 123;
    private PrefManager prefManager;
    private ApiConstantText apiText;
    public static boolean isConnected = false;
    private CovidDao covidDao;
    private CovidRepository repository;


    private boolean missionEnable = false;

    // bool used to determine if we should enable the mission start button
    boolean isMissionButtonEnabled = true;


    private static final String LIST_NAME = "NAME";
    private static final String LIST_UUID = "UUID";
    private final String TAG = "Smartsense";

    private BluetoothLeService mBluetoothLeService;
    private List<List<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<>();

    // define the UUIDs of the HSP BLE service and characteristics
    public final static String BLE_MAXIM_HSP_SERVICE = "5c6e40e8-3b7f-4286-a52f-daec46abe851"; // hsp service
    public final static String BLE_MAXIM_HSP_TEMPERATURE_TOP_CHARACTERISTIC = "3544531b-00c3-4342-9755-b56abe8e6c67"; // temp top
    public final static String BLE_MAXIM_HSP_TEMPERATURE_BOTTOM_CHARACTERISTIC = "3544531b-00c3-4342-9755-b56abe8e6a66"; // temp bottom
    public final static String BLE_MAXIM_HSP_ACCEL_CHARACTERISTIC = "e6c9da1a-8096-48bc-83a4-3fca383705af"; //accel
    public final static String BLE_MAXIM_HSP_PRESSURE_CHARACTERISTIC = "1d8a1932-da49-49ad-91d8-800832e7e940";  // pressure
    public final static String BLE_MAXIM_HSP_HEARTRATE_CHARACTERISTIC = "621a00e3-b093-46bf-aadc-abe4c648c569";  // heart rate
    public final static String BLE_MAXIM_HSP_COMMAND_CHARACTERISTIC = "36e55e37-6b5b-420b-9107-0d34a0e8675a"; // command
    public final static String BLE_MAXIM_HSP_OXYGEN = "621a00e4-b093-46bf-aadc-abe4c648c569"; // OXYGEN TEST
    public final static String BLE_MAXIM_HSP_X_CHARACTERISTIC = "aa8a1932-da49-49ad-91d8-800832e7e940";  // pressure

    public final static String BLE_SMARTSENSE_HSP_SERVICE = "0000FFE0-0000-1000-8000-00805F9B34FB";
    public final static String BLE_SMARTSENSE_HSP_COMMAND_CHARACTERISTIC = "0000FFE1-0000-1000-8000-00805F9B34FB";


    private int SP = 0, Pa = 0, HR = 0;
    private float S1 = 0, S2 = 0;
    private int locationTime = 15000 * 1;//TODO: Update for release = 1000 * 60 * 15 = 15 min
    private int tempSampleSize = 10, heartSampleSize = 10, spO2SampleSize = 10;
    private int tempSampleCounter1 = 0, tempSampleCounter2 = 0, heartSampleCounter = 0, spO2SampleCounter = 0, lastHeart = 0;
    private float temp1 = 0.0f, temp2 = 0.0f, lastTemp = 0.0f, lastSpO2 = 0.0f;
    public static int warningType = 0, batteryPercentage = 0;
    public static boolean isHomeSetClicked = false;


    private BluetoothDevice device;
    private final int BAND_SMARTSENSE_REQUEST = 220;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 340;
    private int locationCounter = 0, locationUpdateType = MyConstant.LOCATION_SEND;
    private ProgressDialog progressDialog;


    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler mServiceHandler;
    private Location mLocation;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5 * 1000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5 * 1000;

    private final int SPO2_NOTIFICATION_INTERVAL = 60; //60 min
    private final int DATA_SENT_INTERVAL = 10; //10 min
    private int BLUETOOTH_DATA_IN_MIN = 60; //TODO: Update for release = 20 // for 1963 = 60
    private static long LAST_SEND_TIME = 0;
    private static long LOCATION_DATA_SENT_INTERVAL = 15 * 1000;

    private static long CONNECTING_CHANGE_TIME = 0;
    private static long CONNECTING_CHANGE_INTERVAL = 3000;

    private static Menu navMenu;
    private LocationManager locationManager;
    private final int REQUEST_CHECK_SETTINGS = 111;
    public static boolean isForHome = false, isWearingNow = false;
    private static boolean isWearingSend = false, isWearingDisconnectSend = false;
    private String testData = "232033302E383820252033312E3935202520302E3030202520302E30302025202D313935392E37382025202D3731382E34342025202D3832322E3835202520342E3036202520300D0A";


    private final int BAND_1963_REQUEST = 221;
    private Disposable subscription;
    private String address;
    boolean isStartReal;
    private Runnable batteryRunnable;
    private Handler batteryHandler;
    private int batteryTime = 120000;
    private Handler band1963And1939Handler, band1963And1939ConnectHandler, band1963And1939SpO2Handler;
    private Runnable band1963And1939Runnable, band1963And1939ConnectRunnable, band1963And1939SpO2Runnable;
    public static long lastDataTime;
    public static boolean isBand1963And1939Connected;
    private boolean isSubscribe = false;

    MyBackgroundService mService = null;
    private boolean mBound = false, isRequestEnable = false;
    private final ServiceConnection mLocationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBackgroundService.LocalBinder binder = (MyBackgroundService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
            Log.i(TAG, "onServiceConnected: " + isRequestEnable);
            mService.removeLocationUpdates();
            EventBus.getDefault().removeAllStickyEvents();
            if (!prefManager.getIsLocationCanChange()) {
                requestLocationData(MyConstant.LOCATION_SEND);
            } else {
                Log.i(TAG, "onServiceConnected: home location should set");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected: ");
            mService = null;
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

        */
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_covid_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_connection, R.id.nav_data_show)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navMenu = navigationView.getMenu();

        prefManager = new PrefManager(getApplicationContext());
        apiText = new ApiConstantText(getApplicationContext());

        checkNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_home);

        TextView navUsername = headerView.findViewById(R.id.navHeaderNameText);
        TextView navEmail = headerView.findViewById(R.id.navHeaderMailText);

        if (prefManager.getName() != null) {
            navUsername.setText((prefManager.getName() + " " + prefManager.getSurname()));
        } else {
            navUsername.setText((getString(R.string.app_name) + " " + getString(R.string.user)));
        }

        if (prefManager.getUserEmail() != null) {
            navEmail.setText(prefManager.getUserEmail());
        } else {
            navEmail.setText("");
        }

        repository = new CovidRepository(getApplication());

        snackbar = Snackbar.make(findViewById(android.R.id.content),
                getString(R.string.location_permission_for_tracking), Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.WHITE);


        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCancelable(false);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        /*
        Covid covid = new Covid((System.currentTimeMillis()), 96.0, MyConstant.SPO2, MyConstant.AUTO_SAVE);
        repository.insert(covid);
        if (prefManager.getSentDataServer()) {
            sendVitalDataToServer(MyConstant.SPO2, 96.0);
        }

        Covid covid2 = new Covid((System.currentTimeMillis()), 37.4, MyConstant.TEMP, MyConstant.AUTO_SAVE);
        repository.insert(covid2);
        if (prefManager.getSentDataServer()) {
            sendVitalDataToServer(MyConstant.TEMP, 37.4);
        }

        Covid covid3 = new Covid((System.currentTimeMillis()), 70, MyConstant.HEART, MyConstant.AUTO_SAVE);
        repository.insert(covid3);
        if (prefManager.getSentDataServer()) {
            sendVitalDataToServer(MyConstant.HEART, 70);
        }
*/

        /*device = getIntent().getParcelableExtra(EXTRA_DEVICE);
        if (device != null) {
            mDeviceName = device.getName();
            mDeviceAddress =    device.getAddress();
            Log.i(TAG, (mDeviceName + " " + mDeviceAddress));

            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        } else {
            Log.e(TAG, "Device null");
        }
         */

        //For band 1963
        band1963And1939Handler = new Handler();
        band1963And1939Runnable = () -> {
            band1963And1939Handler.postDelayed(band1963And1939Runnable, 5000);
            Log.i(TAG, "onCreate: band1963And1939Runnable");
            if (isConnected && isBand1963And1939Connected) {
                if (lastDataTime + 5000 < System.currentTimeMillis()) {
                    sendValue(BleSDK.RealTimeStep(isStartReal, true));
                }
            }
        };

        batteryHandler = new Handler();
        batteryRunnable = () -> {
            Log.i(TAG, "onCreate: batteryRunnable");
            batteryHandler.postDelayed(batteryRunnable, batteryTime);
            if (isConnected && isBand1963And1939Connected) {
                sendValue(BleSDK.GetDeviceBatteryLevel());

                sendValue(BleSDK.GetBloodOxygen(0));
            }
        };


        band1963And1939ConnectHandler = new Handler();
        band1963And1939ConnectHandler.postDelayed(band1963And1939ConnectRunnable = () -> {
            if (!isConnected && !isBand1963And1939Connected &&
                    prefManager.getBandType() == MyConstant.BAND_1963 &&
                    prefManager.getBandMac() != null) {
                band1963And1939Connect();
            }
            band1963And1939ConnectHandler.postDelayed(band1963And1939ConnectRunnable, 5000);
        }, 200);

        band1963And1939SpO2Handler = new Handler();
        band1963And1939SpO2Runnable = () -> {
            band1963And1939SpO2Handler.postDelayed(band1963And1939SpO2Runnable, (SPO2_NOTIFICATION_INTERVAL * 60 * 1000));
            if (isConnected && isBand1963And1939Connected &&
                    prefManager.getBandType() == MyConstant.BAND_1963 &&
                    prefManager.getBandMac() != null) {

                sendNotification(getString(R.string.ask_spo2_notify_title), getString(R.string.ask_spo2_notify));
            }
        };
        //For band 1963


        isHomeSetClicked = true;
    }


    private void checkNavigationDrawer() {
        if (prefManager.getUserRole() == MyConstant.PATIENT_ROLE) {
            navMenu.findItem(R.id.nav_add_doctor).setVisible(true);
            navMenu.findItem(R.id.nav_request).setVisible(true);
            navMenu.findItem(R.id.nav_companion).setVisible(true);
        }
    }

    public void setNotPatientNavigationDrawer() {
        if (prefManager.getUserRole() != MyConstant.PATIENT_ROLE) {
            navMenu.findItem(R.id.nav_add_doctor).setVisible(false);
            navMenu.findItem(R.id.nav_request).setVisible(false);
            navMenu.findItem(R.id.nav_companion).setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.epilepsy_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            //requestLocationData(MyConstant.LOCATION_SEND);

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            //isConnected=true;
            //checkTemp(39.4f);

            //getGPSDialog();
            /*requestLocationData(MyConstant.LOCATION_SEND);
            Log.i(TAG, "Location sent request");*/
            /*setupLocation();
            requestLocationUpdates();
             */

            //sendValue(BleSDK.RealTimeStep(isStartReal, true));
        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    private void tempData(float value) {
        tempSampleSize = DATA_SENT_INTERVAL * BLUETOOTH_DATA_IN_MIN;
        if (tempSampleSize > tempSampleCounter1 + 1) {
            tempSampleCounter1++;
            Log.i(TAG, "tempData: " + tempSampleCounter1 + "/" + tempSampleSize);
        } else {
            tempSampleCounter1 = 0;

            if (value == 0.0f) {
                value = lastTemp;
            }
            if (value == 0.0f) {
                return;
            }
            Covid covid = new Covid((System.currentTimeMillis()), value, MyConstant.TEMP, MyConstant.AUTO_SAVE);
            repository.insert(covid);

            checkTemp(value);

            if (prefManager.getSentDataServer()) {
                sendVitalDataToServer(MyConstant.TEMP, value);
                lastTemp = 0;
            }
        }
    }

    private void heartData(int value) {
        heartSampleSize = DATA_SENT_INTERVAL * BLUETOOTH_DATA_IN_MIN;
        if (heartSampleSize > heartSampleCounter + 1) {
            heartSampleCounter++;
            Log.i(TAG, "heartData: " + heartSampleCounter + "/" + heartSampleSize);
        } else {
            heartSampleCounter = 0;

            if (value == 0) {
                value = lastHeart;
            }
            if (value == 0) {
                return;
            }
            Covid covid = new Covid((System.currentTimeMillis()), value, MyConstant.HEART, MyConstant.AUTO_SAVE);
            repository.insert(covid);

            checkHeartRate(value);

            if (prefManager.getSentDataServer()) {
                sendVitalDataToServer(MyConstant.HEART, value);
                lastHeart = 0;
            }
        }
    }

    private void spO2Data(float value) {
        spO2SampleSize = DATA_SENT_INTERVAL * BLUETOOTH_DATA_IN_MIN;
        if (spO2SampleSize > spO2SampleCounter + 1) {
            spO2SampleCounter++;
            Log.i(TAG, "spO2Data: " + spO2SampleCounter + "/" + spO2SampleSize);
        } else {
            spO2SampleCounter = 0;

            if (value == 0.0f) {
                value = lastSpO2;
            }
            if (value == 0.0f) {
                return;
            }

            Covid covid = new Covid((System.currentTimeMillis()), value, MyConstant.SPO2, MyConstant.AUTO_SAVE);
            repository.insert(covid);

            checkSpO2(value);

            if (prefManager.getSentDataServer()) {
                sendVitalDataToServer(MyConstant.SPO2, value);
                lastSpO2 = 0;
            }
        }
    }

    private void checkSpO2(float SpO2) {
        if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_AT_HOME) {
            if (SpO2 <= 92) {
                warningType = MyConstant.SPO2;
                sendNotification(getString(R.string.warning), getString(R.string.warning_spo2));
                prefManager.setWarningData(SpO2);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
                prefManager.setWarningType(MyConstant.SPO2);
            }
        } else if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_IN_HOSPITAL) {
            if (SpO2 <= 90) {
                warningType = MyConstant.SPO2;
                sendNotification(getString(R.string.warning), getString(R.string.warning_spo2));
                prefManager.setWarningData(SpO2);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
                prefManager.setWarningType(MyConstant.SPO2);
            }
        } else {
            if (SpO2 <= 93) {
                warningType = MyConstant.SPO2;
                sendNotification(getString(R.string.warning), getString(R.string.warning_spo2));
                prefManager.setWarningData(SpO2);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
                prefManager.setWarningType(MyConstant.SPO2);
            }
        }
    }

    private void checkTemp(float temp) {
        if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_AT_HOME) {
            if (temp >= 38.0f) {
                warningType = MyConstant.TEMP;
                sendNotification(getString(R.string.warning), getString(R.string.warning_temp));
                prefManager.setWarningData(temp);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
                prefManager.setWarningType(MyConstant.TEMP);
            }
        } else if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_IN_HOSPITAL) {
            if (temp >= 38.3f) {
                warningType = MyConstant.TEMP;
                sendNotification(getString(R.string.warning), getString(R.string.warning_temp));
                prefManager.setWarningData(temp);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
                prefManager.setWarningType(MyConstant.TEMP);
            }
        } else {
            if (temp >= 38.0f) {
                warningType = MyConstant.TEMP;
                sendNotification(getString(R.string.warning), getString(R.string.warning_temp));
                prefManager.setWarningData(temp);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
                prefManager.setWarningType(MyConstant.TEMP);
            }
        }
    }

    private void checkHeartRate(int heartRate) {
        if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_AT_HOME) {
            if (heartRate >= 110 || heartRate <= 50) {
                warningType = MyConstant.HEART;
                if (heartRate >= 110) {
                    warningType = MyConstant.HEART_HIGH;
                    sendNotification(getString(R.string.warning), getString(R.string.warning_heartrate_high));
                    prefManager.setWarningType(MyConstant.HEART_HIGH);
                } else {
                    sendNotification(getString(R.string.warning), getString(R.string.warning_heartrate_low));
                    prefManager.setWarningType(MyConstant.HEART);
                }
                prefManager.setWarningData(heartRate);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
            }
        } else if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_IN_HOSPITAL) {
            if (heartRate >= 110 || heartRate <= 50) {
                warningType = MyConstant.HEART;
                if (heartRate >= 110) {
                    warningType = MyConstant.HEART_HIGH;
                    sendNotification(getString(R.string.warning), getString(R.string.warning_heartrate_high));
                    prefManager.setWarningType(MyConstant.HEART_HIGH);
                } else {
                    sendNotification(getString(R.string.warning), getString(R.string.warning_heartrate_low));
                    prefManager.setWarningType(MyConstant.HEART);
                }
                prefManager.setWarningData(heartRate);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
            }
        } else {
            if (heartRate >= 100 || heartRate <= 50) {
                warningType = MyConstant.HEART;
                if (heartRate >= 100) {
                    warningType = MyConstant.HEART_HIGH;
                    sendNotification(getString(R.string.warning), getString(R.string.warning_heartrate_high));
                    prefManager.setWarningType(MyConstant.HEART_HIGH);
                } else {
                    sendNotification(getString(R.string.warning), getString(R.string.warning_heartrate_low));
                    prefManager.setWarningType(MyConstant.HEART);
                }
                prefManager.setWarningData(heartRate);
                prefManager.setWarningDate(Calendar.getInstance().getTimeInMillis());
            }
        }
    }

    //region Bluetooth Methods


    /**
     * Subscribe to all of the HSP characteristics that we care about
     */
    private void subscribeCharacteristics() {
        for (List<BluetoothGattCharacteristic> chars : mGattCharacteristics) {
            for (BluetoothGattCharacteristic cha : chars) {
                if (cha.getService().getUuid().compareTo(UUID.fromString(BLE_MAXIM_HSP_SERVICE)) == 0) {
                    Log.i(TAG, cha.getUuid().toString());
                    if (cha.getUuid().compareTo(UUID.fromString(BLE_MAXIM_HSP_TEMPERATURE_TOP_CHARACTERISTIC)) == 0) {
                        mBluetoothLeService.Subscribe(cha.getService().getUuid(), cha.getUuid());
                    }
                    if (cha.getUuid().compareTo(UUID.fromString(BLE_MAXIM_HSP_TEMPERATURE_BOTTOM_CHARACTERISTIC)) == 0) {
                        mBluetoothLeService.Subscribe(cha.getService().getUuid(), cha.getUuid());
                    }
                    if (cha.getUuid().compareTo(UUID.fromString(BLE_MAXIM_HSP_ACCEL_CHARACTERISTIC)) == 0) {
                        mBluetoothLeService.Subscribe(cha.getService().getUuid(), cha.getUuid());
                    }
                    if (cha.getUuid().compareTo(UUID.fromString(BLE_MAXIM_HSP_HEARTRATE_CHARACTERISTIC)) == 0) {
                        mBluetoothLeService.Subscribe(cha.getService().getUuid(), cha.getUuid());
                    }
                    if (cha.getUuid().compareTo(UUID.fromString(BLE_MAXIM_HSP_PRESSURE_CHARACTERISTIC)) == 0) {
                        mBluetoothLeService.Subscribe(cha.getService().getUuid(), cha.getUuid());
                    }
                    if (cha.getUuid().compareTo(UUID.fromString(BLE_MAXIM_HSP_OXYGEN)) == 0) {
                        mBluetoothLeService.Subscribe(cha.getService().getUuid(), cha.getUuid());
                    }
                }

                if (cha.getService().getUuid().compareTo(UUID.fromString(BLE_SMARTSENSE_HSP_SERVICE)) == 0) {
                    Log.i(TAG, cha.getUuid().toString());
                    if (cha.getUuid().compareTo(UUID.fromString(BLE_SMARTSENSE_HSP_COMMAND_CHARACTERISTIC)) == 0) {
                        mBluetoothLeService.Subscribe(cha.getService().getUuid(), cha.getUuid());
                    }
                }
            }
        }
    }

    /**
     * Update the UI for a given characteristic uuid using data
     * This will update all of the text view values for the given characteristic
     * It also will graph the incoming data
     *
     * @param characteristic Characteristic to update with given data
     * @param data           Data used to update the UI
     */

    private void UpdateUI(UUID characteristic, byte[] data) {


        float x = 0;
        float y = 0;
        float z = 0;
        float value = 0;
        String str = "";
        str = bytesToHex(data);
        // temperature top
        if (characteristic.compareTo(UUID.fromString(BLE_MAXIM_HSP_TEMPERATURE_TOP_CHARACTERISTIC)) == 0) {
            Log.i(TAG, "Temp Top: " + str);
            //value = ToFahrenheit(ToTemperature(data));
            value = ToTemperature(data);
            S1 = value;
            /*mTextTempTopRaw.setText(str);
            mTextTempTop.setText(String.format("%.1f", value) + "C");
            linePointCollectionTemperature1.addpoint(value);
            lineGraphingTemperature1.plotPointCollection();
             */

            //TODO:Düzenle
           /* tempDataSet.addEntry(new Entry(i, value));
            tempChart.notifyDataSetChanged(); // let the chart know it's data changed
            tempChart.invalidate(); // refresh
            i++;
            */


            if (tempSampleSize > tempSampleCounter1) {
                temp1 += value;
                tempSampleCounter1++;
            } else {
                if (tempSampleSize == tempSampleCounter2) {
                    temp1 /= tempSampleSize;
                    temp2 /= tempSampleSize;

                    Covid covid = new Covid((System.currentTimeMillis()), ((temp1 + temp2) / 2), MyConstant.TEMP, MyConstant.AUTO_SAVE);
                    repository.insert(covid);

                    if (prefManager.getSentDataServer()) {
                        sendVitalDataToServer(MyConstant.TEMP, (temp1 + temp2) / 2);
                    }

                    tempSampleCounter1 = 0;
                    tempSampleCounter2 = 0;
                    temp1 = 0;
                    temp2 = 0;
                }
            }

        }
        // temperature bottom
        if (characteristic.compareTo(UUID.fromString(BLE_MAXIM_HSP_TEMPERATURE_BOTTOM_CHARACTERISTIC)) == 0) {
            Log.i(TAG, "Temp Bot: " + str);
            //value = ToFahrenheit(ToTemperature(data));
            value = ToTemperature(data);
            S2 = value;

           /*mTextTempBottomRaw.setText(str);
            mTextTempBottom.setText(String.format("%.1f", value) + "C");
            linePointCollectionTemperature2.addpoint(value);
            lineGraphingTemperature2.plotPointCollection();

            */

            if (tempSampleSize > tempSampleCounter2) {
                temp2 += value;
                tempSampleCounter2++;

            }
        }
        // acceleration

        if (characteristic.compareTo(UUID.fromString(BLE_MAXIM_HSP_ACCEL_CHARACTERISTIC)) == 0) {
            Log.i(TAG, "Accel: " + str);

            x = PartialArrayToFloat(data, 0);
            y = PartialArrayToFloat(data, 2);
            z = PartialArrayToFloat(data, 4);
            /*
            mTextLIS2DHRaw.setText(str);
            String date = sekil.format(tarih.getTime());

            mTextLIS2DH_X.setText(String.format("X: %.1f", x));
            mTextLIS2DH_Y.setText(String.format("Y: %.1f", y));
            mTextLIS2DH_Z.setText(String.format("Z: %.1f", z));

            linePointCollectionX.addpoint(x);
            linePointCollectionY.addpoint(y);
            linePointCollectionZ.addpoint(z);
            lineGraphingAccelerometer.plotPointCollection();

             */
        }
        // pressure
        if (characteristic.compareTo(UUID.fromString(BLE_MAXIM_HSP_PRESSURE_CHARACTERISTIC)) == 0) {
            Log.i(TAG, "Pressure: " + str);
            String pressureStr = "";
            int n = 0;
            int bits;
            byte[] arr = {data[0], data[1], data[2], data[3]};
            ByteBuffer bb = ByteBuffer.wrap(arr);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bits = bb.getInt();
            float temperature = Float.intBitsToFloat(bits);
            Log.i(TAG, "Pressure Temp: " + String.valueOf(temperature));
            n = 4;
            //bits = data[n] | data[n+1]<<8 | data[n+2]<<16 | data[n+3]<<24;
            byte[] arr2 = {data[4], data[5], data[6], data[7]};
            ByteBuffer bb2 = ByteBuffer.wrap(arr2);
            bb2.order(ByteOrder.LITTLE_ENDIAN);
            bits = bb2.getInt();
            float pressure = Float.intBitsToFloat(bits);
            Pa = (int) Math.floor(pressure);
            /*mTextPressure.setText(String.format("%.2f", pressure / 100.0f) + "hPa");
            linePointCollectionPressure.addpoint(pressure);
            lineGraphingPressure.plotPointCollection();

             */
        }
        // heartrate
        if (characteristic.compareTo(UUID.fromString(BLE_MAXIM_HSP_HEARTRATE_CHARACTERISTIC)) == 0) {
            Log.i(TAG, "HeartRate: " + str);
            Log.i(TAG, "HeartRate2 : " + Arrays.toString(data));

            value = CalculateHeartRate(data);
            HR = (int) Math.floor(value);

            if (heartSampleSize > heartSampleCounter) {
                HR += HR;
                heartSampleCounter++;
            } else {
                HR /= heartSampleSize;

                Covid covid = new Covid((System.currentTimeMillis()), HR, MyConstant.HEART, MyConstant.AUTO_SAVE);
                repository.insert(covid);

                if (prefManager.getSentDataServer()) {
                    sendVitalDataToServer(MyConstant.HEART, HR);
                }

                HR = 0;
                heartSampleCounter = 0;
            }

           /* mTextHeartRateRaw.setText(str);
            mTextHeartRate.setText(String.format("%.1f", value));*/
        }
        //oksijen
        if (characteristic.compareTo(UUID.fromString(BLE_MAXIM_HSP_OXYGEN)) == 0) {
            Log.i(TAG, "Oxygen: " + str);

        }

        if (characteristic.compareTo(UUID.fromString(BLE_MAXIM_HSP_COMMAND_CHARACTERISTIC)) == 0) {
            Log.i(TAG, "Char: " + str);

        }
        if (characteristic.compareTo(UUID.fromString(BLE_MAXIM_HSP_X_CHARACTERISTIC)) == 0) {
            Log.i(TAG, "X: " + str);

        }


        if (characteristic.compareTo(UUID.fromString(BLE_SMARTSENSE_HSP_COMMAND_CHARACTERISTIC)) == 0) {
            //Log.i(TAG, "BLE_SMARTSENSE_HSP_COMMAND_CHARACTERISTIC: " + str);
            logIncomingData(str);
        }

    }

    private String oldHex = "";

    private void logIncomingData(String hex) {
        Log.i(TAG, "Hex data: " + hex);
        Log.i(TAG, "Raw data:" + hexToString(hex));
        splitIncomingData2(hexToString(hex));
        /*if (!oldHex.equals(hex)) {
            Log.i(TAG, "Hex data: " + hex);
            splitIncomingData2(hexToString(hex));
        }
        oldHex = hex;*/

        /*List<String[]> data = splitIncomingData(hexToString(hex));
        for (int i = 0; i < data.size(); i++) {
            String[] line = data.get(i);
            Log.i(TAG, "ax: " + line[0] + " ay: " + line[1] + " az: " + line[2] + " gx: " + line[3] + " gy: " + line[4] + " gz: " + line[5]);

        }*/
        Log.i(TAG, "--------------------------------------");
    }

    public String hexToString(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    public List<String[]> splitIncomingData(String data) {
        List<String[]> splitedData = new ArrayList<>();
        try {
            String[] splitDataForAG = data.split("#");
            for (int i = 1; i < splitDataForAG.length; i++) {
                String[] splitData = splitDataForAG[i].split("%");
                //String[] splitDataForFirst = splitData[0].split("#");
                //splitData[0] = splitDataForFirst[1];
                splitedData.add(splitData);
            }
        } catch (Exception e) {
            Log.e(TAG, "splitIncomingData Error: " + e.getMessage());
            return null;
        }
        return splitedData;
    }

    //23203020252030202520    # 0 % 0 %
    //32352E34370D0A 28.24

    public void splitIncomingData2(String data) {

        if (data.contains("#")) {
            String[] deleteHash = data.split("# ");
            String[] splitData = deleteHash[1].split(" % ");

            if (splitData.length == 9) {
                Log.i(TAG, "Sıcaklık: " + splitData[0]);
                Log.i(TAG, "Sıcaklık 2: " + splitData[1]);
                Log.i(TAG, "Nabız: " + splitData[2]);
                Log.i(TAG, "SpO2: " + splitData[3]);
                Log.i(TAG, "X: " + splitData[4]);
                Log.i(TAG, "Y: " + splitData[5]);
                Log.i(TAG, "Z: " + splitData[6]);
                batteryPercentage = (int) (((Float.parseFloat(splitData[7]) + 1.2f) - 4.2f) / 0.012f);
                Log.i(TAG, "Batarya: " + splitData[7] + "  %" + batteryPercentage);    //4.2-3.0

                if (Integer.parseInt(splitData[8].trim()) == 0) {
                    Log.i(TAG, "Takılma Durumu: Takılı değil"); //takılysan 1, çıkardıysa 0
                    isWearingNow = false;
                    if (!isWearingDisconnectSend) {
                        isWearingDisconnectSend = true;
                        isWearingSend = false;

                        sendNotification(getString(R.string.disconnected_to_clamps), getString(R.string.smartsense_band));
                        if (prefManager.getSentDataServer()) {
                            addWarning(MyConstant.WARNING_CONNECTION_LOST, 0);
                        }
                    }

                } else {
                    Log.i(TAG, "Takılma Durumu: Takılı"); //takılysan 1, çıkardıysa 0
                    isWearingNow = true;

                    if (!isWearingSend) {
                        isWearingDisconnectSend = false;
                        isWearingSend = true;

                        sendNotification(getString(R.string.connected_to_clamps), getString(R.string.smartsense_band));
                        if (prefManager.getSentDataServer()) {
                            addWarning(MyConstant.WARNING_CONNECTION_LOST, 1);
                        }
                    }
                    BLUETOOTH_DATA_IN_MIN = 20;

                    float temp = Float.parseFloat(splitData[0]);
                    int heart = (int) Float.parseFloat(splitData[2]);
                    float spO2 = Float.parseFloat(splitData[3]);

                    if (temp != 0.0f) {
                        lastTemp = temp;
                    }

                    if (spO2 != 0.0f) {
                        lastSpO2 = spO2;
                    }

                    if (heart != 0) {
                        lastHeart = heart;
                    }

                    tempData(temp);
                    heartData(heart);
                    spO2Data(spO2);

                }

            } else {
                Log.i(TAG, "splitIncomingData: veri uzunluğu hatalı");
            }


        } else {
            Log.i(TAG, "splitIncomingData2: kayma");
        }


    }


    /**
     * Define an intent filter of actions to respond to
     *
     * @return Returns the intent filter that is created
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_CHARACTERISTIC_WRITE);
        return intentFilter;
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName componentName, final IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                //finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            //TODO: Oto bağlanmada eğer cihaz yoksa bağlı görünüyor yine de
            if (prefManager.getBandType() == MyConstant.BAND_SMARTSENSE) {
                if (prefManager.getBandMac() != null && prefManager.getBandName() != null) {
                    mBluetoothLeService.connect(prefManager.getBandMac());
                }
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName componentName) {
            mBluetoothLeService = null;
            //finish();
        }
    };

    public void onDisconnect() {
        if (isConnected) {
            if (mBluetoothLeService != null) {
                mBluetoothLeService.disconnect();
            }
        }
    }

    public void onConnect() {

    }

    /**
     * Handles various events fired by the Service.
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i(TAG, "onReceive: ACTION_GATT_CONNECTED");

                Calendar calendar = Calendar.getInstance();
                if (calendar.getTimeInMillis() >= CONNECTING_CHANGE_TIME + CONNECTING_CHANGE_INTERVAL) {
                    CONNECTING_CHANGE_TIME = calendar.getTimeInMillis();
                    updateConnectionState(true);
                }
                invalidateOptionsMenu();
                /*//TODO: Heart rate active
                byte[] data = new byte[]{0x00};
                mBluetoothLeService.writeCharacteristic(UUID.fromString(BLE_MAXIM_HSP_SERVICE), UUID.fromString(BLE_MAXIM_HSP_COMMAND_CHARACTERISTIC), data);
                 */
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i(TAG, "onReceive: ACTION_GATT_DISCONNECTED");
                Calendar calendar = Calendar.getInstance();
                if (calendar.getTimeInMillis() >= CONNECTING_CHANGE_TIME + CONNECTING_CHANGE_INTERVAL) {
                    CONNECTING_CHANGE_TIME = calendar.getTimeInMillis();
                    updateConnectionState(false);
                }
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                subscribeCharacteristics();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                final String noData = getString(R.string.no_data);
                final String uuid = intent.getStringExtra(BluetoothLeService.EXTRA_UUID_CHAR);
                final byte[] dataArr = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA_RAW);
                if (uuid != null) {
                    isConnected = true;
                    UpdateUI(UUID.fromString(uuid), dataArr);
                }
            } else if (BluetoothLeService.ACTION_CHARACTERISTIC_WRITE.equals(action)) {
                final String uuid = intent.getStringExtra(BluetoothLeService.EXTRA_UUID_CHAR);
                if (uuid.equalsIgnoreCase(BLE_MAXIM_HSP_COMMAND_CHARACTERISTIC)) {
                    final byte[] dataArr = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA_RAW);
                    String str = "";
                    str = bytesToHex(dataArr);
                    Log.i("Smartsense", "Char data: " + str);
                    EnableMissionButton(true);

                }
            }
        }
    };

    /**
     * Update the connection status
     *
     * @param connected Resource id that is bound to the status test view field
     */
    private void updateConnectionState(boolean connected) {
        if (connected) {
            Log.i(TAG, "updateConnectionState: connected");
            //waitData = 0;
            //sendNotification(getString(R.string.connected_to_wristband), getString(R.string.smartsense_band));
            isConnected = true;
        } else {
            Log.i(TAG, "updateConnectionState: unconnected");
            //sendNotification(getString(R.string.disconnected_to_wristband), getString(R.string.smartsense_band));
            isConnected = false;
            /*if (prefManager.getSentDataServer()) {
                addWarning(MyConstant.WARNING_CONNECTION_LOST, 0);
            }*/
        }
        isBand1963And1939Connected = false;
    }

    /**
     * Enable or disable the Start Mission Button based on HSP state variable
     *
     * @param state Enabled or disabled status
     */
    private void EnableMissionButton(boolean state) {
        isMissionButtonEnabled = state;
        if (state) {
            //TODO: Mission Enable olacak mı?
        } else {

        }
    }


    /**
     * Start or stop the mission
     */
    private void missionClick() {
        byte[] data = new byte[1];
        //if (isMissionButtonEnabled == false) return;

        missionEnable = !missionEnable;

        if (!missionEnable) {
            // stop
            data[0] = 0x00;
            mBluetoothLeService.writeCharacteristic(UUID.fromString(BLE_MAXIM_HSP_SERVICE), UUID.fromString(BLE_MAXIM_HSP_COMMAND_CHARACTERISTIC), data);
            //mClickStartBtn2.setText(getResources().getText(R.string.start_mission));
            EnableMissionButton(false);

        } else {
            // start
            data[0] = 0x01;
            mBluetoothLeService.writeCharacteristic(UUID.fromString(BLE_MAXIM_HSP_SERVICE), UUID.fromString(BLE_MAXIM_HSP_COMMAND_CHARACTERISTIC), data);

            //setOutputMode OUTPUT_MODE (0x10), Index Byte: SET_FORMAT (0x00),


            //mClickStartBtn2.setText(getResources().getText(R.string.stop_mission));


            EnableMissionButton(false);
           /* final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // need to do tasks on the UI thread
                            //Submit(datam);
                        }
                    }, 0);
                    //Add some downtime
                    SystemClock.sleep(5000);
                }
            };
            new Thread(runnable).start();

            */
        }
    }

    /**
     * Demonstrates how to iterate through the supported GATT Services/Characteristics.
     * In this sample, we populate the data structure that is bound to the ExpandableListView
     * on the UI.
     *
     * @param gattServices List of services to parse and display
     */
    private void displayGattServices(final List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        String uuid = null;
        final String unknownServiceString = getResources().getString(R.string.unknown_service);
        final String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        final List<Map<String, String>> gattServiceData = new ArrayList<>();
        final List<List<Map<String, String>>> gattCharacteristicData = new ArrayList<>();
        mGattCharacteristics = new ArrayList<>();

        // Loops through available GATT Services.
        for (final BluetoothGattService gattService : gattServices) {
            final Map<String, String> currentServiceData = new HashMap<>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_NAME, GattAttributeLookupTable.getAttributeName(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            final List<Map<String, String>> gattCharacteristicGroupData = new ArrayList<>();
            final List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            final List<BluetoothGattCharacteristic> charas = new ArrayList<>();

            // Loops through available Characteristics.
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                final Map<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(LIST_NAME, GattAttributeLookupTable.getAttributeName(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
                Log.i(TAG, (LIST_NAME + ": " + GattAttributeLookupTable.getAttributeName(uuid, unknownCharaString) + " " + LIST_UUID + ": " + uuid));
            }

            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    /**
     * Convert the temperature from celsius to fahrenheit
     *
     * @param celsius temperature to convert
     * @return converted temperature in fahrenheit
     */
    private float ToFahrenheit(float celsius) {
        return celsius * 9 / 5 + 32;
    }

    /**
     * Convert the incoming byte data array into a temperature
     *
     * @param data Data array containing two elements
     * @return temperature as a float in celcius
     */
    private float ToTemperature(byte[] data) {
        float value;
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
        value = (float) data[1] + (float) data[0] / 256.0f;
        return value;
    }

    /**
     * Convert the portion of the byte array into a float
     *
     * @param data   Array to convert, a part of this array is converted based on a offset
     * @param offset Offset into the array to extract the float
     * @return Returns the float that was converted
     */
    private float PartialArrayToFloat(byte[] data, int offset) {
        short value = (short) ((short) (data[offset + 1] << 8) + (short) data[offset]);
        return (float) value;
    }

    /**
     * Calculate the heart rate from the ECG RtoR device
     *
     * @param data Incoming data to convert
     * @return Returns a float representing the heart rate
     */
    private float CalculateHeartRate(byte[] data) {
        float t = 8.0f;
        float value = 0;
        float RtoR = (float) ((int) data[1] << 8) + (float) data[0];
        float fmStr = (float) ((int) data[3] << 8) + (float) data[2];
        if (fmStr == 0.0f) t = 7.813f;
        if (RtoR > 0.0f) {
            value = 60000.0f / (RtoR * t);
        }
        return value;
    }


    /// array of hex ascii values used for hex byte to string conversion
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Convert a series of bytes within a given array into a string
     *
     * @param bytes Arbitrary array of bytes to convert
     * @return String is returned containing the bytes as hex ascii values
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    //endregion Bluetooth Methods


    //region Server Methods
    private void sendVitalDataToServer(int dataType, double dataValue) {
        AddValueRequest request = new AddValueRequest();
        request.setDataType(dataType);
        request.setDataValue(dataValue);

        Call<AddValueResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).addValue(request);

        call.enqueue(new Callback<AddValueResponse>() {
            @Override
            public void onResponse(Call<AddValueResponse> call, Response<AddValueResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, apiText.getText(ApiConstant.DATA_INSERTED));
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 281");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 282");
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
                            getShortToast(getString(R.string.occurred_error) + " 285");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 286");
                    }
                }
            }

            @Override
            public void onFailure(Call<AddValueResponse> call, Throwable t) {
                getShortToast(getString(R.string.occurred_error) + " 283");
            }
        });
    }

    private void addWarning(int warningType, double warningValue) {
        AddWarningRequest request = new AddWarningRequest();
        request.setDataType(warningType);
        request.setWarningValue(warningValue);

        Call<AddWarningResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).addWarning(request);

        call.enqueue(new Callback<AddWarningResponse>() {
            @Override
            public void onResponse(Call<AddWarningResponse> call, Response<AddWarningResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                Log.i(TAG, "addWarning: " + apiText.getText(ApiConstant.DATA_INSERTED));
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 331");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 332");
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
                            getShortToast(getString(R.string.occurred_error) + " 333");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 334");
                    }
                }
            }

            @Override
            public void onFailure(Call<AddWarningResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 335");
            }
        });
    }

    //TODO deprecated api
    /*
    private void insertDataToServer(float S1, float S2, int SP, int Pa, int HR) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi(getApplicationContext())
                .insertData("null", prefManager.getUserEmail(), S1, S2, SP, Pa, HR, "null");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 202) {
                    Log.i(TAG, "response.code() == 202");
                    Log.i(TAG, apiText.getText(ApiConstant.DATA_INSERTED));
                } else if (response.code() == 400) {
                    Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 173", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(CovidMainActivity.this, apiText.getText(ApiConstant.UNAUTHORIZED), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(CovidMainActivity.this, apiText.getText(ApiConstant.FORBIDDEN), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 172", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 173", Toast.LENGTH_SHORT).show();
            }
        });
    }
    */

    //TODO deprecated api
    /*
    private void insertPurchaseDataToServer(String orderID, String productID, String developerPayload, String purchaseTime, String purchaseToken, String signature, String purchaseState, boolean status) {

        Call<InsertResponse> call = RetrofitClient
                .getInstance()
                .getApi(getApplicationContext())
                .insertPurchase(prefManager.getID(), orderID, productID, developerPayload, purchaseTime, purchaseToken, signature, purchaseState, status);


        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            if (response.body().getCode() == ApiConstant.DATA_INSERTED) {
                                Log.i("Smartsense", apiText.getText(response.body().getCode()));
                            } else {
                                Toast.makeText(CovidMainActivity.this, apiText.getText(response.body().getCode()), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 198", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 191", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.i("Smartsense", response.errorBody().string());
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        if (obj.getInt("code") == ApiConstant.UNAUTHORIZED) {
                            Toast.makeText(CovidMainActivity.this, apiText.getText(ApiConstant.UNAUTHORIZED), Toast.LENGTH_SHORT).show();
                        } else if (obj.getInt("code") == ApiConstant.FORBIDDEN) {
                            Toast.makeText(CovidMainActivity.this, apiText.getText(ApiConstant.FORBIDDEN), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 192", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 194", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 193", Toast.LENGTH_SHORT).show();
            }
        });

    }
    */

    //TODO deprecated api
    /*
    private void refreshToken(String email, String password) {
        try {
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
                                if (response.body().getAccess_token() != null) {
                                    Log.i(TAG, response.body().getAccess_token());
                                    prefManager.setAuthToken(response.body().getAccess_token());
                                    getQuarantineStatus();
                                } else {
                                    Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 168", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 167", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 164", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code() == 400) {
                        Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 165", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 163", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 166", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.w("WARNING", e.getMessage());
            Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 162", Toast.LENGTH_SHORT).show();
        }
    }
    */

    //TODO deprecated api
    /*
    private void sendWarning(int code) {
        try {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance().getApi(getApplicationContext()).sendWarning(prefManager.getUserEmail(), "Bağlantı koptu.");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Log.i(TAG, "response.code()==200");
                        if (response.isSuccessful()) {
                            Log.i(TAG, "İsSuccessful");
                        } else {
                            Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 164", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code() == 400) {
                        Toast.makeText(CovidMainActivity.this, apiText.getText(ApiConstant.BAD_REQUEST), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 401) {
                        Toast.makeText(CovidMainActivity.this, apiText.getText(ApiConstant.UNAUTHORIZED), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 403) {
                        Toast.makeText(CovidMainActivity.this, apiText.getText(ApiConstant.FORBIDDEN), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 172", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 166", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.w("WARNING", e.getMessage());
            Toast.makeText(CovidMainActivity.this, getString(R.string.occurred_error) + " 162", Toast.LENGTH_SHORT).show();
        }
    }
     */

    //endregion Server Methods


    //region Location Methods
    private boolean isConnectionHas() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    private void getGPSDialog() {
        if (!isGpsEnabled()) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true); //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS

            Task<LocationSettingsResponse> task =
                    LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

            task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here
                        setState(Common.requestingLocationUpdates(CovidMainActivity.this));
                        bindService(new Intent(CovidMainActivity.this, MyBackgroundService.class), mLocationServiceConnection, Context.BIND_AUTO_CREATE);
                        if (isForHome) {
                            setupLocationForHome();
                        }

                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            CovidMainActivity.this,
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });
        } else {
            Log.i(TAG, "GPS is already Enabled!");
            setState(Common.requestingLocationUpdates(CovidMainActivity.this));
            bindService(new Intent(CovidMainActivity.this, MyBackgroundService.class), mLocationServiceConnection, Context.BIND_AUTO_CREATE);
            if (isForHome) {
                setupLocationForHome();

            }

        }
    }

    private boolean isGpsEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Common.KEY_REQUESTING_LOCATION_UPDATES)) {
            setState(sharedPreferences.getBoolean(Common.KEY_REQUESTING_LOCATION_UPDATES, false));
        }
    }

    private void setState(boolean isRequestEnable) {
        this.isRequestEnable = isRequestEnable;
    }

    public void requestLocationData(int type) {
        Log.i(TAG, "requestLocationData: ");
        locationUpdateType = type;
        // new locationRequestTask().execute();
        mService.requestLocationUpdates();
        if (type == MyConstant.LOCATION_HOME) {
            progressDialog.setMessage(getResources().getString(R.string.location_set_wait));
            progressDialog.show();
        }
    }


    private void setHomeLocation(double latitude, double longitude) {
        quarantineLocationToServer(latitude, longitude);
    }

    public void setupHomeLocation() {
        mRequestLocationPermissions();
    }

    public void setupLocationForHome() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();
        requestLocationUpdates();

        HandlerThread handlerThread = new HandlerThread("SmartsenseLocation");
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());

        progressDialog.setMessage(getResources().getString(R.string.location_set_wait));
        progressDialog.show();

    }


    public void removeLocationUpdates() {
        try {
            Log.i(TAG, "removeLocationUpdates: ");
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        } catch (SecurityException e) {
            Log.e(TAG, getString(R.string.not_have_location_permission_for_remove) + ": " + e);
        }
    }

    private void getLastLocation() {
        try {
            Log.i(TAG, "getLastLocation: ");
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLocation = task.getResult();
                        Log.i(TAG, "getLastLocation: " + mLocation.getLatitude() + "," + mLocation.getLongitude());
                    } else {
                        Log.e(TAG, getString(R.string.not_get_location));
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, getString(R.string.location_permission_gone) + ": " + e);
        }
    }

    private void createLocationRequest() {
        Log.i(TAG, "createLocationRequest: ");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void onNewLocation(Location lastLocation) {
        mLocation = lastLocation;
        Log.i(TAG, "onNewLocation: " + lastLocation.getLatitude() + "," + lastLocation.getLongitude());
        locationCounter++;
        if (locationCounter >= 2) {
            locationCounter = 0;
            Log.i(TAG, "Location finded: " + lastLocation.getLatitude() + "," + lastLocation.getLongitude());
            setHomeLocation(lastLocation.getLatitude(), lastLocation.getLongitude());
            removeLocationUpdates();
        }
    }

    public void requestLocationUpdates() {
        /*Common.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), MyBackgroundService.class));*/
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        } catch (SecurityException e) {
            Log.e("Smartsense", "Lost location permission. Could not request it " + e);
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onListenLocation(SendLocationToActivity event) {
        if (event != null) {
            /*Log.i(TAG,"Buradfaaaa");
            String data = new StringBuilder()
                    .append(event.getLocation().getLatitude())
                    .append("/")
                    .append(event.getLocation().getLongitude())
                    .toString();
            Toast.makeText(mService, data, Toast.LENGTH_SHORT).show();*/
            Calendar calendar = Calendar.getInstance();

            Log.i(TAG, "onListenLocation Location updated: " + event.getLocation().getLatitude() + "," + event.getLocation().getLongitude());

            if (calendar.getTimeInMillis() >= LAST_SEND_TIME + LOCATION_DATA_SENT_INTERVAL) {
                LAST_SEND_TIME = calendar.getTimeInMillis();
                if (prefManager.getSentDataServer()) {
                    sendLocationToServer(event.getLocation().getLatitude(), event.getLocation().getLongitude());
                }
            }
            //mService.removeLocationUpdates();
        }
    }
    //endregion Location Methods


    //region Location Server Methods

    private void sendLocationToServer(double latitude, double longitude) {
        PutNowLocationRequest request = new PutNowLocationRequest();
        request.setLatitudeNow(latitude);
        request.setLongitudeNow(longitude);

        Call<PutNowLocationResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).sendLocationData(request);

        call.enqueue(new Callback<PutNowLocationResponse>() {
            @Override
            public void onResponse(Call<PutNowLocationResponse> call, Response<PutNowLocationResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                isHomeSetClicked = true;
                                Log.i(TAG, "sendLocationToServer: " + latitude + " " + longitude);
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 301");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 302");
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
                            getShortToast(getString(R.string.occurred_error) + " 303");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 304");
                    }
                }
            }

            @Override
            public void onFailure(Call<PutNowLocationResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 305");
            }
        });
    }

    private void quarantineLocationToServer(double latitude, double longitude) {
        AddQuarantineLocationRequest request = new AddQuarantineLocationRequest();
        request.setLatitudeQuarantine(latitude);
        request.setLongitudeQuarantine(longitude);

        Call<AddQuarantineLocationResponse> call = RetrofitClient.getInstance()
                .getApi(getApplicationContext()).setQuarantineLocation(request);

        call.enqueue(new Callback<AddQuarantineLocationResponse>() {
            @Override
            public void onResponse(Call<AddQuarantineLocationResponse> call, Response<AddQuarantineLocationResponse> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals("200")) {
                                //Log.i(TAG, "quarantineLocationToServer: " + latitude + " " + longitude);
                                prefManager.setIsLocationCanChange(false);
                                prefManager.setHomeLocationLat((float) latitude);
                                prefManager.setHomeLocationLong((float) longitude);
                                Toast.makeText(getApplicationContext(), getString(R.string.home_location_saved), Toast.LENGTH_SHORT).show();
                                requestLocationData(MyConstant.LOCATION_SEND);
                            } else if (response.body().getCode().equals("400")) {
                                StringBuilder errors = new StringBuilder();
                                for (int i = 0; i < response.body().getErrors().size(); i++) {
                                    errors.append(response.body().getErrors().get(response.body().getErrors().size() - 1 - i));
                                    errors.append("\n");
                                }
                                getLongToast(errors.toString());
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 301");
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 302");
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
                            getShortToast(getString(R.string.occurred_error) + " 303");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getShortToast(getString(R.string.occurred_error) + " 304");
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AddQuarantineLocationResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                getShortToast(getString(R.string.occurred_error) + " 305");
                progressDialog.dismiss();
            }
        });
    }

    //TODO deprecated api
    /*
    private void getLocationStatus() {
        Call<SetLocationStatusResponse> call = RetrofitClient
                .getInstance().getApi(getApplicationContext()).getUserLocationStatus(prefManager.getUserEmail());

        call.enqueue(new Callback<SetLocationStatusResponse>() {
            @Override
            public void onResponse(Call<SetLocationStatusResponse> call, Response<SetLocationStatusResponse> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "getLocationStatus response.code()==200");
                    if (response.isSuccessful()) {
                        Log.i(TAG, "getLocationStatus İsSuccessful");
                        if (response.body() != null) {
                            Log.i(TAG, "getLocationStatus response.body()!=null");
                            prefManager.setIsLocationCanChange(response.body().isStatus());
                            prefManager.setHomeLocationLat((float) response.body().getLat());
                            prefManager.setHomeLocationLong((float) response.body().getLongt());
                            HomeFragment.isHomeSetClicked = true;
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 261", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 262", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Log.i(TAG, "getLocationStatus response.code()==400");
                    Toast.makeText(getApplicationContext(), apiText.getText(ApiConstant.BAD_REQUEST), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Log.i(TAG, "getLocationStatus response.code()==401");
                    Toast.makeText(getApplicationContext(), apiText.getText(ApiConstant.UNAUTHORIZED), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 263", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SetLocationStatusResponse> call, Throwable t) {
                Log.i(TAG, "onFailure");
                Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 264", Toast.LENGTH_SHORT).show();
            }
        });

    }
    */

    //TODO deprecated api
    /*
    private void sendLocation(double latitude, double longitude) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance().getApi(getApplicationContext())
                .sendLocationData(prefManager.getUserEmail(), prefManager.getHomeLocationLat(),
                        prefManager.getHomeLocationLong(), (float) latitude, (float) longitude);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "sendLocation response.code()==200");
                    if (response.isSuccessful()) {
                        Log.i(TAG, "sendLocation İsSuccessful");
                        if (response.body() != null) {
                            Log.i(TAG, "sendLocation response.body()!=null");
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 261", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 262", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Log.i(TAG, "getLocationStatus response.code()==400");
                    Toast.makeText(getApplicationContext(), apiText.getText(ApiConstant.USER_NOT_FOUND), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 263", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure");
                Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 264", Toast.LENGTH_SHORT).show();
            }
        });
    }
    */

    //TODO deprecated api
    /*
    private void sendHomeLocation(float lat, float longt) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance().getApi(getApplicationContext()).setUserHomeLocation(prefManager.getUserEmail(), lat, longt, lat, longt);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    Log.i(TAG, "setHomeLocation response.code()==201");
                    if (response.isSuccessful()) {
                        Log.i(TAG, "setHomeLocation İsSuccessful");
                        if (response.body() != null) {
                            Log.i(TAG, "setHomeLocation response.body()!=null");
                            prefManager.setIsLocationCanChange(false);
                            prefManager.setHomeLocationLat(lat);
                            prefManager.setHomeLocationLong(longt);
                            Toast.makeText(getApplicationContext(), getString(R.string.home_location_saved), Toast.LENGTH_SHORT).show();
                            requestLocationData(MyConstant.LOCATION_SEND);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 171", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 172", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Log.i(TAG, "setHomeLocation response.code()==400");
                    Toast.makeText(getApplicationContext(), apiText.getText(ApiConstant.USER_NOT_FOUND), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 173", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure");
                Toast.makeText(getApplicationContext(), getString(R.string.occurred_error) + " 174", Toast.LENGTH_SHORT).show();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }
    */

    //endregion Location Server Methods


    private String getMysqlDataTimeStandart(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString();
    }

    private class startCovidTask extends AsyncTask<double[][], Integer, int[]> {
        protected int[] doInBackground(double[][]... datas) {
            double[][] data = datas[0];
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("SmartSense", "Task başladı");
        }


        @Override
        protected void onPostExecute(int[] ints) {
            super.onPostExecute(ints);
            Log.i("SmartSense", "Task bitti.");

        }
    }

    private void sendNotification(String title, String messageBody) {
        int notificationId = new Random().nextInt(1000);

        //Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_alarm);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notify)
                        .setContentTitle(title)
                        //.setLargeIcon(icon)
                        .setContentText(messageBody)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{100, 200, 200, 100});

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        getResources().getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(getResources().getString(R.string.notification_description));
                channel.enableLights(true);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
        }
    }

    public void mRequestLocationPermissions() {
        try {
            String[] permissionRequested;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                permissionRequested = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (snackbar != null) {
                        if (snackbar.isShown()) {
                            snackbar.dismiss();
                        }
                    }
                    getGPSDialog();
                } else {
                    requestPermissions(permissionRequested, REQUEST_PERMISSIONS_REQUEST_CODE);
                }
            } else {
                permissionRequested = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (snackbar != null) {
                        if (snackbar.isShown()) {
                            snackbar.dismiss();
                        }
                    }
                    getGPSDialog();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(permissionRequested, REQUEST_PERMISSIONS_REQUEST_CODE);
                    }
                }
            }
        } catch (Exception e) {
            Log.w("SmartSense", e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        (grantResults[1] == PackageManager.PERMISSION_GRANTED) &&
                        (grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                    if (snackbar != null) {
                        if (snackbar.isShown()) {
                            snackbar.dismiss();
                        }
                    }
                    getGPSDialog();
                } else {
                    snackbar.setAction(getString(R.string.location_permission_enable), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.setData(Uri.parse("package:" + getPackageName()));
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }
                    }).show();
                }
            } else {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    if (snackbar != null) {
                        if (snackbar.isShown()) {
                            snackbar.dismiss();
                        }
                    }
                    getGPSDialog();
                } else {
                    snackbar.setAction(getString(R.string.location_permission_enable), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.setData(Uri.parse("package:" + getPackageName()));
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }
                    }).show();
                }
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                // All required changes were successfully made
                Log.i(TAG, "User has clicked on OK - So GPS is on");
                setState(Common.requestingLocationUpdates(CovidMainActivity.this));
                bindService(new Intent(CovidMainActivity.this, MyBackgroundService.class), mLocationServiceConnection, Context.BIND_AUTO_CREATE);
                if (isForHome) {
                    setupLocationForHome();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user was asked to change settings, but chose not to
                Log.i(TAG, "User has clicked on NO, THANKS - So GPS is still off.");
                getGPSDialog();
            }
        } else if (requestCode == BAND_1963_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "onActivityResult: new band result ok");
                device = data.getParcelableExtra(EXTRA_DEVICE);
                if (device != null) {
                    prefManager.setBandType(MyConstant.BAND_1963);
                    prefManager.setBandName(device.getName());
                    prefManager.setBandMac(device.getAddress());
                    Log.i(TAG, (prefManager.getBandName() + " " + prefManager.getBandMac()));

                    BLUETOOTH_DATA_IN_MIN = 60;

                    band1963And1939Connect();

                } else {
                    Log.e(TAG, "Device null");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.i(TAG, "onActivityResult: new band RESULT_CANCELED");
            }
        } else if (requestCode == BAND_SMARTSENSE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                device = data.getParcelableExtra(EXTRA_DEVICE);
                if (device != null) {
                    prefManager.setBandType(MyConstant.BAND_SMARTSENSE);
                    prefManager.setBandName(device.getName());
                    prefManager.setBandMac(device.getAddress());
                    Log.i(TAG, (prefManager.getBandName() + " " + prefManager.getBandMac()));
                    //isConnected = true;

                    Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
                    bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                } else {
                    Log.e(TAG, "Device null");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }


    }//onActivityResult


    //region new band 1963

    private void band1963And1939Connect() {
        Log.i(TAG, "band1963And1939Connect");
        tempSampleCounter1 = 0;
        heartSampleCounter = 0;
        spO2SampleCounter = 0;
        batteryHandler.removeCallbacks(batteryRunnable);
        band1963And1939Handler.removeCallbacks(band1963And1939Runnable);
        band1963And1939SpO2Handler.removeCallbacks(band1963And1939SpO2Runnable);

        if (!isSubscribe) {
            isSubscribe = true;
            subscribe();
            init();
        }

        Log.i(TAG, " BleManager.init");
        BleManager.init(this);
        BleManager.getInstance().connectDevice(prefManager.getBandMac());

        /*new Handler().postDelayed(() ->
                sendValue(BleSDK.RealTimeStep(isStartReal, true)), 500);*/
    }

    private void init() {
        Log.i(TAG, "init: 1963");
        subscription = RxBus.getInstance().toObservable(BleData.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(bleData -> {
            String action = bleData.getAction();
            if (action.equals(BleService.ACTION_GATT_onDescriptorWrite)) {
                Log.i(TAG, "init: ACTION_GATT_onDescriptorWrite");
                isConnected = true;
                isBand1963And1939Connected = true;
                lastDataTime = System.currentTimeMillis();
                sendValue(BleSDK.GetDeviceBatteryLevel());
                batteryHandler.postDelayed(batteryRunnable, batteryTime);
                band1963And1939Handler.postDelayed(band1963And1939Runnable, 1000);
                band1963And1939SpO2Handler.postDelayed(band1963And1939SpO2Runnable, (SPO2_NOTIFICATION_INTERVAL * 60 * 1000));
            } else if (action.equals(BleService.ACTION_GATT_DISCONNECTED)) {
                Log.i(TAG, "init: ACTION_GATT_DISCONNECTED");
                isStartReal = false;
                isConnected = false;
                isBand1963And1939Connected = false;
            }
        });
    }

    @Override
    public void dataCallback(Map<String, Object> map) {
        String dataType = getDataType(map);
        Log.e("info", map.toString());

        switch (dataType) {
           /* case BleConst.ReadSerialNumber:
                showDialogInfo(map.toString());
                break;*/
            case BleConst.RealTimeStep:
                try {
                    Map<String, String> maps = getData(map);
                    String step = maps.get(DeviceKey.Step);
                    String cal = maps.get(DeviceKey.Calories);
                    String distance = maps.get(DeviceKey.Distance);
                    String time = maps.get(DeviceKey.ExerciseMinutes);
                    String ActiveTime = maps.get(DeviceKey.ActiveMinutes);
                    String heart = maps.get(DeviceKey.HeartRate);
                    String TEMP = maps.get(DeviceKey.TempData);
                /*textViewCal.setText(cal);
                textViewStep.setText(step);
                textViewDistance.setText(distance);
                textViewTime.setText(time);
                textViewHeartValue.setText(heart);
                textViewActiveTime.setText(ActiveTime);
                textViewTempValue.setText(TEMP);*/
                    Log.i(TAG, "dataCallback: step: " + step + " heart: " + heart + " temp: " + TEMP);
                    lastDataTime = System.currentTimeMillis();

                    float temp = 0;
                    if (TEMP != null) {
                        TEMP = TEMP.replace(",", ".");
                        temp = Float.parseFloat(TEMP);
                    }

                    int heartInt = 0;
                    if (heart != null) {
                        heart = heart.replace(",", ".");
                        heartInt = (int) Float.parseFloat(heart);
                    }

                    //float spO2 = Float.parseFloat();

                    if (temp != 0.0f) {
                        lastTemp = temp;
                    }

                    if (heartInt != 0) {
                        lastHeart = heartInt;
                    }

               /* if (spO2 != 0.0f) {
                    lastSpO2 = spO2;
                }*/

                    tempData(temp);
                    heartData(heartInt);

                    // spO2Data(spO2);
                } catch (Exception e) {
                    Log.i(TAG, "dataCallback: " + e.getMessage());
                }
                break;

            case BleConst.GetDeviceBatteryLevel:
                try {
                    Map<String, String> data = getData(map);
                    String battery = data.get(DeviceKey.BatteryLevel);
                    Log.i(TAG, "dataCallback: battery: " + battery);
                    if (battery != null) {
                        batteryPercentage = Integer.parseInt(battery);
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
                break;
            case BleConst.Blood_oxygen:
                try {
                    String spO2String = map.toString();
                    Log.i(TAG, "Blood_oxygen: " + spO2String);
                    String[] spo2s = spO2String.split("Blood_oxygen=");
                    if (spo2s.length > 1) {
                        int spO2 = Integer.parseInt(spo2s[1].substring(0, spo2s[1].indexOf("}")));
                        Log.i(TAG, "dataCallback: spO2: " + spO2);
                        if (spO2 != 0) {
                            saveSpo2ForBand1963(spO2);
                            sendValue(BleSDK.GetBloodOxygen(0x99));
                        }
                    }
                } catch (Exception e) {
                    Log.i(TAG, "dataCallback: " + e.getMessage());
                }
                break;

            /*case BleConst.DeviceSendDataToAPP:
                showDialogInfo(BleConst.DeviceSendDataToAPP);
                break;
            case BleConst.FindMobilePhoneMode:
                showDialogInfo(BleConst.FindMobilePhoneMode);
                break;
            case BleConst.RejectTelMode:
                showDialogInfo(BleConst.RejectTelMode);
                break;
            case BleConst.TelMode:
                showDialogInfo(BleConst.TelMode);
                break;
            case BleConst.BackHomeView:
                showToast(map.toString());
                break;
            case BleConst.Sos:
                showToast(map.toString());
                break;*/
        }
    }

    public void saveSpo2ForBand1963(int value) {
        Covid covid = new Covid((System.currentTimeMillis()), value, MyConstant.SPO2, MyConstant.AUTO_SAVE);
        repository.insert(covid);

        checkSpO2(value);

        if (prefManager.getSentDataServer()) {
            sendVitalDataToServer(MyConstant.SPO2, value);
        }
    }

    private void unsubscribe() {
        Log.i(TAG, "unsubscribe");
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    protected void subscribe() {
        Log.i(TAG, "subscribe");
        subscription = RxBus.getInstance().toObservable(BleData.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(bleData -> {
            String action = bleData.getAction();
            if (action.equals(BleService.ACTION_DATA_AVAILABLE)) {
                byte[] value = bleData.getValue();
                BleSDK.DataParsingWithData(value, CovidMainActivity.this);
            }
        });
    }


    public void disconnect1963Band() {
        if(BleManager.getInstance() != null){
            if (BleManager.getInstance().isConnected()) {
                BleManager.getInstance().disconnectDevice();
            }
        }
    }


    @Override
    public void dataCallback(byte[] value) {

    }


    protected void sendValue(byte[] value) {
        if (!BleManager.getInstance().isConnected()) {
            return;
        }
        if (value == null) return;

        BleManager.getInstance().writeValue(value);

    }

    AlertDialog alertDialog = null;

    protected void showDialogInfo(String message) {
        if (null == alertDialog) {
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage(message).setPositiveButton("Ok", null).create();
            alertDialog.show();
        } else {
            alertDialog.dismiss();
            alertDialog = null;
            alertDialog = new AlertDialog.Builder(this)
                    .setMessage(message).setPositiveButton("Ok", null).create();
            alertDialog.show();
        }

    }

    protected void showSetSuccessfulDialogInfo(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message + " Successful").setPositiveButton("Ok", null).create().show();
    }

    protected String getDataType(Map<String, Object> maps) {
        return (String) maps.get(DeviceKey.DataType);
    }

    protected boolean getEnd(Map<String, Object> maps) {
        return (boolean) maps.get(DeviceKey.End);
    }

    protected Map<String, String> getData(Map<String, Object> maps) {
        return (Map<String, String>) maps.get(DeviceKey.Data);
    }

    protected void offerData(byte[] value) {
        BleManager.getInstance().offerValue(value);
    }

    protected void offerData() {

        BleManager.getInstance().writeValue();
    }

    protected void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
        }
        if (!progressDialog.isShowing()) progressDialog.show();
    }

    protected void disMissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }


    //endregion new band 1963


    private void getShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");

        isForHome = false;
        mRequestLocationPermissions();

        if (!prefManager.isLoggedIn()) {
            Intent intent = new Intent(CovidMainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            Log.i(TAG, "onStart: register eventbus");
            EventBus.getDefault().register(this);
        }


    }

    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        //Bluetooth connection
        if (prefManager.getBandType() == MyConstant.BAND_SMARTSENSE) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            if (prefManager.getBandMac() != null && prefManager.getBandName() != null) {

                if (mBluetoothLeService != null) {
                    mBluetoothLeService.connect(prefManager.getBandMac());
                }

                Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
            }
        }
        //Bluetooth connection end


        //TODO: Düzenle
       /* if (myMyo != null) {
            if (myMyo.isConnected()) {
                Log.i("Smartsense", "Armband connected");
                isConnected = true;
            } else {
                Log.i("Smartsense", "Armband not connected");
                isConnected = false;
                if (prefManager.getMyoArmbandMac() != null) {
                    if (!EpilepsyMainActivity.isConnected) {
                        myoConnect();
                    }
                }
            }
        }

        */
    }


    protected void onPause() {
        super.onPause();
        Log.i(TAG, "CovidMain Pause");
        //For bluetooth connection


    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        //Location
        if (mBound) {
            unbindService(mLocationServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        //Location end

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (prefManager.getBandType() == MyConstant.BAND_SMARTSENSE) {
            if (mGattUpdateReceiver != null) {
                unregisterReceiver(mGattUpdateReceiver);
            }
        }

        //We don't want any callbacks when the Activity is gone, so unregister the listener.
        unsubscribe();
        disconnect1963Band();


        batteryHandler.removeCallbacks(batteryRunnable);
        band1963And1939Handler.removeCallbacks(band1963And1939Runnable);
        band1963And1939ConnectHandler.removeCallbacks(band1963And1939ConnectRunnable);
        band1963And1939ConnectHandler.removeCallbacks(band1963And1939ConnectRunnable);

        super.onDestroy();
    }


}