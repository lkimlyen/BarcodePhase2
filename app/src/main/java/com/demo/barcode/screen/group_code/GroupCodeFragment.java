package com.demo.barcode.screen.group_code;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.barcode.R;
import com.demo.barcode.adapter.GroupCodeLVAdapter;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.TypeSOManager;
import com.demo.barcode.screen.capture.ScanActivity;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

/**
 * Created by MSI on 26/11/2017.
 */

public class GroupCodeFragment extends BaseFragment implements GroupCodeContract.View {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 332;
    private final String TAG = GroupCodeFragment.class.getName();
    public static final String ORDER_ID = "order_id";
    private GroupCodeContract.Presenter mPresenter;
    private GroupCodeLVAdapter lvAdapter;
    private Set<String> countersToSelect = new HashSet<String>();
    private List<RadioButton> radioButtonList = new ArrayList<>();
    private HashMap<String, List<ImageView>> deleteButtonList = new HashMap<>();
    private HashMap<Long, ProductGroupEntity> productUpdateList = new HashMap<>();
    private HashMap<String, List<EditText>> editTextList = new HashMap<>();
    private HashMap<String, List<TextView>> textViewList = new HashMap<>();

    private long orderId = 0;

    public MediaPlayer mp1, mp2;
    @Bind(R.id.ss_serial_module)
    SearchableSpinner ssModule;

