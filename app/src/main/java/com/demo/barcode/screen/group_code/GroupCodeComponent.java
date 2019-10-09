package com.demo.barcode.screen.group_code;


import com.demo.barcode.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {GroupCodeModule.class})
public interface GroupCodeComponent {
    void inject(GroupCodeActivity activity);

}
