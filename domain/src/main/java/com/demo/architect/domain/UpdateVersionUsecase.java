package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.UpdateAppResponse;
import com.demo.architect.data.model.UpdateAppResponse;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class UpdateVersionUsecase extends BaseUseCase<UpdateAppResponse>  {
    private static final String TAG = UpdateVersionUsecase.class.getSimpleName();
    private final AuthRepository remoteRepository;

    public UpdateVersionUsecase(AuthRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<UpdateAppResponse>  buildUseCaseObservable() {
        return remoteRepository.getUpdateVersionACR();
    }


    @Override
    protected DisposableObserver<UpdateAppResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<UpdateAppResponse>() {
            @Override
            public void onNext(UpdateAppResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    String result = data.getLink();
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

    }

    public static final class ResponseValue implements ResponseValues {
        private String link;

        public ResponseValue(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
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
