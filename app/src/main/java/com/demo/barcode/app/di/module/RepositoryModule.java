package com.demo.barcode.app.di.module;


import android.content.Context;

import com.demo.architect.data.repository.base.account.remote.AuthRepository;
import com.demo.architect.data.repository.base.account.remote.AuthRepositoryImpl;
import com.demo.architect.data.repository.base.local.DatabaseRealm;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.local.LocalRepositoryImpl;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;
import com.demo.architect.data.repository.base.order.remote.OrderRepositoryImpl;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;
import com.demo.architect.data.repository.base.other.remote.OtherRepositoryImpl;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;
import com.demo.architect.data.repository.base.product.remote.ProductRepositoryImpl;
import com.demo.architect.data.repository.base.remote.RemoteRepository;
import com.demo.architect.data.repository.base.remote.RemoteRepositoryImpl;
import com.demo.barcode.app.CoreApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public LocalRepository provideMessageRepository() {
        return new LocalRepositoryImpl(CoreApplication.getInstance());
    }

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseRealm() {
        return new DatabaseRealm();
    }

    @Provides
    @Singleton
    RemoteRepository provideRemoteRepository(RemoteRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }

    @Provides
    @Singleton
    AuthRepository provideAuthRepository(AuthRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }

    @Provides
    @Singleton
    OrderRepository provideOrderRepository(OrderRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }

    @Provides
    @Singleton
    ProductRepository provideProductRepository(ProductRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }
    @Provides
    @Singleton
    OtherRepository provideOtherRepository(OtherRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }

}
