package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;

import java.util.List;

import io.realm.RealmResults;
import rx.Observable;

public interface LocalRepository {

    Observable<String> add(MessageModel model);

    Observable<List<MessageModel>> findAll();

    Observable<Integer> countLogScanStages(final int orderId, final int departmentId);

    Observable<List<LogScanStages>> getListLogScanStagesUpdate(final int orderId);

    Observable<List<LogScanStages>> getListLogScanStagesUpdate();

    Observable<String> addLogScanStagesAsync(final LogScanStages model);

    Observable<ProductDetail> getProductDetail(ProductEntity productEntity);

    Observable<String> updateNumberScanStages(final int stagesId, final int numberInput);

    Observable<String> deleteScanStages(final int stagesId);

    Observable<LogListScanStages> getListScanStagseByDepartment(final int orderId, int departmentId, int userId, int times);

    Observable<String> addOrderConfirm(final List<OrderConfirmEntity> list);

    Observable<RealmResults<LogScanConfirm>> getListConfirm(final int orderId, final int departmentIdOut,final int times);

    Observable<List<LogScanConfirm>> getListLogScanConfirm();


    Observable<ConfirmInputModel> findConfirmByBarcode(final String barcode);

    Observable<String> updateNumnberLogConfirm(final int orderId,final int orderProductId, final int departmentIdOut, final int times, final int numberScan,final boolean scan);

    Observable<String> updateStatusLogConfirm();
    Observable<String> updateStatusScanStagesByOrder(int orderId);

    Observable<String> updateStatusScanStages();

}
