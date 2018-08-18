package com.demo.barcode.widgets.spinner;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SearchableSpinner extends android.support.v7.widget.AppCompatSpinner implements View.OnTouchListener,
        SearchableListDialog.SearchableItem {

    public static final int NO_ITEM_SELECTED = -1;
    private Context _context;
    private List _items;
    private SearchableListDialog _searchableListDialog;
    private boolean _isDirty;
    private ArrayAdapter _arrayAdapter;
    private String _strHintText;
    private boolean _isFromInit;
    private OnClickListener listener;
    private OnUploadDataListener uploadDataListener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }


    public SearchableSpinner(Context context) {
        super(context);
        this._context = context;
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SearchableSpinner_hintText) {
                _strHintText = a.getString(attr);
            }
        }
        a.recycle();
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init();
    }

    private void init() {
        _items = new ArrayList();
        _searchableListDialog = SearchableListDialog.newInstance
                (_items);
        _searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);

        _arrayAdapter = (ArrayAdapter) getAdapter();
        if (!TextUtils.isEmpty(_strHintText)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout
                    .simple_list_item_1, new String[]{_strHintText});
            _isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (_searchableListDialog.isAdded()) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (listener.onClick()) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(CoreApplication.getInstance().getString(R.string.text_title_noti))
                        .setContentText(CoreApplication.getInstance().getString(R.string.text_back_have_detail_waiting))
                        .setConfirmText(CoreApplication.getInstance().getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                uploadDataListener.uploadData();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelText(CoreApplication.getInstance().getString(R.string.text_no))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                if (null != _arrayAdapter) {

                                    // Refresh content #6
                                    // Change Start
                                    // Description: The items were only set initially, not reloading the data in the
                                    // spinner every time it is loaded with items in the adapter.
                                    _items.clear();
                                    for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                                        _items.add(_arrayAdapter.getItem(i));
                                    }
                                    // Change end.

                                    _searchableListDialog.show(scanForActivity(_context).getFragmentManager(), "TAG");
                                }

                            }
                        })
                        .show();
                // return true;
            } else {
                if (null != _arrayAdapter) {

                    // Refresh content #6
                    // Change Start
                    // Description: The items were only set initially, not reloading the data in the
                    // spinner every time it is loaded with items in the adapter.
                    _items.clear();
                    for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                        _items.add(_arrayAdapter.getItem(i));
                    }
                    // Change end.

                    _searchableListDialog.show(scanForActivity(_context).getFragmentManager(), "TAG");
                }
            }

        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {

        if (!_isFromInit) {
            _arrayAdapter = (ArrayAdapter) adapter;
            if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout
                        .simple_list_item_1, new String[]{_strHintText});
                super.setAdapter(arrayAdapter);
            } else {
                super.setAdapter(adapter);
            }

        } else {
            _isFromInit = false;
            super.setAdapter(adapter);
        }
    }

    @Override
    public void onSearchableItemClicked(Object item, int position) {
        setSelection(_arrayAdapter.getPosition(item));

        if (!_isDirty) {
            _isDirty = true;
            setAdapter(_arrayAdapter);
            setSelection(_arrayAdapter.getPosition(item));
        }
    }

    public void closeSpinnerSearch() {
        _searchableListDialog.dismiss();
    }

    public void setTitle(String strTitle) {
        _searchableListDialog.setTitle(strTitle);
    }

    public void setPositiveButton(String strPositiveButtonText) {
        _searchableListDialog.setPositiveButton(strPositiveButtonText);
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        _searchableListDialog.setPositiveButton(strPositiveButtonText, onClickListener);
    }

    public void setOnSearchTextChangedListener(SearchableListDialog.OnSearchTextChanged onSearchTextChanged) {
        _searchableListDialog.setOnSearchTextChangedListener(onSearchTextChanged);
    }

    private Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }

    public void setUploadDataListener(OnUploadDataListener uploadDataListener) {
        this.uploadDataListener = uploadDataListener;
    }

    public interface OnClickListener {
        boolean onClick();
    }

    public interface OnUploadDataListener {
        void uploadData();
    }
}
