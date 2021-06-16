package com.smartsense.covid.bluetoothlegatt;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class DerpData {
    public static List<BluetoothDevice> getListData() {
        List<BluetoothDevice> data = new ArrayList<>();
        return data;
    }
}
