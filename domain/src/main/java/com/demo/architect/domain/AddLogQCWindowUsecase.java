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

public class AddLogQCWindowUsecase extends BaseUseCase {
    private static final String TAG = AddLogQCWindowUsecase.class.getSimpleName();
    private final OtherRepository otherRepository;

    public AddLogQCWindowUsecase(OtherRepository otherRepository) {
        this.otherRepository = otherRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        String machineName = ((RequestValue) requestValues).machineName;
        String violator = ((RequestValue) requestValues).violator;
        String qcCode = ((RequestValue) requestValues).qcCode;
        long orderId = ((RequestValue) requestValues).orderId;
        int departmentId = ((RequestValue) requestValues).departmentId;
        long userId = ((RequestValue) requestValues).userId;
        return otherRepository.addLogQCWindow(Constants.KEY,machineName,violator,qcCode,orderId,departmentId,userId, json);
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
        private final String machineName;
        private final String violator;
        private final String qcCode;
        private final long orderId;
        private final int departmentId;
        private final long userId;
        private final String json;

        public RequestValue(String machineName, String violator, String qcCode, long orderId, int departmentId, long userId, String json) {
            this.machineName = machineName;
            this.violator = violator;
            this.qcCode = qcCode;
            this.orderId = orderId;
            this.departmentId = departmentId;
            this.userId = userId;
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
