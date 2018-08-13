package com.demo.architect.data.repository.base.other.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.DepartmentEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OtherRepository {
    Observable<BaseListResponse<DepartmentEntity>> getListDepartment();


}
