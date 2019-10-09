package com.demo.barcode.screen.confirm_receive_window;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanConfirmWindowModel;
import com.demo.barcode.R;
import com.demo.barcode.adapter.ConfirmInputWindowAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.dialogs.ChangeIPAddressDialog;
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

public class ConfirmReceiveWindowFragment extends BaseFragment implements ConfirmReceiveWindowContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = ConfirmReceiveWindowFragment.class.getName();
    private ConfirmReceiveWindowContract.Presenter mPresenter;
    private int departmentId = 0;
    private boolean printed;
    private ConfirmInputWindowAdapter adapter;
    public MediaPlayer mp1, mp2;
    private int times = 0;
    private boolean change;
    private long maPhieuId = 0;
    @BindView(R.id.tv_code_so)
    TextView tvCodeSO;

    @BindView(R.id.bt_print)
    Button btPrint;

    @BindView(R.id.ll_times)
    LinearLayout llTimes;

    @BindView(R.id.txt_customer_name)
    TextView txtCustomerName;

    @BindView(R.id.tv_delivery_department)
    TextView tvDepartment;

    @BindView(R.id.edt_barcode)
    EditText edtBarcode;

    @BindView(R.id.lv_confirm)
    RecyclerView lvConfirm;

    @BindView(R.id.ll_type_product)
    LinearLayout llTypeProduct;

    @BindView(R.id.ll_type_scan)
    LinearLayout llTypeScan;

    @BindView(R.id.cb_all)
    CheckBox cbConfirmAll;

    @BindView(R.id.tv_delivery_code)
    TextView tvDeliveryCode;

    @BindView(R.id.ll_root)
    LinearLayout lLRoot;

    @BindView(R.id.tv_column)
    TextView tvColumn;
    private Vibrator vibrate;
    private long orderId = 0;


    public ConfirmReceiveWindowFragment() {
        // Required empty public constructor
    }


    public static ConfirmReceiveWindowFragment newInstance() {
        ConfirmReceiveWindowFragment fragment = new ConfirmReceiveWindowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//nhận kết quả trả về từ BarcodeScannerActivity
            if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
                Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.KEY_CAPTURED_BARCODE);

                String contents = barcode.rawValue;
                String barcode2 = contents.replace("DEMO", "");
                Log.d(TAG, barcode2);
                mPresenter.checkBarcode(barcode2);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_receive, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);
        initView();
        return view;
    }

    private void initView() {
        tvColumn.setText(getString(R.string.text_product_window_break));
        btPrint.setVisibility(View.GONE);
        llTypeProduct.setVisibility(View.GONE);
        llTimes.setVisibility(View.GONE);
        llTypeScan.setVisibility(View.GONE);
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        lvConfirm.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mPresenter.getListDepartment();
        mPresenter.getListSO();

    }


    @Override
    public void setPresenter(ConfirmReceiveWindowContract.Presenter presenter) {
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
                            tvDeliveryCode.setText(getString(R.string.text_choose_delivery_code));
                            maPhieuId = 0;
                            tvDepartment.setText(getString(R.string.text_choose_receiving_department));
                            departmentId = 0;
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
    public void showListDepartment(List<DepartmentEntity> list) {
        if (list.size() > 0) {
            tvDepartment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                            (list);
                    searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                        @Override
                        public void onSearchableItemClicked(Object item, int position) {
                            DepartmentEntity departmentEntity = (DepartmentEntity) item;
                            tvDepartment.setText(departmentEntity.getName());
                            departmentId = list.get(position).getId();
                            tvDeliveryCode.setText(getString(R.string.text_choose_delivery_code));
                            maPhieuId = 0;
                            mPresenter.getListDeliveryCode(orderId, departmentId);

                        }
                    });
                    searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                }
            });
        } else {
            tvDepartment.setOnClickListener(null);
        }

    }

    @Override
    public void showListConfirm(RealmResults<LogScanConfirmWindowModel> list) {
        if (cbConfirmAll.isChecked()) {
            change = true;
            cbConfirmAll.setChecked(false);

        }
        if (list.size() > 0) {
            cbConfirmAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!change) {
                        if (isChecked) {
                            mPresenter.confirmAll();
                        } else {

                            mPresenter.cancelConfirmAll();
                        }
                    } else {
                        change = false;
                    }

                }
            });
            cbConfirmAll.setClickable(true);
        } else {
            cbConfirmAll.setOnCheckedChangeListener(null);
            cbConfirmAll.setClickable(false);
        }
        adapter = new ConfirmInputWindowAdapter(list, new ConfirmInputWindowAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(long outputId, int number) {
                printed = false;
                mPresenter.updateNumberConfirm(outputId, number);
            }
        }, new ConfirmInputWindowAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                showToast(message);
                turnOnVibrator();
                startMusicError();
            }
        }, new ConfirmInputWindowAdapter.OnWarningListener() {
            @Override
            public void warningListener(long outputId, int numberOld, int numberNew) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_exceed_the_number_of_requests))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                mPresenter.updateNumberConfirm(outputId, numberNew);

                                sweetAlertDialog.dismiss();

                            }
                        })
                        .setCancelText(getString(R.string.text_no))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mPresenter.updateNumberConfirm(outputId, numberOld);
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
            }
        });

        lvConfirm.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lvConfirm.setHasFixedSize(true);
        lvConfirm.setAdapter(adapter);


    }

    @Override
    public void setStatusPrint(boolean printed) {
        this.printed = printed;
    }


    @Override
    public void showWarningPrint() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_do_you_want_print))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        // mPresenter.deleteAllItemLog();
                        sweetAlertDialog.dismiss();
                        mPresenter.print(
                                maPhieuId, orderId, departmentId, times, -1, true);
                    }
                })
                .setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                        mPresenter.uploadData(maPhieuId, orderId, departmentId, times, true);

                    }
                })
                .show();
    }

    @Override
    public void showListDeliveryCode(List<DeliveryNoteEntity> list) {

        if (list.size() > 0) {
            tvDeliveryCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                            (list);
                    searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                        @Override
                        public void onSearchableItemClicked(Object item, int position) {
                            DeliveryNoteEntity deliveryNoteEntity = (DeliveryNoteEntity) item;
                            tvDeliveryCode.setText(deliveryNoteEntity.getNoteCode());
                            maPhieuId = deliveryNoteEntity.getId();
                            mPresenter.getListConfirm(maPhieuId, false);
                        }
                    });
                    searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                }
            });
        } else {
            tvDeliveryCode.setOnClickListener(null);
        }

    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @OnClick(R.id.btn_save)
    public void save() {
        if (edtBarcode.getText().toString().equals("")) {
            return;
        }
        if (orderId == 0) {
            showError(getString(R.string.text_order_id_null));
            return;
        }

        if (departmentId == 0) {
            showError(getString(R.string.text_department_id_null));
            return;
        }

        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(edtBarcode.getText().toString());
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
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_back_have_detail_waiting))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            // mPresenter.deleteAllItemLog();
                            mPresenter.uploadData(maPhieuId, orderId, departmentId, times, false);
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

    @OnClick(R.id.img_upload)
    public void upload() {
        if (adapter != null && adapter.countDataEdit() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_upload_data))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.uploadData(maPhieuId, orderId, departmentId, times, false);

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
        } else {
            showError(getString(R.string.text_no_data_upload));
        }

    }

    @OnClick(R.id.btn_scan)
    public void scan() {
        if (orderId == 0) {
            showError(getString(R.string.text_order_id_null));
            return;
        }

        if (departmentId == 0) {
            showError(getString(R.string.text_department_id_null));
            return;
        }
        Intent launchIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
        getActivity().startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }

    @OnClick(R.id.btn_confirm_all)
    public void confirmAll() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_confirm_all_scan))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.confirmAll();
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

    @OnClick(R.id.bt_print)
    public void print() {
        if (adapter.countDataEdit() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_do_you_want_print))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            mPresenter.print(
                                    maPhieuId, orderId, departmentId, times, -1, false);
                        }
                    })
                    .setCancelText(getString(R.string.text_no))
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void showDialogCreateIPAddress(boolean upload) {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
//                mPresenter.saveIPAddress(ipAddress, port,
//                        maPhieuId, orderId, departmentId, times, -1, upload);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showErrorByType(String message, int type) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(message)
                .setConfirmText(getString(R.string.text_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                    }
                })
                .setCancelText(getString(R.string.text_try))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        switch (type) {
                            case 1:
                                mPresenter.getListSO();
                                break;
                            case 2:
                                mPresenter.getListDeliveryCode(orderId, departmentId);
                                break;
                            case 3:
                                mPresenter.getListConfirm(maPhieuId, false);
                                break;
                        }
                    }
                })
                .show();
    }


}
