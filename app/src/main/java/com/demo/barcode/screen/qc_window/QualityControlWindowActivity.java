package com.demo.barcode.screen.qc_window;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import android.view.WindowManager;

import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseActivity;
import com.demo.barcode.app.di.Precondition;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class QualityControlWindowActivity extends BaseActivity {
    @Inject
    QualityControlWindowPresenter QualityControlWindowPresenter;

    QualityControlWindowFragment fragment;

    public static void start(Context context) {
        Intent intent = new Intent(context, QualityControlWindowActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initFragment();
        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new QualityControlWindowModule(fragment))
                .inject(this);

//        Window w = getWindow(); // in Activity's onCreate() for instance
//        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//        }
    }

    private void initFragment() {
        fragment = (QualityControlWindowFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = QualityControlWindowFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(QualityControlWindowFragment fragment, int frameId) {
        Precondition.checkNotNull(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        fragment.back();
        // super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
