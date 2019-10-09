package com.demo.barcode.screen.setting;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {SettingModule.class})
public interface SettingComponent {
    void inject(SettingActivity activity);

}
