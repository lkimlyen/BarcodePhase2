package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.ConfirmInputModel;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;

import java.util.List;

import io.realm.RealmList;
import rx.Observable;

public interface LocalRepository {

    Observable<String> add(MessageModel model);

    Observable<List<MessageModel>> findAll();

    Observable<Integer> countLogScanStages(final int orderId, final int departmentId);

    Observable<List<LogScanStages>> getListLogScanStagesUpdate(final int orderId);

    Observable<String> addLogScanStagesAsync(final LogScanStages model);

    Observable<ProductDetail> getProductDetail(ProductEntity productEntity);

    Observable<String> updateNumberScanStages(final int stagesId, final int numberInput);

    Observable<String> deleteScanStages(final int stagesId);

    Observable<LogListScanStages> getListScanStagseByDepartment(final int orderId, int departmentId, int userId);

    Observable<String> addOrderConfirm(final List<OrderConfirmEntity> list);

    Observable<RealmList<ConfirmInputModel>> getListConfirm(final int times);

    Observable<List<LogScanConfirm>> getListLogScanConfirm();

    Observable<String> addLogScanConfirm(final LogScanConfirm logScanConfirm);

    Observable<ConfirmInputModel> findConfirmByBarcode(final String barcode);

    Observable<String> updateNumnberLogConfirm(final int logId, final int numberScan);

    Observable<String> updateStatusLogConfirm();


}
