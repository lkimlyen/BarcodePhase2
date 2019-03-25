package com.demo.barcode.widgets.barcodereader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.vision.barcode.Barcode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import com.demo.barcode.R;


public class BarcodeScanner {


    /**
     * Request codes
     */
    public static final int RC_HANDLE_CAMERA_PERM = 2;

    /**
     * Scanner modes
     */
    public static final int SCANNER_MODE_FREE = 1;
    public static final int SCANNER_MODE_CENTER = 2;

    protected final BarcodeScannerBuilder mBarcodeScannerBuilder;

    private FrameLayout mContentView; //Content frame for fragments

    private OnResultListener onResultListener;

    public BarcodeScanner(@NonNull BarcodeScannerBuilder barcodeScannerBuilder) {
        this.mBarcodeScannerBuilder = barcodeScannerBuilder;
    }

    public void setOnResultListener(OnResultListener onResultListener) {

        this.onResultListener = onResultListener;
    }
    @org.greenrobot.eventbus.Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBarcodeScannerResult(Barcode barcode){
        Log.d("bambi5","2");
        onResultListener.onResult(barcode);
        EventBus.getDefault().removeStickyEvent(barcode);
        EventBus.getDefault().unregister(this);
        mBarcodeScannerBuilder.clean();
    }


    /**
     * Interface definition for a callback to be invoked when a view is clicked.
     */
    public interface OnResultListener {
        void onResult(Barcode barcode);
    }

    /**
     * Start a scan for a barcode
     *
     * This opens a new activity with the parameters provided by the BarcodeScannerBuilder
     */
    public void startScan(){
        EventBus.getDefault().register(this);
        if(mBarcodeScannerBuilder.getActivity() == null){
            throw new RuntimeException("Could not start scan: Activity reference lost (please rebuild the BarcodeScanner before calling startScan)");
        }
        int mCameraPermission = ActivityCompat.checkSelfPermission(mBarcodeScannerBuilder.getActivity(), Manifest.permission.CAMERA);
        if (mCameraPermission != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }else{
            //Open activity
            EventBus.getDefault().postSticky(this);
            Intent intent = new Intent(mBarcodeScannerBuilder.getActivity(), BarcodeScannerActivity.class);
            mBarcodeScannerBuilder.getActivity().startActivityForResult(intent,332);
        }
    }

    private void requestCameraPermission() {
        final String[] mPermissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(mBarcodeScannerBuilder.getActivity(), Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(mBarcodeScannerBuilder.getActivity(), mPermissions, RC_HANDLE_CAMERA_PERM);
            return;
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(mBarcodeScannerBuilder.getActivity(), mPermissions, RC_HANDLE_CAMERA_PERM);
            }
        };
        Snackbar.make(mBarcodeScannerBuilder.mRootView, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, listener)
                .show();
    }

    public BarcodeScannerBuilder getMaterialBarcodeScannerBuilder() {
        return mBarcodeScannerBuilder;
    }



}
