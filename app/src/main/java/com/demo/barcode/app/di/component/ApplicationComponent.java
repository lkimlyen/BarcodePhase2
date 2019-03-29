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
import com.demo.barcode.screen.confirm_receive_window.ConfirmReceiveWindowComponent;
import com.demo.barcode.screen.confirm_receive_window.ConfirmReceiveWindowModule;
import com.demo.barcode.screen.create_pack_window.CreatePackagingWindowComponent;
import com.demo.barcode.screen.create_pack_window.CreatePackagingWindowModule;
import com.demo.barcode.screen.create_packaging.CreatePackagingComponent;
import com.demo.barcode.screen.create_packaging.CreatePackagingModule;
import com.demo.barcode.screen.dashboard.DashboardComponent;
import com.demo.barcode.screen.dashboard.DashboardModule;
import com.demo.barcode.screen.detail_error.DetailErrorComponent;
import com.demo.barcode.screen.detail_error.DetailErrorModule;
import com.demo.barcode.screen.detail_package.DetailPackageComponent;
import com.demo.barcode.screen.detail_package.DetailPackageModule;
import com.demo.barcode.screen.group_code.GroupCodeComponent;
import com.demo.barcode.screen.group_code.GroupCodeModule;
import com.demo.barcode.screen.history_pack.HistoryPackageComponent;
import com.demo.barcode.screen.history_pack.HistoryPackageModule;
import com.demo.barcode.screen.history_pack_window.HistoryPackWindowComponent;
import com.demo.barcode.screen.history_pack_window.HistoryPackWindowModule;
import com.demo.barcode.screen.login.LoginComponent;
import com.demo.barcode.screen.login.LoginModule;
import com.demo.barcode.screen.print_stamp.PrintStempComponent;
import com.demo.barcode.screen.print_stamp.PrintStempModule;
import com.demo.barcode.screen.print_stamp_window.PrintStempWindowComponent;
import com.demo.barcode.screen.print_stamp_window.PrintStempWindowModule;
import com.demo.barcode.screen.qc_window.QualityControlWindowComponent;
import com.demo.barcode.screen.qc_window.QualityControlWindowModule;
import com.demo.barcode.screen.quality_control.QualityControlComponent;
import com.demo.barcode.screen.quality_control.QualityControlModule;
import com.demo.barcode.screen.setting.SettingComponent;
import com.demo.barcode.screen.setting.SettingModule;
import com.demo.barcode.screen.stages.StagesComponent;
import com.demo.barcode.screen.stages.StagesModule;
import com.demo.barcode.screen.stages_window.StagesWindowComponent;
import com.demo.barcode.screen.stages_window.StagesWindowModule;

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

    QualityControlComponent plus(QualityControlModule module);

    DetailErrorComponent plus(DetailErrorModule module);

    GroupCodeComponent plus(GroupCodeModule module);

    StagesWindowComponent plus(StagesWindowModule module);

    ConfirmReceiveWindowComponent plus(ConfirmReceiveWindowModule module);

    QualityControlWindowComponent plus(QualityControlWindowModule module);

    CreatePackagingWindowComponent plus(CreatePackagingWindowModule module);

    PrintStempWindowComponent plus(PrintStempWindowModule module);

    HistoryPackWindowComponent plus(HistoryPackWindowModule module);
}
