package com.demo.barcode.screen.print_stamp_window;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {PrintStempWindowModule.class})
public interface PrintStempWindowComponent {
    void inject(PrintStempWindowActivity activity);

}
