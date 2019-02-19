package com.demo.barcode.screen.confirm_receive;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.barcode.R;
import com.demo.barcode.adapter.ConfirmInputAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.dialogs.ChangeIPAddressDialog;
import com.demo.barcode.dialogs.ChooseGroupDialog;
import com.demo.barcode.manager.TypeSOManager;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.barcodereader.BarcodeScanner;
import com.demo.barcode.widgets.barcodereader.BarcodeScannerBuilder;
import com.demo.barcode.widgets.spinner.SearchableListDialog;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmReceiveFragment extends BaseFragment implements ConfirmReceiveContract.View {
    private static final int MY_LOCATION_REQUEST_CODE = 1234;
    private final String TAG = ConfirmReceiveFragment.class.getName();
    private ConfirmReceiveContract.Presenter mPresenter;
    private int departmentId = 0;
    private ConfirmInputAdapter adapter;
    public MediaPlayer mp1, mp2;
    private int times = 0;
    private boolean change;
    private long maPhieuId = 0;
    private String maPhieu;
    @Bind(R.id.tv_code_so)
    TextView tvCodeSO;

    @Bind(R.id.tv_times)
    TextView tvTimes;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.tv_delivery_department)
    TextView tvDepartment;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.lv_confirm)
    RecyclerView lvConfirm;

    @Bind(R.id.tv_type_product)
    TextView tvTypeProduct;

    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;

    @Bind(R.id.cb_all)
    CheckBox cbConfirmAll;

    @Bind(R.id.tv_delivery_code)
    TextView tvDeliveryCode;

    @Bind(R.id.ll_root)
    LinearLayout lLRoot;
    @Bind(R.id.btn_scan)
    Button btnScan;
    private int typeScan = 0;
    private Vibrator vibrate;
    private long orderId = 0;

    private IntentIntegrator integrator = new IntentIntegrator(getActivity());

    public ConfirmReceiveFragment() {
        // Required empty public constructor
    }


    public static ConfirmReceiveFragment newInstance() {
        ConfirmReceiveFragment fragment = new ConfirmReceiveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String contents = data.getStringExtra(Constants.KEY_SCAN_RESULT);
                String barcode = contents.replace("DEMO", "");
                mPresenter.checkBarcode(maPhieuId, orderId, barcode, departmentId, times, typeScan == 2);

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
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds

        mPresenter.getListDepartment();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_default:
                        typeScan = 1;
                        break;
                    case R.id.rb_group:
                        typeScan = 2;
                        break;
                }
            }
        });

    }


    @Override
    public void setPresenter(ConfirmReceiveContract.Presenter presenter) {
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

        showNotification(message, SweetAlertDialog.ERROR_TYPE);
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
                            mPresenter.getListGroupCode(orderId);

                            if (departmentId > 0) {
                                mPresenter.getListConfirm(maPhieuId, orderId, departmentId, times, false);
                            }
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
    public void showListTimes(List<Integer> list) {
        if (list.size() > 0) {
            tvTimes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                            (list);
                    searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                        @Override
                        public void onSearchableItemClicked(Object item, int position) {
                            Integer number = (Integer) item;
                            tvTimes.setText(String.valueOf(number));
                            times = number;
                            if (orderId > 0 && departmentId > 0) {
                                mPresenter.getListConfirmByTimes(maPhieuId, orderId, departmentId, times);
                            }
                        }
                    });
                    searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                }
            });
        } else {
            tvTimes.setOnClickListener(null);
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
                            mPresenter.getListDeliveryCode(orderId, departmentId);
                            if (times > 0 && orderId > 0) {
                                mPresenter.getListConfirmByTimes(maPhieuId, orderId, departmentId, times);
                            }
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
    public void showListConfirm(RealmResults<LogScanConfirm> list) {
        adapter = new ConfirmInputAdapter(list, new ConfirmInputAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanConfirm item, int number) {
                mPresenter.updateNumberConfirm(maPhieuId, item.getOrderId(), item.getMasterOutputID(), item.getDepartmentIDOut(), item.getTimesInput(), number);
            }
        }, new ConfirmInputAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                showToast(message);
                turnOnVibrator();
                startMusicError();
            }
        },  new ConfirmInputAdapter.OnWarningListener() {
            @Override
            public void warningListener(LogScanConfirm item, int number) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_exceed_the_number_of_requests))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                mPresenter.updateNumberConfirm(maPhieuId, item.getOrderId(), item.getMasterOutputID(), item.getDepartmentIDOut(), item.getTimesInput(), number);

                                sweetAlertDialog.dismiss();

                            }
                        })
                        .setCancelText(getString(R.string.text_no))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mPresenter.updateNumberConfirm(maPhieuId, item.getOrderId(), item.getMasterOutputID(), item.getDepartmentIDOut(), item.getTimesInput(), item.getNumberConfirmed());
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
            }
        });

        lvConfirm.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lvConfirm.setHasFixedSize(true);
        lvConfirm.setAdapter(adapter);
        if (list.size() > 0) {
            cbConfirmAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!change) {
                        if (isChecked) {
                            mPresenter.confirmAll(orderId, departmentId, times);
                        } else {

                            mPresenter.cancelConfirmAll(orderId, departmentId, times);
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
    }

    @Override
    public void clearDataNoProduct(boolean chooseType) {
//        lvConfirm.setAdapter(null);
//        ArrayAdapter<DeliveryNoteEntity> adapter = new ArrayAdapter<DeliveryNoteEntity>(getContext(), android.R.layout.simple_spinner_item);
//        ssDeliveryCode.setAdapter(adapter);
//        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item);
//        ssTimes.setAdapter(adapter);
    }

    @Override
    public void showDialogConfirm(List<ProductGroupEntity> list, int times) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_number_in_group_exceed_number_received_save_enough))
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
    public void setCheckedAll(boolean checkedAll) {
        if (cbConfirmAll.isChecked() != checkedAll) {
            change = true;
            cbConfirmAll.setChecked(checkedAll);
        }
    }

    @Override
    public void showDialogChooseGroup(List<GroupEntity> list) {
        ChooseGroupDialog dialog = new ChooseGroupDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setList(list);
        dialog.setListener(new ChooseGroupDialog.OnItemSaveListener() {
            @Override
            public void onSave(GroupEntity groupEntity) {
                mPresenter.saveNumberConfirmGroup(groupEntity, maPhieuId, orderId, times, departmentId);
            }
        });
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
                            mPresenter.getListConfirm(list.get(position).getId(), orderId, departmentId, times, false);
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
        if (times == 0) {
            showError(getString(R.string.text_times_id_null));
            return;
        }
        if (typeScan == 0) {
            showError(getString(R.string.text_type_scan_null));
            return;
        }
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(maPhieuId, orderId, edtBarcode.getText().toString(), departmentId, times, typeScan == 2);
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
        if (lLRoot.getVisibility() == View.GONE) {
            lLRoot.setVisibility(View.VISIBLE);
        } else {
            if (mPresenter.countListConfirmByTimesWaitingUpload(orderId, departmentId, times) > 0) {
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

    }

    @OnClick(R.id.img_upload)
    public void upload() {
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
        if (times == 0) {
            showError(getString(R.string.text_times_id_null));
            return;
        }

        if (typeScan == 0) {
            showError(getString(R.string.text_type_scan_null));
            return;
        }
//        integrator = new IntentIntegrator(getActivity());
//        integrator.setCaptureActivity(ScanActivity.class);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        integrator.setPrompt("Đặt mã cần quét vào khung");
//        integrator.setCameraId(CAMERA_FACING_BACK);  // Use a specific camera of the device
//        integrator.setBeepEnabled(false);
//        integrator.setBarcodeImageEnabled(true);
//        integrator.setOrientationLocked(false);
//        integrator.initiateScan();


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
                        mPresenter.checkBarcode(maPhieuId, orderId, barcode2, departmentId, times, typeScan == 2);
                    }
                })
                .build();
        barcodeScanner.startScan();
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
                        mPresenter.confirmAll(orderId, departmentId, times);
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

    @Override
    public void showDialogCreateIPAddress(boolean upload) {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port,
                        maPhieuId, maPhieu, orderId, departmentId, times, -1, upload);
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.tv_type_product)
    public void chooseProduct() {
        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                (TypeSOManager.getInstance().getListType());
        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
            @Override
            public void onSearchableItemClicked(Object item, int position) {
                TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
                tvTypeProduct.setText(typeSO.getName());
                mPresenter.getListSO(typeSO.getValue());
            }
        });
        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
    }


}
