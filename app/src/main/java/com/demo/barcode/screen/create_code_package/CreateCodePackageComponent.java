package com.demo.barcode.screen.create_code_package;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {CreateCodePackageModule.class})
public interface CreateCodePackageComponent {
    void inject(CreateCodePackageActivity activity);

}
