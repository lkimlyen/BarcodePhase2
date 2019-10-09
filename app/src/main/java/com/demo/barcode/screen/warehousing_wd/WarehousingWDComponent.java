package com.demo.barcode.screen.warehousing_wd;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {WarehousingWDModule.class})
public interface WarehousingWDComponent {
    void inject(WarehousingWDActivity activity);

}
