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

import butterknife.Bind;
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
    private int orderId;
    private int apartmentId;
    private HistoryEntity historyEntity;
    public MediaPlayer mp1, mp2;
    private Vibrator vibrate;
    @Bind(R.id.lv_codes)
    ListView lvCode;

    @Bind(R.id.txt_code_pack)
    TextView txtCodePack;

    @Bind(R.id.txt_code_so)
    TextView txtCodeSO;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;
    @Bind(R.id.ss_code_pack)
    SearchableSpinner ssCodePack;
    @Bind(R.id.txt_total)
    TextView txtTotal;

    @Bind(R.id.txt_date_create)
    TextView txtDate;

    @Bind(R.id.txt_serial_module)
    TextView txtSerialModule;

    @Bind(R.id.txt_floor)
    TextView txtFloor;

    private int packageId;
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
        orderId = getActivity().getIntent().getIntExtra(ORDER_ID, 0);
        historyEntity = (HistoryEntity) getActivity().getIntent().getSerializableExtra(HISTORY);
        apartmentId = getActivity().getIntent().getIntExtra(APARTMENT_ID, 0);
        orderType = getActivity().getIntent().getIntExtra(ORDER_TYPE, 0);
        initView();
        return view;
    }

    private void initView() {
        txtDate.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
        txtSerialModule.setText(historyEntity.getModule());
        mPresenter.getOrderPackaging(orderId);
        mPresenter.getApartment(apartmentId);
        mPresenter.getListCodePack(orderId, orderType, historyEntity.getProductId());
        ssCodePack.setPrintStamp(true);
        ssCodePack.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });
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
    public void showListProductHistory(PackageEntity packageEntity) {
        if (packageEntity == null){
            packageId = 0;
            showError(getString(R.string.text_no_data));
            adapter = new ProductHistoryAdapter(new ArrayList<>());
            lvCode.setAdapter(adapter);
            return;
        }
        adapter = new ProductHistoryAdapter(packageEntity.getProductPackagingEntityList());
        lvCode.setAdapter(adapter);
        txtTotal.setText(String.valueOf(packageEntity.getTotal()));
        txtCodePack.setText(packageEntity.getPackCode());
        packageId = packageEntity.getPackageId();
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
    public void showListCodePack(List<CodePackEntity> list) {
        ArrayAdapter<CodePackEntity> adapter = new ArrayAdapter<CodePackEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssCodePack.setAdapter(adapter);
        ssCodePack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.getListHistoryBySerialPack(historyEntity, list.get(position).getSttPack());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        if (packageId == 0){
            showError(getString(R.string.text_no_data_print));
            return;
        }
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
