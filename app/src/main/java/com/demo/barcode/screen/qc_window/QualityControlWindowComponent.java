package com.demo.barcode.screen.qc_window;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {QualityControlWindowModule.class})
public interface QualityControlWindowComponent {
    void inject(QualityControlWindowActivity activity);

}
