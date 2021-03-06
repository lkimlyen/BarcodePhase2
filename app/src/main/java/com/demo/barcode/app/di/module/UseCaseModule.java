package com.demo.barcode.app.di.module;


import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;
import com.demo.architect.data.repository.base.order.remote.OrderRepositoryImpl;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;
import com.demo.architect.domain.AddLogQCUsecase;
import com.demo.architect.domain.AddLogQCWindowUsecase;
import com.demo.architect.domain.AddPhieuGiaoNhanUsecase;
import com.demo.architect.domain.AddScanTemHangCuaUsecase;
import com.demo.architect.domain.ChangePasswordUsecase;
import com.demo.architect.domain.CheckUpdateForGroupUsecase;
import com.demo.architect.domain.ConfirmInputUsecase;
import com.demo.architect.domain.ConfirmInputWindowUsecase;
import com.demo.architect.domain.DeactiveProductDetailGroupUsecase;
import com.demo.architect.domain.GetAllStaffUsecase;
import com.demo.architect.domain.GetApartmentUsecase;
import com.demo.architect.domain.GetCodePackUsecase;
import com.demo.architect.domain.GetDateServerUsecase;
import com.demo.architect.domain.GetDetailInByDeliveryWindowUsecase;
import com.demo.architect.domain.GetHistoryIntemCuaUsecase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetInputForProductDetailWindowUsecase;
import com.demo.architect.domain.GetInputForProductWarehouseUsecase;
import com.demo.architect.domain.GetInputUnConfirmedUsecase;
import com.demo.architect.domain.GetListDepartmentUsecase;
import com.demo.architect.domain.GetListInputUnConfirmByMaPhieuUsecase;
import com.demo.architect.domain.GetListMaPhieuGiaoUsecase;
import com.demo.architect.domain.GetListMachineUsecase;
import com.demo.architect.domain.GetListModuleByOrderUsecase;
import com.demo.architect.domain.GetListPrintPackageHistoryUsecase;
import com.demo.architect.domain.GetListProductDetailGroupUsecase;
import com.demo.architect.domain.GetListProductInPackageUsecase;
import com.demo.architect.domain.GetListQCUsecase;
import com.demo.architect.domain.GetListReasonUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetListSOWarehouseUsecase;
import com.demo.architect.domain.GetModuleUsecase;
import com.demo.architect.domain.GetProductSetDetailBySetAndDirecUsecase;
import com.demo.architect.domain.GetProductSetUsecase;
import com.demo.architect.domain.GetTimesInputAndOutputByDepartmentUsecase;
import com.demo.architect.domain.GroupProductDetailUsecase;
import com.demo.architect.domain.LoginUsecase;
import com.demo.architect.domain.PostCheckBarCodeUsecase;
import com.demo.architect.domain.PostListCodeProductDetailUsecase;
import com.demo.architect.domain.ScanProductDetailOutUsecase;
import com.demo.architect.domain.ScanProductDetailOutWindowUsecase;
import com.demo.architect.domain.ScanWarehousingUsecase;
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
    GetListSOWarehouseUsecase provideGetListSOWarehouseUsecase(OrderRepository orderRepository) {
        return new GetListSOWarehouseUsecase(orderRepository);
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
    GetInputForProductWarehouseUsecase provideGetInputForProductWarehouseUsecase(ProductRepository productRepository) {
        return new GetInputForProductWarehouseUsecase(productRepository);
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

    @Provides
    GetListModuleByOrderUsecase provideGetListModuleByOrderUsecase(OrderRepository orderRepository) {
        return new GetListModuleByOrderUsecase(orderRepository);
    }

    @Provides
    GetTimesInputAndOutputByDepartmentUsecase provideGetTimesInputAndOutputByDepartmentUsecase(OtherRepository otherRepository) {
        return new GetTimesInputAndOutputByDepartmentUsecase(otherRepository);
    }

    @Provides
    CheckUpdateForGroupUsecase provideCheckUpdateForGroupUsecase(ProductRepository productRepository) {
        return new CheckUpdateForGroupUsecase(productRepository);
    }

    @Provides
    AddPhieuGiaoNhanUsecase provideAddPhieuGiaoNhanUsecase(ProductRepository productRepository) {
        return new AddPhieuGiaoNhanUsecase(productRepository);
    }

    @Provides
    GetListMaPhieuGiaoUsecase provideAddGetListMaPhieuGiaoUsecase(OrderRepository productRepository) {
        return new GetListMaPhieuGiaoUsecase(productRepository);
    }

    @Provides
    GetListInputUnConfirmByMaPhieuUsecase provideGetListInputUnConfirmByMaPhieuUsecase(OrderRepository productRepository) {
        return new GetListInputUnConfirmByMaPhieuUsecase(productRepository);
    }

    @Provides
    GetInputForProductDetailWindowUsecase provideGetInputForProductDetailWindowUsecase(ProductRepository productRepository) {
        return new GetInputForProductDetailWindowUsecase(productRepository);
    }

    @Provides
    ScanProductDetailOutWindowUsecase provideScanProductDetailOutWindowUsecase(OrderRepository productRepository) {
        return new ScanProductDetailOutWindowUsecase(productRepository);
    }

    @Provides
    ConfirmInputWindowUsecase provideConfirmInputWindowUsecase(OrderRepository orderRepository) {
        return new ConfirmInputWindowUsecase(orderRepository);
    }

    @Provides
    GetDetailInByDeliveryWindowUsecase provideGetDetailInByDeliveryWindowUsecase(OrderRepository orderRepository) {
        return new GetDetailInByDeliveryWindowUsecase(orderRepository);
    }

    @Provides
    AddLogQCWindowUsecase provideAddLogQCWindowUsecase(OtherRepository otherRepository) {
        return new AddLogQCWindowUsecase(otherRepository);
    }


    @Provides
    GetAllStaffUsecase provideGetAllStaffUsecase(OtherRepository otherRepository) {
        return new GetAllStaffUsecase(otherRepository);
    }


    @Provides
    GetListQCUsecase provideGetListQCUsecase(OtherRepository otherRepository) {
        return new GetListQCUsecase(otherRepository);
    }


    @Provides
    GetListMachineUsecase provideGetListMachineUsecase(OtherRepository otherRepository) {
        return new GetListMachineUsecase(otherRepository);
    }


    @Provides
    AddScanTemHangCuaUsecase provideAddScanTemHangCuaUsecase(ProductRepository otherRepository) {
        return new AddScanTemHangCuaUsecase(otherRepository);
    }

    @Provides
    GetProductSetDetailBySetAndDirecUsecase provideGetProductSetDetailBySetAndDirecUsecase(ProductRepository otherRepository) {
        return new GetProductSetDetailBySetAndDirecUsecase(otherRepository);
    }

    @Provides
    GetProductSetUsecase provideGetProductSetUsecase(OtherRepository otherRepository) {
        return new GetProductSetUsecase(otherRepository);
    }

    @Provides
    GetHistoryIntemCuaUsecase provideGetHistoryIntemCuaUsecase(ProductRepository otherRepository) {
        return new GetHistoryIntemCuaUsecase(otherRepository);
    }
    @Provides
    ScanWarehousingUsecase provideScanWarehousingUsecase(OrderRepository orderRepository) {
        return new ScanWarehousingUsecase(orderRepository);
    }

}

