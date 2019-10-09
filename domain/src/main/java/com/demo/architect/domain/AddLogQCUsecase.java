package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UploadEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by ACER on 7/26/2017.
 */

public class AddLogQCUsecase extends BaseUseCase<BaseResponse> {
    private static final String TAG = AddLogQCUsecase.class.getSimpleName();
    private final OtherRepository otherRepository;

    public AddLogQCUsecase(OtherRepository otherRepository) {
        this.otherRepository = otherRepository;
    }

    @Override
    protected Observable<BaseResponse> buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        return otherRepository.addLogQC(Constants.KEY, json);
    }

    @Override
    protected DisposableObserver<BaseResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse data) {
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
        private final String json;

        public RequestValue( String json) {
            this.json = json;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private final String json;

        public ResponseValue(String json) {
            this.json = json;
        }
    }

    public static final class ErrorValue implements ErrorValues {

        private final String Description;

        public ErrorValue(String description) {
            Description = description;
        }

        public String getDescription() {
            return Description;
        }
    }

}
