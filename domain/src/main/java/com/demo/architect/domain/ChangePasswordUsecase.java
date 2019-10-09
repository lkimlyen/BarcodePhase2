package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class ChangePasswordUsecase extends BaseUseCase<BaseListResponse> {
    private static final String TAG = ChangePasswordUsecase.class.getSimpleName();
    private final AuthRepository remoteRepository;

    public ChangePasswordUsecase(AuthRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse> buildUseCaseObservable() {
        long userId = ((RequestValue) requestValues).userId;
        String oldPass = ((RequestValue) requestValues).oldPass;
        String newPass = ((RequestValue) requestValues).newPass;
        return remoteRepository.changePassword(userId+"", oldPass, newPass);
    }

    @Override
    protected DisposableObserver<BaseListResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse>() {
            @Override
            public void onNext(BaseListResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    String result = data.getDescription();
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
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
                    useCaseCallback.onError(new ErrorValue(e.getMessage()));
                }
            }
        };
    }


    public static final class RequestValue implements RequestValues {
        public final long userId;
        public final String oldPass;
        public final String newPass;

        public RequestValue(long userId, String oldPass, String newPass) {
            this.userId = userId;
            this.oldPass = oldPass;
            this.newPass = newPass;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private String description;
        public ResponseValue(String description){
            this.description = description;
        }

        public String getDescription() {
            return description;
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
