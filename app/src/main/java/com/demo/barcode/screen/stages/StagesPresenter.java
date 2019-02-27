package com.demo.barcode.screen.stages;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.GroupScan;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocketDelivery;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.CheckUpdateForGroupUsecase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetListProductDetailGroupUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetTimesInputAndOutputByDepartmentUsecase;
import com.demo.architect.domain.ScanProductDetailOutUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListGroupManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class StagesPresenter implements StagesContract.Presenter {

    private final String TAG = StagesPresenter.class.getName();
    private final StagesContract.View view;
    private final GetInputForProductDetailUsecase getInputForProductDetail;
    private final GetListSOUsecase getListSOUsecase;
    private final ScanProductDetailOutUsecase scanProductDetailOutUsecase;
    private final GetListProductDetailGroupUsecase getListProductDetailGroupUsecase;
    private final GetTimesInputAndOutputByDepartmentUsecase getTimesInputAndOutputByDepartmentUsecase;
    private final CheckUpdateForGroupUsecase checkUpdateForGroupUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    StagesPresenter(@NonNull StagesContract.View view,
                    GetInputForProductDetailUsecase getInputForProductDetail, GetListSOUsecase getListSOUsecase, ScanProductDetailOutUsecase scanProductDetailOutUsecase, GetListProductDetailGroupUsecase getListProductDetailGroupUsecase, GetTimesInputAndOutputByDepartmentUsecase getTimesInputAndOutputByDepartmentUsecase, CheckUpdateForGroupUsecase checkUpdateForGroupUsecase) {
        this.view = view;
        this.getInputForProductDetail = getInputForProductDetail;
        this.getListSOUsecase = getListSOUsecase;
        this.scanProductDetailOutUsecase = scanProductDetailOutUsecase;
        this.getListProductDetailGroupUsecase = getListProductDetailGroupUsecase;
        this.getTimesInputAndOutputByDepartmentUsecase = getTimesInputAndOutputByDepartmentUsecase;
        this.checkUpdateForGroupUsecase = checkUpdateForGroupUsecase;
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

    private boolean allowedToSave = true;

    @Override
    public void checkBarcode(String barcode, int departmentId, int times, boolean groupCode) {
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        List<ProductEntity> list = ListProductManager.getInstance().getListProduct();

        if (list.size() == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_product_empty));
            return;
        }

        ProductEntity model = ListProductManager.getInstance().getProductByBarcode(barcode);
        if (model != null) {
            if (!groupCode) {
                // if (model.getListDepartmentID().contains(departmentId)) {
                localRepository.getProductDetail(model, times).subscribe(new Action1<ProductDetail>() {
                    @Override
                    public void call(ProductDetail productDetail) {
                        if (productDetail != null) {
                            if (productDetail.getNumberRest() > 0) {
                                saveBarcodeToDataBase(times, productDetail, 1, departmentId, null, true, false);
                            } else {
                                view.showCheckResidual(times, productDetail, departmentId);
                            }
                        } else {
                            showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_times));
                        }
                    }

                });

            } else {
                int count = ListGroupManager.getInstance().countProductById(model.getProductDetailID());
                if (count > 1) {
                    view.showDialogChooseGroup(ListGroupManager.getInstance().getListGroupEntityByProductId(model.getProductDetailID()));
                } else if (count == 1) {
                    ProductGroupEntity groupEntity = ListGroupManager.getInstance().getProductById(model.getProductDetailID());
                    GroupEntity groupEntityList = ListGroupManager.getInstance().getGroupEntityByGroupCode(groupEntity.getGroupCode());
                    saveBarcodeWithGroup(groupEntityList, times, departmentId);
                } else {
                    showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_group));
                    return;

                }

            }
        } else {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
        }
    }

    private int numberLoop = 0;
    private int countProductNull = 0;

    @Override
    public void saveBarcodeWithGroup(GroupEntity groupEntity, int times, int departmentId) {
        allowedToSave = true;
        numberLoop = 0;
        countProductNull = 0;
        for (ProductGroupEntity item : groupEntity.getProducGroupList()) {
            numberLoop++;
            ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());
            if (productEntity != null) {
                localRepository.getProductDetail(productEntity, times).subscribe(new Action1<ProductDetail>() {
                    @Override
                    public void call(ProductDetail productDetail) {

                        if (productDetail != null) {
                            if (productDetail.getNumberRest() > 0 && productDetail.getNumberRest() >= item.getNumber()) {
                               // allowedToSave = true;
                            } else {
                                allowedToSave = false;
                            }
                        } else {
                            countProductNull ++;

                        }

                        if (countProductNull == 0){
                            if (numberLoop == groupEntity.getProducGroupList().size()) {
                                if (allowedToSave) {
                                    saveListWithGroupCode(times, groupEntity, departmentId);
                                }else {
                                    showError(CoreApplication.getInstance().getString(R.string.text_exceed_the_number_of_requests_in_group));
                                }
                            }
                        }else {
                            showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_times));
                        }
                    }

                });
            }


        }


    }

    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

    private int count = 0;


    @Override
    public void deleteScanStages(long stagesId) {
        view.showProgressBar();
        localRepository.deleteScanStages(stagesId).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                view.refreshLayout();
            }
        });
    }

    @Override
    public void updateNumberScanInGroup(LogScanStages stages, double number) {
        allowedToSave = true;
        double numberOut = number - stages.getNumberInput();
        localRepository.getScanByProductDetailId(stages)
                .subscribe(new Action1<RealmResults<LogScanStages>>() {
                    @Override
                    public void call(RealmResults<LogScanStages> list) {
                        for (LogScanStages scanStages : list) {
                            final ProductDetail ProductDetail = scanStages.getProductDetail();
                            if (numberOut > ProductDetail.getNumberRest()) {
                                allowedToSave = false;
                                break;
                            }
                        }
                        if (!allowedToSave) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_quantity_input_bigger_quantity_rest_in_group));
                            updateNumberScan(stages.getId(), stages.getNumberInput(), false);
                        } else {
                            updateNumberScan(stages.getId(), number, true);
                        }


                    }
                });


    }


    @Override
    public void getListTimes(long orderId, int departmentId) {
        getTimesInputAndOutputByDepartmentUsecase.executeIO(new GetTimesInputAndOutputByDepartmentUsecase.RequestValue(orderId, departmentId),
                new BaseUseCase.UseCaseCallback<GetTimesInputAndOutputByDepartmentUsecase.ResponseValue, GetTimesInputAndOutputByDepartmentUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetTimesInputAndOutputByDepartmentUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListTimes(successResponse.getEntity().getListTimesOutput());
                    }

                    @Override
                    public void onError(GetTimesInputAndOutputByDepartmentUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void uploadData(long orderId) {
        view.showProgressBar();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        localRepository.getListGroupScanVersion().subscribe(new Action1<List<GroupScan>>() {
            @Override
            public void call(List<GroupScan> groupScans) {
                if (groupScans.size() > 0) {
                    checkUpdateForGroupUsecase.executeIO(new CheckUpdateForGroupUsecase.RequestValue(gson.toJson(groupScans)),
                            new BaseUseCase.UseCaseCallback<CheckUpdateForGroupUsecase.ResponseValue,
                                    CheckUpdateForGroupUsecase.ErrorValue>() {
                                @Override
                                public void onSuccess(CheckUpdateForGroupUsecase.ResponseValue successResponse) {
                                    localRepository.getListLogScanStagesUpload().subscribe(new Action1<List<LogScanStages>>() {
                                        @Override
                                        public void call(List<LogScanStages> list) {
                                            if (list.size() == 0) {
                                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_no_data_upload));
                                                return;
                                            }
                                            scanProductDetailOutUsecase.executeIO(new ScanProductDetailOutUsecase.RequestValue(gson.toJson(list)),
                                                    new BaseUseCase.UseCaseCallback<ScanProductDetailOutUsecase.ResponseValue,
                                                            ScanProductDetailOutUsecase.ErrorValue>() {
                                                        @Override
                                                        public void onSuccess(ScanProductDetailOutUsecase.ResponseValue successResponse) {
                                                            view.hideProgressBar();
                                                            localRepository.deleteAllScanStages().subscribe(new Action1<String>() {
                                                                @Override
                                                                public void call(String s) {
                                                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                                                    view.showPrintDeliveryNote(successResponse.getId());
                                                                    getListProduct(orderId, true);
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onError(ScanProductDetailOutUsecase.ErrorValue errorResponse) {
                                                            view.hideProgressBar();
                                                            view.showError(errorResponse.getDescription());
                                                        }
                                                    });
                                        }
                                    });

                                }

                                @Override
                                public void onError(CheckUpdateForGroupUsecase.ErrorValue errorResponse) {
                                    view.showError(errorResponse.getDescription());
                                    view.hideProgressBar();
                                    if (errorResponse.getEntity() != null) {
                                        ListGroupManager.getInstance().setListGroup(errorResponse.getEntity());
                                        localRepository.addGroupScan(errorResponse.getEntity()).subscribe();
                                    }
                                }
                            });
                } else {
                    localRepository.getListLogScanStagesUpload().subscribe(new Action1<List<LogScanStages>>() {
                        @Override
                        public void call(List<LogScanStages> list) {
                            if (list.size() == 0) {
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_no_data_upload));
                                return;
                            }
                            scanProductDetailOutUsecase.executeIO(new ScanProductDetailOutUsecase.RequestValue(gson.toJson(list)),
                                    new BaseUseCase.UseCaseCallback<ScanProductDetailOutUsecase.ResponseValue,
                                            ScanProductDetailOutUsecase.ErrorValue>() {
                                        @Override
                                        public void onSuccess(ScanProductDetailOutUsecase.ResponseValue successResponse) {
                                            view.hideProgressBar();

                                            localRepository.deleteAllScanStages().subscribe(new Action1<String>() {
                                                @Override
                                                public void call(String s) {
                                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                                    view.showPrintDeliveryNote(successResponse.getId());
                                                    getListProduct(orderId, true);


                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(ScanProductDetailOutUsecase.ErrorValue errorResponse) {
                                            view.hideProgressBar();
                                            view.showError(errorResponse.getDescription());
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void saveBarcodeToDataBase(int times, ProductDetail
            productDetail, double number, int departmentId, GroupEntity groupEntity, boolean typeScan, boolean residual) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        String groupCode = null;
        long groupCodeId = -1;
        if (groupEntity != null) {
            groupCode = groupEntity.getGroupCode();
            groupCodeId = groupEntity.getMasterGroupId();
        }
        LogScanStages logScanStages = new LogScanStages(productDetail.getOrderId(), departmentId, user.getRole(), productDetail.getProductDetailId(),
                groupCodeId, groupCode, productDetail.getBarcode(), productDetail.getModule(), number, typeScan, times,
                ConvertUtils.getDateTimeCurrent(), user.getId(), number);
        localRepository.addLogScanStagesAsync(logScanStages, productDetail.getId()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (!residual) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                    view.startMusicSuccess();
                }
                view.turnOnVibrator();
                view.hideProgressBar();
                view.refreshLayout();

            }
        });

    }

    @Override
    public void getListGroupCode(long orderId) {
        UserEntity user = UserManager.getInstance().getUser();
        getListProductDetailGroupUsecase.executeIO(new GetListProductDetailGroupUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetListProductDetailGroupUsecase.ResponseValue,
                        GetListProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListProductDetailGroupUsecase.ResponseValue successResponse) {

                        ListGroupManager.getInstance().setListGroup(successResponse.getEntity());
                        localRepository.addGroupScan(successResponse.getEntity()).subscribe();
                        getListTimes(orderId, user.getRole());
                    }

                    @Override
                    public void onError(GetListProductDetailGroupUsecase.ErrorValue errorResponse) {

                        ListGroupManager.getInstance().setListGroup(new ArrayList<>());
                        getListTimes(orderId, user.getRole());
                        //  view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void saveListWithGroupCode(int times, GroupEntity groupEntity, int departmentId) {
        for (ProductGroupEntity item : groupEntity.getProducGroupList()) {
            final ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());

            if (productEntity != null) {
                localRepository.getProductDetail(productEntity, times).subscribe(new Action1<ProductDetail>() {
                    @Override
                    public void call(ProductDetail productDetail) {
                        saveBarcodeToDataBase(times, productDetail, item.getNumber(), departmentId, groupEntity, false, false);
                    }

                });

            }

        }
    }


    @Override
    public void countListAllData(long orderId) {
        localRepository.countAllDetailWaitingUpload(orderId).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer > 0) {
                    view.showDialogUpload();
                }
            }
        });
    }

    @Override
    public void updateNumberScan(long stagesId, double number, boolean update) {
        view.showProgressBar();
        localRepository.updateNumberScanStages(stagesId, number).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (update) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                    view.startMusicSuccess();
                    view.turnOnVibrator();
                }
                view.hideProgressBar();

            }
        });
    }

    @Override
    public void print(int id, int idPrint) {
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                if (address == null) {
                    //view.showError(CoreApplication.getInstance().getString(R.string.text_no_ip_address));
                    view.showDialogCreateIPAddress(idPrint);
                    view.hideProgressBar();
                    return;
                }
                view.showProgressBar();
                ConnectSocketDelivery connectSocket = new ConnectSocketDelivery(address.getIpAddress(), address.getPortNumber(),
                        id, new ConnectSocketDelivery.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {

                            if (id == -1) {
                                print(idPrint, idPrint);


                            } else {
                                view.hideProgressBar();
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_print_success));


                            }
                        } else {
                            view.hideProgressBar();
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_connect_printer));

                        }
                    }
                });

                connectSocket.execute();
            }
        });
    }

    @Override
    public void saveIPAddress(String ipAddress, int port, int idPrint) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                print(-1, idPrint);
            }
        });
    }

    @Override
    public void deleteAllData() {

        localRepository.deleteAllScanStages().subscribe();
    }

    @Override
    public void getAllListStages() {
        localRepository.deleteAllScanStages().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                localRepository.getAllListStages().subscribe(new Action1<RealmResults<LogScanStages>>() {
                    @Override
                    public void call(RealmResults<LogScanStages> results) {
                        view.showListLogScanStages(results);
                    }
                });
            }
        });

    }


    @Override
    public void getListDepartment() {

        view.showListDepartment(ListDepartmentManager.getInstance().getListDepartment(
                UserManager.getInstance().getUser().getRole()
        ));
    }

    @Override
    public void getListSO(int orderType) {
        view.showProgressBar();
        getListSOUsecase.executeIO(new GetListSOUsecase.RequestValue(orderType),
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

                    }
                });
    }

    @Override
    public void getListProduct(long orderId, boolean refresh) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        getInputForProductDetail.executeIO(new GetInputForProductDetailUsecase.RequestValue(orderId, UserManager.getInstance().getUser().getRole()),
                new BaseUseCase.UseCaseCallback<GetInputForProductDetailUsecase.ResponseValue,
                        GetInputForProductDetailUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductDetailUsecase.ResponseValue successResponse) {

                        ListProductManager.getInstance().setListProduct(successResponse.getEntity());
                        localRepository.deleteAllProductDetail().subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (!refresh) {
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_detail_success));
                                }
                                getListGroupCode(orderId);
                            }
                        });


                    }

                    @Override
                    public void onError(GetInputForProductDetailUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductManager.getInstance().setListProduct(new ArrayList<>());

                    }
                });

    }

}