    @Bind(R.id.tv_code_so)
    TextView tvCodeSO;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.tv_type_product)
    TextView tvTypeProduct;

    @Bind(R.id.lv_code)
    RecyclerView lvCode;
    @Bind(R.id.cb_all)
    CheckBox cbAll;

    @Bind(R.id.layoutContent)
    LinearLayout layoutContent;

    @Bind(R.id.btn_scan)
    Button btnScan;
    private Vibrator vibrate;


    public GroupCodeFragment() {
        // Required empty public constructor
    }


    public static GroupCodeFragment newInstance() {
        GroupCodeFragment fragment = new GroupCodeFragment();
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

                mPresenter.checkBarcode(barcode2);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_code, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);

        orderId = getActivity().getIntent().getIntExtra(ORDER_ID, 0);
        initView();
        return view;
    }

    private void initView() {
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);


        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (lvAdapter != null) {
                    lvAdapter.enableSelectMode(isChecked);
                } else {
                    cbAll.setChecked(false);
                }

            }
        });

    }

    @Override
    public void setPresenter(GroupCodeContract.Presenter presenter) {
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
    public void showListModule(List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        ssModule.setAdapter(adapter);
        ssModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //module = list.get(position);
                mPresenter.getListGroupCode(orderId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showGroupCode(HashMap<String, List<ProductGroupEntity>> groupCodes) {
        layoutContent.removeAllViews();
        for (Map.Entry<String, List<ProductGroupEntity>> map : groupCodes.entrySet()) {
            setLayoutGroup(map.getKey(), map.getValue());
        }
        if (groupCodes.size() > 0) {
            layoutContent.setVisibility(View.VISIBLE);

        } else {
            layoutContent.setVisibility(View.GONE);
        }
    }


    @Override
    public void showGroupCodeScanList(RealmResults<GroupCode> groupCodes) {
        lvAdapter = new GroupCodeLVAdapter(groupCodes, new GroupCodeLVAdapter.OnRemoveListener() {
            @Override
            public void onRemove(long id) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_code))
                        .setConfirmText(getString(R.string.text_ok))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.deleteScanGroupCode(id);
                            }
                        })
                        .setCancelText(getString(R.string.text_cancel))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }
        }, new GroupCodeLVAdapter.onClickEditTextListener() {
            @Override
            public void onClick() {

            }
        }, new GroupCodeLVAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(GroupCode item, int number) {
                mPresenter.updateNumberGroup(item.getProductDetailId(), item.getId(), number);
            }
        }, new GroupCodeLVAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {
                showSuccess(message);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        lvCode.setLayoutManager(linearLayoutManager);
        lvCode.setHasFixedSize(true);
        lvCode.setAdapter(lvAdapter);
        lvCode.setAdapter(lvAdapter);
    }

    private void setLayoutGroup(String groupCode, List<ProductGroupEntity> list) {
        LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.item_content_group, null);
        TextView txtTitle = (TextView) v.findViewById(R.id.txt_name_detail);
        RadioButton rbSelect = (RadioButton) v.findViewById(R.id.rb_select);
        LinearLayout layoutMain = (LinearLayout) v.findViewById(R.id.layoutMain);
        ImageButton btnEdit = (ImageButton) v.findViewById(R.id.btn_edit);
        btnEdit.setTag(groupCode);
        ImageButton btnSave = (ImageButton) v.findViewById(R.id.btn_save);
        btnSave.setTag(groupCode);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productUpdateList.clear();
                if (btnSave.getTag().equals(v.getTag())) {
                    btnSave.setVisibility(View.VISIBLE);
                }
                v.setVisibility(View.GONE);

                for (Map.Entry<String, List<ImageView>> map : deleteButtonList.entrySet()) {
                    if (map.getKey().equals(v.getTag())) {
                        for (ImageView imageButton : map.getValue()) {
                            imageButton.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
                for (Map.Entry<String, List<EditText>> map : editTextList.entrySet()) {
                    if (map.getKey().equals(v.getTag())) {
                        for (EditText editText : map.getValue()) {
                            editText.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
                for (Map.Entry<String, List<TextView>> map : textViewList.entrySet()) {
                    if (map.getKey().equals(v.getTag())) {
                        for (TextView textView : map.getValue()) {
                            textView.setVisibility(View.GONE);
                        }
                        break;
                    }
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnEdit.getTag().equals(v.getTag())) {
                    btnEdit.setVisibility(View.VISIBLE);
                }

                v.setVisibility(View.GONE);

                for (Map.Entry<String, List<ImageView>> map : deleteButtonList.entrySet()) {
                    if (map.getKey().equals(v.getTag())) {
                        for (ImageView imageButton : map.getValue()) {
                            imageButton.setVisibility(View.INVISIBLE);
                        }
                        break;
                    }
                }
                for (Map.Entry<String, List<EditText>> map : editTextList.entrySet()) {
                    if (map.getKey().equals(v.getTag())) {
                        for (EditText editText : map.getValue()) {
                            editText.setVisibility(View.GONE);
                        }
                        break;
                    }
                }
                for (Map.Entry<String, List<TextView>> map : textViewList.entrySet()) {
                    if (map.getKey().equals(v.getTag())) {
                        for (TextView textView : map.getValue()) {
                            textView.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
                if (productUpdateList.size() == 0) {
                    return;
                }
                mPresenter.updateNumberInGroup((String) v.getTag(), orderId, productUpdateList.values());

            }
        });
        rbSelect.setTag(groupCode);
        radioButtonList.add(rbSelect);

        rbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // rbSelect.getTag().equals(countersToSelect.iterator().next()))
                if (countersToSelect.size() > 0) {
                    for (RadioButton radioButton : radioButtonList) {
                        if (radioButton.getTag().equals(countersToSelect.iterator().next()) && !((RadioButton) v).getTag().equals(countersToSelect.iterator().next())) {
                            radioButton.setChecked(false);
                            break;
                        }
                    }
                }
                countersToSelect.clear();
                countersToSelect.add((String) rbSelect.getTag());
            }
        });
        txtTitle.setText(groupCode);

        List<ImageView> listDelete = new ArrayList<>();
        List<EditText> listEditText = new ArrayList<>();
        List<TextView> listTextView = new ArrayList<>();

        for (ProductGroupEntity product : list) {
            View view = inf.inflate(R.layout.item_code_in_group, null);
            TextView txtNameDetail = (TextView) view.findViewById(R.id.txt_name_detail);
            TextView txtNumberGroup = (TextView) view.findViewById(R.id.txt_number_group);
            EditText edtNumberGroup = (EditText) view.findViewById(R.id.edt_number_group);
            TextView txtNumberScan = (TextView) view.findViewById(R.id.txt_number_scan);
            TextView txtModule = (TextView) view.findViewById(R.id.txt_module);
            ImageView imgRemove = (ImageView) view.findViewById(R.id.btn_remove);
            txtNameDetail.setText(product.getProductDetailName());
            txtNumberGroup.setText(String.valueOf((int) product.getNumber()));
            edtNumberGroup.setText(String.valueOf((int) product.getNumber()));
            edtNumberGroup.setTag(product);
            txtNumberScan.setTag(product);
            txtModule.setText(product.getModule());
            txtNumberScan.setText(String.valueOf((int) product.getNumberTotal()));
            imgRemove.setTag(product);
            listDelete.add(imgRemove);
            listEditText.add(edtNumberGroup);
            listTextView.add(txtNumberGroup);
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.removeItemInGroup((ProductGroupEntity) v.getTag(), orderId);
                }
            });

            edtNumberGroup.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    try {
                        int numberInput = Integer.parseInt(s.toString());
                        ProductGroupEntity productGroupEntity = (ProductGroupEntity) edtNumberGroup.getTag();
                        if (numberInput <= 0) {
                            if (edtNumberGroup.getTag().equals(productGroupEntity)) {
                                edtNumberGroup.setText((int) productGroupEntity.getNumber() + "");
                            }
                            showError(CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                            return;
                        }

                        if (numberInput + mPresenter.totalNumberScanGroup(productGroupEntity.getProductDetailID()) > productGroupEntity.getNumberTotal()) {
                            if (edtNumberGroup.getTag().equals(productGroupEntity)) {
                                edtNumberGroup.setText((int) productGroupEntity.getNumber() + "");
                            }
                            showError(CoreApplication.getInstance().getText(R.string.text_number_group_bigger_number_total).toString());
                            return;
                        }

                        if (numberInput == productGroupEntity.getNumber()) {
                            productUpdateList.remove(productGroupEntity.getProductDetailID());
                            return;
                        }
                        ProductGroupEntity productGroupEntity1 = productGroupEntity;
                        productGroupEntity1.setNumber(numberInput);
                        productUpdateList.put(productGroupEntity1.getProductDetailID(), productGroupEntity1);
                    } catch (Exception e) {

                    }
                }
            });

            layoutMain.addView(view);
        }
        deleteButtonList.put(groupCode, listDelete);
        editTextList.put(groupCode, listEditText);
        textViewList.put(groupCode, listTextView);
        layoutContent.addView(v);
    }


    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    @Override
    public void showListSO(List<SOEntity> list) {
        if (list.size() > 0) {
            tvCodeSO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (adapter.getItemCount() > 0) {
//                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText(CoreApplication.getInstance().getString(R.string.text_title_noti))
//                                .setContentText(CoreApplication.getInstance().getString(R.string.text_back_have_detail_waiting))
//                                .setConfirmText(CoreApplication.getInstance().getString(R.string.text_yes))
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                        sweetAlertDialog.dismiss();
//                                        mPresenter.uploadData(orderId);
//                                    }
//                                })
//                                .setCancelText(CoreApplication.getInstance().getString(R.string.text_no))
//                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        mPresenter.deleteAllData();
//                                        sweetAlertDialog.dismiss();
//                                        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
//                                                (list);
//                                        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
//                                            @Override
//                                            public void onSearchableItemClicked(Object item, int position) {
//                                                SOEntity soItem = (SOEntity) item;
//                                                tvCodeSO.setText(soItem.getCodeSO());
//                                                txtCustomerName.setText(soItem.getCustomerName());
//                                                orderId = soItem.getOrderId();
//                                                mPresenter.getListProduct(orderId, false);
//                                            }
//                                        });
//                                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
//
//                                    }
//                                })
//                                .show();
//                    } else {
                        SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                                (list);
                        searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                            @Override
                            public void onSearchableItemClicked(Object item, int position) {
                                SOEntity soItem = (SOEntity) item;
                                tvCodeSO.setText(soItem.getCodeSO());
                                txtCustomerName.setText(soItem.getCustomerName());
                                orderId = soItem.getOrderId();
                                if (orderId > 0) {
                                    mPresenter.getListProductDetailInGroupCode(orderId);
                                    mPresenter.getListProduct(orderId);
                                    mPresenter.getGroupCodeScanList(orderId);
                                }

                            }
                        });
                        searchableListDialog.show(getActivity().getFragmentManager(), TAG);
                    //}

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
    public void refeshLayout() {
        lvCode.requestLayout();
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @OnClick(R.id.img_back)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_group_code)
    public void groupCode() {

        if (lvAdapter.getCountersToSelect().size() > 1 && countersToSelect.size() == 0) {
            mPresenter.groupCode(orderId, lvAdapter.getCountersToSelect());
            cbAll.setChecked(false);
            lvAdapter.enableSelectMode(false);
            countersToSelect.clear();
        } else if (lvAdapter.getCountersToSelect().size() > 0 && countersToSelect.size() > 0) {
            mPresenter.updateGroupCode(countersToSelect.iterator().next(), orderId, lvAdapter.getCountersToSelect());
            cbAll.setChecked(false);
            lvAdapter.enableSelectMode(false);
            countersToSelect.clear();
            for (RadioButton radioButton : radioButtonList) {
                if (radioButton.isChecked()) {
                    radioButton.setChecked(false);
                }
            }
        } else {
            showError(getString(R.string.text_not_product_upload));
        }
    }

    @OnClick(R.id.btn_detached_code)
    public void detachedCode() {
        if (countersToSelect.size() > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_want_detached_code_scan))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.dismiss();
                            mPresenter.detachedCode(orderId, countersToSelect.iterator().next());
                            countersToSelect.clear();
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
            showError(getString(R.string.text_not_product_detached));
        }
    }

    @OnClick(R.id.btn_save)
    public void save() {
        if (TextUtils.isEmpty(edtBarcode.getText().toString())) {
            return;
        }
        if (orderId == 0) {
            showError(getString(R.string.text_order_id_null));
            return;
        }

        mPresenter.checkBarcode(edtBarcode.getText().toString());
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
    @OnClick(R.id.tv_type_product)
    public void chooseProduct() {

//        if (adapter.getItemCount() > 0) {
//            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(CoreApplication.getInstance().getString(R.string.text_title_noti))
//                    .setContentText(CoreApplication.getInstance().getString(R.string.text_back_have_detail_waiting))
//                    .setConfirmText(CoreApplication.getInstance().getString(R.string.text_yes))
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                            sweetAlertDialog.dismiss();
//                            mPresenter.uploadData(orderId);
//                        }
//                    })
//                    .setCancelText(CoreApplication.getInstance().getString(R.string.text_no))
//                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            mPresenter.deleteAllData();
//                            sweetAlertDialog.dismiss();
//                            SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
//                                    (TypeSOManager.getInstance().getListType());
//                            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
//                                @Override
//                                public void onSearchableItemClicked(Object item, int position) {
//                                    TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
//                                    tvTypeProduct.setText(typeSO.getName());
//
//                                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
//                                    orderId = 0;
//
//                                    txtCustomerName.setText("");
//                                    mPresenter.getListSO(typeSO.getValue());
//                                }
//                            });
//                            searchableListDialog.show(getActivity().getFragmentManager(), TAG);
//
//                        }
//                    })
//                    .show();
//        } else {
            SearchableListDialog searchableListDialog = SearchableListDialog.newInstance
                    (TypeSOManager.getInstance().getListType());
            searchableListDialog.setOnSearchableItemClickListener(new SearchableListDialog.SearchableItem() {
                @Override
                public void onSearchableItemClicked(Object item, int position) {
                    TypeSOManager.TypeSO typeSO = (TypeSOManager.TypeSO) item;
                    tvTypeProduct.setText(typeSO.getName());

                    tvCodeSO.setText(getString(R.string.text_choose_code_so));
                    orderId = 0;

                    txtCustomerName.setText("");
                    mPresenter.getListSO(typeSO.getValue());
                }
            });
            searchableListDialog.show(getActivity().getFragmentManager(), TAG);
      //  }


    }
}

