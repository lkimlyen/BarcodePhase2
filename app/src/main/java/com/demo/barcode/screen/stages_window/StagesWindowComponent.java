package com.demo.barcode.screen.stages_window;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {StagesWindowModule.class})
public interface StagesWindowComponent {
    void inject(StagesWindowActivity activity);

}
