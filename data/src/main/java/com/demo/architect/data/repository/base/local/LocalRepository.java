package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.ImportWorksModel;
import com.demo.architect.data.model.offline.LogCompleteCreatePack;
import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.LogCompleteMainList;
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

import java.util.HashMap;
import java.util.List;

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
}
