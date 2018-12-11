package com.demo.architect.data.repository.base.account.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UpdateAppResponse;
import com.demo.architect.data.model.UserEntity;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Skull on 04/01/2018.
 */

public class AuthRepositoryImpl implements AuthRepository {
    private final static String TAG = AuthRepositoryImpl.class.getName();
    private AuthApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public AuthRepositoryImpl(AuthApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;

    }

    private void handleLoginResponse(Call<BaseResponse<UserEntity>> call, Subscriber subscriber) {
        try {
            BaseResponse<UserEntity> response = call.execute().body();
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

    private void handleBaseResponse(Call<BaseListResponse> call, Subscriber subscriber) {
        try {
            BaseListResponse response = call.execute().body();
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

    private void handleUpdateResponse(Call<UpdateAppResponse> call, Subscriber subscriber) {
        try {
            UpdateAppResponse response = call.execute().body();
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

    private void handleStringResponse(Call<String> call, Subscriber subscriber) {
        try {
            String response = call.execute().body();
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
    public Observable<BaseResponse<UserEntity>> login(final String key, final String username, final String password) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse<UserEntity>>() {
            @Override
            public void call(Subscriber<? super BaseResponse<UserEntity>> subscriber) {
                handleLoginResponse(mRemoteApiInterface.login(
                        server + "/WS/api/GD2Login"
                        ,key, username, password), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse> changePassword(final String userId, final String oldPass, final String newPass) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse>() {
            @Override
            public void call(Subscriber<? super BaseListResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.changePassWord(
                        server + "/WS/api/ChangePassWord"
                        , userId, oldPass, newPass), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse> updateSoft(final String appCode, final long userId, final String version, final int numNotUpdate, final String dateServer, final String ime) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse>() {
            @Override
            public void call(Subscriber<? super BaseListResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.updateSoft(
                        server + "/WS/api/UpdateSoft"
                        ,appCode, userId, version,numNotUpdate, dateServer, ime), subscriber);
            }
        });
    }

    @Override
    public Observable<UpdateAppResponse> getUpdateVersionACR() {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<UpdateAppResponse>() {
            @Override
            public void call(Subscriber<? super UpdateAppResponse> subscriber) {
                handleUpdateResponse(mRemoteApiInterface.getUpdateVersionACR(server + "/WS/api/GD2GetUpdateVersion?pAppCode=ids"), subscriber);
            }
        });
    }

    @Override
    public Observable<String> getDateServer() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                handleStringResponse(mRemoteApiInterface.getDateServer(), subscriber);
            }
        });
    }


}
