package com.demo.barcode.screen.stages;

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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.utils.view.DateUtils;
import com.demo.barcode.R;
import com.demo.barcode.adapter.StagesAdapter;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.dialogs.ChangeIPAddressDialog;
import com.demo.barcode.dialogs.ChooseGroupDialog;
import com.demo.barcode.dialogs.DetailGroupDialog;
import com.demo.barcode.manager.TypeSOManager;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.barcodereader.BarcodeScanner;
import com.demo.barcode.widgets.barcodereader.BarcodeScannerActivity;
import com.demo.barcode.widgets.barcodereader.BarcodeScannerBuilder;
import com.demo.barcode.widgets.spinner.SearchableListDialog;
import com.demo.barcode.widgets.spinner.SearchableSpinner;
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

/**
 * Created by MSI on 26/11/2017.
 */

public class StagesFragment extends BaseFragment implements StagesContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = StagesFragment.class.getName();
    private StagesContract.Presenter mPresenter;
    private StagesAdapter adapter;
    public MediaPlayer mp1, mp2, mp3;
    private int times = 0;
    private int typeProduct;
    @Bind(R.id.tv_code_so)
    TextView tvCodeSO;

    @Bind(R.id.tv_times)
    TextView tvTimes;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.lv_code)
    RecyclerView rvCode;

    @Bind(R.id.tv_type_product)
    TextView tvTypeProduct;

    @Bind(R.id.tv_receiving_department)
    TextView tvDepartment;

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
                return;
            }
        }

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
                Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.KEY_CAPTURED_BARCODE);

                String contents = barcode.rawValue;
                String barcode2 = contents.replace("DEMO", "");
                Log.d(TAG, barcode2);
                mPresenter.checkBarcode(barcode2, departmentId, times, typeScan == 2);
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

        mPresenter.getAllListStages();
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
    public void showListDepartment(List<DepartmentEntity> list) {
        if (list.size() > 0) {
            tvDepartment.setOnClickListener(new View.OnClickListener() {
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
                                                DepartmentEntity departmentEntity = (DepartmentEntity) item;
                                                tvDepartment.setText(departmentEntity.getName());
                                                departmentId = departmentEntity.getId();
                                                tvTimes.setText(getString(R.string.text_choose_times_scan));
                                                times = 0;

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
                                DepartmentEntity departmentEntity = (DepartmentEntity) item;
                                tvDepartment.setText(departmentEntity.getName());
                                departmentId = departmentEntity.getId();
                                tvTimes.setText(getString(R.string.text_choose_times_scan));
                                times = 0;

                            }
                        });
                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                    }

                }
            });
        } else {
            tvDepartment.setOnClickListener(null);
        }
    }


    @Override
    public void showListLogScanStages(RealmResults<LogScanStages> parent) {

        adapter = new StagesAdapter(parent, new StagesAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(LogScanStages item) {
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

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rvCode.setLayoutManager(linearLayoutManager);
        rvCode.setHasFixedSize(true);
        rvCode.setAdapter(adapter);


    }

    @Override
    public void showListSO(List<SOEntity> list) {
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
                                                SOEntity soItem = (SOEntity) item;
                                                tvCodeSO.setText(soItem.getCodeSO());
                                                txtCustomerName.setText(soItem.getCustomerName());
                                                orderId = soItem.getOrderId();
                                                tvTimes.setText(getString(R.string.text_choose_times_scan));
                                                times = 0;
                                                tvDepartment.setText(getString(R.string.text_choose_receiving_department));
                                                departmentId = 0;
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
                                SOEntity soItem = (SOEntity) item;
                                tvCodeSO.setText(soItem.getCodeSO());
                                txtCustomerName.setText(soItem.getCustomerName());
                                orderId = soItem.getOrderId();
                                tvTimes.setText(getString(R.string.text_choose_times_scan));
                                times = 0;
                                tvDepartment.setText(getString(R.string.text_choose_receiving_department));
                                departmentId = 0;
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
    public void showCheckResidualInGroup(long id, int number, int numberInput) {
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
        if (list.size() > 0) {
            tvTimes.setOnClickListener(new View.OnClickListener() {
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
                                                Integer number = (Integer) item;
                                                tvTimes.setText(String.valueOf(number));
                                                times = number;
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
                                Integer number = (Integer) item;
                                tvTimes.setText(String.valueOf(number));
                                times = number;
                            }
                        });
                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                    }

                }
            });
        } else {
            tvTimes.setOnClickListener(null);
        }

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

    @Override
    public void refreshLayout() {
        rvCode.requestLayout();
    }

    @Override
    public void showPrintDeliveryNote(int id) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_do_you_want_print_delivery_note))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mPresenter.print(-1, id);

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
    public void showDialogCreateIPAddress(int idPrint) {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port, idPrint);
                dialog.dismiss();
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
        if (adapter != null && adapter.getItemCount() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_back_have_detail_waiting))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            // mPresenter.deleteAllItemLog();
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
        Intent launchIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
        getActivity().startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);

    }

    @OnClick(R.id.tv_type_product)
    public void chooseProduct() {

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
                                    (TypeSOManager.getInstance().getListType());
                            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                                @Override
                                public void onSearchableItemClicked(Object item, int position) {
                                    TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
                                    tvTypeProduct.setText(typeSO.getName());
                                    typeProduct = typeSO.getValue();
                                    tvTimes.setText(getString(R.string.text_choose_times_scan));
                                    times = 0;
                                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
                                    orderId = 0;
                                    tvDepartment.setText(getString(R.string.text_choose_receiving_department));
                                    departmentId = 0;
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
                    typeProduct = typeSO.getValue();
                    tvTimes.setText(getString(R.string.text_choose_times_scan));
                    times = 0;
                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
                    orderId = 0;
                    tvDepartment.setText(getString(R.string.text_choose_receiving_department));
                    departmentId = 0;
                    txtCustomerName.setText("");
                    mPresenter.getListSO(typeSO.getValue());
                }
            });
            searchableListDialog.show(getActivity().getFragmentManager(), TAG);
        }


    }

}
