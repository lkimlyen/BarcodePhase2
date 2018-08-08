package com.demo.architect.data.repository.base.other.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ResultEntity;

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

    private void handleDepartmentResponse(Call<BaseResponse<DepartmentEntity>> call, Subscriber subscriber) {
        try {
            BaseResponse<DepartmentEntity> response = call.execute().body();
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
    public Observable<BaseResponse<DepartmentEntity>> getListDepartment() {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse<DepartmentEntity>>() {
            @Override
            public void call(Subscriber<? super BaseResponse<DepartmentEntity>> subscriber) {
                handleDepartmentResponse(mRemoteApiInterface.getListDepartment(
                        server + "/WS/api/GD2GetListDepartment"), subscriber);
            }
        });
    }
}
