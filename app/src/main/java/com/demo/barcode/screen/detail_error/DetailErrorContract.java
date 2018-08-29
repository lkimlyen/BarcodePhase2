package com.demo.barcode.screen.detail_error;

import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.offline.DetailError;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

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

        void showImageError(DetailError detailError);

        void showListReason(List<ReasonsEntity> list);
    }

    interface Presenter extends BasePresenter {

        void addImage(String pathFile);

        void deleteImage(int id);

        void getListReason();
    }
}
