package com.demo.architect.data.repository.base.other.remote;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ResultEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OtherRepository {
    Observable<BaseResponse<DepartmentEntity>> getListDepartment();


}
