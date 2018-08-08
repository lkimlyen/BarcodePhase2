package com.demo.barcode.screen.stages;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.barcode.R;
import com.demo.barcode.adapter.CreateCodePackAdapter;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.screen.capture.ScanActivity;
import com.demo.barcode.screen.print_stemp.PrintStempActivity;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.spinner.SearchableSpinner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private FusedLocationProviderClient mFusedLocationClient;
    private CreateCodePackAdapter adapter;
    public MediaPlayer mp1, mp2;
    public boolean isClick = false;
    @Bind(R.id.ss_code_so)
    SearchableSpinner ssCodeSO;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.lv_code)
    ListView rvCode;

    @Bind(R.id.ss_receiving_department)
    SearchableSpinner ssDepartment;

    private Vibrator vibrate;
    private int orderId = 0;
    private Location mLocation;

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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            checkPermissionLocation();
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String contents = data.getStringExtra(Constants.KEY_SCAN_RESULT);
                String barcode = contents.replace("DEMO", "");
                checkPermissionLocation();
                mPresenter.checkBarcode(barcode, orderId, mLocation != null ? mLocation.getLatitude() : 0,
                        mLocation != null ? mLocation.getLongitude() : 0);
            }
        }

        if (requestCode == PrintStempActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showSuccess(getString(R.string.text_print_success));
                mPresenter.getProduct(orderId);
            } else {
                isClick = false;
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
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        checkPermissionLocation();
        ssCodeSO.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
//                if (mPresenter.countListScan(orderId) > 0) {
//                    return true;
//                }
                return false;
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
        if (!isClick) {
            // mPresenter.deleteAllItemLog();
        }
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
    public void showListDepartment(List<DepartmentEntity> list) {
        ArrayAdapter<DepartmentEntity> adapter = new ArrayAdapter<DepartmentEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssDepartment.setAdapter(adapter);
        ssDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.getListProduct(orderId, list.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void showLogScanCreatePack(LogScanCreatePackList list) {

        adapter = new CreateCodePackAdapter(list.getItemList(), new CreateCodePackAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(LogScanCreatePack item) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_code))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.deleteItemLog(item);
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
        }, new CreateCodePackAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanCreatePack item, int number) {
                mPresenter.updateNumberInput(item.getId(), number, item.getSerial(), item.getNumInput());
            }
        }, new CreateCodePackAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                showToast(message);
                turnOnVibrator();
                startMusicError();
            }
        });
        rvCode.setAdapter(adapter);

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
    public void showCheckResidual(int times) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(String.format(getString(R.string.text_number_residual), times))
                .setConfirmText(getString(R.string.text_yes))
                .setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

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
        if (ssCodeSO.getSelectedItem().toString().equals(getString(R.string.text_choose_code_so))) {
            return;
        }

        if (ssDepartment.getSelectedItem().toString().equals(getString(R.string.text_choose_receiving_department))) {
            return;
        }
        checkPermissionLocation();
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(edtBarcode.getText().toString().trim(), orderId,
                                mLocation != null ? mLocation.getLatitude() : 0,
                                mLocation != null ? mLocation.getLongitude() : 0);
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

    public void checkPermissionLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        } else {
            // Access to the location has been granted to the app.
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLocation = location;  // Logic to handle location object
                            }
                        }
                    }).addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showError(e.getMessage());
                }
            });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionLocation();
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @OnClick(R.id.img_back)
    public void back() {
//        if (mPresenter.countListScan(orderId) > 0) {
//            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(getString(R.string.text_title_noti))
//                    .setContentText(getString(R.string.text_back_cancel_order_not_print))
//                    .setConfirmText(getString(R.string.text_yes))
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            mPresenter.deleteAllItemLog();
//                            sweetAlertDialog.dismiss();
//                            getActivity().finish();
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
//            getActivity().finish();
//        }
    }

    @OnClick(R.id.img_upload)
    public void upload() {
    }

    @OnClick(R.id.btn_scan)
    public void scan() {
        if (ssCodeSO.getSelectedItem().toString().equals(getString(R.string.text_choose_code_so))) {
            showError(getString(R.string.text_order_id_null));
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
