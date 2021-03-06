package com.demo.barcode.screen.create_pack_window;

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
import com.demo.architect.data.model.GroupSetEntity;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SetWindowEntity;
import com.demo.architect.data.model.offline.ListPackCodeWindowModel;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.barcode.R;
import com.demo.barcode.adapter.SerialPackAdapter;
import com.demo.barcode.adapter.SerialPackWindowAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.dialogs.ChooseSetDialog;
import com.demo.barcode.manager.DirectionManager;
import com.demo.barcode.screen.print_stamp.PrintStempActivity;
import com.demo.barcode.screen.print_stamp_window.PrintStempWindowActivity;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.barcodereader.BarcodeScannerActivity;
import com.demo.barcode.widgets.spinner.SearchableListDialog;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreatePackagingWindowFragment extends BaseFragment implements CreatePackagingWindowContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = CreatePackagingWindowFragment.class.getName();
    private CreatePackagingWindowContract.Presenter mPresenter;
    private SerialPackWindowAdapter adapter;
    public MediaPlayer mp1, mp2;
    public boolean isClick = false;
    @BindView(R.id.tv_code_so)
    TextView tvCodeSO;

    @BindView(R.id.tv_direction)
    TextView tvDirection;

    @BindView(R.id.tv_set_window)
    TextView tvSetWindow;

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
    private long productSetId = 0;
    private int direction = -1;


    public CreatePackagingWindowFragment() {
        // Required empty public constructor
    }


    public static CreatePackagingWindowFragment newInstance() {
        CreatePackagingWindowFragment fragment = new CreatePackagingWindowFragment();
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
                mPresenter.checkBarcode(barcode2, direction);
            }
        }
        //sau khi in thành công lấy lại ds bộ cửa
        if (requestCode == PrintStempActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showSuccess(getString(R.string.text_print_success));
                mPresenter.getListDetailInSet(productSetId, direction);
            } else {
                isClick = false;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_pack_window, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);
        initView();
        return view;
    }

    private void initView() {

        mPresenter.getListSO();
        mPresenter.getListScan();
        txtDateScan.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
    }


    @Override
    public void setPresenter(CreatePackagingWindowContract.Presenter presenter) {
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
    public void showListScan(RealmResults<ListPackCodeWindowModel> results) {
        adapter = new SerialPackWindowAdapter(results, new SerialPackWindowAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(long logId, String packCode, int numberOnPack) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_code))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.deleteLogScan(logId,packCode,numberOnPack);
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
        }, new SerialPackWindowAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(long logId, int number, String packCode, int numberOnPack, int totalNumber) {
                mPresenter.updateNumberScan(logId, number, packCode, numberOnPack, totalNumber);
            }
        }, new SerialPackWindowAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                showToast(message);
                startMusicError();
                turnOnVibrator();
            }
        }, new SerialPackWindowAdapter.onPrintListener() {
            @Override
            public void onPrint(long mainId) {
                PrintStempWindowActivity.start(getActivity(), orderId, productSetId, direction,mainId);
            }
        }, new SerialPackWindowAdapter.onClickEditTextListener() {
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
                                                tvSetWindow.setText(getString(R.string.text_choose_set_window));
                                                productSetId = 0;
                                                tvDirection.setText(getString(R.string.text_choose_direction));
                                                direction = -1;
                                                mPresenter.getListSetWindow(orderId);
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
                                tvSetWindow.setText(getString(R.string.text_choose_set_window));
                                productSetId = 0;
                                tvDirection.setText(getString(R.string.text_choose_direction));
                                direction = -1;
                                mPresenter.getListSetWindow(orderId);
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
    public void showListSetWindow(List<SetWindowEntity> list) {
        if (list.size() > 0) {
            tvSetWindow.setOnClickListener(new View.OnClickListener() {
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
                                                SetWindowEntity apartmentEntity = (SetWindowEntity) item;
                                                tvSetWindow.setText(apartmentEntity.getProductSetName());
                                                productSetId = apartmentEntity.getProductSetId();
                                                tvDirection.setText(getString(R.string.text_choose_direction));
                                                direction = -1;
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
                                SetWindowEntity apartmentEntity = (SetWindowEntity) item;
                                tvSetWindow.setText(apartmentEntity.getProductSetName());
                                productSetId = apartmentEntity.getProductSetId();
                                tvDirection.setText(getString(R.string.text_choose_direction));
                                direction = -1;
                            }
                        });
                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);

                    }
                }
            });

        } else {
            tvSetWindow.setOnClickListener(null);
        }
    }


    @Override
    public void setHeightListView() {
        adapter.setRefresh(true);
    }

    @Override
    public void showDialogChooseSet(ProductPackagingWindowEntity entity) {
        ChooseSetDialog dialog = new ChooseSetDialog();
        dialog.show(getActivity().getFragmentManager(),TAG);
        dialog.setList(entity.getListGroup());
        dialog.setListener(new ChooseSetDialog.OnItemSaveListener() {
            @Override
            public void onSave(GroupSetEntity groupEntity) {
                mPresenter.saveBarcode(entity,direction,groupEntity);
            }
        });
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

        if (productSetId == 0) {
            showError(getString(R.string.text_set_null));
            return;
        }
        if (direction == -1) {
            showError(getString(R.string.text_direction_null));
            return;
        }
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(edtBarcode.getText().toString(), direction);
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

        if (productSetId == 0) {
            showError(getString(R.string.text_set_null));
            return;
        }
        if (direction == -1) {
            showError(getString(R.string.text_direction_null));
            return;
        }
        Intent launchIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
        getActivity().startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }


    @OnClick(R.id.tv_direction)
    public void chooseDirection() {
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
                                    (DirectionManager.getInstance().getListType());
                            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                                @Override
                                public void onSearchableItemClicked(Object item, int position) {
                                    DirectionManager.Direction direction1 = (DirectionManager.Direction) item;
                                    tvDirection.setText(direction1.getName());
                                    direction = direction1.getValue();
                                    mPresenter.getListDetailInSet(productSetId, direction);
                                }
                            });
                            searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                        }
                    })
                    .show();
        } else {
            SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                    (DirectionManager.getInstance().getListType());
            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                @Override
                public void onSearchableItemClicked(Object item, int position) {
                    DirectionManager.Direction direction1 = (DirectionManager.Direction) item;
                    tvDirection.setText(direction1.getName());
                    direction = direction1.getValue();
                    mPresenter.getListDetailInSet(productSetId, direction);

                }
            });
            searchableListDialog.show(getActivity().getFragmentManager(), TAG);
        }

    }
}
