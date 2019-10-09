package com.demo.barcode.screen.confirm_receive_window;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ConfirmReceiveWindowModule.class})
public interface ConfirmReceiveWindowComponent {
    void inject(ConfirmReceiveWindowActivity activity);

}
