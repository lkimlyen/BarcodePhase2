package com.demo.barcode.app.di.component;


import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.app.base.BaseActivity;
import com.demo.barcode.app.base.BaseFragment;
import com.demo.barcode.app.di.module.ApplicationModule;
import com.demo.barcode.app.di.module.NetModule;
import com.demo.barcode.app.di.module.RepositoryModule;
import com.demo.barcode.app.di.module.UseCaseModule;
import com.demo.barcode.screen.chang_password.ChangePasswordComponent;
import com.demo.barcode.screen.chang_password.ChangePasswordModule;
import com.demo.barcode.screen.confirm_receive.ConfirmReceiveComponent;
import com.demo.barcode.screen.confirm_receive.ConfirmReceiveModule;
import com.demo.barcode.screen.create_packaging.CreatePackagingComponent;
import com.demo.barcode.screen.create_packaging.CreatePackagingModule;
import com.demo.barcode.screen.dashboard.DashboardComponent;
import com.demo.barcode.screen.dashboard.DashboardModule;
import com.demo.barcode.screen.detail_package.DetailPackageComponent;
import com.demo.barcode.screen.detail_package.DetailPackageModule;
import com.demo.barcode.screen.history_pack.HistoryPackageComponent;
import com.demo.barcode.screen.history_pack.HistoryPackageModule;
import com.demo.barcode.screen.login.LoginComponent;
import com.demo.barcode.screen.login.LoginModule;
import com.demo.barcode.screen.print_stamp.PrintStempComponent;
import com.demo.barcode.screen.print_stamp.PrintStempModule;
import com.demo.barcode.screen.setting.SettingComponent;
import com.demo.barcode.screen.setting.SettingModule;
import com.demo.barcode.screen.stages.StagesComponent;
import com.demo.barcode.screen.stages.StagesModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by uyminhduc on 12/16/16.
 */

@Singleton
@Component(modules = {ApplicationModule.class,
        NetModule.class,
        UseCaseModule.class,
        RepositoryModule.class})
public interface ApplicationComponent {

    void inject(CoreApplication application);

    void inject(BaseActivity activity);

    void inject(BaseFragment fragment);

    LoginComponent plus(LoginModule module);

    CreatePackagingComponent plus(CreatePackagingModule module);

    DashboardComponent plus(DashboardModule module);

    SettingComponent plus(SettingModule module);

    ChangePasswordComponent plus(ChangePasswordModule module);

    PrintStempComponent plus(PrintStempModule module);

    HistoryPackageComponent plus(HistoryPackageModule module);

    DetailPackageComponent plus(DetailPackageModule module);

    StagesComponent plus(StagesModule module);

    ConfirmReceiveComponent plus(ConfirmReceiveModule module);

}
