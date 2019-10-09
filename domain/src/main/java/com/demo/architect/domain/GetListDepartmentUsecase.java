package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetListDepartmentUsecase extends BaseUseCase<BaseListResponse<DepartmentEntity>> {
    private static final String TAG = GetListDepartmentUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetListDepartmentUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<DepartmentEntity>> buildUseCaseObservable() {
        return remoteRepository.getListDepartment();
    }


    @Override
    protected DisposableObserver<BaseListResponse<DepartmentEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<DepartmentEntity>>() {
            @Override
            public void onNext(BaseListResponse<DepartmentEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<DepartmentEntity> result = data.getData();
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

    }

    public static final class ResponseValue implements ResponseValues {
        private List<DepartmentEntity> entity;

        public ResponseValue( List<DepartmentEntity> entity) {
            this.entity = entity;
        }

        public  List<DepartmentEntity> getEntity() {
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
