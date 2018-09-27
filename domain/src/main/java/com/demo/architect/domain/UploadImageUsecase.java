package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UploadEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import java.io.File;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ACER on 7/26/2017.
 */

public class UploadImageUsecase extends BaseUseCase {
    private static final String TAG = UploadImageUsecase.class.getSimpleName();
    private final OtherRepository otherRepository;

    public UploadImageUsecase(OtherRepository otherRepository) {
        this.otherRepository = otherRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        File file = ((RequestValue) requestValues).file;
        long orderId = ((RequestValue) requestValues).orderId;
        int departmentId = ((RequestValue) requestValues).departmentId;
        String fileName = ((RequestValue) requestValues).fileName;
        long userId = ((RequestValue) requestValues).userId;
        return otherRepository.uploadImage(file, Constants.KEY, orderId,departmentId,fileName,userId);
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
            public void onNext(BaseResponse<UploadEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    long result = data.getData().getImageID();
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
        private final File file;
        private final long orderId;
        private final int departmentId;
        private final String fileName;
        private final long userId;

        public RequestValue(File file, long orderId, int departmentId, String fileName, long userId) {
            this.file = file;
            this.orderId = orderId;
            this.departmentId = departmentId;
            this.fileName = fileName;
            this.userId = userId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private final long  imageId;

        public ResponseValue(long imageId) {
            this.imageId = imageId;
        }

        public long getImageId() {
            return imageId;
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
