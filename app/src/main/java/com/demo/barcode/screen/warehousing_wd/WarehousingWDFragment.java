package com.demo.barcode.screen.warehousing_wd;


import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.SOWarehouseEntity;
import com.demo.architect.data.model.offline.WarehousingModel;
import com.demo.architect.utils.view.DateUtils;
import com.demo.barcode.R;
import com.demo.barcode.adapter.WarehousingAdapter;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.app.di.Precondition;
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

public class WarehousingWDFragment extends BaseFragment implements WarehousingWDContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = WarehousingWDFragment.class.getName();
    private WarehousingWDContract.Presenter mPresenter;
    private WarehousingAdapter adapter;
    public MediaPlayer mp1, mp2, mp3;
    @BindView(R.id.tv_code_so)
    TextView tvCodeSO;

    @BindView(R.id.edt_barcode)
    EditText edtBarcode;

    @BindView(R.id.lv_code)
    RecyclerView rvCode;

    @BindView(R.id.txt_delivery_date)
    TextView txtDateScan;

    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.btn_scan)
    Button btnScan;
    private Vibrator vibrate;
    private long orderId = 0;

    public WarehousingWDFragment() {
        // Required empty public constructor
    }


    public static WarehousingWDFragment newInstance() {
        WarehousingWDFragment fragment = new WarehousingWDFragment();
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
                mPresenter.checkBarcode(orderId,barcode2 );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_warehousing_wd, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);
        mp3 = MediaPlayer.create(getActivity(), R.raw.beepdup);
        initView();
        return view;
    }


    private void initView() {


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        rvCode.addItemDecoration(dividerItemDecoration);
        txtDateScan.setText(DateUtils.getShortDateCurrent());
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        mPresenter.getAllListScanWarehousing();
        mPresenter.getListSO();


    }

    @Override
    public void setPresenter(WarehousingWDContract.Presenter presenter) {
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
    public void showListWarehousing(RealmResults<WarehousingModel> parent) {

        adapter = new WarehousingAdapter(parent, new WarehousingAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(WarehousingModel item) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_code))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.deleteItem(item.getId());


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
        }, new WarehousingAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(WarehousingModel item, int number) {
                mPresenter.updateNumberScan(item.getId(), number, true);
            }
        }, new WarehousingAdapter.OnErrorListener() {
            @Override
            public void onError(WarehousingModel item, int numberInput, String message) {
                if (!TextUtils.isEmpty(message)) {
                    showToast(message);
                    turnOnVibrator();
                    startMusicError();
                } else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.text_title_noti))
                            .setContentText(getString(R.string.text_exceed_the_number_of_requests))
                            .setConfirmText(getString(R.string.text_yes))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mPresenter.updateNumberScan(item.getId(), numberInput, true);
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .setCancelText(getString(R.string.text_no))
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mPresenter.updateNumberScan(item.getId(), item.getNumberInput(), false);
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();


                }

            }
        }, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rvCode.setLayoutManager(linearLayoutManager);
        rvCode.setHasFixedSize(true);
        rvCode.setAdapter(adapter);


    }

    @Override
    public void showListSO(List<SOWarehouseEntity> list) {
        if (list.size() > 0) {
            tvCodeSO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.getItemCount() > 0) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(CoreApplication.getInstance().getString(R.string.text_title_noti))
                                .setContentText(CoreApplication.getInstance().getString(R.string.text_back_have_detail_waiting))
                                .setConfirmText(CoreApplication.getInstance().getString(R.string.text_yes))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismiss();
                                        mPresenter.uploadData(orderId);
                                    }
                                })
                                .setCancelText(CoreApplication.getInstance().getString(R.string.text_no))
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        mPresenter.deleteAllData();
                                        sweetAlertDialog.dismiss();
                                        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                                                (list);
                                        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                                            @Override
                                            public void onSearchableItemClicked(Object item, int position) {
                                                SOWarehouseEntity soItem = (SOWarehouseEntity) item;
                                                tvCodeSO.setText(soItem.getCodeSO());
                                                orderId = soItem.getOrderId();
                                                tvPercent.setText(String.valueOf(soItem.getPercent()));
                                                mPresenter.getListProduct(orderId, false);
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
                                SOWarehouseEntity soItem = (SOWarehouseEntity) item;
                                tvCodeSO.setText(soItem.getCodeSO());
                                tvPercent.setText(String.valueOf(soItem.getPercent()));
                                orderId = soItem.getOrderId();
                                mPresenter.getListProduct(orderId, false);
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
    public void showCheckResidual() {
        mp3.start();
        Toast toast = Toast.makeText(getActivity(), "Mã quét vượt số lượng yêu cầu!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

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
                        mPresenter.uploadData(orderId);
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

        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(orderId,edtBarcode.getText().toString().trim());
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
        if (adapter != null && adapter.getItemCount() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_back_have_detail_waiting))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.uploadData(orderId);
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
        if (adapter != null && adapter.getItemCount() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_upload_data))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.uploadData(orderId);
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

        Intent launchIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
        getActivity().startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }


}
