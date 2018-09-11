package com.demo.architect.data.repository.base.local;

import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
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
import com.demo.architect.data.model.offline.QualityControlModel;

import java.security.acl.Group;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Completable;
import rx.Observable;

public interface LocalRepository {

    Observable<String> add(MessageModel model);

    Observable<List<MessageModel>> findAll();

    Observable<Integer> countLogScanStages(final int orderId, final int departmentId, final int times);

    Observable<List<LogScanStages>> getListLogScanStagesUpdate(final int orderId);

    Observable<List<LogScanStages>> getListLogScanStagesUpdate();

    Observable<RealmResults<LogScanStages>> getListScanStages(final int orderId, final int departmentIdOut, final int times, final String module);

    Observable<RealmResults<GroupCode>> getListGroupCodeScan(final int orderId, String module);


    Observable<String> addLogScanStagesAsync(final LogScanStages model,final ProductEntity productEntity);

    Observable<ProductDetail> getProductDetail(ProductEntity productEntity);

    Observable<String> updateNumberScanStages(final int stagesId, final int numberInput);

    Observable<String> deleteScanStages(final int stagesId);

    Observable<LogListScanStages> getListScanStagseByDepartment(final int orderId, int departmentId, int userId, int times);

    Observable<String> addOrderConfirm(final List<OrderConfirmEntity> list);

    Observable<RealmResults<LogScanConfirm>> getListConfirm(final int orderId, final int departmentIdOut, final int times);

    Observable<Integer> countListConfirmByTimesWaitingUpload(final int orderId, final int departmentIdOut, final int times);

    Observable<List<LogScanConfirm>> getListLogScanConfirm();

    Observable<LogScanConfirm> findConfirmByBarcode(final int orderId, int departmentIdOut, int times, final String barcode);

    Observable<String> updateNumnberLogConfirm(final int orderId, final int orderProductId, final int departmentIdOut, final int times, final int numberScan, final boolean scan);

    Observable<String> updateStatusLogConfirm();

    Observable<String> updateStatusScanStagesByOrder(int orderId);

    Observable<String> updateStatusScanStages();

    Observable<String> updateStatusAndServerIdImage(final int id, final int imageId, int serverId);

    Observable<String> addImageModel(final int id, final String pathFile);

    Observable<String> deleteImageModel(final int id);

    Observable<RealmResults<LogScanPackaging>> getListScanPackaging(SOEntity soEntity, ModuleEntity moduleEntity, ApartmentEntity apartment, CodePackEntity codePack);

    Observable<List<LogScanPackaging>> getListScanPackaging(int orderId, int apartmentId, int moduleId, String serialPack);


    Observable<String> saveBarcodeScanPackaging(ProductPackagingEntity product, String barcode, int orderId, int apartmentId, int moduleId, String packCode, String serialPack);

    Observable<String> deleteScanPackaging(int logId);

    Observable<String> updateNumberScanPackaging(int logId, int number);

    Observable<ProductPackagingModel> findProductPackaging(int productId, String serialPack);

    Observable<LogListOrderPackaging> findOrderPackaging(int orderId);

    Observable<Integer> getTotalScanBySerialPack(final int orderId, final int apartmentId, final int moduleId, final String serialPack);

    Observable<String> addGroupCode(String groupCode, int orderId,String module, GroupCode[] listSelect);

    Observable<String> addGroupCode(String groupCode,  LogScanStages logScanStages,ProductEntity productEntity);

    Observable<Boolean> updateNumberGroup(final ProductEntity productEntity,int groupId, int numberGroup);

    Observable<String> detachedCodeStages(int orderId, final String module,  String groupCode);

    Observable<String> removeItemInGroup(String groupCode, GroupCode logScanStages, int orderId, final String module);

    Observable<IPAddress> findIPAddress();

    Observable<IPAddress> insertOrUpdateIpAddress(final IPAddress model);

    Observable<String> updateStatusScanPackaging(int serverId);

    Observable<String> deleteAllItemLogScanPackaging();

    Observable<RealmResults<QualityControlModel>> getListQualityControl(int orderId, int departmentId);

    Observable<QualityControlModel> getDetailQualityControl(int id);

    Observable<RealmList<Integer>> getListReasonQualityControl(int id);

    Observable<String> saveBarcodeQC(int orderId, int departmentId, ProductEntity productEntity);

    Observable<String> updateDetailErrorQC(int id, int numberFailed, String description, Collection<Integer> idList);

    Observable<List<QualityControlModel>> getListQualityControlUpload();

    Observable<String> updateImageIdAndStatus(int qcId, int id, int imageId);

    Observable<String> updateStatusQC();

    Observable<String> addGroupCode(final ProductEntity productEntity);

    Observable<Boolean>  checkProductExistInGroupCode(ProductEntity model);

    Observable<Boolean>  checkNumberProductInGroupCode(ProductEntity model);

    Observable<RealmResults<ListGroupCode>> getListGroupCode(int orderId, String module);

    Observable<String> updateGroupCode(String groupCode, int orderId,  String module, GroupCode[] listSelect);

    Observable<List<GroupCode>> updateNumberGroup(int id, int number);

    Observable<String> confirmAllProductReceive(int orderId, int departmentId, int times);
}
