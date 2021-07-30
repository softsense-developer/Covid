package com.smartsense.covid.ui.baglanti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.smartsense.covid.bluetoothlegatt.BluetoothScanActivity;
import com.smartsense.covid.CovidMainActivity;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;
import com.smartsense.covid.newBand.DeviceScanActivity;

public class BaglantiFragment extends Fragment {


    private MaterialCardView smartsenseConnect, band1963Connection;
    private MaterialButton disconnect, band1963Disconnect;
    private PrefManager prefManager;
    private LinearLayout connectedLayout, band1963ConnectedLayout;
    private TextView connectedText, band1963ConnectedText;

    private Runnable runnable;
    private Handler hndler;
    private final int BAND_1963_REQUEST = 221;

   /* public interface onBluetoothScanListener {
        public void onClick(int i);
    }
    private onBluetoothScanListener bluetoothScanListener;

    */


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_baglanti, container, false);

        smartsenseConnect = root.findViewById(R.id.SmartsenseBandConnection);
        disconnect = root.findViewById(R.id.disconnect);
        connectedLayout = root.findViewById(R.id.connectedLayout);
        connectedText = root.findViewById(R.id.connectedText);
        prefManager = new PrefManager(getContext());

        band1963Connection = root.findViewById(R.id.band1963Connection);
        band1963Disconnect = root.findViewById(R.id.band1963Disconnect);
        band1963ConnectedText = root.findViewById(R.id.band1963ConnectedText);
        band1963ConnectedLayout = root.findViewById(R.id.band1963ConnectedLayout);

        smartsenseConnect.setOnClickListener(view -> {
            if (!CovidMainActivity.isConnected) {
               // ((EpilepsyMainActivity) getActivity()).myoConnect();
            }
            Intent intent = new Intent(getActivity(), BluetoothScanActivity.class);
            startActivityForResult(intent, 1);
            //bluetoothScanListener.onClick(1);
        });


        disconnect.setOnClickListener(view -> {
            prefManager.setBandMac(null);
            prefManager.setBandName(null);
            connectedLayout.setVisibility(View.GONE);
            try {
                ((CovidMainActivity) getActivity()).onDisconnect();
            }catch (Exception e){
                Log.e("SmartSense", e.getMessage());
            }
        });

        hndler = new Handler();


        band1963Connection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DeviceScanActivity.class);
            startActivityForResult(intent, BAND_1963_REQUEST);
        });


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            bluetoothScanListener = (onBluetoothScanListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onBluetoothScanListener");
        }
    }

     */

    @Override
    public void onResume() {
        super.onResume();
        //TODO: Düzenle

        runnable = () -> {
            if (prefManager.getBandMac() != null) {
                hndler.postDelayed(runnable, 100);
                connectedLayout.setVisibility(View.VISIBLE);
                if (CovidMainActivity.isConnected) {
                    connectedText.setText((prefManager.getBandName() + " " + getString(R.string.connected) + "."));
                    disconnect.setText(getString(R.string.disconnect));
                } else {
                    connectedText.setText((prefManager.getBandName() + " " + getString(R.string.not_connected) + "."));
                    disconnect.setText(getString(R.string.disconnect_pairing));
                }
            }
        };
        hndler.postDelayed(runnable, 100);
    }

    @Override
    public void onPause() {
        super.onPause();
        hndler.removeCallbacks(runnable);
    }
}