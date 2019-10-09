package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class LoginUsecase extends BaseUseCase<BaseResponse<UserEntity>>  {
    private static final String TAG = LoginUsecase.class.getSimpleName();
    private final AuthRepository remoteRepository;

    public LoginUsecase(AuthRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<UserEntity>>  buildUseCaseObservable() {
        String userName = ((RequestValue) requestValues).userName;
        String password = ((RequestValue) requestValues).password;
        return remoteRepository.login("13AKby8uFhdlayHD6oPsaU90b8o00=",userName, password);
    }


    @Override
    protected DisposableObserver<BaseResponse<UserEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<UserEntity>>() {
            @Override
            public void onNext(BaseResponse<UserEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    UserEntity result = data.getData();
                    if (result != null && data.getStatus() == 1) {
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
        public final String userName;
        public final String password;

        public RequestValue(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private UserEntity entity;

        public ResponseValue(UserEntity entity) {
            this.entity = entity;
        }

        public UserEntity getEntity() {
            return entity;
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
