package com.demo.architect.data.repository.base.other.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.UploadEntity;

import java.io.File;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OtherRepository {
    Observable<BaseListResponse<DepartmentEntity>> getListDepartment();

    Observable<BaseListResponse<ReasonsEntity>> getRAndSQC();

    Observable<BaseResponse> addLogQC(String key, String json);

    Observable<BaseResponse<UploadEntity>> uploadImage(File file, String key, int orderId,
                                                       int departmentId, String fileName, String userId);

}
