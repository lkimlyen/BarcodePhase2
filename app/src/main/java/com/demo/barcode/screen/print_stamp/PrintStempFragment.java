package com.demo.barcode.screen.print_stamp;

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
import com.demo.barcode.R;
import com.demo.barcode.adapter.DetailPrintTempAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.spinner.SearchableSpinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempFragment extends BaseFragment implements PrintStempContract.View {
    public static final String ORDER_ID = "order_id";
    public static final String FLOOR = "floor";
    public static final String MODULE = "module";
    private final String TAG = PrintStempFragment.class.getName();
    private PrintStempContract.Presenter mPresenter;
    private DetailPrintTempAdapter adapter;
    private int orderId;
    private String floor;
    private String module;
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

    @Bind(R.id.txt_serial_pack)
    TextView txtSerialPack;

    @Bind(R.id.txt_date_create)
    TextView txtDate;

    @Bind(R.id.ss_serial_package)
    SearchableSpinner ssSerialPack;

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
        module = getActivity().getIntent().getStringExtra(FLOOR);
        floor = getActivity().getIntent().getStringExtra(MODULE);
        initView();
        return view;
    }

    private void initView() {
        txtDate.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
        txtFloor.setText(floor);
        txtSerialModule.setText(module);

        ssSerialPack.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });
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
    public void showListSerialPack(LogListModulePagkaging log) {
        ArrayAdapter<LogListSerialPackPagkaging> adapter = new ArrayAdapter<LogListSerialPackPagkaging>(
                getContext(), android.R.layout.simple_spinner_item, log.getList());
        ssSerialPack.setAdapter(adapter);
        ssSerialPack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PrintStempFragment.this.adapter = new DetailPrintTempAdapter(log.getList().get(position).getList());
                lvCode.setAdapter(PrintStempFragment.this.adapter);
                txtCodePack.setText(log.getList().get(position).getCodeProduct());
                txtSerialPack.setText(log.getList().get(position).getSerialPack());
                mPresenter.getTotalScanBySerialPack(log.getList().get(position).getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
