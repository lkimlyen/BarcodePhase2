package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.MachineEntity;
import com.demo.architect.data.model.QCEntity;
import com.demo.architect.data.model.QCEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetListQCUsecase extends BaseUseCase<BaseListResponse<QCEntity>> {
    private static final String TAG = GetListQCUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetListQCUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<QCEntity>> buildUseCaseObservable() {
        return remoteRepository.getListQC();
    }


    @Override
    protected DisposableObserver<BaseListResponse<QCEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<QCEntity>>() {
            @Override
            public void onNext(BaseListResponse<QCEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null){
                List<QCEntity> result = data.getData();
                if (result != null && data.getStatus() == 1) {
                    useCaseCallback.onSuccess(new ResponseValue(result));
                } else {
                    useCaseCallback.onError(new ErrorValue(data.getDescription()));
                }}
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
        private List<QCEntity> entity;

        public ResponseValue( List<QCEntity> entity) {
            this.entity = entity;
        }

        public  List<QCEntity> getEntity() {
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
