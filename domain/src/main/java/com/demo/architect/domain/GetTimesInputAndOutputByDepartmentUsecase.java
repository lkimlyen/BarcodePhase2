package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.TimesEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetTimesInputAndOutputByDepartmentUsecase extends BaseUseCase<BaseResponse<TimesEntity>>  {
    private static final String TAG = GetTimesInputAndOutputByDepartmentUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetTimesInputAndOutputByDepartmentUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<TimesEntity>>  buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        int departmentId = ((RequestValue) requestValues).departmentId;
        return remoteRepository.getTimesInputAndOutputByDepartment(orderId,departmentId);
    }

    @Override
    protected DisposableObserver<BaseResponse<TimesEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<TimesEntity>>() {
            @Override
            public void onNext(BaseResponse<TimesEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    TimesEntity result = data.getData();
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
        private final long orderId;
        private final int departmentId;

        public RequestValue(long orderId, int departmentId) {
            this.orderId = orderId;
            this.departmentId = departmentId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private TimesEntity entity;

        public ResponseValue(TimesEntity entity) {
            this.entity = entity;
        }

        public TimesEntity getEntity() {
            return entity;
        }
    }

    public static final class ErrorValue implements ErrorValues {
        private String description;

        public ErrorValue(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
