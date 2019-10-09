package com.demo.barcode.screen.confirm_receive;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ConfirmReceiveModule.class})
public interface ConfirmReceiveComponent {
    void inject(ConfirmReceiveActivity activity);

}
