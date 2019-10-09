package com.demo.barcode.screen.history_pack_window;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {HistoryPackWindowModule.class})
public interface HistoryPackWindowComponent {
    void inject(HistoryPackWindowActivity activity);

}
