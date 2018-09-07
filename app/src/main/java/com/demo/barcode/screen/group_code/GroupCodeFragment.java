package com.demo.barcode.screen.group_code;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.barcode.R;
import com.demo.barcode.adapter.GroupCodeContentAdapter;
import com.demo.barcode.adapter.GroupCodeLVAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.TypeSOManager;
import com.demo.barcode.screen.capture.ScanActivity;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.spinner.SearchableSpinner;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmList;
import io.realm.RealmResults;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

/**
 * Created by MSI on 26/11/2017.
 */

public class GroupCodeFragment extends BaseFragment implements GroupCodeContract.View {
    private final String TAG = GroupCodeFragment.class.getName();
    public static final String ORDER_ID = "order_id";
    public static final String DEPARTMENT_ID = "department_id";
    public static final String TIMES = "times";
    public static final String GROUP_CODE = "group_code";
    private GroupCodeContract.Presenter mPresenter;
    private GroupCodeLVAdapter lvAdapter;
    private Set<ListGroupCode> countersToSelect = new HashSet<ListGroupCode>();
    private List<RadioButton> radioButtonList = new ArrayList<>();
    private IntentIntegrator integrator = new IntentIntegrator(getActivity());
    private int orderId = 0;
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
                mPresenter.checkBarcode(barcode, module);
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
                mPresenter.getGroupCodeScanList(orderId, list.get(position));
                mPresenter.getListGroupCode(orderId, module);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void showGroupCode(RealmResults<ListGroupCode> groupCodes) {
        layoutContent.removeAllViews();
        for (ListGroupCode item : groupCodes) {
            setLayout(item, item.getList());
        }
        if (groupCodes.size() > 0) {
            layoutContent.setVisibility(View.VISIBLE);

        } else {
            layoutContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void showGroupCodeScanList(RealmResults<GroupCode> groupCodes) {
        lvAdapter = new GroupCodeLVAdapter(groupCodes, new GroupCodeLVAdapter.OnEditTextChangeListener() {
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

    private void setLayout(ListGroupCode item, RealmList<GroupCode> list) {
        LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.item_content_group, null);
        TextView txtTitle = (TextView) v.findViewById(R.id.txt_name_detail);
        RadioButton rbSelect = (RadioButton) v.findViewById(R.id.rb_select);
        EditText edtNumberGroup = (EditText) v.findViewById(R.id.edt_number_group);
        edtNumberGroup.setTag(item);
        edtNumberGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    mPresenter.updateNumberListGroup(((ListGroupCode)edtNumberGroup.getTag()).getId(),Integer.parseInt(editable.toString()) );
                }catch (NumberFormatException e){

                }


            }
        });
        edtNumberGroup.setText(String.valueOf(item.getNumber()));
        rbSelect.setTag(item);
        radioButtonList.add(rbSelect);
        final ListView lvCodes = (ListView) v.findViewById(R.id.lv_code);
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
                countersToSelect.add((ListGroupCode) rbSelect.getTag());

            }
        });
        txtTitle.setText(item.getGroupCode());
        GroupCodeContentAdapter islandContentAdapter = new GroupCodeContentAdapter(list, new GroupCodeContentAdapter.OnRemoveListener() {
            @Override
            public void onRemove(GroupCode item) {
                mPresenter.removeItemInGroup(item.getGroupCode(),item,orderId,module);
            }
        });

        lvCodes.setAdapter(islandContentAdapter);

        lvCodes.postDelayed(new Runnable() {
            @Override
            public void run() {
                setListViewHeightBasedOnItems(lvCodes);
            }
        },1000);

//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//        param.gravity = Gravity.CENTER;
//        v.setLayoutParams(param);
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
                    mPresenter.getListProduct(orderId);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void backScanStages(String message) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Message", message);
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
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
        } else if (lvAdapter.getCountersToSelect().size() > 0 && countersToSelect.size() > 0) {
            mPresenter.updateGroupCode(countersToSelect.iterator().next(), orderId,
                    module, lvAdapter.getCountersToSelect());
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
            mPresenter.detachedCode(orderId, module, countersToSelect.iterator().next());
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

        if (TextUtils.isEmpty(module)) {
            showError(getString(R.string.text_module_is_empty));
            return;
        }
        mPresenter.checkBarcode(edtBarcode.getText().toString(), module);
    }

    @OnClick(R.id.btn_scan)
    public void scan() {
        if (orderId == 0) {
            showError(getString(R.string.text_order_id_null));
            return;
        }

        if (TextUtils.isEmpty(module)) {
            showError(getString(R.string.text_module_is_empty));
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

