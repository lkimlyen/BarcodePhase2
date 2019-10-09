package com.demo.barcode.screen.setting;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.UpdateVersionUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;
import com.demo.barcode.util.DownloadUtils;

import java.io.File;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private final String TAG = SettingPresenter.class.getName();
    private final SettingContract.View view;
    private final UpdateVersionUsecase updateVersionUsecase;
    private BroadcastReceiver mUpdateReceiver;
    @Inject
    LocalRepository localRepository;

    @Inject
    SettingPresenter(@NonNull SettingContract.View view, UpdateVersionUsecase updateVersionUsecase) {
        this.view = view;
        this.updateVersionUsecase = updateVersionUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        getVersion();
        getIPAddress();

    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void updateApp() {
        view.showProgressBar();
        updateVersionUsecase.executeIO(new UpdateVersionUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<UpdateVersionUsecase.ResponseValue, UpdateVersionUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateVersionUsecase.ResponseValue successResponse) {
                        String fileName = successResponse.getLink().substring(successResponse.getLink().lastIndexOf('/') + 1);
                        File mDownloadDir = CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        if (!mDownloadDir.exists()) {
                            mDownloadDir.mkdirs();
                        }
                        AndroidNetworking.download(successResponse.getLink(), mDownloadDir.getPath(), fileName)
                                .build()
                                .startDownload(new DownloadListener() {
                                    @Override
                                    public void onDownloadComplete() {
                                        view.hideProgressBar();
                                        File file = new File(mDownloadDir.getPath(), fileName);
                                        if (!file.exists()) {
                                            file.mkdirs();
                                        }

                                        view.installApp(file.getPath());
                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        view.hideProgressBar();
                                        view.showError(error.getErrorDetail());
                                    }
                                });
                    }

                    @Override
                    public void onError(UpdateVersionUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();

                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public String getVersion() {
        PackageManager manager = CoreApplication.getInstance().getPackageManager();
        PackageInfo info;
        String version = "";
        try {
            info = manager.getPackageInfo(
                    CoreApplication.getInstance().getPackageName(), 0);
            view.showVersion(info.versionName);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    public void saveIPAddress(String ipAddress, int port) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                view.showIPAddress(address);
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
            }
        });
    }

    @Override
    public void getIPAddress() {
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                view.showIPAddress(address);
            }
        });
    }

    @Override
    public void cloneDataAndSendMail() {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        String dataPath = ConvertUtils.exportRealmFile();
        if (!dataPath.equals("")) {
            view.uploadFile(dataPath,user.getId(), user.getName());
        }else {
            view.showError(CoreApplication.getInstance().getString(R.string.text_backup_fail));
            view.hideProgressBar();
        }
    }

    @Override
    public void deleteDataLocal() {
        localRepository.deleteDataLocal().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_all_data_offline_success));
            }
        });
    }
}
