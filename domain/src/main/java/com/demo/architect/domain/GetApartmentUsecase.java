package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.StaffEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetApartmentUsecase extends BaseUseCase<BaseListResponse<ApartmentEntity>>  {
    private static final String TAG = GetApartmentUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetApartmentUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ApartmentEntity>>  buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        return remoteRepository.getApartment(orderId);
    }

    @Override
    protected DisposableObserver<BaseListResponse<ApartmentEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ApartmentEntity>>() {
            @Override
            public void onNext(BaseListResponse<ApartmentEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<ApartmentEntity> result = data.getData();
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
                    useCaseCallback.onError(new ErrorValue(e.toString()));
                }
            }
        };
    }
    public static final class RequestValue implements RequestValues {
        private final long orderId;

        public RequestValue(long orderId) {
            this.orderId = orderId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ApartmentEntity> entity;

        public ResponseValue(List<ApartmentEntity> entity) {
            this.entity = entity;
        }

        public List<ApartmentEntity> getEntity() {
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
