package com.demo.barcode.screen.quality_control;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.utils.view.DateUtils;
import com.demo.barcode.R;
import com.demo.barcode.adapter.QualityControlAdapter;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.TypeSOManager;
import com.demo.barcode.screen.capture.ScanActivity;
import com.demo.barcode.screen.detail_error.DetailErrorActivity;
import com.demo.barcode.screen.print_stamp.PrintStempActivity;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.barcodereader.BarcodeScanner;
import com.demo.barcode.widgets.barcodereader.BarcodeScannerBuilder;
import com.demo.barcode.widgets.spinner.SearchableListDialog;
import com.demo.barcode.widgets.spinner.SearchableSpinner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

/**
 * Created by MSI on 26/11/2017.
 */

public class QualityControlFragment extends BaseFragment implements QualityControlContract.View {
    private static final int MY_LOCATION_REQUEST_CODE = 1234;
    private final String TAG = QualityControlFragment.class.getName();
    private QualityControlContract.Presenter mPresenter;
    private QualityControlAdapter adapter;
    private long orderId;
    public MediaPlayer mp1, mp2;
    public boolean isClick = false;

    private boolean editMachineName, editViolator, editQCCode;
    @Bind(R.id.tv_code_so)
    TextView tvCodeSO;

    @Bind(R.id.tv_type_product)
    TextView tvTypeProduct;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.et_machine_name)
    EditText etMachineName;

    @Bind(R.id.et_qc_code)
    EditText etQCCode;

    @Bind(R.id.et_violator)
    EditText etViolator;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.lv_code)
    ListView lvCode;

    @Bind(R.id.txt_date_scan)
    TextView txtDateScan;

    @Bind(R.id.iv_save_machine_name)
    ImageView ivSaveMachineName;

    @Bind(R.id.iv_save_violator)
    ImageView ivSaveViolator;

    @Bind(R.id.iv_save_qc_code)
    ImageView ivSaveQCCode;

    private Vibrator vibrate;


    public QualityControlFragment() {
        // Required empty public constructor
    }


    public static QualityControlFragment newInstance() {
        QualityControlFragment fragment = new QualityControlFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DetailErrorActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showSuccess(getString(R.string.text_update_success));

            } else {
                isClick = false;
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quality_control, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);
        initView();
        return view;
    }

    private void initView() {
        txtDateScan.setText(DateUtils.getShortDateCurrent());
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds

    }


    @Override
    public void setPresenter(QualityControlContract.Presenter presenter) {
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
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.text_title_noti));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.text_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    @Override
    public void showSuccess(String message) {
        showToast(message);
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
    public void turnOnVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrate.vibrate(500);
        }
    }

    @Override
    public void showListQualityControl(RealmResults<QualityControlModel> results) {
        adapter = new QualityControlAdapter(results, new QualityControlAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(QualityControlModel item) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_code))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.removeItemQualityControl(item.getId());
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
        });
        lvCode.setAdapter(adapter);
        lvCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailErrorActivity.start(getActivity(), adapter.getItem(position).getId());
            }
        });

    }

    @Override
    public void showListSO(List<SOEntity> list) {


        if (list.size() > 0) {
            tvCodeSO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.countDataEdit() > 0) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.dialog_default_title))
                                .setContentText(getString(R.string.text_upload_data))
                                .setConfirmText(getString(R.string.text_yes))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        mPresenter.uploadData();
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .setCancelText(getString(R.string.text_no))
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        mPresenter.deleteAllQC();
                                        sweetAlertDialog.dismiss();
                                        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                                                (list);
                                        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                                            @Override
                                            public void onSearchableItemClicked(Object item, int position) {
                                                SOEntity soItem = (SOEntity) item;
                                                tvCodeSO.setText(soItem.getCodeSO());
                                                txtCustomerName.setText(soItem.getCustomerName());
                                                orderId = soItem.getOrderId();
                                                mPresenter.getListProduct(orderId);
                                                mPresenter.getListQualityControl();
                                            }
                                        });
                                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                                    }
                                })
                                .show();
                    } else {
                    SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                            (list);
                    searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                        @Override
                        public void onSearchableItemClicked(Object item, int position) {
                            SOEntity soItem = (SOEntity) item;
                            tvCodeSO.setText(soItem.getCodeSO());
                            txtCustomerName.setText(soItem.getCustomerName());
                            orderId = soItem.getOrderId();
                            mPresenter.getListProduct(orderId);
                            mPresenter.getListQualityControl();
                        }
                    });
                    searchableListDialog.show(getActivity().getFragmentManager(), TAG);}
                }
            });
        } else {
            tvCodeSO.setOnClickListener(null);
        }
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @OnClick(R.id.btn_save)
    public void save() {
        if (orderId == 0) {
            showError(getString(R.string.text_order_id_null));
            return;
        }
        if (TextUtils.isEmpty(edtBarcode.getText().toString())) {
            showError(getString(R.string.text_barcode_null));
            return;
        }

        if (TextUtils.isEmpty(etMachineName.getText().toString())) {
            showError(getString(R.string.text_machine_name_null));
            return;
        }

        if (TextUtils.isEmpty(etViolator.getText().toString())) {
            showError(getString(R.string.text_violator_null));
            return;
        }

        if (TextUtils.isEmpty(etQCCode.getText().toString())) {
            showError(getString(R.string.text_qc_code_null));
            return;
        }
        if (editMachineName ) {
            showError(getString(R.string.text_save_machine_name));
            return;
        }
        if (editViolator ) {
            showError(getString(R.string.text_save_violator));
            return;
        }
        if (editQCCode ) {
            showError(getString(R.string.text_save_qc_code));
            return;
        }
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(edtBarcode.getText().toString(), orderId, etMachineName.getText().toString()
                                , etViolator.getText().toString(), etQCCode.getText().toString());
                        sweetAlertDialog.dismiss();
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

    @OnClick(R.id.img_back)
    public void back() {
        if (adapter.countDataEdit() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.dialog_default_title))
                    .setContentText(getString(R.string.text_upload_data))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.uploadData();
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setCancelText(getString(R.string.text_no))
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.deleteAllQC();
                            sweetAlertDialog.dismiss();
                            getActivity().finish();
                        }
                    })
                    .show();
        } else {

            if(adapter.getCount() > 0){
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.dialog_default_title))
                        .setContentText(getString(R.string.text_do_you_want_exit))
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
                                mPresenter.deleteAllQC();
                                sweetAlertDialog.dismiss();
                                getActivity().finish();
                            }
                        })
                        .show();
            }else {

                getActivity().finish();
            }
        }
    }

    @OnClick(R.id.btn_scan)
    public void scan() {
        if (orderId == 0) {
            showError(getString(R.string.text_order_id_null));
            return;
        }

        if (TextUtils.isEmpty(etMachineName.getText().toString())) {
            showError(getString(R.string.text_machine_name_null));
            return;
        }

        if (TextUtils.isEmpty(etViolator.getText().toString())) {
            showError(getString(R.string.text_violator_null));
            return;
        }

        if (TextUtils.isEmpty(etQCCode.getText().toString())) {
            showError(getString(R.string.text_qc_code_null));
            return;
        }
        if (editMachineName ) {
            showError(getString(R.string.text_save_machine_name));
            return;
        }
        if (editViolator ) {
            showError(getString(R.string.text_save_violator));
            return;
        }
        if (editQCCode ) {
            showError(getString(R.string.text_save_qc_code));
            return;
        }

        final BarcodeScanner barcodeScanner = new BarcodeScannerBuilder()
                .withActivity(getActivity())
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
                .withResultListener(new BarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        //  barcodeResult = barcode;
                        String contents = barcode.rawValue;
                        String barcode2 = contents.replace("DEMO", "");
                        mPresenter.checkBarcode(barcode2, orderId, etMachineName.getText().toString()
                                , etViolator.getText().toString(), etQCCode.getText().toString());
                    }
                })
                .build();
        barcodeScanner.startScan();
    }

    @OnClick(R.id.img_upload)
    public void upload() {
        if (adapter.countDataEdit() > 0) {
            mPresenter.uploadData();
        } else {
            if(adapter.getCount() > 0){
                showError(getString(R.string.text_edit_data_before_upload));
            }else {
                showError(getString(R.string.text_no_data_upload));
            }
        }
    }


    @OnClick(R.id.tv_type_product)
    public void chooseProduct() {
        if (adapter.countDataEdit() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.dialog_default_title))
                    .setContentText(getString(R.string.text_upload_data))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.uploadData();
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setCancelText(getString(R.string.text_no))
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.deleteAllQC();
                            sweetAlertDialog.dismiss();
                            SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                                    (TypeSOManager.getInstance().getListType());
                            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                                @Override
                                public void onSearchableItemClicked(Object item, int position) {
                                    TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
                                    tvTypeProduct.setText(typeSO.getName());
                                    txtCustomerName.setText("");
                                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
                                    orderId = 0;
                                    mPresenter.getListSO(typeSO.getValue());
                                }
                            });
                            searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                        }
                    })
                    .show();
        } else {
            SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                    (TypeSOManager.getInstance().getListType());
            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                @Override
                public void onSearchableItemClicked(Object item, int position) {
                    TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
                    tvTypeProduct.setText(typeSO.getName());
                    txtCustomerName.setText("");
                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
                    orderId = 0;
                    mPresenter.getListSO(typeSO.getValue());
                }
            });
            searchableListDialog.show(getActivity().getFragmentManager(), TAG);

        }
    }

    @OnClick(R.id.iv_save_qc_code)
    public void saveQCCode() {
        if (TextUtils.isEmpty(etQCCode.getText().toString())) {
            return;
        }

        if (editQCCode) {
            ivSaveQCCode.setImageResource(R.drawable.ic_edit_pencil);
            etQCCode.setEnabled(false);
            editQCCode = false;
        }else {
            if(adapter.getCount() > 0){
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.dialog_default_title))
                        .setContentText(getString(R.string.text_edit_violator))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                ivSaveQCCode.setImageResource(R.drawable.ic_save);
                                etQCCode.setEnabled(true);
                                editQCCode = true;
                                mPresenter.deleteAllQC();
                                sweetAlertDialog.dismiss();
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
            }else {
                ivSaveQCCode.setImageResource(R.drawable.ic_save);
                etQCCode.setEnabled(true);
                editQCCode = true;
            }


        }

    }

    @OnClick(R.id.iv_save_machine_name)
    public void saveMachineName() {
        if (TextUtils.isEmpty(etMachineName.getText().toString())) {
            return;
        }
        if (editMachineName) {
            ivSaveMachineName.setImageResource(R.drawable.ic_edit_pencil);
            etMachineName.setEnabled(false);
            editMachineName = false;
        }else {
            if(adapter.getCount() > 0){
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.dialog_default_title))
                        .setContentText(getString(R.string.text_edit_violator))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                ivSaveMachineName.setImageResource(R.drawable.ic_save);
                                etMachineName.setEnabled(true);
                                editMachineName = true;
                                mPresenter.deleteAllQC();
                                sweetAlertDialog.dismiss();
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
            }else {
                ivSaveMachineName.setImageResource(R.drawable.ic_save);
                etMachineName.setEnabled(true);
                editMachineName = true;
            }
        }


    }

    @OnClick(R.id.iv_save_violator)
    public void saveViolator() {

        if (TextUtils.isEmpty(etViolator.getText().toString())) {
            return;
        }

        if (editViolator) {
            ivSaveViolator.setImageResource(R.drawable.ic_edit_pencil);
            etViolator.setEnabled(false);
            editViolator = false;
        }else {
            if(adapter.getCount() > 0){
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.dialog_default_title))
                        .setContentText(getString(R.string.text_edit_violator))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                ivSaveViolator.setImageResource(R.drawable.ic_save);
                                etViolator.setEnabled(true);
                                editViolator = true;
                                mPresenter.deleteAllQC();
                                sweetAlertDialog.dismiss();
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
            }else {
                ivSaveViolator.setImageResource(R.drawable.ic_save);
                etViolator.setEnabled(true);
                editViolator = true;
            }


        }


    }
}
