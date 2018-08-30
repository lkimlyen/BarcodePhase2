package com.demo.barcode.screen.print_stamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;
import com.demo.barcode.adapter.DetailPrintTempAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.spinner.SearchableSpinner;

import java.util.List;

import butterknife.Bind;
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
    public static final String SERIAL_PACK = "serial_pack";
    private final String TAG = PrintStempFragment.class.getName();
    private PrintStempContract.Presenter mPresenter;
    private DetailPrintTempAdapter adapter;
    private int orderId;
    private int apartmentId;
    private int moduleId;
    private String serialPack;
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
        orderId = getActivity().getIntent().getIntExtra(ORDER_ID, 0);
        moduleId = getActivity().getIntent().getIntExtra(MODULE_ID, 0);
        apartmentId = getActivity().getIntent().getIntExtra(APARTMENT_ID, 0);
        serialPack = getActivity().getIntent().getStringExtra(SERIAL_PACK);
        initView();
        return view;
    }

    private void initView() {
        txtDate.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
        txtSerialPack.setText(serialPack);
        mPresenter.getOrderPackaging(orderId);

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
    public void showOrderPackaging(LogListOrderPackaging log) {
        txtCodeSO.setText(log.getCodeSO());
        txtCustomerName.setText(log.getCustomerName());
    }

    @Override
    public void showTotalNumberScan(int sum) {
        txtTotal.setText(String.valueOf(sum));
    }

    @Override
    public void showListScanPackaging(List<LogScanPackaging> list) {

    }

    @Override
    public void showDialogCreateIPAddress() {

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
