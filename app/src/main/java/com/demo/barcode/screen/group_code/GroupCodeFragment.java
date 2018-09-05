package com.demo.barcode.screen.group_code;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.barcode.R;
import com.demo.barcode.adapter.GroupCodeContentAdapter;
import com.demo.barcode.adapter.GroupCodeLVAdapter;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.util.Precondition;
import com.demo.barcode.widgets.AnimatedExpandableListView;
import com.demo.barcode.widgets.spinner.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmList;
import io.realm.RealmResults;

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
    private int orderId = 0;
    private int departmentId = 0;
    private String module;
    private int times = 0;
    private boolean groupCode;
    @Bind(R.id.ss_serial_module)
    SearchableSpinner ssModule;

    @Bind(R.id.txt_code_so)
    TextView txtCodeSO;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.lv_code)
    ListView lvCode;

    @Bind(R.id.txt_title)
    TextView txtTitle;

    @Bind(R.id.btn_group_code)
    Button btnGroupCode;

    @Bind(R.id.cb_all)
    CheckBox cbAll;

    @Bind(R.id.layoutContent)
    LinearLayout layoutContent;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_code, container, false);
        ButterKnife.bind(this, view);
        orderId = getActivity().getIntent().getIntExtra(ORDER_ID, 0);
        departmentId = getActivity().getIntent().getIntExtra(DEPARTMENT_ID, 0);
        times = getActivity().getIntent().getIntExtra(TIMES, 0);
        groupCode = getActivity().getIntent().getBooleanExtra(GROUP_CODE, true);
        initView();
        return view;
    }

    private void initView() {
        if (!groupCode) {
            txtTitle.setText(getString(R.string.text_detached_code));
            btnGroupCode.setText(getString(R.string.text_detached_code));
        }
        ssModule.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;

            }
        });
        mPresenter.getListModule(orderId);
        mPresenter.getListOrderDetail(orderId);
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
                if (groupCode) {
                    mPresenter.getListGroupCode(orderId, departmentId, times, list.get(position));

                    mPresenter.getListScanStages(orderId, departmentId, times, list.get(position));
                } else {
                    mPresenter.getListGroupCode(orderId, departmentId, times, list.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListScanStages(RealmResults<LogScanStages> results) {
        lvAdapter = new GroupCodeLVAdapter(results, new GroupCodeLVAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanStages item, int number) {
                mPresenter.updateNumberGroup(item.getId(), number);
            }
        }, new GroupCodeLVAdapter.onErrorListener() {
            @Override
            public void errorListener(String message) {

            }
        });
        lvCode.setAdapter(lvAdapter);
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

    private void setLayout(ListGroupCode item, RealmList<LogScanStages> list) {
        LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.item_content_group, null);
        TextView txtTitle = (TextView) v.findViewById(R.id.txt_name_detail);
        RadioButton rbSelect = (RadioButton) v.findViewById(R.id.rb_select);
        rbSelect.setTag(item);
        radioButtonList.add(rbSelect);
        final ListView lvCode = (ListView) v.findViewById(R.id.lv_code);
        rbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // rbSelect.getTag().equals(countersToSelect.iterator().next()))
                if (countersToSelect.size() > 0) {
                    for (RadioButton radioButton : radioButtonList) {
                        if (radioButton.getTag().equals(countersToSelect.iterator().next())) {
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
            public void onRemove(ListGroupCode groupCode,LogScanStages item) {
                mPresenter.removeItemInGroup(groupCode, item, orderId, departmentId, times);
            }
        });

        islandContentAdapter.setListGroupCode(item);
        lvCode.setAdapter(islandContentAdapter);

        lvCode.post(new Runnable() {
            @Override
            public void run() {
                setListViewHeightBasedOnItems(lvCode);
            }
        });

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
    public void showSODetail(SOEntity soEntity) {
        txtCodeSO.setText(soEntity.getCodeSO());
        txtCustomerName.setText(soEntity.getCustomerName());
    }

    @Override
    public void backScanStages(String message) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Message", message);
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @OnClick(R.id.img_back)
    public void back() {
        if (lvAdapter != null) {
            if (lvAdapter.getCountersToSelect().size() > 0) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_cancel_group_code))
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
        } else {
            getActivity().finish();
        }

    }

    @OnClick(R.id.btn_group_code)
    public void groupCode() {
        if (groupCode) {
            if (lvAdapter.getCountersToSelect().size() > 1 && countersToSelect.size() == 0) {
                mPresenter.groupCode(orderId, departmentId, times, module, lvAdapter.getCountersToSelect());
            } else if (lvAdapter.getCountersToSelect().size() > 0 && countersToSelect.size() > 0) {
                mPresenter.updateGroupCode(countersToSelect.iterator().next(), orderId,
                        departmentId, times, lvAdapter.getCountersToSelect());
            } else {
                showError(getString(R.string.text_not_product_upload));
            }
        } else {
            if (countersToSelect.size() > 0) {
                mPresenter.detachedCode(orderId, departmentId, times, countersToSelect.iterator().next());
            } else {
                showError(getString(R.string.text_not_product_detached));
            }
        }

    }


}
