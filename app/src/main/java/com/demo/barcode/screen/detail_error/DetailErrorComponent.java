package com.demo.barcode.screen.detail_error;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {DetailErrorModule.class})
public interface DetailErrorComponent {
    void inject(DetailErrorActivity activity);

}
