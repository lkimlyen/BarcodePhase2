package com.demo.barcode.screen.print_stamp_window;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class PrintStempWindowModule {
    private final PrintStempWindowContract.View CreateCodePackageView;

    public PrintStempWindowModule(PrintStempWindowContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    PrintStempWindowContract.View providePrintStempView() {
        return this.CreateCodePackageView;
    }
}

