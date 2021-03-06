package com.demo.barcode.screen.detail_package;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.barcode.R;
import com.demo.barcode.adapter.ProductHistoryAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.dialogs.ChangeIPAddressDialog;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.spinner.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailPackageFragment extends BaseFragment implements DetailPackageContract.View {
    private final String TAG = DetailPackageFragment.class.getName();
    private DetailPackageContract.Presenter mPresenter;
    private ProductHistoryAdapter adapter;
    public static final String ORDER_ID = "order_id";
    public static final String APARTMENT_ID = "apartment_id";
    public static final String ORDER_TYPE = "order_type";
    public static final String HISTORY = "history";
    private long orderId;
    private long apartmentId;
    private HistoryEntity historyEntity;
    public MediaPlayer mp1, mp2;
    private Vibrator vibrate;
    @BindView(R.id.lv_codes)
    ListView lvCode;

    @BindView(R.id.txt_code_pack)
    TextView txtCodePack;

    @BindView(R.id.txt_code_so)
    TextView txtCodeSO;

    @BindView(R.id.txt_customer_name)
    TextView txtCustomerName;

    @BindView(R.id.txt_total)
    TextView txtTotal;

    @BindView(R.id.txt_date_create)
    TextView txtDate;

    @BindView(R.id.txt_serial_module)
    TextView txtSerialModule;

    @BindView(R.id.txt_floor)
    TextView txtFloor;

    @BindView(R.id.txt_serial_pack)
    TextView txtSerialPack;

    private long packageId;
    private int orderType;

    public DetailPackageFragment() {
        // Required empty public constructor
    }

    public static DetailPackageFragment newInstance() {
        DetailPackageFragment fragment = new DetailPackageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_pack, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);
        orderId = getActivity().getIntent().getLongExtra(ORDER_ID, 0);
        historyEntity = (HistoryEntity) getActivity().getIntent().getSerializableExtra(HISTORY);
        apartmentId = getActivity().getIntent().getLongExtra(APARTMENT_ID, 0);
        orderType = getActivity().getIntent().getIntExtra(ORDER_TYPE, 0);
        initView();
        return view;
    }

    private void initView() {
        txtDate.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
        txtSerialModule.setText(historyEntity.getModule());
        txtSerialPack.setText(historyEntity.getPackageList().get(0).getSerialPack());
        txtCodePack.setText(historyEntity.getPackageList().get(0).getPackCode());
        mPresenter.getOrderPackaging(orderId);
        mPresenter.getApartment(apartmentId);
        adapter = new ProductHistoryAdapter(historyEntity.getPackageList().get(0).getProductPackagingEntityList());
        lvCode.setAdapter(adapter);
        txtTotal.setText(String.valueOf((int)historyEntity.getPackageList().get(0).getTotal()));
        packageId = historyEntity.getPackageList().get(0).getPackageId();
    }


    @Override
    public void setPresenter(DetailPackageContract.Presenter presenter) {
        this.mPresenter = Precondition.checkNotNull(presenter);
    }

    @Override
    public void showProgressBar() {
        showProgressDialog();
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    public void showNotification(String content, int type) {
        new SweetAlertDialog(getContext(), type)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(content)
                .setConfirmText(getString(R.string.text_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void showError(String message) {
        showNotification(message, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void showSuccess(String message) {
        showToast(message);
    }


    @Override
    public void turnOnVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrate.vibrate(500);
        }
    }

    @Override
    public void startActivityHistory() {
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    @Override
    public void showApartmentName(String apartmentName) {
        txtFloor.setText(apartmentName);
    }


    @Override
    public void showOrder(SOEntity so) {
        txtCustomerName.setText(so.getCustomerName());
        txtCodeSO.setText(so.getCodeSO());
    }

    @Override
    public void showDialogCreateIPAddress() {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port, 0, packageId);
                dialog.dismiss();
            }
        });
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @OnClick(R.id.img_back)
    public void back() {

        getActivity().finish();
    }

    @OnClick(R.id.btn_print)
    public void print() {
        mPresenter.printTemp(0, packageId);
    }


    @Override
    public void startMusicError() {
        mp2.start();
    }

    @Override
    public void startMusicSuccess() {
        mp1.start();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
