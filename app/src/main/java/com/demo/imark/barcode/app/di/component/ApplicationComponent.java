package com.demo.imark.barcode.app.di.component;

import com.goitho.employeeapp.app.CoreApplication;
import com.goitho.employeeapp.app.base.BaseActivity;
import com.goitho.employeeapp.app.base.BaseFragment;
import com.goitho.employeeapp.app.di.module.ApplicationModule;
import com.goitho.employeeapp.app.di.module.NetModule;
import com.goitho.employeeapp.app.di.module.RepositoryModule;
import com.goitho.employeeapp.app.di.module.UseCaseModule;
import com.goitho.employeeapp.screen.dashboard.DashboardComponent;
import com.goitho.employeeapp.screen.dashboard.DashboardModule;
import com.goitho.employeeapp.screen.detail_order.DetailOrderComponent;
import com.goitho.employeeapp.screen.detail_order.DetailOrderModule;
import com.goitho.employeeapp.screen.detail_promotion.DetailPromotionComponent;
import com.goitho.employeeapp.screen.detail_promotion.DetailPromotionModule;
import com.goitho.employeeapp.screen.edit_account.EditAccountComponent;
import com.goitho.employeeapp.screen.edit_account.EditAccountModule;
import com.goitho.employeeapp.screen.edit_profile.EditProfileComponent;
import com.goitho.employeeapp.screen.edit_profile.EditProfileModule;
import com.goitho.employeeapp.screen.history.HistoryComponent;
import com.goitho.employeeapp.screen.history.HistoryModule;
import com.goitho.employeeapp.screen.history.failed_history.FailedHistoryModule;
import com.goitho.employeeapp.screen.income.IncomeComponent;
import com.goitho.employeeapp.screen.income.IncomeModule;
import com.goitho.employeeapp.screen.employeewallet.EmployeeWalletComponent;
import com.goitho.employeeapp.screen.employeewallet.EmployeeWalletModule;
import com.goitho.employeeapp.screen.login.LoginComponent;
import com.goitho.employeeapp.screen.login.LoginModule;
import com.goitho.employeeapp.screen.notification.NotificationComponent;
import com.goitho.employeeapp.screen.notification.NotificationModule;
import com.goitho.employeeapp.screen.order.OrderComponent;
import com.goitho.employeeapp.screen.order.OrderModule;
import com.goitho.employeeapp.screen.phone_verification.PhoneVerificationComponent;
import com.goitho.employeeapp.screen.phone_verification.PhoneVerificationModule;
import com.goitho.employeeapp.screen.promotion.PromotionComponent;
import com.goitho.employeeapp.screen.promotion.PromotionModule;
import com.goitho.employeeapp.screen.register.RegisterComponent;
import com.goitho.employeeapp.screen.register.RegisterModule;
import com.goitho.employeeapp.screen.register_success.RegisterSuccessComponent;
import com.goitho.employeeapp.screen.register_success.RegisterSuccessModule;
import com.goitho.employeeapp.screen.screen_goitho.profile.ProfileComponent;
import com.goitho.employeeapp.screen.screen_goitho.profile.ProfileModule;
import com.goitho.employeeapp.screen.splash.SplashComponent;
import com.goitho.employeeapp.screen.splash.SplashModule;
import com.goitho.employeeapp.screen.history.success_history.SuccessHistoryModule;
import com.goitho.employeeapp.screen.technique.TechniqueComponent;
import com.goitho.employeeapp.screen.technique.TechniqueModule;
import com.goitho.employeeapp.screen.wallet_pay.WalletPayComponent;
import com.goitho.employeeapp.screen.wallet_pay.WalletPayModule;

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

    SplashComponent plus(SplashModule splashModule);
    DetailOrderComponent plus(DetailOrderModule detailOrderModule);
    WalletPayComponent plus(WalletPayModule walletPayModule);

    LoginComponent plus(LoginModule loginModule);

    IncomeComponent plus(IncomeModule incomeModule);
    EmployeeWalletComponent plus(EmployeeWalletModule employeeWalletModule);

    RegisterSuccessComponent plus(RegisterSuccessModule registerSuccessModule);

    RegisterComponent plus(RegisterModule registerModule);

    EditProfileComponent plus(EditProfileModule editProfileModule);

    HistoryComponent plus(SuccessHistoryModule successHistoryModule, FailedHistoryModule failedHistoryModule);

    DashboardComponent plus(OrderModule orderModule, EmployeeWalletModule employeeWalletModule, ProfileModule profileModule,
                            HistoryModule historyModule, PromotionModule promotionModule, NotificationModule notificationModule,
                            DashboardModule dashboardModule);

    ProfileComponent plus(ProfileModule profileModule);

    PhoneVerificationComponent plus(PhoneVerificationModule phoneVerificationModule);

    NotificationComponent plus(NotificationModule notificationModule);

    EditAccountComponent plus(EditAccountModule editAccountModule);

    TechniqueComponent plus(TechniqueModule techniqueModule);

    PromotionComponent plus(PromotionModule promotionModule);

    OrderComponent plus(OrderModule orderModule);

    DetailPromotionComponent plus(DetailPromotionModule detailPromotionModule);
}
