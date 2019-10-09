package com.demo.barcode.screen.detail_error;

import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.offline.DetailError;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.Collection;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by MSI on 26/11/2017.
 */

public interface DetailErrorContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showImageError(RealmList<ImageModel> imageModels);

        void showListReason(List<ReasonsEntity> list);

        void showDetailQualityControl(QualityControlModel qualityControlModel);

        void showDetailQualityControlƯindow(QualityControlWindowModel qualityControlModel);

        void showUpdateListCounterSelect(RealmList<Integer> integerRealmList);

        void goBackQualityControl();
    }

    interface Presenter extends BasePresenter {

        //thêm hình ảnh lỗi
        void addImage(long id, String pathFile);

        //xóa hình ảnh lỗi
        void deleteImage(long id);

        //lấy ds lý do
        void getListReason(long id);

        //lấy detail của chi tiết lỗi
        void getDetailQualityControl(long id);

        //lưu thông tin cập nhật của chi tiết lỗi
        void save(long id, int numberFailed, String description, Collection<Integer> idList);

    }
}
