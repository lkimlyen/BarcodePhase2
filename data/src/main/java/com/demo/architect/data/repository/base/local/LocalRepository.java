package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.OrderConfirmWindowEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.ProductWindowEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.GroupScan;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanConfirmModel;
import com.demo.architect.data.model.offline.LogScanConfirmWindowModel;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.LogScanStagesWindowModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductDetailWindowModel;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;

import java.util.Collection;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Completable;
import rx.Observable;

public interface LocalRepository {

    Observable<String> add(MessageModel model);

    Observable<List<MessageModel>> findAll();

    Observable<Integer> countAllDetailWaitingUpload(final long orderId);

    Observable<List<LogScanStages>> getListLogScanStagesUpload();


    Observable<RealmResults<LogScanStages>> getListScanStages(final long orderId, final int departmentIdOut, final int times, final String module);

    Observable<RealmResults<GroupCode>> getListGroupCodeScan(final long orderId);


    Observable<String> addLogScanStagesAsync(final LogScanStages model, final long productId);

    Observable<ProductDetail> getProductDetail(ProductEntity productEntity, int times);

    Observable<String> updateNumberScanStages(final long stagesId, final double numberInput);

    Observable<String> deleteScanStages(final long stagesId);

    Observable<LogListScanStages> getListScanStagseByDepartment(final long orderId, int departmentId, long userId, int times);

    Observable<String> addOrderConfirm(final List<OrderConfirmEntity> list, final int times);

    Observable<RealmResults<LogScanConfirmModel>> getListConfirm();

    Observable<List<LogScanConfirmModel>> getListLogScanConfirm();

    Observable<LogScanConfirmModel> findConfirmByBarcode(final String barcode);

    Observable<String> updateNumnberLogConfirm(long outputId, final int numberScan, final boolean scan);

    Observable<String> updateStatusLogConfirm();

    Observable<String> updateStatusScanStagesByOrder(long orderId);

    Observable<String> updateStatusScanStages(long orderId, int departmentId, int times);

    Observable<String> updateStatusAndServerIdImage(final long id, final long imageId, long serverId);

    Observable<String> addImageModel(final long id, final String pathFile, int type);

    Observable<String> deleteImageModel(final long id);

    Observable<RealmResults<LogListSerialPackPagkaging>> getListScanPackaging(SOEntity soEntity, ApartmentEntity apartment);

    Observable<List<LogScanPackaging>> getListScanPackaging(long orderId, long apartmentId, long moduleId, String serialPack);


    Observable<String> saveBarcodeScanPackaging(final ListModuleEntity module, final PackageEntity packageEntity, final ProductPackagingEntity productPackagingEntity, final long orderId, final long apartmentI);

    Observable<String> deleteScanPackaging(long logId);

    Observable<String> updateNumberScanPackaging(long logId, double number);

    Observable<ProductPackagingModel> findProductPackaging(long productId, String serialPack);

    Observable<LogListOrderPackaging> findOrderPackaging(long orderId);

    Observable<Integer> getTotalScanBySerialPack(final long orderId, final long apartmentId, final long moduleId, final String serialPack);

    Observable<String> addGroupCode(String groupCode, long orderId, GroupCode[] listSelect);

    Observable<String> addGroupCode(String groupCode, LogScanStages logScanStages, ProductEntity productEntity);

    Observable<Boolean> updateNumberGroup(long groupId, double numberGroup);

    Observable<String> detachedCodeStages(final List<ProductGroupEntity> list, long orderId, String groupCode);

    Observable<String> removeItemInGroup(ProductGroupEntity logScanStages, long orderId);

    Observable<IPAddress> findIPAddress();

    Observable<IPAddress> insertOrUpdateIpAddress(final IPAddress model);

    Observable<String> updateStatusScanPackaging(long orderId, long apartmentId, long moduleId, String serialPack, long serverId);

    Observable<String> deleteAllItemLogScanPackaging();

