package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ListReasonsEntity;
import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.ListReasonsEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetListReasonUsecase extends BaseUseCase<BaseResponse<ListReasonsEntity>>  {
    private static final String TAG = GetListReasonUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetListReasonUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<ListReasonsEntity>>  buildUseCaseObservable() {
        return remoteRepository.getRAndSQC();
    }

    @Override
    protected DisposableObserver<BaseResponse<ListReasonsEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<ListReasonsEntity>>() {
            @Override
            public void onNext(BaseResponse<ListReasonsEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null){
                ListReasonsEntity result = data.getData();
                if (result != null && data.getStatus() == 1) {
                    useCaseCallback.onSuccess(new ResponseValue(result));
                } else {
                    useCaseCallback.onError(new ErrorValue(data.getDescription()));
                }
            }}

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
        private ListReasonsEntity entity;

        public ResponseValue( ListReasonsEntity entity) {
            this.entity = entity;
        }

        public  ListReasonsEntity getEntity() {
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
