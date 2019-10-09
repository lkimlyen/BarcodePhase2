package com.demo.barcode.screen.chang_password;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ChangePasswordModule.class})
public interface ChangePasswordComponent {
    void inject(ChangePasswordActivity activity);

}
