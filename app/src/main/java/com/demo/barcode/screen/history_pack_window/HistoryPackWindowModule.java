package com.demo.barcode.screen.history_pack_window;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class HistoryPackWindowModule {
    private final HistoryPackWindowContract.View CreateCodePackageView;

    public HistoryPackWindowModule(HistoryPackWindowContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    HistoryPackWindowContract.View provideHistoryPackageView() {
        return this.CreateCodePackageView;
    }
}

