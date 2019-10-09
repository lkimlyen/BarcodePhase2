package com.demo.barcode.screen.stages;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class StagesModule {
    private final StagesContract.View StagesView;

    public StagesModule(StagesContract.View StagesView) {
        this.StagesView = StagesView;
    }

    @Provides
    @NonNull
    StagesContract.View provideStagesView() {
        return this.StagesView;
    }
}

