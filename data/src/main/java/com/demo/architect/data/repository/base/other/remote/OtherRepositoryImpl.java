package com.demo.architect.data.repository.base.other.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.UploadEntity;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Skull on 04/01/2018.
 */

public class OtherRepositoryImpl implements OtherRepository {
    private final static String TAG = OtherRepositoryImpl.class.getName();

    private OtherApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public OtherRepositoryImpl(OtherApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
    }

    private void handleDepartmentResponse(Call<BaseListResponse<DepartmentEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<DepartmentEntity> response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleBaseResponse(Call<BaseResponse> call, Subscriber subscriber) {
        try {
            BaseResponse response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleUploadResponse(Call<BaseResponse<UploadEntity>> call, Subscriber subscriber) {
        try {
            BaseResponse<UploadEntity> response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }
    private void handleReasonResponse(Call<BaseListResponse<ReasonsEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ReasonsEntity> response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    @Override
    public Observable<BaseListResponse<DepartmentEntity>> getListDepartment() {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<DepartmentEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<DepartmentEntity>> subscriber) {
                handleDepartmentResponse(mRemoteApiInterface.getListDepartment(
                        server + "/WS/api/GD2GetListDepartment"), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ReasonsEntity>> getRAndSQC() {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ReasonsEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ReasonsEntity>> subscriber) {
                handleReasonResponse(mRemoteApiInterface.getRAndSQC(
                        server + "/WS/api/GD2GetRAndSQC"), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addLogQC(final String key, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.addLogQC(
                        server + "/WS/api/GD2AddLogQC",key,json), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse<UploadEntity>> uploadImage(final File file, final String key, final int orderId, final int departmentId, final String fileName, final String userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse<UploadEntity>>() {
            @Override
            public void call(Subscriber<? super BaseResponse<UploadEntity>> subscriber) {

                handleUploadResponse(mRemoteApiInterface.uploadImage( MultipartBody.Part
                                .createFormData("file", file.getName(),
                                        RequestBody.create(MultipartBody.FORM, file)),
                        RequestBody.create(MultipartBody.FORM, key),
                        RequestBody.create(MultipartBody.FORM, orderId+""),
                        RequestBody.create(MultipartBody.FORM, departmentId+""),
                        RequestBody.create(MultipartBody.FORM,fileName),
                        RequestBody.create(MultipartBody.FORM,userId)), subscriber);
            }
        });
    }
}
