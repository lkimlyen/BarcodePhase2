package com.demo.barcode.screen.print_stamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;
import com.demo.barcode.adapter.PrintStampAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.dialogs.ChangeIPAddressDialog;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempFragment extends BaseFragment implements PrintStempContract.View {
    public static final String ORDER_ID = "order_id";
    public static final String APARTMENT_ID = "apartment_id";
    public static final String MODULE_ID = "module_id";
    public static final String ORDER_TYPE = "order_type";
    public static final String LOG_SERIAL_ID = "LOG_SERIAL_ID";
    private final String TAG = PrintStempFragment.class.getName();
    private boolean isEnough = false;
    private PrintStempContract.Presenter mPresenter;
    private PrintStampAdapter adapter;
    private long orderId;
    private long apartmentId;
    private long moduleId;
    private int orderType;
    private long logSerialId;
    private String serialPack;
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

    @BindView(R.id.txt_serial_pack)
    TextView txtSerialPack;

    @BindView(R.id.txt_serial_module)
    TextView txtSerialModule;

    @BindView(R.id.txt_floor)
    TextView txtFloor;

    public PrintStempFragment() {
        // Required empty public constructor
    }


    public static PrintStempFragment newInstance() {
        PrintStempFragment fragment = new PrintStempFragment();
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
        View view = inflater.inflate(R.layout.fragment_print_stemp, container, false);
        ButterKnife.bind(this, view);
        orderId = getActivity().getIntent().getLongExtra(ORDER_ID, 0);
        moduleId = getActivity().getIntent().getLongExtra(MODULE_ID, 0);
        apartmentId = getActivity().getIntent().getLongExtra(APARTMENT_ID, 0);
        serialPack = getActivity().getIntent().getStringExtra(PrintStempActivity.SERIAL_PACK_ID);
        orderType = getActivity().getIntent().getIntExtra(ORDER_TYPE, 0);
        logSerialId = getActivity().getIntent().getLongExtra(LOG_SERIAL_ID, 0);
        initView();
        return view;
    }

    private void initView() {
        txtDate.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
        mPresenter.getOrderPackaging(orderId);
        mPresenter.getApartment(apartmentId);
        mPresenter.getModule(moduleId, serialPack);
        mPresenter.getListDetailPackageById(logSerialId);
        mPresenter.getListCodePack(orderId, orderType, moduleId);
        mPresenter.getTotalScanBySerialPack(orderId, apartmentId, moduleId, serialPack);

    }


    @Override
    public void setPresenter(PrintStempContract.Presenter presenter) {
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
    public void showOrderPackaging(SOEntity soEntity) {
        txtCodeSO.setText(soEntity.getCodeSO());
        txtCustomerName.setText(soEntity.getCustomerName());
    }

    @Override
    public void showTotalNumberScan(int sum) {
        txtTotal.setText(String.valueOf(sum));
    }

    @Override
    public void showListScanPackaging(LogListSerialPackPagkaging list, boolean enough) {
        isEnough = enough;
        adapter = new PrintStampAdapter(list.getList());
        lvCode.setAdapter(adapter);
    }


    @Override
    public void showDialogCreateIPAddress() {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port, orderId, apartmentId, moduleId, serialPack, 0,logSerialId);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showListCodePack(List<CodePackEntity> list) {

    }

    @Override
    public void startActivityCreate() {
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    @Override
    public void showApartmentName(String apartmentName) {
        txtFloor.setText(apartmentName);
    }

    @Override
    public void showModuleName(String module) {
        txtSerialModule.setText(module);
    }

    @Override
    public void showSerialPack(PackageEntity packageEntity) {
        txtSerialPack.setText(packageEntity.getSerialPack());
        txtCodePack.setText(packageEntity.getPackCode());
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

    @OnClick(R.id.btn_save)
    public void save() {
        if (!isEnough) {
            showError(getString(R.string.text_quality_product_not_enough_in_package));
            return;
        }
        mPresenter.printTemp(orderId, apartmentId, moduleId, serialPack, 0,logSerialId);
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
