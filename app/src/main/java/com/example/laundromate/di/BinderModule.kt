package com.example.laundromate.di

import com.example.laundromate.data.repository.AuthRepositoryImpl
import com.example.laundromate.data.repository.LaundryRepositoryImpl
import com.example.laundromate.domain.repository.AuthRepository
import com.example.laundromate.domain.repository.LaundryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BinderModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindLaundryRepository(
        laundryRepositoryImpl: LaundryRepositoryImpl
    ): LaundryRepository
}