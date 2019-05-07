package com.demo.barcode.screen.warehousing_wd;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class WarehousingWDModule {
    private final WarehousingWDContract.View StagesView;

    public WarehousingWDModule(WarehousingWDContract.View StagesView) {
        this.StagesView = StagesView;
    }

    @Provides
    @NonNull
    WarehousingWDContract.View provideStagesView() {
        return this.StagesView;
    }
}

