package com.demo.barcode.screen.group_code;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.demo.barcode.widgets.spinner.SearchableSpinner;
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

    private IntentIntegrator integrator = new IntentIntegrator(getActivity());
    private long orderId = 0;
    private String module;

    public MediaPlayer mp1, mp2;
    @Bind(R.id.ss_serial_module)
    SearchableSpinner ssModule;

    @Bind(R.id.ss_code_so)
    SearchableSpinner ssCodeSO;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.ss_type_product)
    SearchableSpinner ssTypeProduct;

    @Bind(R.id.lv_code)
    ListView lvCode;
    @Bind(R.id.cb_all)
    CheckBox cbAll;

    @Bind(R.id.layoutContent)
    LinearLayout layoutContent;
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

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String contents = data.getStringExtra(Constants.KEY_SCAN_RESULT);
                String barcode = contents.replace("DEMO", "");
                mPresenter.checkBarcode(barcode);
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
        ssModule.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });
        ssCodeSO.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
//                if (mPresenter.countLogScanStages(orderId, departmentId, times) > 0) {
//                    return true;
//                }
                return false;
            }
        });

        ssCodeSO.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
            @Override
            public void uploadData() {
                // uploadData();
            }
        });

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
        ssTypeProduct.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
//                if (mPresenter.countLogScanStages(orderId, departmentId, times) > 0) {
//                    return true;
//                }
                return false;
            }
        });
        ssTypeProduct.setUploadDataListener(new SearchableSpinner.OnUploadDataListener() {
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
        showNotification(message, SweetAlertDialog.ERROR_TYPE);
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
                module = list.get(position);
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
                new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE)
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
        lvCode.setAdapter(lvAdapter);
        lvCode.post(new Runnable() {
            @Override
            public void run() {
                setListViewHeightBasedOnItems(lvCode);
            }
        });
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
            txtNumberGroup.setText(String.valueOf((int)product.getNumber()));
            edtNumberGroup.setText(String.valueOf((int)product.getNumber()));
            edtNumberGroup.setTag(product);
            txtNumberScan.setTag(product);
            txtModule.setText(product.getModule());
            txtNumberScan.setText(String.valueOf((int)product.getNumberTotal()));
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
                                edtNumberGroup.setText((int)productGroupEntity.getNumber() + "");
                            }
                            showError(CoreApplication.getInstance().getText(R.string.text_number_bigger_zero).toString());
                            return;
                        }

                        if (numberInput > productGroupEntity.getNumberTotal()) {
                            if (edtNumberGroup.getTag().equals(productGroupEntity)) {
                                edtNumberGroup.setText((int)productGroupEntity.getNumber() + "");
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
        ArrayAdapter<SOEntity> adapter = new ArrayAdapter<SOEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssCodeSO.setAdapter(adapter);
        ssCodeSO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCustomerName.setText(list.get(position).getCustomerName());
                orderId = list.get(position).getOrderId();
                if (orderId > 0) {
                    mPresenter.getListProductDetailInGroupCode(orderId);
                    mPresenter.getListProduct(orderId);
                    mPresenter.getGroupCodeScanList(orderId);
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
    public void setHeightListView() {
        setListViewHeightBasedOnItems(lvCode);
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
        if (TextUtils.isEmpty(module)) {
            showError(getString(R.string.text_module_is_empty));
            return;
        }
        if (lvAdapter.getCountersToSelect().size() > 1 && countersToSelect.size() == 0) {
            mPresenter.groupCode(orderId, module, lvAdapter.getCountersToSelect());
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
        if (TextUtils.isEmpty(module)) {
            showError(getString(R.string.text_module_is_empty));
            return;
        }
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

