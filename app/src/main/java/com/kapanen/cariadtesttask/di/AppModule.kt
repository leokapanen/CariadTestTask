package com.kapanen.cariadtesttask.di

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kapanen.cariadtesttask.BuildConfig
import com.kapanen.cariadtesttask.delegate.AdapterDelegatesManager
import com.kapanen.cariadtesttask.delegate.DefaultDelegatesManager
import com.kapanen.cariadtesttask.delegate.RecyclerViewAdapterDelegate
import com.kapanen.cariadtesttask.setting.AppSettings
import com.kapanen.cariadtesttask.setting.SharedPreferencesStorage
import com.kapanen.cariadtesttask.setting.Storage
import com.kapanen.cariadtesttask.ui.filtering.delegate.FilteringHeaderDelegate
import com.kapanen.cariadtesttask.ui.filtering.delegate.FilteringItemDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

private const val APP_SETTINGS = "APP SETTINGS"
private const val OK_HTTP_CACHE_NAME = "okHttp-cache"
private const val CACHE_SIZE = 10 * 1024 * 1024L //10 MB

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BaseApiUrl

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @BaseApiUrl
    @Provides
    fun provideBaseHearthstoneUrl() = BuildConfig.BASE_API_URL

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(
            APP_SETTINGS,
            Context.MODE_PRIVATE
        )

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideStorage(sharedPreferences: SharedPreferences, gson: Gson): Storage =
        SharedPreferencesStorage(sharedPreferences, gson)

    @Singleton
    @Provides
    fun provideAppSettings(storage: Storage) = AppSettings(storage)

    @Singleton
    @Provides
    fun provideResources(@ApplicationContext context: Context) = context.resources

    @Provides
    fun provideHttpCache(@ApplicationContext context: Context): Cache? {
        return if (context.applicationContext.cacheDir != null) {
            val cacheDir = File(context.applicationContext.cacheDir, OK_HTTP_CACHE_NAME)
            Cache(cacheDir, CACHE_SIZE)
        } else {
            null
        }
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message: String? -> Timber.d(message) }
            .setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.HEADERS
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )
    }

    @Provides
    fun provideDefaultOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        cache: Cache?
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .cache(cache)
            .build()
    }

}

@Module
@InstallIn(SingletonComponent::class)
object DelegatesModule {

    @Suppress("UNCHECKED_CAST")
    @Singleton
    @Provides
    fun provideDelegateManager(
        ioDispatcher: CoroutineDispatcher,
        appSettings: AppSettings
    ): AdapterDelegatesManager {
        return DefaultDelegatesManager().apply {
            addDelegate(
                FilteringItemDelegate(
                    appSettings,
                    ioDispatcher
                ) as RecyclerViewAdapterDelegate<Any, RecyclerView.ViewHolder>
            )
            addDelegate(FilteringHeaderDelegate() as RecyclerViewAdapterDelegate<Any, RecyclerView.ViewHolder>)
        }
    }

}
