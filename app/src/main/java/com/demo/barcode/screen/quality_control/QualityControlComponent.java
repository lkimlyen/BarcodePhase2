package com.demo.barcode.screen.quality_control;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {QualityControlModule.class})
public interface QualityControlComponent {
    void inject(QualityControlActivity activity);

}
