package com.demo.barcode.screen.detail_package;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;

import com.demo.architect.data.model.HistoryEntity;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseActivity;
import com.demo.barcode.app.di.Precondition;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.screen.dashboard.DashboardFragment;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailPackageActivity extends BaseActivity {
    public static final int REQUEST_CODE = 123;
    @Inject
    DetailPackagePresenter DetailPackagePresenter;

    DetailPackageFragment fragment;

    public static void start(Activity activity, long orderId, long apartmentId,int orderType, HistoryEntity historyEntity) {
        Intent intent = new Intent(activity, DetailPackageActivity.class);
        intent.putExtra(DetailPackageFragment.ORDER_ID, orderId);
        intent.putExtra(DetailPackageFragment.APARTMENT_ID, apartmentId);
        intent.putExtra(DetailPackageFragment.ORDER_TYPE,orderType);
        intent.putExtra(DetailPackageFragment.HISTORY, historyEntity);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new DetailPackageModule(fragment))
                .inject(this);
    }

    private void initFragment() {
        fragment = (DetailPackageFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = DetailPackageFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(DetailPackageFragment fragment, int frameId) {
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
        fragment.onActivityResult(requestCode,resultCode, data);

    }

}
