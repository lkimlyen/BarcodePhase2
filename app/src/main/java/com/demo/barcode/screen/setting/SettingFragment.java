package com.demo.barcode.screen.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.utils.view.DateUtils;
import com.demo.barcode.R;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.dialogs.ChangeIPAddressDialog;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.screen.chang_password.ChangePasswordActivity;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.Precondition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class SettingFragment extends BaseFragment implements SettingContract.View {
    private final String TAG = SettingFragment.class.getName();

    private SettingContract.Presenter mPresenter;
    private IPAddress mModel;
    private StorageReference storageRef;
    private FirebaseAuth auth;

    @Bind(R.id.txt_version)
    TextView txtVersion;

    @Bind(R.id.btn_change_ip_address)
    Button btnChangeIp;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        UserEntity user = UserManager.getInstance().getUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        if (auth.getCurrentUser() == null) {
            String email = getString(R.string.text_email);
            String password = getString(R.string.text_password);
            auth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // there was an error
                                Log.d(TAG, "Login fail");

                            } else {
                                storageRef = storage.getReference();
                                Log.d(TAG, "Success");
                            }
                        }
                    });

        } else {
            storageRef = storage.getReference();
        }
        return view;
    }


    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
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

    @OnClick(R.id.btn_check_update)
    public void checkUpdate() {
        mPresenter.updateApp();
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
    public void installApp(String path) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//        Uri uriApk = FileProvider.getUriForFile(getContext(),
//                "com.demo.barcode.fileprovider",
//                new File(path));

        Intent install = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (!getContext().getPackageManager().canRequestPackageInstalls()) {
                startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:com.demo.barcode")));
            }
            install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        } else {
            install = new Intent(Intent.ACTION_VIEW);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        }
        install.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivityForResult(install, 333);

    }

    @Override
    public void showVersion(String version) {
        txtVersion.setText(String.format(getString(R.string.text_version), version));
    }

    @Override
    public void showIPAddress(IPAddress model) {
        mModel = model;
    }

    @Override
    public void showSuccess(String message) {

        showNotification(message, SweetAlertDialog.SUCCESS_TYPE);

    }

    @Override
    public void showError(String message) {
        showNotification(message, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void uploadFile(String path, long userId, String userName) {


        UploadTask uploadTask;
        Uri file = Uri.fromFile(new File(path));
        String dateCurrent = DateUtils.getShortDateCurrent();
        if (storageRef != null) {
            StorageReference riversRef = storageRef.child(userId + "_" + userName + "_" + dateCurrent + "/" + ConvertUtils.getTimeMillis() + file.getLastPathSegment());
            uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    if (isAdded()) {
                        showError(getString(R.string.text_backup_fail));
                        Log.d(TAG, exception.getMessage());
                        hideProgressBar();
                    }

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (isAdded()) {
                        showSuccess(getString(R.string.text_backup_success));
                        hideProgressBar();
                    }

                }
            });
        } else {
            showError(getString(R.string.text_backup_fail));
            hideProgressBar();
        }

    }


    @OnClick(R.id.btn_change_ip_address)
    public void changeIPAddress() {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port);
                dialog.dismiss();
            }
        });
        if (mModel != null) {
            dialog.setContent(mModel.getIpAddress(), String.valueOf(mModel.getPortNumber()));
        }
    }

    @OnClick(R.id.btn_change_password)
    public void changePassword() {
        ChangePasswordActivity.start(getActivity());
    }

    public void showDialogChangePassSuccess() {
        showNotification(getString(R.string.text_change_password_succes), SweetAlertDialog.SUCCESS_TYPE);
    }

    @OnClick(R.id.img_back)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_clone)
    public void cloneData() {
        mPresenter.cloneDataAndSendMail();
    }

    @OnClick(R.id.btn_clear)
    public void deleteAllDataLocal() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText("Bạn có muốn xóa toàn bộ offline không?")
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                        mPresenter.deleteDataLocal();
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
}
