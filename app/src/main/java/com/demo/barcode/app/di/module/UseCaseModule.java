package com.demo.barcode.app.di.module;


import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;
import com.demo.architect.domain.AddLogQCUsecase;
import com.demo.architect.domain.ChangePasswordUsecase;
import com.demo.architect.domain.ConfirmInputUsecase;
import com.demo.architect.domain.DeactiveProductDetailGroupUsecase;
import com.demo.architect.domain.GetApartmentUsecase;
import com.demo.architect.domain.GetCodePackUsecase;
import com.demo.architect.domain.GetDateServerUsecase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetInputUnConfirmedUsecase;
import com.demo.architect.domain.GetListDepartmentUsecase;
import com.demo.architect.domain.GetListPrintPackageHistoryUsecase;
import com.demo.architect.domain.GetListProductDetailGroupUsecase;
import com.demo.architect.domain.GetListProductInPackageUsecase;
import com.demo.architect.domain.GetListReasonUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetModuleUsecase;
import com.demo.architect.domain.GroupProductDetailUsecase;
import com.demo.architect.domain.LoginUsecase;
import com.demo.architect.domain.PostCheckBarCodeUsecase;
import com.demo.architect.domain.PostListCodeProductDetailUsecase;
import com.demo.architect.domain.ScanProductDetailOutUsecase;
import com.demo.architect.domain.UpdateProductDetailGroupUsecase;
import com.demo.architect.domain.UpdateSoftUsecase;
import com.demo.architect.domain.UpdateVersionUsecase;
import com.demo.architect.domain.UploadImageUsecase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by uyminhduc on 12/16/16.
 */
@Module
public class UseCaseModule {
    public UseCaseModule() {
    }


    @Provides
    LoginUsecase provideLoginUsecase(AuthRepository remoteRepository) {
        return new LoginUsecase(remoteRepository);
    }

    @Provides
    ChangePasswordUsecase provideChangePasswordUsecase(AuthRepository remoteRepository) {
        return new ChangePasswordUsecase(remoteRepository);
    }


    @Provides
    UpdateVersionUsecase provideUpdateVersionUsecase(AuthRepository remoteRepository) {
        return new UpdateVersionUsecase(remoteRepository);
    }

    @Provides
    UpdateSoftUsecase provideUpdateSoftUsecase(AuthRepository authRepository) {
        return new UpdateSoftUsecase(authRepository);
    }

    @Provides
    ConfirmInputUsecase provideConfirmInputUsecase(OrderRepository orderRepository) {
        return new ConfirmInputUsecase(orderRepository);
    }

    @Provides
    GetInputUnConfirmedUsecase provideGetInputUnConfirmedUsecase(OrderRepository orderRepository) {
        return new GetInputUnConfirmedUsecase(orderRepository);
    }

    @Provides
    GetListDepartmentUsecase provideGetListDepartmentUsecase(OtherRepository otherRepository) {
        return new GetListDepartmentUsecase(otherRepository);
    }

    @Provides
    GetListSOUsecase provideGetListSOUsecase(OrderRepository orderRepository) {
        return new GetListSOUsecase(orderRepository);
    }

    @Provides
    ScanProductDetailOutUsecase provideScanProductDetailOutUsecase(OrderRepository orderRepository) {
        return new ScanProductDetailOutUsecase(orderRepository);
    }


    @Provides
    GetInputForProductDetailUsecase provideGetInputForProductDetailUsecase(ProductRepository productRepository) {
        return new GetInputForProductDetailUsecase(productRepository);
    }

    @Provides
    GroupProductDetailUsecase provideGroupProductDetailUsecase(ProductRepository productRepository) {
        return new GroupProductDetailUsecase(productRepository);
    }

    @Provides
    DeactiveProductDetailGroupUsecase provideDeactiveProductDetailGroupUsecase(ProductRepository productRepository) {
        return new DeactiveProductDetailGroupUsecase(productRepository);
    }

    @Provides
    GetListProductDetailGroupUsecase provideGetListProductDetailGroupUsecase(ProductRepository productRepository) {
        return new GetListProductDetailGroupUsecase(productRepository);
    }

    @Provides
    UpdateProductDetailGroupUsecase provideUpdateProductDetailGroupUsecase(ProductRepository productRepository) {
        return new UpdateProductDetailGroupUsecase(productRepository);
    }

    @Provides
    PostListCodeProductDetailUsecase providePostListCodeProductDetailUsecase(ProductRepository productRepository) {
        return new PostListCodeProductDetailUsecase(productRepository);
    }

    @Provides
    GetListReasonUsecase provideGetListReasonUsecase(OtherRepository otherRepository) {
        return new GetListReasonUsecase(otherRepository);
    }

    @Provides
    GetApartmentUsecase provideGetApartmentUsecase(OtherRepository otherRepository) {
        return new GetApartmentUsecase(otherRepository);
    }

    @Provides
    GetModuleUsecase provideGetModuleUsecase(OrderRepository orderRepository) {
        return new GetModuleUsecase(orderRepository);
    }

    @Provides
    GetCodePackUsecase provideGetCodePackUsecase(OrderRepository orderRepository) {
        return new GetCodePackUsecase(orderRepository);
    }

    @Provides
    PostCheckBarCodeUsecase providePostCheckBarCodeUsecase(OrderRepository orderRepository) {
        return new PostCheckBarCodeUsecase(orderRepository);
    }

    @Provides
    GetListProductInPackageUsecase provideGetListProductInPackageUsecase(ProductRepository productRepository) {
        return new GetListProductInPackageUsecase(productRepository);
    }

    @Provides
    GetListPrintPackageHistoryUsecase provideGetListPrintPackageHistoryUsecase(OrderRepository orderRepository) {
        return new GetListPrintPackageHistoryUsecase(orderRepository);
    }
    @Provides
    UploadImageUsecase provideUploadImageUsecase(OtherRepository otherRepository) {
        return new UploadImageUsecase(otherRepository);
    }
    @Provides
    AddLogQCUsecase provideAddLogQCUsecase(OtherRepository otherRepository) {
        return new AddLogQCUsecase(otherRepository);
    }


}

