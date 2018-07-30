package com.demo.barcode.screen.import_works;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ImportWorksModule.class})
public interface ImportWorksComponent {
    void inject(ImportWorksActivity activity);

}
