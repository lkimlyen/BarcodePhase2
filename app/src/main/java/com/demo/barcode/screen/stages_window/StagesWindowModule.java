package com.demo.barcode.screen.stages_window;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class StagesWindowModule {
    private final StagesWindowContract.View StagesView;

    public StagesWindowModule(StagesWindowContract.View StagesView) {
        this.StagesView = StagesView;
    }

    @Provides
    @NonNull
    StagesWindowContract.View provideStagesView() {
        return this.StagesView;
    }
}

