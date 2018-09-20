package com.demo.barcode.screen.detail_error;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.offline.DetailError;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.barcode.R;
import com.demo.barcode.adapter.ImageAdapter;
import com.demo.barcode.adapter.ReasonAdapter;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.util.Precondition;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailErrorFragment extends BaseFragment implements DetailErrorContract.View {
    private final String TAG = DetailErrorFragment.class.getName();
    private DetailErrorContract.Presenter mPresenter;
    public static final int REQUEST_CODE_PICK_IMAGE = 666;
    public static final int REQUEST_CODE_TAKE_IMAGE = 667;
    private ImageAdapter imAdapter;
    public MediaPlayer mp1, mp2;
    private Vibrator vibrate;
    private String mCurrentPhotoPath;
    private ReasonAdapter rsAdapter;
    private boolean edit;
    private int id;

    @Bind(R.id.txt_barcode)
    TextView txtBarcode;

    @Bind(R.id.txt_serial_module)
    TextView txtModule;

    @Bind(R.id.txt_name_detail)
    TextView txtNameDetail;

    @Bind(R.id.txt_number_order)
    TextView txtNumberOrder;

    @Bind(R.id.edt_number_failed)
    EditText edtNumberFailed;

    @Bind(R.id.rv_image)
    RecyclerView rvImage;

    @Bind(R.id.gv_reason)
    GridView gvReason;

    @Bind(R.id.edt_description)
    EditText edtDescription;

    public DetailErrorFragment() {
        // Required empty public constructor
    }

    public static DetailErrorFragment newInstance() {
        DetailErrorFragment fragment = new DetailErrorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_IMAGE) {
                if (mCurrentPhotoPath != null) {
                    mPresenter.addImage(id, mCurrentPhotoPath);
                }
            }

            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                Uri uri  = data.getData();
                mPresenter.addImage(id, getPathFromURI(uri));
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_error, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);

        id = getActivity().getIntent().getIntExtra("qc_id", 0);
        initView();
        return view;
    }

    private void initView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvImage.setLayoutManager(layoutManager);
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        edtNumberFailed.addTextChangedListener(new TextWatcher() {
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
                    if (numberInput <= 0) {
                        edtNumberFailed.setText("1");
                        Toast.makeText(getContext(), getString(R.string.text_number_bigger_zero), Toast.LENGTH_SHORT).show();
                        return;

                    }
                    if (numberInput > Integer.parseInt(txtNumberOrder.getText().toString())) {
                        edtNumberFailed.setText(txtNumberOrder.getText().toString());
                        Toast.makeText(getContext(), getString(R.string.text_number_bigger_number_order), Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (Exception e) {

                }
            }
        });
        mPresenter.getListReason(id);
        mPresenter.getDetailQualityControl(id);
    }


    @Override
    public void setPresenter(DetailErrorContract.Presenter presenter) {
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
    public void turnOnVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrate.vibrate(500);
        }
    }

    @Override
    public void showImageError(RealmList<ImageModel> imageModels) {
        imAdapter = new ImageAdapter(imageModels, new ImageAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(int id) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_image))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.deleteImage(id);
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
        });
        rvImage.setAdapter(imAdapter);

    }

    @Override
    public void showListReason(List<ReasonsEntity> list) {
        rsAdapter = new ReasonAdapter(getContext(), list);
        gvReason.setAdapter(rsAdapter);
        gvReason.post(new Runnable() {
            @Override
            public void run() {
                setGridViewHeightBasedOnChildren(gvReason, 2);
            }
        });
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    @Override
    public void showDetailQualityControl(QualityControlModel qualityControlModel) {
        txtBarcode.setText(qualityControlModel.getBarcode());
        txtModule.setText(qualityControlModel.getModule());
        txtNameDetail.setText(qualityControlModel.getProductName());
        txtNumberOrder.setText(String.valueOf(qualityControlModel.getTotalNumber()));
        edtNumberFailed.setText(String.valueOf(qualityControlModel.getNumber()));
        edtDescription.setText(qualityControlModel.getDescription());
        edit = qualityControlModel.isEdit();
    }

    @Override
    public void showUpdateListCounterSelect(RealmList<Integer> integerRealmList) {
        rsAdapter.setListCounterSelect(integerRealmList);
    }

    @Override
    public void goBackQualityControl() {
        Intent returnIntent = new Intent();
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
        if (edit) {
            if (imAdapter.getItemCount() == 0) {
                showError(getString(R.string.text_no_image_in_detail));
                return;
            }
        }
        getActivity().finish();
    }

    @OnClick(R.id.txt_add_image)
    public void addImage() {
        showAlertDialogImage(REQUEST_CODE_PICK_IMAGE, REQUEST_CODE_TAKE_IMAGE);
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
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showAlertDialogImage(int codePickImage, int codeTakeImage) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.text_add_image));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.text_pick_picture),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        getActivity().startActivityForResult(photoPickerIntent, codePickImage);
                        dialogInterface.dismiss();
                    }
                });
        builder.setNegativeButton(getString(R.string.text_take_picture),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dispatchTakePictureIntent(codeTakeImage);
                        dialog.dismiss();
                    }
                });
        builder.setNeutralButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(int codeTakeImage) {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.demo.barcode.fileprovider",
                        photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(takePicture, codeTakeImage);
            }
        }
    }

    @OnClick(R.id.btn_save)
    public void save() {
        if (imAdapter == null) {
            showError(getString(R.string.text_no_image_in_detail));
            return;
        }

        if (imAdapter.getItemCount() == 0) {
            showError(getString(R.string.text_no_image_in_detail));
            return;
        }

        if (rsAdapter.getCountersToSelect().size() == 0) {
            showError(getString(R.string.text_no_reason_selected));
            return;
        }

        if (TextUtils.isEmpty(edtDescription.getText().toString())) {
            showError(getString(R.string.text_no_description));
            return;
        }

        if (TextUtils.isEmpty(edtNumberFailed.getText().toString())) {
            showError(getString(R.string.text_no_number_failed));
            return;
        }

        if (TextUtils.isEmpty(edtNumberFailed.getText().toString())) {
            showError(getString(R.string.text_no_number_failed));
            return;
        }

        if (Integer.parseInt(edtNumberFailed.getText().toString()) == 0) {
            showError(getString(R.string.text_number_failed_bigger_zero));
            return;
        }
        mPresenter.save(id, Integer.parseInt(edtNumberFailed.getText().toString()), edtDescription.getText().toString(), rsAdapter.getCountersToSelect());
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
