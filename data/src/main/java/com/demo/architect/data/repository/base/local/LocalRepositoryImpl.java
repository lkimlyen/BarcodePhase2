package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.ImportWorksModel;
import com.demo.architect.data.model.offline.LogCompleteCreatePack;
import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.LogCompleteMainList;
import com.demo.architect.data.model.offline.LogDeleteCreatePack;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.architect.data.model.offline.ScanDeliveryModel;
import com.demo.architect.data.model.offline.ScanWarehousingModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class LocalRepositoryImpl implements LocalRepository {

    DatabaseRealm databaseRealm;

    public LocalRepositoryImpl(Context context) {
        databaseRealm = new DatabaseRealm(context);
    }

    @Override
    public Observable<String> add(final MessageModel model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.add(model);

                    subscriber.onNext(model.getUuid());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<MessageModel>> findAll() {
        return Observable.create(new Observable.OnSubscribe<List<MessageModel>>() {
            @Override
            public void call(Subscriber<? super List<MessageModel>> subscriber) {
                try {
                    List<MessageModel> models = databaseRealm.findAll(MessageModel.class);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countLogScanStages(final int orderId, final int departmentId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int count = databaseRealm.countLogScanStagesWatingUpload(orderId, departmentId);

                    subscriber.onNext(count);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogScanStages>> getListLogScanStagesUpdate(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<List<LogScanStages>>() {
            @Override
            public void call(Subscriber<? super List<LogScanStages>> subscriber) {
                try {
                    List<LogScanStages> list = databaseRealm.getListLogScanStagesUpload(orderId);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addLogScanStagesAsync(final LogScanStages model, final ProductEntity entity) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addLogScanStagesAsync(model, entity);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ProductDetail> getProductDetail(final ProductEntity product) {
        return Observable.create(new Observable.OnSubscribe<ProductDetail>() {
            @Override
            public void call(Subscriber<? super ProductDetail> subscriber) {
                try {
                    ProductDetail productDetail = databaseRealm.getProductDetail(product);
                    subscriber.onNext(productDetail);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberScanStages(final int stagesId, final int numberInput) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberScanStages(stagesId, numberInput);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteScanStages(final int stagesId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteScanStages(stagesId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogListScanStages> getListScanStagseByDepartment(final int orderId, final int departmentId, final int userId) {
        return Observable.create(new Observable.OnSubscribe<LogListScanStages>() {
            @Override
            public void call(Subscriber<? super LogListScanStages> subscriber) {
                try {
                    LogListScanStages logListScanStages =  databaseRealm.getListScanStages(orderId, departmentId, userId);
                    subscriber.onNext(logListScanStages);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


}
