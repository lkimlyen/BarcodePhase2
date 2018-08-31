package com.demo.barcode.screen.history_pack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.barcode.R;
import com.demo.barcode.adapter.HistoryAdapter;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.TypeSOManager;
import com.demo.barcode.screen.detail_package.DetailPackageActivity;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.AnimatedExpandableListView;
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

public class HistoryPackageFragment extends BaseFragment implements HistoryPackageContract.View {
    private final String TAG = HistoryPackageFragment.class.getName();
    private HistoryPackageContract.Presenter mPresenter;
    private HistoryAdapter adapter;
    @Bind(R.id.ss_code_so)
    SearchableSpinner ssCodeSO;

    @Bind(R.id.ss_type_product)
    SearchableSpinner ssTypeProduct;

    @Bind(R.id.ss_module)
    SearchableSpinner ssModule;

    @Bind(R.id.ss_code_pack)
    SearchableSpinner ssCodePack;

    @Bind(R.id.ss_apartment)
    SearchableSpinner ssApartment;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.lv_history)
    ListView lvHistory;

    private String serialPack;
    private String codePack;
    private int orderId = 0;
    private int apartmentId = 0;
    private int orderType = 0;
    private int moduleId = 0;

    public HistoryPackageFragment() {
        // Required empty public constructor
    }


    public static HistoryPackageFragment newInstance() {
        HistoryPackageFragment fragment = new HistoryPackageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == DetailPackageActivity.REQUEST_CODE) {
//            if(resultCode == Activity.RESULT_OK){
//                if (data.getIntExtra(Constants.KEY_RESULT,0) == Constants.DELETE){
//                    showSuccess(getString(R.string.text_delete_success));
//                }
//                if (data.getIntExtra(Constants.KEY_RESULT,0) == Constants.PRINT){
//                    showSuccess(getString(R.string.text_print_success));
//                }
//
//                if (data.getIntExtra(Constants.KEY_RESULT,0) == Constants.DONE){
//                    showSuccess(getString(R.string.text_done_pack_success));
//                }
//
//
//            }
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_pack, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        // Vibrate for 500 milliseconds
        ssCodeSO.setPrintStamp(true);
        ssTypeProduct.setPrintStamp(true);
        ssCodePack.setPrintStamp(true);
        ssApartment.setPrintStamp(true);
        ssModule.setPrintStamp(true);
        ssCodeSO.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });

        ssTypeProduct.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {

                return false;
            }
        });

        ssCodePack.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });

        ssApartment.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });

        ssModule.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });

        ArrayAdapter<TypeSOManager.TypeSO> adapter = new ArrayAdapter<TypeSOManager.TypeSO>(
                getContext(), android.R.layout.simple_spinner_item, TypeSOManager.getInstance().getListType());
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        ssTypeProduct.setAdapter(adapter);
        ssTypeProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.getListSO(TypeSOManager.getInstance().getValueByPositon(position));
                orderType = TypeSOManager.getInstance().getValueByPositon(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void setPresenter(HistoryPackageContract.Presenter presenter) {
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
    public void showListSO(List<SOEntity> list) {
        ArrayAdapter<SOEntity> adapter = new ArrayAdapter<SOEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssCodeSO.setAdapter(adapter);
        ssCodeSO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCustomerName.setText(list.get(position).getCustomerName());
                orderId = list.get(position).getOrderId();
                if (orderId > 0) {
                    mPresenter.getListApartment(orderId);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListApartment(List<ApartmentEntity> list) {
        ArrayAdapter<ApartmentEntity> adapter = new ArrayAdapter<ApartmentEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssApartment.setAdapter(adapter);
        ssApartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apartmentId = list.get(position).getApartmentID();
                mPresenter.getListModule(orderId, orderType, apartmentId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListModule(List<ModuleEntity> list) {
        ArrayAdapter<ModuleEntity> adapter = new ArrayAdapter<ModuleEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssModule.setAdapter(adapter);
        ssModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                moduleId = list.get(position).getProductId();
                mPresenter.getListCodePack(orderId, orderType, list.get(position).getProductId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListCodePack(List<CodePackEntity> list) {
        ArrayAdapter<CodePackEntity> adapter = new ArrayAdapter<CodePackEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssCodePack.setAdapter(adapter);
        ssCodePack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                serialPack = list.get(position).getSttPack();
                codePack = list.get(position).getPackCode();
                mPresenter.getListHistory(orderId, moduleId, apartmentId, codePack, serialPack);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListHistory(List<HistoryEntity> list) {
        adapter = new HistoryAdapter(getContext(), list);
        lvHistory.setAdapter(adapter);
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailPackageActivity.start(getActivity(), orderId, apartmentId, moduleId, serialPack,list.get(position).getPackageId());
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


}
