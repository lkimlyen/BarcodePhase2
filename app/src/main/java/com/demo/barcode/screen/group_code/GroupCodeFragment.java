package com.demo.barcode.screen.group_code;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.barcode.R;
import com.demo.barcode.adapter.GroupCodeAdapter;
import com.demo.barcode.adapter.GroupCodeLVAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.AnimatedExpandableListView;
import com.demo.barcode.widgets.spinner.SearchableSpinner;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public class GroupCodeFragment extends BaseFragment implements GroupCodeContract.View {
    private final String TAG = GroupCodeFragment.class.getName();
    public static final String ORDER_ID = "order_id";
    public static final String DEPARTMENT_ID = "department_id";
    public static final String TIMES = "times";
    public static final String GROUP_CODE = "group_code";
    private GroupCodeContract.Presenter mPresenter;
    private GroupCodeLVAdapter lvAdapter;
    private GroupCodeAdapter adapter;
    private int orderId = 0;
    private int departmentId = 0;
    private int times = 0;
    private boolean groupCode;
    @Bind(R.id.ss_serial_module)
    SearchableSpinner ssModule;

    @Bind(R.id.txt_code_so)
    TextView txtCodeSO;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.lv_code)
    ListView lvCode;

    @Bind(R.id.elv_code)
    AnimatedExpandableListView elvCode;

    @Bind(R.id.txt_title)
    TextView txtTitle;

    @Bind(R.id.btn_group_code)
    Button btnGroupCode;

    public GroupCodeFragment() {
        // Required empty public constructor
    }


    public static GroupCodeFragment newInstance() {
        GroupCodeFragment fragment = new GroupCodeFragment();
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
        View view = inflater.inflate(R.layout.fragment_group_code, container, false);
        ButterKnife.bind(this, view);
        orderId = getActivity().getIntent().getIntExtra(ORDER_ID, 0);
        departmentId = getActivity().getIntent().getIntExtra(DEPARTMENT_ID, 0);
        times = getActivity().getIntent().getIntExtra(TIMES, 0);
        groupCode = getActivity().getIntent().getBooleanExtra(GROUP_CODE, true);
        initView();
        return view;
    }

    private void initView() {
        if (!groupCode) {
            txtTitle.setText(getString(R.string.text_detached_code));
            btnGroupCode.setText(getString(R.string.text_detached_code));
        }
    }


    @Override
    public void setPresenter(GroupCodeContract.Presenter presenter) {
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
    public void showListModule(List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        ssModule.setAdapter(adapter);
    }

    @Override
    public void showListScanStages(RealmResults<LogScanStages> results) {
        lvAdapter = new GroupCodeLVAdapter(results, new GroupCodeLVAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanStages item, int number) {
                mPresenter.updateNumberGroup(item.getId(), number);
            }
        }, new GroupCodeLVAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {

            }
        });
        lvCode.setAdapter(lvAdapter);
    }

    @Override
    public void showGroupCode(RealmResults<ListGroupCode> groupCodes) {
        adapter = new GroupCodeAdapter(getContext(), groupCodes, new GroupCodeAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(ListGroupCode groupCode, LogScanStages item) {


            }
        });
        elvCode.setAdapter(adapter);
        if (groupCodes.size() > 0) {
            elvCode.setVisibility(View.VISIBLE);

        } else {
            elvCode.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSODetail(SOEntity soEntity) {
        txtCodeSO.setText(soEntity.getCodeSO());
        txtCustomerName.setText(soEntity.getCustomerName());
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @OnClick(R.id.img_back)
    public void back() {

        if (lvAdapter.getCountersToSelect().size() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_cancel_group_code))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();

                        }
                    })
                    .setCancelText(getString(R.string.text_no))
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            getActivity().finish();
                        }
                    })
                    .show();
        } else {
            getActivity().finish();
        }
    }

    @OnClick(R.id.btn_group_code)
    public void groupCode() {
        if (groupCode) {
            if (lvAdapter.getCountersToSelect().size() > 0 && adapter.getListGroupCodeSelect() == null) {
                mPresenter.groupCode(orderId, departmentId, times, lvAdapter.getCountersToSelect());
            } else if (lvAdapter.getCountersToSelect().size() > 0 && adapter.getListGroupCodeSelect() != null) {
                mPresenter.updateGroupCode(adapter.getListGroupCodeSelect(), orderId,
                        departmentId, times, lvAdapter.getCountersToSelect());
            }
        } else {
            if (adapter.getListGroupCodeSelect() != null) {
                mPresenter.detachedCode(orderId, departmentId, times, adapter.getListGroupCodeSelect());
            }
        }

    }


}
