package com.demo.barcode.screen.history_pack;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {HistoryPackageModule.class})
public interface HistoryPackageComponent {
    void inject(HistoryPackageActivity activity);

}
