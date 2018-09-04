package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UploadEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ACER on 7/26/2017.
 */

public class AddLogQCUsecase extends BaseUseCase {
    private static final String TAG = AddLogQCUsecase.class.getSimpleName();
    private final OtherRepository otherRepository;

    public AddLogQCUsecase(OtherRepository otherRepository) {
        this.otherRepository = otherRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        return otherRepository.addLogQC(Constants.KEY, json);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseResponse<UploadEntity>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.getMessage()));
                }
            }

            @Override
            public void onNext(BaseResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    String result = data.getDescription();
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
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
