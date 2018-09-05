package com.demo.barcode.screen.group_code;

import android.app.Activity;
import android.content.Context;
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

public class GroupCodeActivity extends BaseActivity {
    private final static int REQUEST_CODE = 178;
    @Inject
    GroupCodePresenter GroupCodePresenter;

    GroupCodeFragment fragment;

    public static void start(Activity context, boolean groupType, int orderId, int departmentId, int times) {
        Intent intent = new Intent(context, GroupCodeActivity.class);
        intent.putExtra(GroupCodeFragment.GROUP_CODE, groupType);
        intent.putExtra(GroupCodeFragment.DEPARTMENT_ID, departmentId);
        intent.putExtra(GroupCodeFragment.ORDER_ID, orderId);
        intent.putExtra(GroupCodeFragment.TIMES, times);
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new GroupCodeModule(fragment))
                .inject(this);

//        Window w = getWindow(); // in Activity's onCreate() for instance
//        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//        }
    }

    private void initFragment() {
        fragment = (GroupCodeFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = GroupCodeFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(GroupCodeFragment fragment, int frameId) {
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

}
