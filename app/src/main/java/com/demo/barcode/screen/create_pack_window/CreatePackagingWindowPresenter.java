package com.demo.barcode.screen.create_pack_window;

import androidx.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.GroupSetEntity;
import com.demo.architect.data.model.PositionScanWindow;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.model.offline.ListPackCodeWindowModel;
import com.demo.architect.data.model.offline.ProductPackWindowModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetProductSetDetailBySetAndDirecUsecase;
import com.demo.architect.domain.GetProductSetUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListProductPackagingWindowManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.ListSetWindowManager;
import com.demo.barcode.manager.PositionScanWindowManager;
import com.demo.barcode.manager.UserManager;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreatePackagingWindowPresenter implements CreatePackagingWindowContract.Presenter {

    private final String TAG = CreatePackagingWindowPresenter.class.getName();
    private final CreatePackagingWindowContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetProductSetUsecase getProductSetUsecase;
    private final GetProductSetDetailBySetAndDirecUsecase getProductSetDetailBySetAndDirecUsecase;
    private PositionScanWindow positionScan;
    @Inject
    LocalRepository localRepository;

    @Inject
    CreatePackagingWindowPresenter(@NonNull CreatePackagingWindowContract.View view,
                                   GetListSOUsecase getListSOUsecase,
                                   GetProductSetUsecase getProductSetUsecase,
                                   GetProductSetDetailBySetAndDirecUsecase getProductSetDetailBySetAndDirecUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;

        this.getProductSetUsecase = getProductSetUsecase;
        this.getProductSetDetailBySetAndDirecUsecase = getProductSetDetailBySetAndDirecUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");

    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getListSO() {
        view.showProgressBar();
        getListSOUsecase.executeIO(new GetListSOUsecase.RequestValue(UserManager.getInstance().getUser().getOrderType()),
                new BaseUseCase.UseCaseCallback<GetListSOUsecase.ResponseValue,
                        GetListSOUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListSOUsecase.ResponseValue successResponse) {
                        view.showListSO(successResponse.getEntity());
                        ListSOManager.getInstance().setListSO(successResponse.getEntity());
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_so_success));
                    }

                    @Override
                    public void onError(GetListSOUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListSOManager.getInstance().setListSO(new ArrayList<>());
                        //   view.clearDataNoProduct(true);
                    }
                });
    }

    @Override
    public void getListSetWindow(long orderId) {
        view.showProgressBar();
        getProductSetUsecase.executeIO(new GetProductSetUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetProductSetUsecase.ResponseValue,
                        GetProductSetUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetProductSetUsecase.ResponseValue successResponse) {
                        ListSetWindowManager.getInstance().setListSet(successResponse.getEntity());
                        view.showListSetWindow(successResponse.getEntity());
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_set_window_success));

                    }

                    @Override
                    public void onError(GetProductSetUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListSetWindowManager.getInstance().setListSet(new ArrayList<>());
                    }
                });
    }


    @Override
    public void getListScan() {
        localRepository.deleteAllItemLogScanPackagingWindow().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                localRepository.getListScanPackagingWindow()
                        .subscribe(new Action1<RealmResults<ListPackCodeWindowModel>>() {
                            @Override
                            public void call(RealmResults<ListPackCodeWindowModel> results) {
                                view.showListScan(results);
                            }
                        });
            }
        });

    }

    @Override
    public void getListDetailInSet(long productSetId, int direction) {
        view.showProgressBar();
        getProductSetDetailBySetAndDirecUsecase.executeIO(new GetProductSetDetailBySetAndDirecUsecase.RequestValue(productSetId, direction),
                new BaseUseCase.UseCaseCallback<GetProductSetDetailBySetAndDirecUsecase.ResponseValue,
                        GetProductSetDetailBySetAndDirecUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetProductSetDetailBySetAndDirecUsecase.ResponseValue successResponse) {
                        ListProductPackagingWindowManager.getInstance().setList(successResponse.getEntity());
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_detail_in_set_success));

                    }

                    @Override
                    public void onError(GetProductSetDetailBySetAndDirecUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductPackagingWindowManager.getInstance().setList(new ArrayList<>());
                    }
                });
    }


    @Override
    public void deleteLogScan(long logId, String packCode, int numberOnPack) {
        if (positionScan != null) {
            if (!positionScan.getPackCode().equals(packCode) || positionScan.getNumberPack() != numberOnPack) {
                showError(CoreApplication.getInstance().getString(R.string.text_incomplete_pack));
                return;
            }
        }
        //xóa chi tiết và tính lại tổng sl chi tiết trong gói
        localRepository.deleteScanPackagingWindow(logId, packCode, numberOnPack).subscribe(new Action1<String>() {
            @Override
            public void call(String total) {
                localRepository.getTotalNumberDetaiLInPackWindow(packCode, numberOnPack).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                        if (integer == 0) {
                            if (positionScan != null) {
                                positionScan = null;
                            }
                        } else {
                            if (positionScan == null) {
                                positionScan = new PositionScanWindow(packCode, numberOnPack);
                            }
                        }

                        PositionScanWindowManager.getInstance().setPositionScan(positionScan);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                        view.turnOnVibrator();
                        view.startMusicSuccess();
                    }
                });
            }
        });
    }

    @Override
    public void updateNumberScan(long logId, int number, String packCode, int numberOnPack, int totalNumber) {
        //cập nhật lại sl chi tiết
        localRepository.updateNumberScanPackagingWindow(packCode, numberOnPack, logId, number).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
//lấy lại tổng sl chi tiết trong gói có đủ chưa để quét gói khác
                localRepository.getTotalNumberDetaiLInPackWindow(packCode,
                        numberOnPack).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (totalNumber == integer) {
                            positionScan = null;
                        } else {
                            positionScan = new PositionScanWindow(packCode,
                                    numberOnPack
                            );
                            // PositionScanManager.getInstance().addPositionScan(logScanPackaging.getOrderId(), logScanPackaging.getProductSetId(), positionScan);
                        }
                        PositionScanWindowManager.getInstance().setPositionScan(positionScan);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
                        view.turnOnVibrator();
                        view.startMusicSuccess();
                    }
                });
            }
        });
    }

    @Override
    public void checkBarcode(String barcode, int direction) {
        barcode = barcode.toUpperCase();

        ProductPackagingWindowEntity entity = ListProductPackagingWindowManager.getInstance()
                .getDetailByBarcode(barcode);
        if (entity == null) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
            return;
        }
        //kiểm tra gói hiện tại đang quét
        if (positionScan != null) {
            GroupSetEntity groupSetEntity = null;
            for (GroupSetEntity item : entity.getListGroup()) {
                //kiểm tra chi tiết có nằm trong gói hiện tại đang quét hay khôg
                if (item.getPackCode().equals(positionScan.getPackCode()) && item.getNumberSetOnPack() == positionScan.getNumberPack()) {
                    groupSetEntity = item;
                    break;
                }
            }

            if (groupSetEntity == null) {
                showError(CoreApplication.getInstance().getString(R.string.text_incomplete_pack));
                return;
            }

            //kiểm tra sl đã quét đủ chưa
            if (entity.getNumberTotal() == entity.getNumberScaned()) {
                showError(CoreApplication.getInstance().getString(R.string.text_detail_scan_enough));
                return;
            }
            saveBarcode(entity, direction, groupSetEntity);

        } else {
            if (entity.getNumberTotal() == entity.getNumberScaned()) {
                showError(CoreApplication.getInstance().getString(R.string.text_detail_scan_enough));
                return;
            }
            if (entity.getListGroup().size() > 1) {
                view.showDialogChooseSet(entity);
            } else {
                saveBarcode(entity, direction, entity.getListGroup().get(0));

            }
        }


    }

    @Override
    public void saveBarcode(ProductPackagingWindowEntity entity, int direction, GroupSetEntity groupSetEntity) {
        positionScan = new PositionScanWindow(groupSetEntity.getPackCode(), groupSetEntity.getNumberSetOnPack());
        //lấy chi tiết gói hàng cửa
        localRepository.getProductPackingWindow(entity).subscribe(new Action1<ProductPackWindowModel>() {
            @Override
            public void call(ProductPackWindowModel product) {
                if (product != null) {
                    //kiểm tra chi tiết sl có còn hay ko?
                    if (product.getNumberRest() > 0) {
                        //nếu còn thì lấy số lượng đã quét của chi tiết này theo mã gói và sl 1 set trong 1 gói
                        localRepository.getNumberScanWindowByBarcode(groupSetEntity.getPackCode(), groupSetEntity.getNumberSetOnPack(), entity.getBarcode()).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                //so sánh tổng số lượng đã lưu và sl còn lại có đủ cho sl của chi tiết  này ko
                                if (integer + product.getNumberRest() >= groupSetEntity.getNumberOnSet() * groupSetEntity.getNumberSetOnPack()) {
                                    localRepository.saveBarcodeScanPackagingWindow(product.getId(), direction, groupSetEntity)
                                            .subscribe(new Action1<Boolean>() {
                                                @Override
                                                public void call(Boolean satisfy) {

                                                    if (satisfy) {
                                                        //kiểm tra lại tổng số lượng trong gói đã đủ chưa, để được quét sang gói khác
                                                        localRepository.getTotalNumberDetaiLInPackWindow(groupSetEntity.getPackCode(), groupSetEntity.getNumberSetOnPack())
                                                                .subscribe(new Action1<Integer>() {
                                                                    @Override
                                                                    public void call(Integer integer) {
                                                                        if (groupSetEntity.getNumberTotal() == integer) {
                                                                            positionScan = null;
                                                                        } else {
                                                                            positionScan = new PositionScanWindow(groupSetEntity.getPackCode(),
                                                                                    groupSetEntity.getNumberSetOnPack());
                                                                        }
                                                                        PositionScanWindowManager.getInstance().setPositionScan(positionScan);
                                                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                                                                        view.startMusicSuccess();
                                                                        view.turnOnVibrator();
                                                                        view.setHeightListView();
                                                                    }
                                                                });
                                                    } else {
                                                        showError(CoreApplication.getInstance().getString(R.string.text_scan_enough_detail_in_pack));
                                                    }

                                                }
                                            });
                                } else {
                                    showError(CoreApplication.getInstance().getString(R.string.text_detail_not_enough));
                                }
                            }
                        });

                    } else {
                        showError(CoreApplication.getInstance().getString(R.string.text_detail_scan_enough));
                    }
                }
            }
        });
    }


    @Override
    public void deleteAllItemLog() {
        localRepository.deleteAllItemLogScanPackagingWindow().subscribe(new Action1<String>() {
            @Override
            public void call(String integer) {
                positionScan = null;
            }
        });
    }

    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }


}
