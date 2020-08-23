package com.example.myecommerce

import android.app.Application
import com.example.myecommerce.cart.ShoppingCartViewModel
import com.example.myecommerce.detail.DetailViewModel
import com.example.myecommerce.list.MovieListViewModel
import com.example.myecommerce.persistence.AppDatabase
import com.example.myecommerce.persistence.Repository
import com.example.myecommerce.persistence.RepositoryImpl
import com.example.myecommerce.remote.ServiceBuilder
import com.example.myecommerce.remote.TmdbEndpoints
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Application)
            modules(appModule)
        }
    }
}

val appModule: Module = module {

    viewModel { MovieListViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { ShoppingCartViewModel(get()) }

    single<Repository> { RepositoryImpl(get(), get()) }
    single<TmdbEndpoints> { ServiceBuilder.buildService() }
    single { AppDatabase.getAppDatabase(androidContext()) }
}