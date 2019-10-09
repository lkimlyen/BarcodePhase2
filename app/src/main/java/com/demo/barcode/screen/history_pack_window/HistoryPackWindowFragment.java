package com.demo.barcode.screen.history_pack_window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.HistoryPackWindowEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SetWindowEntity;
import com.demo.barcode.R;
import com.demo.barcode.adapter.HistoryPackWindowAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.dialogs.ChangeIPAddressDialog;
import com.demo.barcode.manager.DirectionManager;
import com.demo.barcode.screen.detail_package.DetailPackageActivity;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.spinner.SearchableListDialog;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class HistoryPackWindowFragment extends BaseFragment implements HistoryPackWindowContract.View {
    private final String TAG = HistoryPackWindowFragment.class.getName();
    private HistoryPackWindowContract.Presenter mPresenter;
    private HistoryPackWindowAdapter adapter;
    @BindView(R.id.tv_code_so)
    TextView tvCodeSO;

    @BindView(R.id.tv_direction)
    TextView tvDirection;

    @BindView(R.id.tv_set_window)
    TextView tvSetWindow;

    @BindView(R.id.txt_customer_name)
    TextView txtCustomerName;

    @BindView(R.id.edt_barcode)
    EditText edtBarcode;

    @BindView(R.id.lv_code)
    ListView lvCode;

    @BindView(R.id.txt_date_scan)
    TextView txtDateScan;

    @BindView(R.id.btn_scan)
    Button btnScan;
    private long orderId = 0;
    private long productSetId = 0;
    private int direction = -1;


    public HistoryPackWindowFragment() {
        // Required empty public constructor
    }


    public static HistoryPackWindowFragment newInstance() {
        HistoryPackWindowFragment fragment = new HistoryPackWindowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DetailPackageActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showSuccess(getString(R.string.text_print_success));
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_pack_window, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {


        mPresenter.getListSO();
        txtDateScan.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
    }


    @Override
    public void setPresenter(HistoryPackWindowContract.Presenter presenter) {
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
        if (list.size() > 0) {
            tvCodeSO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                            (list);
                    searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                        @Override
                        public void onSearchableItemClicked(Object item, int position) {
                            SOEntity soItem = (SOEntity) item;
                            tvCodeSO.setText(soItem.getCodeSO());
                            txtCustomerName.setText(soItem.getCustomerName());
                            orderId = soItem.getOrderId();
                            tvSetWindow.setText(getString(R.string.text_choose_set_window));
                            productSetId = 0;
                            tvDirection.setText(getString(R.string.text_choose_direction));
                            direction = -1;
                            mPresenter.getListSetWindow(orderId);
                        }
                    });
                    searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                }


            });
        } else {
            tvCodeSO.setOnClickListener(null);
        }

    }

    @Override
    public void showListSetWindow(List<SetWindowEntity> list) {
        tvSetWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                        (list);
                searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                    @Override
                    public void onSearchableItemClicked(Object item, int position) {
                        SetWindowEntity apartmentEntity = (SetWindowEntity) item;
                        tvSetWindow.setText(apartmentEntity.getProductSetName());
                        productSetId = apartmentEntity.getProductSetId();
                        tvDirection.setText(getString(R.string.text_choose_direction));
                        direction = -1;
                    }
                });
                searchableListDialog.show(getActivity().getFragmentManager(), TAG);

            }

        });
    }

    @Override
    public void showListHistory(List<HistoryPackWindowEntity> list) {
        adapter = new HistoryPackWindowAdapter(getContext(), list, new HistoryPackWindowAdapter.OnClickPrintListener() {
            @Override
            public void onClickPrint(long id) {
                mPresenter.print(id);
            }
        });
        lvCode.setAdapter(adapter);
    }

    @Override
    public void showDialogCreateIPAddress(long id) {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port, id);
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


    @OnClick(R.id.tv_direction)
    public void chooseDirection() {

        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                (DirectionManager.getInstance().getListType());
        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
            @Override
            public void onSearchableItemClicked(Object item, int position) {
                DirectionManager.Direction direction1 = (DirectionManager.Direction) item;
                tvDirection.setText(direction1.getName());
                direction = direction1.getValue();
                mPresenter.getListHistory(productSetId, direction);

            }
        });
        searchableListDialog.show(getActivity().getFragmentManager(), TAG);


    }
}
