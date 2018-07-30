package com.demo.architect.data.repository.base.remote;

//import javax.inject.Singleton;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by uyminhduc on 10/16/16.
 */
//@Singleton
public class RemoteRepositoryImpl implements RemoteRepository {

    private final static String TAG = RemoteRepositoryImpl.class.getName();

    private RemoteApiInterface mRemoteApiInterface;

    public RemoteRepositoryImpl(RemoteApiInterface mRemoteApiInterface) {
        this.mRemoteApiInterface = mRemoteApiInterface;
    }


//    private void handleLinkContactResponse(Call<BaseResponse<LinkContactEntity>> call, Subscriber subscriber) {
//        try {
//            BaseResponse<LinkContactEntity> response = call.execute().body();
//            if (!subscriber.isUnsubscribed()) {
//                if (response != null) {
//                    subscriber.onNext(response);
//                } else {
//                    subscriber.onError(new Exception("Network Error!"));
//                }
//                subscriber.onCompleted();
//            }
//        } catch (Exception e) {
//            if (!subscriber.isUnsubscribed()) {
//                subscriber.onError(e);
//                subscriber.onCompleted();
//            }
//        }
//    }
//
//    @Override
//    public Observable<BaseResponse<LinkContactEntity>> linkContact() {
//        return Observable.create(new Observable.OnSubscribe<BaseResponse<LinkContactEntity>>() {
//            @Override
//            public void call(Subscriber<? super BaseResponse<LinkContactEntity>> subscriber) {
//                handleLinkContactResponse(mRemoteApiInterface.linkContact(), subscriber);
//            }
//        });
//    }
}


