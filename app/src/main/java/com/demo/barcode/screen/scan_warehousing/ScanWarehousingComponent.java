package com.demo.barcode.screen.scan_warehousing;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ScanWarehousingModule.class})
public interface ScanWarehousingComponent {
    void inject(ScanWarehousingActivity activity);

}
