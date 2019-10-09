package com.demo.architect.domain;

import android.text.TextUtils;
import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetDateServerUsecase extends BaseUseCase<String> {
    private static final String TAG = GetDateServerUsecase.class.getSimpleName();
    private final AuthRepository remoteRepository;

    public GetDateServerUsecase(AuthRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<String> buildUseCaseObservable() {
        return remoteRepository.getDateServer();
    }


    @Override
    protected DisposableObserver<String> buildUseCaseSubscriber() {
        return new DefaultObserver<String>() {
            @Override
            public void onNext(String data) {
                if (useCaseCallback != null) {
                    if (!TextUtils.isEmpty(data)) {
                        useCaseCallback.onSuccess(new ResponseValue(data.toString()));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data));
                    }
                }

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.toString()));
                }
            }
        };
    }
    public static final class RequestValue implements RequestValues {

    }

    public static final class ResponseValue implements ResponseValues {
        private String date;

        public ResponseValue(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }
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
