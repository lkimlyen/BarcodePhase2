package com.demo.barcode.screen.create_packaging;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {CreatePackagingModule.class})
public interface CreatePackagingComponent {
    void inject(CreatePackagingActivity activity);

}
