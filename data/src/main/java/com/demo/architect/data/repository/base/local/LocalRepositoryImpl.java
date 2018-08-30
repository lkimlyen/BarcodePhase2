package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductPackagingModel;

import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;
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
    public Observable<Integer> countLogScanStages(final int orderId, final int departmentId, final int times) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int count = databaseRealm.countLogScanStagesWatingUpload(orderId, departmentId, times);

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
    public Observable<List<LogScanStages>> getListLogScanStagesUpdate() {
        return Observable.create(new Observable.OnSubscribe<List<LogScanStages>>() {
            @Override
            public void call(Subscriber<? super List<LogScanStages>> subscriber) {
                try {
                    List<LogScanStages> list = databaseRealm.getListLogScanStagesUpload();
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<LogScanStages>> getListScanStages(final int orderId, final int departmentIdOut, final int times, final String module) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogScanStages>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogScanStages>> subscriber) {
                try {
                    RealmResults<LogScanStages> list = databaseRealm.getListScanStagesByModule(orderId,
                            departmentIdOut,times,module);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<ListGroupCode>> getListGroupCode(final int orderId, final int departmentIdOut, final int times, final String module) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<ListGroupCode>>() {
            @Override
            public void call(Subscriber<? super RealmResults<ListGroupCode>> subscriber) {
                try {
                    RealmResults<ListGroupCode> list = databaseRealm.getListGroupCodeByModule(orderId,
                            departmentIdOut,times,module);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addLogScanStagesAsync(final LogScanStages model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addLogScanStagesAsync(model);
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
    public Observable<LogListScanStages> getListScanStagseByDepartment(final int orderId, final int departmentId,
                                                                       final int userId, final int times) {
        return Observable.create(new Observable.OnSubscribe<LogListScanStages>() {
            @Override
            public void call(Subscriber<? super LogListScanStages> subscriber) {
                try {
                    LogListScanStages logListScanStages = databaseRealm.getListScanStages(orderId, departmentId, userId, times);
                    subscriber.onNext(logListScanStages);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addOrderConfirm(final List<OrderConfirmEntity> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addOrderConfirm(list);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<LogScanConfirm>> getListConfirm(final int orderId, final int departmentIdOut, final int times) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogScanConfirm>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogScanConfirm>> subscriber) {
                try {
                    RealmResults<LogScanConfirm> realmList = databaseRealm.getListConfirm(orderId, departmentIdOut, times);
                    subscriber.onNext(realmList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countListConfirmByTimesWaitingUpload(final int orderId, final int departmentIdOut, final int times) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int count = databaseRealm.countListConfirmByTimesWaitingUpload(orderId, departmentIdOut, times);
                    subscriber.onNext(count);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogScanConfirm>> getListLogScanConfirm() {
        return Observable.create(new Observable.OnSubscribe<List<LogScanConfirm>>() {
            @Override
            public void call(Subscriber<? super List<LogScanConfirm>> subscriber) {
                try {
                    List<LogScanConfirm> realmList = databaseRealm.getListLogScanConfirm();
                    subscriber.onNext(realmList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogScanConfirm> findConfirmByBarcode(final int orderId, final int departmentIdOut, final int times, final String barcode) {
        return Observable.create(new Observable.OnSubscribe<LogScanConfirm>() {
            @Override
            public void call(Subscriber<? super LogScanConfirm> subscriber) {
                try {
                    LogScanConfirm model = databaseRealm.findConfirmByBarcode(barcode, orderId, departmentIdOut,
                            times);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> updateNumnberLogConfirm(final int orderId, final int orderProductId, final int departmentIdOut, final int times, final int numberScan, final boolean scan) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberLogConfirm(orderId, orderProductId, departmentIdOut, times, numberScan, scan);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusLogConfirm() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusConfirm();
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusScanStagesByOrder(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusScanStagesByOrder(orderId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusScanStages() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusScanStages();
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusAndServerIdImage(final int id, final int serverId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusAndServerIdImage(id, serverId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addImageModel(final String pathFile) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addImageModel(pathFile);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteImageModel(final int id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteImageModel(id);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<LogScanPackaging>> getListScanPackaging(final SOEntity soEntity, final ModuleEntity moduleEntity, final ApartmentEntity apartment, final CodePackEntity codePack) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogScanPackaging>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogScanPackaging>> subscriber) {
                try {
                    RealmResults<LogScanPackaging> listScanPackaging =  databaseRealm.getListScanPackaging(soEntity,
                            moduleEntity,apartment,codePack);
                    subscriber.onNext(listScanPackaging);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogScanPackaging>> getListScanPackaging(final int orderId, final int apartmentId, final int moduleId, final String serialPack) {
        return Observable.create(new Observable.OnSubscribe<List<LogScanPackaging>>() {
            @Override
            public void call(Subscriber<? super List<LogScanPackaging>> subscriber) {
                try {
                    List<LogScanPackaging> listScanPackaging =  databaseRealm.getListScanPackaging(orderId,
                            apartmentId,moduleId,serialPack);
                    subscriber.onNext(listScanPackaging);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }



    @Override
    public Observable<String> saveBarcodeScanPackaging(final ProductPackagingEntity product, final String barcode, final int orderId, final int apartmentId, final int moduleId, final String packCode, final String serialPack) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addBarcodeScanPackaging(product,barcode,orderId,apartmentId,moduleId,packCode,serialPack);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteScanPackaging(final int logId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteScanPackaging(logId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberScanPackaging(final int logId, final int number) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberScanPackaging(logId, number);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ProductPackagingModel> findProductPackaging(final int productId) {
        return Observable.create(new Observable.OnSubscribe<ProductPackagingModel>() {
            @Override
            public void call(Subscriber<? super ProductPackagingModel> subscriber) {
                try {
                    ProductPackagingModel model =  databaseRealm.findProductPackaging(productId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogListOrderPackaging> findOrderPackaging(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<LogListOrderPackaging>() {
            @Override
            public void call(Subscriber<? super LogListOrderPackaging> subscriber) {
                try {
                    LogListOrderPackaging model =  databaseRealm.findOrderPackaging(orderId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> getTotalScanBySerialPack(final int orderId, final int apartmentId, final int moduleId, final String serialPack) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int total =  databaseRealm.getTotalScanBySerialPack(orderId,apartmentId,moduleId,serialPack);
                    subscriber.onNext(total);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addGroupCode(final String groupCode,final int orderId, final int departmentId, final int times, final LogScanStages[] listSelect) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                  databaseRealm.addGroupCode(groupCode,orderId,departmentId,times,listSelect);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberGroup(final int logId, final int numberGroup) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberGroup(logId,numberGroup);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> detachedCodeStages(final int orderId, final int departmentId, final int times, final ListGroupCode list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.detachedCodeStages(orderId,departmentId,times,list);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> removeItemInGroup(final ListGroupCode groupCode, final LogScanStages logScanStages, final int orderId, final int departmentId, final int times) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.removeItemInGroup(groupCode,logScanStages,orderId,departmentId,times);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<IPAddress> insertOrUpdateIpAddress(final IPAddress model) {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    databaseRealm.insertOrUpdate(model);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusScanPackaging(final int serverId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusScanPackaging(serverId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<IPAddress> findIPAddress() {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    IPAddress ipAddress = databaseRealm.findFirst(IPAddress.class);
                    subscriber.onNext(ipAddress);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
