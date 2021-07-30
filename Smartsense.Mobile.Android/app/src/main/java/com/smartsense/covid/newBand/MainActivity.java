package com.smartsense.covid.newBand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jstyle.blesdk1963.Util.BleSDK;
import com.jstyle.blesdk1963.constant.BleConst;
import com.jstyle.blesdk1963.constant.DeviceKey;
import com.smartsense.covid.R;

import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements MainAdapter.onItemClickListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.main_recyclerview)
    RecyclerView mainRecyclerview;
    @BindArray(R.array.item_options)
    String[] options;
    @BindView(R.id.BT_CONNECT)
    Button btConnect;
    @BindView(R.id.button_startreal)
    Button buttonStartreal;
    @BindView(R.id.textView_step)
    TextView textViewStep;
    @BindView(R.id.textView_cal)
    TextView textViewCal;
    @BindView(R.id.textView_distance)
    TextView textViewDistance;
    @BindView(R.id.textView_time)
    TextView textViewTime;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView_heartValue)
    TextView textViewHeartValue;
    @BindView(R.id.textView_tempValue)
    TextView textViewTempValue;
    @BindView(R.id.textView_ActiveTime)
    TextView textViewActiveTime;
    @BindView(R.id.SwitchCompat_temp)
    Switch SwitchCompatTemp;
    private ProgressDialog progressDialog;
    private Disposable subscription;
    private String address;
    boolean isStartReal;
    public static int phoneDataLength = 200;//手机一个包能发送的最多数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        connectDevice();
    }

    private void connectDevice() {
        address = getIntent().getStringExtra("address");
        if (TextUtils.isEmpty(address)) {
            Log.i(TAG, "onCreate: address null ");
            return;
        }
        Log.i(TAG, "onCreate: ");
        BleManager.getInstance().connectDevice(address);
        showConnectDialog();
    }

    private void showConnectDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.connectting));
        if (!progressDialog.isShowing()) progressDialog.show();

    }

    private void dissMissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void init() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mainRecyclerview.setLayoutManager(gridLayoutManager);
        final MainAdapter mainAdapter = new MainAdapter(options, this);
        mainRecyclerview.setAdapter(mainAdapter);
        subscription = RxBus.getInstance().toObservable(BleData.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BleData>() {
            @Override
            public void accept(BleData bleData) throws Exception {
                String action = bleData.getAction();
                if (action.equals(BleService.ACTION_GATT_onDescriptorWrite)) {
                    mainAdapter.setEnable(true);
                    btConnect.setEnabled(false);
                    buttonStartreal.setEnabled(true);
                    SwitchCompatTemp.setEnabled(true);
                    dissMissDialog();
                } else if (action.equals(BleService.ACTION_GATT_DISCONNECTED)) {
                    mainAdapter.setEnable(false);
                    btConnect.setEnabled(true);
                    buttonStartreal.setEnabled(false);
                    SwitchCompatTemp.setEnabled(false);
                    isStartReal = false;
                    dissMissDialog();
                }
            }
        });
        SwitchCompatTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendValue(BleSDK.RealTimeStep(isStartReal, isChecked));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
        if (BleManager.getInstance().isConnected()) BleManager.getInstance().disconnectDevice();
    }

    private void unsubscribe() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
            Log.i(TAG, "unSubscribe: ");
        }
    }

    @OnClick({R.id.BT_CONNECT, R.id.button_startreal, R.id.EnterPhotoMode, R.id.Exitphotomode
            , R.id.ClearBraceletData})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ClearBraceletData:
                sendValue(BleSDK.ClearBraceletData());
                break;
            case R.id.BT_CONNECT:
                connectDevice();
                break;
            case R.id.button_startreal:
                isStartReal = !isStartReal;
                buttonStartreal.setText(isStartReal ? "Stop" : "Start");

                sendValue(BleSDK.RealTimeStep(isStartReal, SwitchCompatTemp.isChecked()));
                break;
            case R.id.EnterPhotoMode:
                sendValue(BleSDK.EnterPhotoMode());
                break;
            case R.id.Exitphotomode:
                sendValue(BleSDK.ExitPhotoMode());
                break;
        }

    }

    @Override
    public void dataCallback(Map<String, Object> map) {
        super.dataCallback(map);
        String dataType = getDataType(map);
        Log.e("info", map.toString());
        switch (dataType) {
            case BleConst.ReadSerialNumber:
                showDialogInfo(map.toString());
                break;
            case BleConst.RealTimeStep:
                Map<String, String> maps = getData(map);
                String step = maps.get(DeviceKey.Step);
                String cal = maps.get(DeviceKey.Calories);
                String distance = maps.get(DeviceKey.Distance);
                String time = maps.get(DeviceKey.ExerciseMinutes);
                String ActiveTime = maps.get(DeviceKey.ActiveMinutes);
                String heart = maps.get(DeviceKey.HeartRate);
                String TEMP = maps.get(DeviceKey.TempData);
                textViewCal.setText(cal);
                textViewStep.setText(step);
                textViewDistance.setText(distance);
                textViewTime.setText(time);
                textViewHeartValue.setText(heart);
                textViewActiveTime.setText(ActiveTime);
                textViewTempValue.setText(TEMP);
                Log.i(TAG, "dataCallback: step: " + step + " heart: " + heart + " temp: " + TEMP);
                break;
            case BleConst.DeviceSendDataToAPP:
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
                break;
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}