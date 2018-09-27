package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;

import rx.Observable;
import rx.Subscriber;

public class UpdateSoftUsecase extends BaseUseCase {
    private static final String TAG = UpdateSoftUsecase.class.getSimpleName();
    private final AuthRepository remoteRepository;

    public UpdateSoftUsecase(AuthRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        long userId = ((RequestValue) requestValues).userId;
        String version = ((RequestValue) requestValues).version;
        int numNotUpdate = ((RequestValue) requestValues).numNotUpdate;
        String dateServer = ((RequestValue) requestValues).dateServer;
        String ime = ((RequestValue) requestValues).ime;
        return remoteRepository.updateSoft("ids",userId, version, numNotUpdate, dateServer, ime);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.toString()));
                }
            }

            @Override
            public void onNext(BaseListResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {

                    if ( data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue());
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        public final long userId;
        public final String version;
        public final int numNotUpdate;
        public final String dateServer;
        public final String ime;


        public RequestValue(long userId, String version, int numNotUpdate, String dateServer, String ime) {
            this.userId = userId;
            this.version = version;
            this.numNotUpdate = numNotUpdate;
            this.dateServer = dateServer;
            this.ime = ime;
        }
    }

    public static final class ResponseValue implements ResponseValues {

    }

    public static final class ErrorValue implements ErrorValues {
        private String description;
        public ErrorValue(String description){
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
