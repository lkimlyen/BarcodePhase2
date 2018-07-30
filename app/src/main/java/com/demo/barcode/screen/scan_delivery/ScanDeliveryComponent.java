package com.demo.barcode.screen.scan_delivery;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ScanDeliveryModule.class})
public interface ScanDeliveryComponent {
    void inject(ScanDeliveryActivity activity);

}