    Observable<String> deleteQC(long id);

    Observable<RealmResults<QualityControlModel>> getListQualityControl();

    Observable<QualityControlModel> getDetailQualityControl(long id);

    Observable<RealmList<Integer>> getListReasonQualityControl(long id, int type);

    Observable<String> saveBarcodeQC(long orderId, int departmentId, String machineName, String violator, String qcCode, ProductEntity productEntity);

    Observable<String> updateDetailErrorQC(long id, double numberFailed, String description, Collection<Integer> idList);

    Observable<List<QualityControlModel>> getListQualityControlUpload();

    Observable<String> updateImageIdAndStatus(long qcId, long id, long imageId, int type);

    Observable<String> updateStatusQC();

    Observable<String> addGroupCode(final ProductEntity productEntity);

    Observable<String> addGroupScan(final List<GroupEntity> list);

    Observable<Boolean> checkNumberProductInGroupCode(ProductEntity model);

    Observable<String> updateGroupCode(String groupCode, long orderId, GroupCode[] listSelect);

    Observable<String> confirmAllProductReceive();

    Observable<String> cancelConfirmAllProductReceive();

    Observable<Boolean> getCheckedConfirmAll(final long orderId, final int departmentIdOut, final int times);


    Observable<RealmResults<LogScanStages>> getScanByProductDetailId(LogScanStages logScanStages);

    Observable<String> deleteScanGroupCode(long id);

    Observable<Double> totalNumberScanGroup(long productDetailId);


    Observable<String> updateNumberTotalProduct(List<ProductEntity> entity);

    Observable<String> deleteDataLocal();

    Observable<List<GroupScan>> getListGroupScanVersion();

    Observable<String> saveListProductDetail(List<ProductEntity> entity);

    Observable<String> deleteAllProductDetail();

    Observable<RealmResults<LogScanStages>> getAllListStages();

    Observable<String> deleteAllScanStages();

    Observable<ProductDetailWindowModel> getProductDetailWindow(ProductWindowEntity model);

    Observable<String> addLogScanStagesWindow(LogScanStagesWindowModel logScanStages);

    Observable<String> updateNumberScanStagesWindow(long stagesId, int number);

    Observable<String> deleteAllScanStagesWindow();

    Observable<RealmResults<LogScanStagesWindowModel>> getAllListStagesWindow();

    Observable<String> deleteScanStagesWindow(long stagesId);

    Observable<List<LogScanStagesWindowModel>> getListLogScanStagesWindowUpload();

    Observable<String> addOrderConfirmWindow(List<OrderConfirmWindowEntity> list);

    Observable<RealmResults<LogScanConfirmWindowModel>> getListConfirmWindow();

    Observable<LogScanConfirmWindowModel> findConfirmByBarcodeInWindow(String barcode);

    Observable<String> updateNumnberLogConfirmWindow(long outputId, int number, boolean scan);

    Observable<List<LogScanConfirmWindowModel>> getListLogScanConfirmWindow();

    Observable<String> confirmAllProductReceiveWindow();

    Observable<String> cancelConfirmAllProductReceiveWindow();

    Observable<String> updateStatusLogConfirmWindow();

    Observable<Boolean> checkBarcodeExistInQC(String barcode);

    Observable<String> deleteAlLQC();

    Observable<String> saveBarcodeQCWindow(String machineName, String violator, String qcCode, ProductWindowEntity productEntity);

    Observable<RealmResults<QualityControlWindowModel>> getListQualityControlWindow();

    Observable<String> deleteAlLQCWindow();


    Observable<Boolean> checkBarcodeExistInQCWindow(String barcode);

    Observable<QualityControlWindowModel> getDetailQualityControlWindow(long id);

    Observable<String> updateDetailErrorQCWindow(long id, int numberFailed, String description, Collection<Integer> idList);

    Observable<List<QualityControlWindowModel>> getListQualityControlUploadWindow();

    Observable<String> updateStatusQCWindow();
}
