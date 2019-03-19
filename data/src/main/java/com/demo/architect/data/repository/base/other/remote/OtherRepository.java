package com.demo.architect.data.repository.base.other.remote;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ListReasonsEntity;
import com.demo.architect.data.model.MachineEntity;
import com.demo.architect.data.model.QCEntity;
import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.StaffEntity;
import com.demo.architect.data.model.TimesEntity;
import com.demo.architect.data.model.UploadEntity;

import java.io.File;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OtherRepository {
    Observable<BaseListResponse<DepartmentEntity>> getListDepartment();

    Observable<BaseResponse<ListReasonsEntity>> getRAndSQC();

    Observable<BaseResponse> addLogQC(String key, String json);
    Observable<BaseListResponse<StaffEntity>> getAllStaff(String key);

    Observable<BaseResponse> addLogQCWindow(String key, int machineId, String violator,
                                            int qcId, long orderId, int departmentId,
                                            long userId, String json);

    Observable<BaseResponse<UploadEntity>> uploadImage(File file, String key, long orderId,
                                                       int departmentId, String fileName, long userId);

    Observable<BaseListResponse<ApartmentEntity>> getApartment(long orderId);

    Observable<BaseResponse<TimesEntity>> getTimesInputAndOutputByDepartment(long orderId, int departmentId);

    Observable<BaseListResponse<MachineEntity>> getListMachine();
    Observable<BaseListResponse<QCEntity>> getListQC();
}
