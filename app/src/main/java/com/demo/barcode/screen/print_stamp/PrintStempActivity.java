package com.demo.barcode.screen.print_stamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;

import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseActivity;
import com.demo.barcode.app.di.Precondition;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempActivity extends BaseActivity {
    public static final String ORDER_ID = "order_id";
    public static final String APARTMENT_ID = "apartment_id";
    public static final String MODULE_ID = "module_id";
    public static final String SERIAL_PACK_ID = "serial_pack_id";
    public static final String ORDER_TYPE = "order_type";
    public static final String LOG_SERIAL_ID = "LOG_SERIAL_ID";
    public static final int REQUEST_CODE = 117;
    @Inject
    PrintStempPresenter PrintStempPresenter;

    PrintStempFragment fragment;

    public static void start(Activity context, long orderId, long apartmentId, long moduleId,String serialPackId, int orderType, long logSerialId) {
        Intent intent = new Intent(context, PrintStempActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(APARTMENT_ID, apartmentId);
        intent.putExtra(MODULE_ID, moduleId);
        intent.putExtra(SERIAL_PACK_ID,serialPackId);
        intent.putExtra(ORDER_TYPE,orderType);
        intent.putExtra(LOG_SERIAL_ID,logSerialId);
        context.startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new PrintStempModule(fragment))
                .inject(this);

    }

    private void initFragment() {
        fragment = (PrintStempFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = PrintStempFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(PrintStempFragment fragment, int frameId) {
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
