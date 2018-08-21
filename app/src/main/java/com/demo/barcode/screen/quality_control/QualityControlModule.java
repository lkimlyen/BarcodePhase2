package com.demo.barcode.screen.quality_control;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class QualityControlModule {
    private final QualityControlContract.View QualityControlView;

    public QualityControlModule(QualityControlContract.View QualityControlView) {
        this.QualityControlView = QualityControlView;
    }

    @Provides
    @NonNull
    QualityControlContract.View provideQualityControlView() {
        return this.QualityControlView;
    }
}

