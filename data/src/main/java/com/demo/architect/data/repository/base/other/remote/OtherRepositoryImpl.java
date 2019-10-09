package com.demo.architect.data.repository.base.other.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ListReasonsEntity;
import com.demo.architect.data.model.MachineEntity;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.model.QCEntity;
import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SetWindowEntity;
import com.demo.architect.data.model.StaffEntity;
import com.demo.architect.data.model.TimesEntity;
import com.demo.architect.data.model.UploadEntity;

import java.io.File;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import io.reactivex.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public class OtherRepositoryImpl implements OtherRepository {
    private final static String TAG = OtherRepositoryImpl.class.getName();

    private OtherApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public OtherRepositoryImpl(OtherApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
    }


    private void handleBaseResponse(Call<BaseResponse> call, ObservableEmitter<BaseResponse> emitter) {
        try {
            BaseResponse response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleDepartmentResponse(Call<BaseListResponse<DepartmentEntity>> call, ObservableEmitter<BaseListResponse<DepartmentEntity>> emitter) {
        try {
            BaseListResponse<DepartmentEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleStaffResponse(Call<BaseListResponse<StaffEntity>> call, ObservableEmitter<BaseListResponse<StaffEntity>> emitter) {
        try {
            BaseListResponse<StaffEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleApartmentResponse(Call<BaseListResponse<ApartmentEntity>> call, ObservableEmitter<BaseListResponse<ApartmentEntity>> emitter) {
        try {
            BaseListResponse<ApartmentEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleMachineResponse(Call<BaseListResponse<MachineEntity>> call, ObservableEmitter<BaseListResponse<MachineEntity>> emitter) {
        try {
            BaseListResponse<MachineEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleQCResponse(Call<BaseListResponse<QCEntity>> call, ObservableEmitter<BaseListResponse<QCEntity>> emitter) {
        try {
            BaseListResponse<QCEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleSetWindowResponse(Call<BaseListResponse<SetWindowEntity>> call, ObservableEmitter<BaseListResponse<SetWindowEntity>> emitter) {
        try {
            BaseListResponse<SetWindowEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleTimesResponse(Call<BaseResponse<TimesEntity>> call, ObservableEmitter<BaseResponse<TimesEntity>> emitter) {
        try {
            BaseResponse<TimesEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }


    private void handleUploadResponse(Call<BaseResponse<UploadEntity>> call, ObservableEmitter<BaseResponse<UploadEntity>> emitter) {
        try {
            BaseResponse<UploadEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleReasonResponse(Call<BaseResponse<ListReasonsEntity>> call, ObservableEmitter<BaseResponse<ListReasonsEntity>> emitter) {
        try {
            BaseResponse<ListReasonsEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
   

    @Override
    public Observable<BaseListResponse<DepartmentEntity>> getListDepartment() {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
   
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<DepartmentEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<DepartmentEntity>> emitter) throws Exception {
                handleDepartmentResponse(mRemoteApiInterface.getListDepartment(
                        server + "/WS/api/GD2GetListDepartment"), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<ListReasonsEntity>> getRAndSQC() {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<ListReasonsEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<ListReasonsEntity>> emitter) throws Exception {
                handleReasonResponse(mRemoteApiInterface.getRAndSQC(
                        server + "/WS/api/GD2GetRAndSQC"), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addLogQC(final String key, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addLogQC(
                        server + "/WS/api/GD2AddLogQC",key,json), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<StaffEntity>> getAllStaff(final String key) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<StaffEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<StaffEntity>> emitter) throws Exception {
                handleStaffResponse(mRemoteApiInterface.getAllStaff(
                        server + "/WS/api/GD2GetAllStaff",key), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addLogQCWindow(final String key, final int machineId,
                                                   final String violator, final int qcId,
                                                   final long orderId, final int departmentId, final long userId, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addLogQCWindow(
                        server + "/WS/api/GD2AddLogQCCua",key,machineId, violator,qcId,
                        orderId,departmentId,userId,json), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<UploadEntity>> uploadImage(final File file, final String key, final long orderId, final int departmentId, final String fileName, final long userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<UploadEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<UploadEntity>> emitter) throws Exception {
                handleUploadResponse(mRemoteApiInterface.uploadImage(server + "/ws/UploadImage/UploadFile", MultipartBody.Part
                                .createFormData("file", file.getName(),
                                        RequestBody.create(MultipartBody.FORM, file)),
                        RequestBody.create(MultipartBody.FORM, key),
                        RequestBody.create(MultipartBody.FORM, orderId+""),
                        RequestBody.create(MultipartBody.FORM, departmentId+""),
                        RequestBody.create(MultipartBody.FORM,fileName),
                        RequestBody.create(MultipartBody.FORM,userId+"")), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<ApartmentEntity>> getApartment(final long orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");


        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ApartmentEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ApartmentEntity>> emitter) throws Exception {
                handleApartmentResponse(mRemoteApiInterface.getApartment(
                        server + "/WS/api/GD2GetApartment",orderId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<TimesEntity>> getTimesInputAndOutputByDepartment(final long orderId, final int departmentId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<TimesEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<TimesEntity>> emitter) throws Exception {
                handleTimesResponse(mRemoteApiInterface.getTimesInputAndOutputByDepartment(
                        server + "/WS/api/GD2GetTimesInputAndOutputByDepartment",orderId,departmentId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<MachineEntity>> getListMachine() {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<MachineEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<MachineEntity>> emitter) throws Exception {
                handleMachineResponse(mRemoteApiInterface.getListMachine(
                        server + "/WS/api/GD2GetListMay"), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<QCEntity>> getListQC() {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<QCEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<QCEntity>> emitter) throws Exception {
                handleQCResponse(mRemoteApiInterface.getListQC(
                        server + "/WS/api/GD2GetListQC"), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<SetWindowEntity>> getProductSet(final long orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<SetWindowEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<SetWindowEntity>> emitter) throws Exception {
                handleSetWindowResponse(mRemoteApiInterface.getProductSet(
                        server + "/WS/api/GD2GetProductSet",orderId), emitter);
            }
        });
    }


}
