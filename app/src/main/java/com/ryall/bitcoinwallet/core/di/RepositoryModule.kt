package com.ryall.bitcoinwallet.core.di

import com.ryall.bitcoinwallet.data.repository.WalletRepositoryImpl
import com.ryall.bitcoinwallet.domain.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        implementation: WalletRepositoryImpl
    ) : WalletRepository
}