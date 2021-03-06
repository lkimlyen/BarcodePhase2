package com.demo.barcode.screen.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.model.UserEntity;
import com.demo.barcode.R;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ServerManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.screen.confirm_receive.ConfirmReceiveActivity;
import com.demo.barcode.screen.confirm_receive_window.ConfirmReceiveWindowActivity;
import com.demo.barcode.screen.create_pack_window.CreatePackagingWindowActivity;
import com.demo.barcode.screen.create_packaging.CreatePackagingActivity;
import com.demo.barcode.screen.group_code.GroupCodeActivity;
import com.demo.barcode.screen.history_pack.HistoryPackageActivity;
import com.demo.barcode.screen.history_pack_window.HistoryPackWindowActivity;
import com.demo.barcode.screen.login.LoginActivity;
import com.demo.barcode.screen.qc_window.QualityControlWindowActivity;
import com.demo.barcode.screen.quality_control.QualityControlActivity;
import com.demo.barcode.screen.setting.SettingActivity;
import com.demo.barcode.screen.stages.StagesActivity;
import com.demo.barcode.screen.stages_window.StagesWindowActivity;
import com.demo.barcode.screen.warehousing_wd.WarehousingWDActivity;
import com.demo.barcode.util.Precondition;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class DashboardFragment extends BaseFragment implements DashboardContract.View {
    private final String TAG = DashboardFragment.class.getName();

    @BindView(R.id.txt_name)
    TextView txtName;

    @BindView(R.id.txt_position)
    TextView txtPosition;

    @BindView(R.id.btn_link)
    Button btnLink;

    @BindView(R.id.btn_scan_stages)
    Button btnScanStages;

    @BindView(R.id.btn_confirm_receive)
    Button btnConfirmreceive;

    @BindView(R.id.btn_scan_packaging)
    Button btnScanPackaging;

    @BindView(R.id.btn_history)
    Button btnHistory;

    @BindView(R.id.btn_quality_control)
    Button btnQC;

    @BindView(R.id.btn_group_code)
    Button btnGroupCode;
    @BindView(R.id.bt_warehousing)
    Button btWarehousing;
    private DashboardContract.Presenter mPresenter;

    public DashboardFragment() {
        // Required empty public constructor
    }


    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        btnLink.setText(String.format(getString(R.string.text_web_report), ServerManager.getInstance().getServer()));
    }


    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
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


    @Override
    public void showUser(UserEntity user) {
        txtName.setText(user.getName());

        btWarehousing.setVisibility(View.GONE);
        if (user.getUserType().equals("SP")) {
            btnQC.setVisibility(View.GONE);
            if (user.getRole() > 0) {
                if ((user.getRole() == 6 || user.getRole() == 8) && user.getOrderType() != 4) {
                    btnGroupCode.setVisibility(View.VISIBLE);
                }
                txtPosition.setText("Công đoạn: " + ListDepartmentManager.getInstance().getDepartmentByRole(user.getRole()));
                btnScanPackaging.setVisibility(View.GONE);
                btnHistory.setVisibility(View.GONE);
                if (user.getRole() == 9 || user.getRole() == 20 || user.getRole() == 17) {
                    btnScanPackaging.setVisibility(View.VISIBLE);
                    btnHistory.setVisibility(View.VISIBLE);
                }

                if (user.getRole() == 20){

                    btWarehousing.setVisibility(View.VISIBLE);
                }

            } else {
                btnScanStages.setVisibility(View.GONE);
                btnConfirmreceive.setVisibility(View.GONE);
                txtPosition.setText("Bộ  phận: In tem bao bì");
            }
        } else {
            btnScanStages.setVisibility(View.GONE);
            btnConfirmreceive.setVisibility(View.GONE);
            btnScanPackaging.setVisibility(View.GONE);
            btnHistory.setVisibility(View.GONE);
            btnGroupCode.setVisibility(View.GONE);
            txtPosition.setText("QC Công đoạn: " + ListDepartmentManager.getInstance().getDepartmentByRole(user.getRole()));
        }

    }


    @OnClick(R.id.btn_logout)
    public void logout() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_do_you_want_logout))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.logout();
                        RealmHelper.getInstance().initRealm(false);
                        LoginActivity.start(getContext());
                        getActivity().finishAffinity();
                    }
                })
                .setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.btn_setting)
    public void setting() {
        SettingActivity.start(getContext());
    }

    @OnClick(R.id.btn_scan_stages)
    public void scanStages() {
        if (UserManager.getInstance().getUser().getOrderType() == 4) {
            StagesWindowActivity.start(getContext());
        } else {
            StagesActivity.start(getContext());
        }
    }

    @OnClick(R.id.btn_confirm_receive)
    public void confirmReceive() {
        if (UserManager.getInstance().getUser().getOrderType() == 4) {
            ConfirmReceiveWindowActivity.start(getContext());
        } else {
            ConfirmReceiveActivity.start(getContext());
        }
    }

    @OnClick(R.id.btn_scan_packaging)
    public void scanPackaging() {
        if (UserManager.getInstance().getUser().getOrderType() == 4) {
            CreatePackagingWindowActivity.start(getContext());
        }else {
        CreatePackagingActivity.start(getContext());}
    }

    @OnClick(R.id.btn_link)
    public void link() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(btnLink.getText().toString()));
        startActivity(intent);
    }

    @OnClick(R.id.btn_history)
    public void history() {
        if (UserManager.getInstance().getUser().getOrderType() == 4) {
            HistoryPackWindowActivity.start(getContext());
        }else {
            HistoryPackageActivity.start(getContext());
        }
    }

    @OnClick(R.id.btn_quality_control)
    public void qualityControl() {
        if (UserManager.getInstance().getUser().getOrderType() == 4) {
            QualityControlWindowActivity.start(getContext());
        } else {
            QualityControlActivity.start(getContext());
        }
    }

    @OnClick(R.id.bt_warehousing)
    public void warehousing() {
        if (UserManager.getInstance().getUser().getOrderType() == 4) {
            WarehousingWDActivity.start(getContext());
        } else {
            QualityControlActivity.start(getContext());
        }
    }

    @OnClick(R.id.btn_group_code)
    public void groupCode() {
        GroupCodeActivity.start(getContext());
    }
}
