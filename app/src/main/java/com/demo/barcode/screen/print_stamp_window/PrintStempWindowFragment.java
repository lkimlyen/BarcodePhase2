package com.demo.barcode.screen.print_stamp_window;

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
import com.demo.architect.data.model.offline.ListPackCodeWindowModel;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.barcode.R;
import com.demo.barcode.adapter.PrintStampAdapter;
import com.demo.barcode.adapter.PrintStampWindowAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.dialogs.ChangeIPAddressDialog;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempWindowFragment extends BaseFragment implements PrintStempWindowContract.View {
    public static final String ORDER_ID = "order_id";
    public static final String PRODUCT_SET_ID = "PRODUCT_SET_ID";
    public static final String DIRECTION = "DIRECTION";
    public static final String MAIN_ID = "serial_pack_id";
    private final String TAG = PrintStempWindowFragment.class.getName();
    private boolean isEnough = false;
    private PrintStempWindowContract.Presenter mPresenter;
    private PrintStampWindowAdapter adapter;
    private long orderId;
    private long productSetId;
    private int direction;
    private long mainId;
    private int numberOnPack;
    @Bind(R.id.lv_codes)
    ListView lvCode;

    @Bind(R.id.txt_code_pack)
    TextView txtCodePack;

    @Bind(R.id.txt_code_so)
    TextView txtCodeSO;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.txt_total)
    TextView txtTotal;

    @Bind(R.id.txt_date_create)
    TextView txtDate;

    @Bind(R.id.txt_serial_pack)
    TextView txtSerialPack;

    @Bind(R.id.txt_serial_module)
    TextView txtSerialModule;

    @Bind(R.id.txt_floor)
    TextView txtFloor;

    public PrintStempWindowFragment() {
        // Required empty public constructor
    }


    public static PrintStempWindowFragment newInstance() {
        PrintStempWindowFragment fragment = new PrintStempWindowFragment();
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
        productSetId = getActivity().getIntent().getLongExtra(PRODUCT_SET_ID, 0);
        direction = getActivity().getIntent().getIntExtra(DIRECTION, 0);
        mainId = getActivity().getIntent().getLongExtra(MAIN_ID, 0);
        initView();
        return view;
    }

    private void initView() {
        txtDate.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
        mPresenter.getOrderPackaging(orderId);
        mPresenter.getSetWindow(productSetId);
        switch (direction) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        mPresenter.getListDetailPackageById(mainId);
    }


    @Override
    public void setPresenter(PrintStempWindowContract.Presenter presenter) {
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
    public void showListScanPackaging(ListPackCodeWindowModel list) {
        adapter = new PrintStampWindowAdapter(list.getList());
        lvCode.setAdapter(adapter);
        txtSerialPack.setText(list.getPackCode());
        txtCodePack.setText(list.getNumberSetOnPack() + "");
        numberOnPack = list.getNumberSetOnPack();
    }


    @Override
    public void showDialogCreateIPAddress() {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port, orderId, productSetId, direction, txtCodePack.getText().toString(),numberOnPack, 0, mainId);
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
    public void showProductSetName(String apartmentName) {
        txtFloor.setText(apartmentName);
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

        mPresenter.printTemp(orderId, productSetId, direction, txtCodePack.getText().toString(), numberOnPack, 0, mainId);
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
