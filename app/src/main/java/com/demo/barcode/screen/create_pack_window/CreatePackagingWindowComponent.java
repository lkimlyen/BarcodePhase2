package com.demo.barcode.screen.create_pack_window;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {CreatePackagingWindowModule.class})
public interface CreatePackagingWindowComponent {
    void inject(CreatePackagingWindowActivity activity);

}
