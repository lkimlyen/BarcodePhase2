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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.demo.barcode.screen.capture.ScanActivity;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.spinner.SearchableSpinner;
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
    @Bind(R.id.ss_code_so)
    SearchableSpinner ssCodeSO;

    @Bind(R.id.ss_times)
    SearchableSpinner ssTimes;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.ss_delivery_department)
    SearchableSpinner ssDepartment;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.lv_confirm)
    RecyclerView lvConfirm;

    @Bind(R.id.ss_type_product)
    SearchableSpinner ssTypeProduct;

    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;

    @Bind(R.id.cb_all)
    CheckBox cbConfirmAll;

    @Bind(R.id.ss_delivery_code)
    SearchableSpinner ssDeliveryCode;

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
                mPresenter.checkBarcode(orderId, barcode, departmentId, times, typeScan == 2);

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
        ssCodeSO.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countListConfirmByTimesWaitingUpload(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });
        ssCodeSO.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                mPresenter.uploadData(maPhieuId,orderId, departmentId, times, false);
            }
        });
        ssTypeProduct.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countListConfirmByTimesWaitingUpload(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });

        ssDeliveryCode.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countListConfirmByTimesWaitingUpload(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });
        ssTypeProduct.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                mPresenter.uploadData(maPhieuId,orderId, departmentId, times, false);
            }
        });
        ssTimes.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countListConfirmByTimesWaitingUpload(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });
        ssTimes.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                mPresenter.uploadData(maPhieuId,orderId, departmentId, times, false);
            }
        });
        ssDepartment.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countListConfirmByTimesWaitingUpload(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });
        ssDepartment.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                mPresenter.uploadData(maPhieuId,orderId, departmentId, times, false);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        ArrayAdapter<SOEntity> adapter = new ArrayAdapter<SOEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssCodeSO.setAdapter(adapter);
        ssCodeSO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCustomerName.setText(list.get(position).getCustomerName());
                orderId = list.get(position).getOrderId();
                mPresenter.getListGroupCode(orderId);

                if (departmentId > 0) {
                    mPresenter.getListConfirm(maPhieuId,orderId, departmentId, times, false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListTimes(List<Integer> list) {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, list);
        ssTimes.setAdapter(adapter);
        ssTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                times = list.get(position);
                if (orderId > 0 && departmentId > 0) {
                    mPresenter.getListConfirmByTimes(maPhieuId,orderId, departmentId, times);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListDepartment(List<DepartmentEntity> list) {
        ArrayAdapter<DepartmentEntity> adapter = new ArrayAdapter<DepartmentEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssDepartment.setAdapter(adapter);
        ssDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentId = list.get(position).getId();
                mPresenter.getListDeliveryCode(orderId, departmentId);
//                if (orderId > 0 && times > 0) {
//                    mPresenter.getListConfirmByTimes(orderId, departmentId, times);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListConfirm(RealmResults<LogScanConfirm> list) {
        adapter = new ConfirmInputAdapter(list, new ConfirmInputAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanConfirm item, int number) {
                mPresenter.updateNumberConfirm(item.getOrderId(), item.getMasterOutputID(), item.getDepartmentIDOut(), item.getTimesInput(), number);
            }
        }, new ConfirmInputAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                showToast(message);
                turnOnVibrator();
                startMusicError();
            }
        });

        lvConfirm.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lvConfirm.setHasFixedSize(true);
        lvConfirm.setAdapter(adapter);
    }

    @Override
    public void clearDataNoProduct(boolean chooseType) {
        if (chooseType) {
            txtCustomerName.setText("");
            ArrayAdapter<SOEntity> adapter = new ArrayAdapter<SOEntity>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
            ssCodeSO.setAdapter(adapter);
            orderId = 0;

        }
        ArrayAdapter<Integer> adapterTimes = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        ssTimes.setAdapter(adapterTimes);
        times = 0;
        lvConfirm.setAdapter(null);
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
                mPresenter.saveNumberConfirmGroup(groupEntity, orderId, times, departmentId);
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
                                maPhieuId,orderId, departmentId, times, -1, true);
                    }
                })
                .setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.uploadData(maPhieuId,orderId, departmentId, times, true);

                    }
                })
                .show();
    }

    @Override
    public void showListDeliveryCode(List<DeliveryNoteEntity> list) {
        ArrayAdapter<DeliveryNoteEntity> adapter = new ArrayAdapter<DeliveryNoteEntity>(getContext(), android.R.layout.simple_spinner_item, list);
        ssDeliveryCode.setAdapter(adapter);
        ssDeliveryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maPhieuId = list.get(position).getId();
                mPresenter.getListConfirm(list.get(position).getId(), orderId, departmentId, times, false);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.ic_refresh)
    public void refresh() {
//        if (mPresenter.countListScan(orderId) > 0) {
//            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(getString(R.string.text_title_noti))
//                    .setContentText(getString(R.string.text_not_done_pack_current_refresh))
//                    .setConfirmText(getString(R.string.text_yes))
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            mPresenter.deleteAllItemLog();
//                            sweetAlertDialog.dismiss();
//                            mPresenter.getData();
//                        }
//                    })
//                    .setCancelText(getString(R.string.text_no))
//                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            sweetAlertDialog.dismiss();
//                        }
//                    })
//                    .show();
//
//        } else {
//
//            mPresenter.getData();
//        }
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
                        mPresenter.checkBarcode(orderId, edtBarcode.getText().toString(), departmentId, times, typeScan == 2);
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
                                mPresenter.uploadData(maPhieuId,orderId, departmentId, times, false);
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
                        mPresenter.uploadData(maPhieuId,orderId, departmentId, times, false);
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
        integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Đặt mã cần quét vào khung");
        integrator.setCameraId(CAMERA_FACING_BACK);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
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
                        maPhieuId, orderId, departmentId, times, -1, upload);
                dialog.dismiss();
            }
        });
    }

}
