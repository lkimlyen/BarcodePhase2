package com.demo.architect.data.repository.base.account.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UpdateAppResponse;
import com.demo.architect.data.model.UserEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface AuthRepository {
    Observable<BaseResponse<UserEntity>> login(String type, String username, String password);
    Observable<BaseListResponse> changePassword(String userId, String oldPass, String newPass);
    Observable<BaseListResponse> updateSoft(String appCode, int userId, String version, int numNotUpdate,
                                            String dateServer, String ime);
    Observable<UpdateAppResponse> getUpdateVersionACR();
    Observable<String> getDateServer();
}
