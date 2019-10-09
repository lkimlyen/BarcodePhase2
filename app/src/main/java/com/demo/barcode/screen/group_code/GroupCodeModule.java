package com.demo.barcode.screen.group_code;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class GroupCodeModule {
    private final GroupCodeContract.View CreateCodePackageView;

    public GroupCodeModule(GroupCodeContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    GroupCodeContract.View provideHistoryPackageView() {
        return this.CreateCodePackageView;
    }
}

