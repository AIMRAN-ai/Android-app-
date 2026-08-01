package com.aimr.aimrpos.di

import android.content.Context
import androidx.room.Room
import com.aimr.aimrpos.data.local.AimrPosDatabase
import com.aimr.aimrpos.data.local.dao.BusinessDao
import com.aimr.aimrpos.data.local.dao.CategoryDao
import com.aimr.aimrpos.data.local.dao.CustomerDao
import com.aimr.aimrpos.data.local.dao.InvoiceDao
import com.aimr.aimrpos.data.local.dao.InvoiceItemDao
import com.aimr.aimrpos.data.local.dao.ProductDao
import com.aimr.aimrpos.data.local.dao.UserDao
import com.aimr.aimrpos.data.repository.ProductRepositoryImpl
import com.aimr.aimrpos.data.repository.InvoiceRepositoryImpl
import com.aimr.aimrpos.data.repository.InvoiceItemRepositoryImpl
import com.aimr.aimrpos.data.repository.CustomerRepositoryImpl
import com.aimr.aimrpos.data.repository.UserRepositoryImpl
import com.aimr.aimrpos.data.repository.BusinessRepositoryImpl
import com.aimr.aimrpos.data.repository.CategoryRepositoryImpl
import com.aimr.aimrpos.domain.repository.ProductRepository
import com.aimr.aimrpos.domain.repository.InvoiceRepository
import com.aimr.aimrpos.domain.repository.InvoiceItemRepository
import com.aimr.aimrpos.domain.repository.CustomerRepository
import com.aimr.aimrpos.domain.repository.UserRepository
import com.aimr.aimrpos.domain.repository.BusinessRepository
import com.aimr.aimrpos.domain.repository.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AimrPosDatabase {
        return Room.databaseBuilder(
            context,
            AimrPosDatabase::class.java,
            "aimr_pos.db"
        ).build()
    }

    @Provides
    fun provideProductDao(database: AimrPosDatabase): ProductDao = database.productDao()

    @Provides
    fun provideInvoiceDao(database: AimrPosDatabase): InvoiceDao = database.invoiceDao()

    @Provides
    fun provideInvoiceItemDao(database: AimrPosDatabase): InvoiceItemDao = database.invoiceItemDao()

    @Provides
    fun provideCustomerDao(database: AimrPosDatabase): CustomerDao = database.customerDao()

    @Provides
    fun provideUserDao(database: AimrPosDatabase): UserDao = database.userDao()

    @Provides
    fun provideBusinessDao(database: AimrPosDatabase): BusinessDao = database.businessDao()

    @Provides
    fun provideCategoryDao(database: AimrPosDatabase): CategoryDao = database.categoryDao()

    @Provides
    @Singleton
    fun provideProductRepository(dao: ProductDao): ProductRepository = ProductRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideInvoiceRepository(dao: InvoiceDao): InvoiceRepository = InvoiceRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideInvoiceItemRepository(dao: InvoiceItemDao): InvoiceItemRepository = InvoiceItemRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCustomerRepository(dao: CustomerDao): CustomerRepository = CustomerRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository = UserRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideBusinessRepository(dao: BusinessDao): BusinessRepository = BusinessRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCategoryRepository(dao: CategoryDao): CategoryRepository = CategoryRepositoryImpl(dao)
}