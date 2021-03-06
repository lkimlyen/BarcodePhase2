package com.demo.barcode.screen.qc_window;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.MachineEntity;
import com.demo.architect.data.model.QCEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.QualityControlWindowModel;
import com.demo.architect.utils.view.DateUtils;
import com.demo.barcode.R;
import com.demo.barcode.adapter.QualityControlWindowAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.screen.detail_error.DetailErrorActivity;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.barcodereader.BarcodeScannerActivity;
import com.demo.barcode.widgets.spinner.SearchableListDialog;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public class QualityControlWindowFragment extends BaseFragment implements QualityControlWindowContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = QualityControlWindowFragment.class.getName();
    private QualityControlWindowContract.Presenter mPresenter;
    private QualityControlWindowAdapter adapter;
    private boolean editViolator = true;
    private long orderId;
    private int qcId = 0;
    private int machineId = 0;
    public MediaPlayer mp1, mp2;
    public boolean isClick = false;
    @BindView(R.id.tv_code_so)
    TextView tvCodeSO;

    @BindView(R.id.ll_type_product)
    LinearLayout llTypeProduct;

    @BindView(R.id.txt_customer_name)
    TextView txtCustomerName;

    @BindView(R.id.tv_machine_name)
    TextView tvMachineName;

    @BindView(R.id.tv_qc_code)
    TextView tvQCCode;

    @BindView(R.id.et_violator)
    EditText etViolator;

    @BindView(R.id.edt_barcode)
    EditText edtBarcode;

    @BindView(R.id.lv_code)
    RecyclerView lvCode;

    @BindView(R.id.txt_date_scan)
    TextView txtDateScan;


    private Vibrator vibrate;


    public QualityControlWindowFragment() {
        // Required empty public constructor
    }


    public static QualityControlWindowFragment newInstance() {
        QualityControlWindowFragment fragment = new QualityControlWindowFragment();
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
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
                Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.KEY_CAPTURED_BARCODE);

                String contents = barcode.rawValue;
                String barcode2 = contents.replace("DEMO", "");
                Log.d(TAG, barcode2);
                mPresenter.checkBarcode(barcode2, orderId, machineId
                        , etViolator.getText().toString(), qcId);
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

        mPresenter.getListSO();
        mPresenter.getListMachine();
        mPresenter.getListQualityControl();
        llTypeProduct.setVisibility(View.GONE);
        txtDateScan.setText(DateUtils.getShortDateCurrent());
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds

    }


    @Override
    public void setPresenter(QualityControlWindowContract.Presenter presenter) {
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
    public void showListMachine(List<MachineEntity> list) {
        if (list.size() > 0) {
            tvMachineName.setOnClickListener(new View.OnClickListener() {
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
                                        mPresenter.uploadData(machineId,
                                                etViolator.getText().toString(), qcId, orderId);
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
                                                MachineEntity soItem = (MachineEntity) item;
                                                tvMachineName.setText(soItem.getName());
                                                tvQCCode.setText(getString(R.string.text_choose_qc_code));
                                                qcId = 0;
                                                etViolator.setText("");
                                                machineId = soItem.getId();
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
                                MachineEntity soItem = (MachineEntity) item;
                                tvMachineName.setText(soItem.getName());
                                machineId = soItem.getId();
                                etViolator.setText("");
                                tvQCCode.setText(getString(R.string.text_choose_qc_code));
                                qcId = 0;
                            }
                        });
                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                    }
                }
            });
        } else {
            tvMachineName.setOnClickListener(null);
        }
    }

    @Override
    public void refreshLayout() {
        lvCode.requestLayout();

    }

    @Override
    public void showListQC(List<QCEntity> list) {
        if (list.size() > 0) {
            tvQCCode.setOnClickListener(new View.OnClickListener() {
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
                                        mPresenter.uploadData(machineId,
                                                etViolator.getText().toString(), qcId, orderId);
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
                                                QCEntity soItem = (QCEntity) item;
                                                tvQCCode.setText(soItem.getCode() + "-" + soItem.getName());

                                                qcId = soItem.getId();
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
                                QCEntity soItem = (QCEntity) item;
                                tvQCCode.setText(soItem.getCode() + "-" + soItem.getName());

                                qcId = soItem.getId();
                            }
                        });
                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                    }
                }
            });
        } else {
            tvQCCode.setOnClickListener(null);
        }
    }

    @Override
    public void showListQualityControl(RealmResults<QualityControlWindowModel> results) {
        adapter = new QualityControlWindowAdapter(results, new QualityControlWindowAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(long id) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_code))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.removeItemQualityControl(id);
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
        }, new QualityControlWindowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                DetailErrorActivity.start(getActivity(), id);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        lvCode.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        lvCode.addItemDecoration(dividerItemDecoration);
        lvCode.setHasFixedSize(true);
        lvCode.setAdapter(adapter);

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
                                        mPresenter.uploadData(machineId,
                                                etViolator.getText().toString(), qcId, orderId);
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
                                                tvMachineName.setText(getString(R.string.text_choose_machine_name));
                                                machineId = 0;
                                                tvQCCode.setText(getString(R.string.text_choose_qc_code));
                                                qcId = 0;
                                                etViolator.setText("");
                                                mPresenter.getListProduct(orderId);
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
                                tvMachineName.setText(getString(R.string.text_choose_machine_name));
                                machineId = 0;
                                tvQCCode.setText(getString(R.string.text_choose_qc_code));
                                qcId = 0;
                                etViolator.setText("");
                                mPresenter.getListProduct(orderId);
                            }
                        });
                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                    }
                }
            });
        } else {
            tvCodeSO.setOnClickListener(null);
        }
    }

    @Override
    public void updateStateEditText(boolean edit) {
        if (!edit){
            etViolator.setFocusableInTouchMode(false);
            etViolator.setFocusable(false);
                etViolator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.dialog_default_title))
                                .setContentText(getString(R.string.text_upload_data))
                                .setConfirmText(getString(R.string.text_yes))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if (adapter != null && adapter.countDataEdit() > 0) {
                                            mPresenter.uploadData(machineId, etViolator.getText().toString(), qcId, orderId);
                                        } else {
                                            if (adapter != null && adapter.getItemCount() > 0) {
                                                showError(getString(R.string.text_edit_data_before_upload));
                                            } else {
                                                showError(getString(R.string.text_no_data_upload));
                                            }
                                        }
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .setCancelText(getString(R.string.text_no))
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        mPresenter.deleteAllQC();
                                    }
                                })
                                .show();
                    }
                });

        }else {
            etViolator.setOnClickListener(null);
            etViolator.setFocusableInTouchMode(true);
            etViolator.setFocusable(true);
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

        if (qcId == 0) {
            showError(getString(R.string.text_qc_code_null));
            return;
        }


        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(edtBarcode.getText().toString(), orderId, machineId
                                , etViolator.getText().toString(), qcId);
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
        if (adapter != null && adapter.countDataEdit() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.dialog_default_title))
                    .setContentText(getString(R.string.text_upload_data))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.uploadData(machineId, etViolator.getText().toString(), qcId, orderId);
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
            if (adapter != null && adapter.getItemCount() > 0) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.dialog_default_title))
                        .setContentText(getString(R.string.text_do_you_want_exit))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.deleteAllQC();
                                getActivity().finish();
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
            } else {
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

        if (qcId == 0) {
            showError(getString(R.string.text_qc_code_null));
            return;
        }

        Intent launchIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
        getActivity().startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);

    }

    @OnClick(R.id.img_upload)
    public void upload() {
        if (adapter != null && adapter.countDataEdit() > 0) {
            mPresenter.uploadData(machineId, etViolator.getText().toString(), qcId, orderId);
        } else {
            if (adapter != null && adapter.getItemCount() > 0) {
                showError(getString(R.string.text_edit_data_before_upload));
            } else {
                showError(getString(R.string.text_no_data_upload));
            }
        }
    }


}
