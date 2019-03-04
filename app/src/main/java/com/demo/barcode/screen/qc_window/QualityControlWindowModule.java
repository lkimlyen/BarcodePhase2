package com.demo.barcode.screen.qc_window;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class QualityControlWindowModule {
    private final QualityControlWindowContract.View QualityControlView;

    public QualityControlWindowModule(QualityControlWindowContract.View QualityControlView) {
        this.QualityControlView = QualityControlView;
    }

    @Provides
    @NonNull
    QualityControlWindowContract.View provideQualityControlView() {
        return this.QualityControlView;
    }
}

