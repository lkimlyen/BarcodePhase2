package com.demo.barcode.screen.stages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.utils.view.DateUtils;
import com.demo.barcode.R;
import com.demo.barcode.adapter.StagesAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.dialogs.ChooseGroupDialog;
import com.demo.barcode.dialogs.DetailGroupDialog;
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

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

/**
 * Created by MSI on 26/11/2017.
 */

public class StagesFragment extends BaseFragment implements StagesContract.View {
    private static final int MY_LOCATION_REQUEST_CODE = 1234;
    private final String TAG = StagesFragment.class.getName();
    private StagesContract.Presenter mPresenter;
    private StagesAdapter adapter;
    public MediaPlayer mp1, mp2;
    private int times = 0;
    @Bind(R.id.ss_code_so)
    SearchableSpinner ssCodeSO;

    @Bind(R.id.ss_times)
    SearchableSpinner ssTimes;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.lv_code)
    ListView rvCode;

    @Bind(R.id.ss_type_product)
    SearchableSpinner ssTypeProduct;

    @Bind(R.id.ss_receiving_department)
    SearchableSpinner ssDepartment;

    @Bind(R.id.txt_delivery_date)
    TextView txtDateScan;

    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;

    @Bind(R.id.ll_root)
    LinearLayout llRoot;

    @Bind(R.id.btn_scan)
    Button btnScan;
    private Vibrator vibrate;
    private long orderId = 0;
    private int departmentId = 0;
    private int typeScan = 0;

    private IntentIntegrator integrator = new IntentIntegrator(getActivity());

    public StagesFragment() {
        // Required empty public constructor
    }


    public static StagesFragment newInstance() {
        StagesFragment fragment = new StagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 178) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("Message");
                showSuccess(result);
                mPresenter.getListScanStages(orderId, departmentId, times);
                mPresenter.getListGroupCode(orderId);
                return;
            }
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String contents = data.getStringExtra(Constants.KEY_SCAN_RESULT);
                String barcode = contents.replace("DEMO", "");
                mPresenter.checkBarcode(barcode, departmentId, times, typeScan == 2);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stages, container, false);
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
        ssCodeSO.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countLogScanStages(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });

        ssCodeSO.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                uploadData();
            }
        });

        ssDepartment.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countLogScanStages(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });
        ssDepartment.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                uploadData();
            }
        });

        ssTypeProduct.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countLogScanStages(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });
        ssTypeProduct.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                uploadData();
            }
        });
        ssTimes.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                if (mPresenter.countLogScanStages(orderId, departmentId, times) > 0) {
                    return true;
                }
                return false;
            }
        });
        ssTimes.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                uploadData();
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
    }

    @Override
    public void setPresenter(StagesContract.Presenter presenter) {
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
        if (llRoot.getVisibility() == View.GONE){
            btnScan.setVisibility(View.VISIBLE);
            llRoot.setVisibility(View.VISIBLE);
        }
        showNotification(message, SweetAlertDialog.ERROR_TYPE);
    }


    @Override
    public void showSuccess(String message) {
        showToast(message);
    }

    @Override
    public void showListDepartment(List<DepartmentEntity> list) {
        ArrayAdapter<DepartmentEntity> adapter = new ArrayAdapter<DepartmentEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssDepartment.setAdapter(adapter);
        ssDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentId = list.get(position).getId();
                if (times > 0 && orderId > 0) {
                    mPresenter.getListScanStages(orderId, departmentId, times);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListLogScanStages(LogListScanStages parent) {

        adapter = new StagesAdapter(parent.getList(), new StagesAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(LogScanStages item) {
                if (llRoot.getVisibility() == View.GONE){
                    btnScan.setVisibility(View.VISIBLE);
                    llRoot.setVisibility(View.VISIBLE);
                }
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_code))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.deleteScanStages(item.getId());


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
        }, new StagesAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanStages item, int number) {
                if (item.getTypeScan()) {
                    mPresenter.updateNumberScan(item.getId(), number, true);
                } else {
                    mPresenter.updateNumberScanInGroup(item, number);
                }

            }
        }, new StagesAdapter.onErrorListener() {
            @Override
            public void errorListener(LogScanStages item, int numberInput, String message) {
                if (!TextUtils.isEmpty(message)) {
                    showToast(message);
                    turnOnVibrator();
                    startMusicError();
                } else {
                    if (llRoot.getVisibility() == View.VISIBLE){
                        btnScan.setVisibility(View.GONE);
                        llRoot.setVisibility(View.GONE);
                    }
                    if (item.getTypeScan()) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.text_title_noti))
                                .setContentText(getString(R.string.text_exceed_the_number_of_requests))
                                .setConfirmText(getString(R.string.text_yes))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        mPresenter.updateNumberScan(item.getId(), numberInput, true);
                                        sweetAlertDialog.dismiss();
                                        if (llRoot.getVisibility() == View.GONE){
                                            btnScan.setVisibility(View.VISIBLE);
                                            llRoot.setVisibility(View.VISIBLE);
                                        }
                                    }
                                })
                                .setCancelText(getString(R.string.text_no))
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        mPresenter.updateNumberScan(item.getId(), item.getNumberInput(), false);
                                        sweetAlertDialog.dismiss();
                                        if (llRoot.getVisibility() == View.GONE){
                                            btnScan.setVisibility(View.VISIBLE);
                                            llRoot.setVisibility(View.VISIBLE);
                                        }
                                    }
                                })
                                .show();
                    } else {
                        mPresenter.updateNumberScanInGroup(item, numberInput);
                    }

                }

            }
        }, new StagesAdapter.onOpenDetailListener() {
            @Override
            public void onOpenDetail(String groupCode) {
                DetailGroupDialog detailGroupDialog = new DetailGroupDialog();
                detailGroupDialog.show(getActivity().getFragmentManager(), TAG);
                detailGroupDialog.setGroupCode(groupCode);
            }
        }, new StagesAdapter.onClickEditTextListener() {
            @Override
            public void onClick() {
                if (llRoot.getVisibility() == View.VISIBLE){
                    btnScan.setVisibility(View.GONE);
                    llRoot.setVisibility(View.GONE);
                }
            }
        });
        rvCode.setAdapter(adapter);


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
                    mPresenter.getListProduct(orderId,times,departmentId, false);
                    //mPresenter.getListTimes(orderId);
                    mPresenter.getListGroupCode(orderId);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
    public void showCheckResidual(int times, ProductEntity
            productEntity, int departmentId) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_exceed_the_number_of_requests))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        mPresenter.saveBarcodeToDataBase(times, productEntity, 1, departmentId, null, typeScan == 1);
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

    @Override
    public void showCheckResidualInGroup(long id, double number, double numberInput) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_exceed_the_number_of_requests_in_group))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        mPresenter.updateNumberScan(id, number, true);
                        sweetAlertDialog.dismiss();

                    }
                })
                .setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.updateNumberScan(id, numberInput, false);
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
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
                    mPresenter.getListScanStages(orderId, departmentId, times);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        rvCode.setAdapter(null);

    }

    @Override
    public void showDialogUpload() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_data_yesterday_not_upload))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.uploadDataAll(orderId, departmentId, times);
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

    @Override
    public void showDialogChooseGroup(List<GroupEntity> list) {
        ChooseGroupDialog dialog = new ChooseGroupDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setList(list);
        dialog.setListener(new ChooseGroupDialog.OnItemSaveListener() {
            @Override
            public void onSave(GroupEntity groupEntity) {
                mPresenter.saveBarcodeWithGroup(groupEntity, times, departmentId);
            }
        });

    }


    @OnClick(R.id.ic_refresh)
    public void refresh() {
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
                        mPresenter.checkBarcode(edtBarcode.getText().toString().trim(), departmentId, times, typeScan == 2);
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
        if (mPresenter.countLogScanStages(orderId, departmentId, times) > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_back_have_detail_waiting))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            // mPresenter.deleteAllItemLog();
                            mPresenter.uploadDataAll(orderId, departmentId, times);
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
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_upload_data))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.uploadDataAll(orderId, departmentId, times);
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


}
