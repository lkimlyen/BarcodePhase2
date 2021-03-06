package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.StaffEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetAllStaffUsecase extends BaseUseCase<BaseListResponse<StaffEntity>>  {
    private static final String TAG = GetAllStaffUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetAllStaffUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<StaffEntity>>  buildUseCaseObservable() {
        return remoteRepository.getAllStaff("13AKby8uFhdlayHD6oPsaU90b8o00=");
    }
    @Override
    protected DisposableObserver<BaseListResponse<StaffEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<StaffEntity>>() {
            @Override
            public void onNext(BaseListResponse<StaffEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<StaffEntity> result = data.getData();
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
                    useCaseCallback.onError(new ConfirmInputWindowUsecase.ErrorValue(e.toString()));
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {

    }

    public static final class ResponseValue implements ResponseValues {
        private List<StaffEntity> entity;

        public ResponseValue(List<StaffEntity> entity) {
            this.entity = entity;
        }

        public List<StaffEntity> getEntity() {
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
