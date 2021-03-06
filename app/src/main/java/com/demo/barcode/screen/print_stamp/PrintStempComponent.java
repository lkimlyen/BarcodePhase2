package com.demo.barcode.screen.print_stamp;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {PrintStempModule.class})
public interface PrintStempComponent {
    void inject(PrintStempActivity activity);

}
