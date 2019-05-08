package com.demo.barcode.screen.create_pack_window;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseActivity;
import com.demo.barcode.app.di.Precondition;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreatePackagingWindowActivity extends BaseActivity {
    @Inject
    CreatePackagingWindowPresenter CreatePackagingWindowPresenter;

    CreatePackagingWindowFragment fragment;


    public static void start(Context context) {
        Intent intent = new Intent(context, CreatePackagingWindowActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new CreatePackagingWindowModule(fragment))
                .inject(this);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    private void initFragment() {
        fragment = (CreatePackagingWindowFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = CreatePackagingWindowFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(CreatePackagingWindowFragment fragment, int frameId) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  mUnregistrar.unregister();
    }
}