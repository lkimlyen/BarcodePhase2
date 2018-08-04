package com.demo.barcode.screen.stages;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {StagesModule.class})
public interface StagesComponent {
    void inject(StagesActivity activity);

}
