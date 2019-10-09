package com.demo.barcode.screen.create_packaging;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.R;
import com.demo.barcode.adapter.SerialPackAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.manager.TypeSOManager;
import com.demo.barcode.screen.print_stamp.PrintStempActivity;
import com.demo.barcode.util.ConvertUtils;
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

public class CreatePackagingFragment extends BaseFragment implements CreatePackagingContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = CreatePackagingFragment.class.getName();
    private CreatePackagingContract.Presenter mPresenter;
    private SerialPackAdapter adapter;
    public MediaPlayer mp1, mp2;
    public boolean isClick = false;
    @BindView(R.id.tv_code_so)
    TextView tvCodeSO;

    @BindView(R.id.tv_type_product)
    TextView tvTypeProduct;

    @BindView(R.id.tv_apartment)
    TextView tvApartment;

    @BindView(R.id.txt_customer_name)
    TextView txtCustomerName;

    @BindView(R.id.edt_barcode)
    EditText edtBarcode;

    @BindView(R.id.lv_code)
    ListView lvCode;

    @BindView(R.id.txt_date_scan)
    TextView txtDateScan;

    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.btn_scan)
    Button btnScan;
    private Vibrator vibrate;
    private long orderId = 0;
    private long apartmentId = 0;
    private int orderType = 0;


    public CreatePackagingFragment() {
        // Required empty public constructor
    }


    public static CreatePackagingFragment newInstance() {
        CreatePackagingFragment fragment = new CreatePackagingFragment();
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

            if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
                Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.KEY_CAPTURED_BARCODE);

                String contents = barcode.rawValue;
                String barcode2 = contents.replace("DEMO", "");
                Log.d(TAG, barcode2);
                mPresenter.checkBarcode(barcode2, orderId, apartmentId);
            }
        }
        if (requestCode == PrintStempActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showSuccess(getString(R.string.text_print_success));
                mPresenter.getListProduct(orderId, apartmentId);
            } else {
                isClick = false;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_stamp_packaging, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);
        initView();
        return view;
    }

    private void initView() {

        mPresenter.getListScan();
        txtDateScan.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
    }


    @Override
    public void setPresenter(CreatePackagingContract.Presenter presenter) {
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
    public void showListScan(RealmResults<LogListSerialPackPagkaging> log) {
        adapter = new SerialPackAdapter(log,
                new SerialPackAdapter.OnItemClearListener() {
                    @Override
                    public void onItemClick(long productId, long logId, String sttPack, String codePack) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.text_title_noti))
                                .setContentText(getString(R.string.text_delete_code))
                                .setConfirmText(getString(R.string.text_yes))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        mPresenter.deleteLogScan(productId, logId, sttPack, codePack);
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
                }, new SerialPackAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(long productId, long logId, int number, String sttPack, String codePack, int numberTotal) {
                mPresenter.updateNumberScan(productId, logId, number, sttPack, codePack, numberTotal);
            }
        }, new SerialPackAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                showToast(message);
                startMusicError();
                turnOnVibrator();
            }
        },
                new SerialPackAdapter.onPrintListener() {
                    @Override
                    public void onPrint(long moduleId, String serialPack, long logSerialId) {
                        PrintStempActivity.start(getActivity(), orderId, apartmentId, moduleId, serialPack, orderType, logSerialId);

                    }
                }, new SerialPackAdapter.onClickEditTextListener() {
            @Override
            public void onClick() {
            }
        });
        lvCode.setAdapter(adapter);
    }


    @Override
    public void showListSO(List<SOEntity> list) {
        if (list.size() > 0) {
            tvCodeSO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.getCount() > 0) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.text_title_noti))
                                .setContentText(getString(R.string.text_upload_data))
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
                                        sweetAlertDialog.dismiss();
                                        mPresenter.deleteAllItemLog();
                                        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                                                (list);
                                        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                                            @Override
                                            public void onSearchableItemClicked(Object item, int position) {
                                                SOEntity soItem = (SOEntity) item;
                                                tvCodeSO.setText(soItem.getCodeSO());
                                                txtCustomerName.setText(soItem.getCustomerName());
                                                orderId = soItem.getOrderId();
                                                tvApartment.setText(getString(R.string.text_choose_floor));
                                                apartmentId = 0;
                                                mPresenter.getListApartment(orderId);
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
                                tvApartment.setText(getString(R.string.text_choose_floor));
                                apartmentId = 0;
                                mPresenter.getListApartment(orderId);
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
    public void showListApartment(List<ApartmentEntity> list) {
        if (list.size() > 0) {
            tvApartment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.getCount() > 0) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.text_title_noti))
                                .setContentText(getString(R.string.text_upload_data))
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
                                        sweetAlertDialog.dismiss();
                                        mPresenter.deleteAllItemLog();
                                        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                                                (list);
                                        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                                            @Override
                                            public void onSearchableItemClicked(Object item, int position) {
                                                ApartmentEntity apartmentEntity = (ApartmentEntity) item;
                                                tvApartment.setText(apartmentEntity.getApartmentName());
                                                apartmentId = apartmentEntity.getApartmentID();
                                                if (orderId > 0) {
                                                    mPresenter.getListScan();
                                                    mPresenter.getListProduct(orderId, apartmentId);
                                                }
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
                                ApartmentEntity apartmentEntity = (ApartmentEntity) item;
                                tvApartment.setText(apartmentEntity.getApartmentName());
                                apartmentId = apartmentEntity.getApartmentID();
                                if (orderId > 0) {
                                    mPresenter.getListProduct(orderId, apartmentId);
                                }
                            }
                        });
                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);

                    }
                }
            });

        } else {
            tvApartment.setOnClickListener(null);
        }

    }

    @Override
    public void setHeightListView() {
        adapter.setRefresh(true);
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

        if (apartmentId == 0) {
            showError(getString(R.string.text_apartment_null));
            return;
        }

        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(edtBarcode.getText().toString(), orderId, apartmentId);
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

        if (adapter != null && adapter.getCount() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_data_scan_not_print))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.dismiss();
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

    @OnClick(R.id.btn_scan)
    public void scan() {
        if (orderId == 0) {
            showError(getString(R.string.text_order_id_null));
            return;
        }

        if (apartmentId == 0) {
            showError(getString(R.string.text_apartment_null));
            return;
        }

        Intent launchIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
        getActivity().startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }


    @OnClick(R.id.tv_type_product)
    public void chooseProduct() {
        if (adapter.getCount() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_upload_data))
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
                            sweetAlertDialog.dismiss();
                            mPresenter.deleteAllItemLog();
                            SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                                    (TypeSOManager.getInstance().getListType());
                            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                                @Override
                                public void onSearchableItemClicked(Object item, int position) {
                                    TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
                                    tvTypeProduct.setText(typeSO.getName());
                                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
                                    orderId = 0;
                                    tvApartment.setText(getString(R.string.text_choose_floor));
                                    apartmentId = 0;
                                    txtCustomerName.setText("");
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
                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
                    orderId = 0;
                    tvApartment.setText(getString(R.string.text_choose_floor));
                    apartmentId = 0;
                    txtCustomerName.setText("");
                    mPresenter.getListSO(typeSO.getValue());
                }
            });
            searchableListDialog.show(getActivity().getFragmentManager(), TAG);
        }

    }
}
