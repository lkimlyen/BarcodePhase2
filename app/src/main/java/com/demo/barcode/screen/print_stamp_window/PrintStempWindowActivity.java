package com.demo.barcode.screen.print_stamp_window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseActivity;
import com.demo.barcode.app.di.Precondition;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempWindowActivity extends BaseActivity {
    public static final String ORDER_ID = "order_id";
    public static final String PRODUCT_SET_ID = "PRODUCT_SET_ID";
    public static final String DIRECTION = "DIRECTION";
    public static final String MAIN_ID = "serial_pack_id";
    public static final int REQUEST_CODE = 117;
    @Inject
    PrintStempWindowPresenter PrintStempWindowPresenter;

    PrintStempWindowFragment fragment;

    public static void start(Activity context, long orderId, long productSetId,int direction, long mainId ) {
        Intent intent = new Intent(context, PrintStempWindowActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(PRODUCT_SET_ID, productSetId);
        intent.putExtra(DIRECTION, direction);
        intent.putExtra(MAIN_ID,mainId);
        context.startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new PrintStempWindowModule(fragment))
                .inject(this);

    }

    private void initFragment() {
        fragment = (PrintStempWindowFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = PrintStempWindowFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(PrintStempWindowFragment fragment, int frameId) {
        Precondition.checkNotNull(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
       fragment.back();
    }
}
