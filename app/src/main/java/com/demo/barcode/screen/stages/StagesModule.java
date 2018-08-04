package com.demo.barcode.screen.stages;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class StagesModule {
    private final StagesContract.View CreateCodePackageView;

    public StagesModule(StagesContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    StagesContract.View provideCreateCodePackageView() {
        return this.CreateCodePackageView;
    }
}

