package to.tawk.sample.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import to.tawk.sample.R
import to.tawk.sample.api.ApiClient
import to.tawk.sample.api.ApiService
import to.tawk.sample.room.AppDatabase
import to.tawk.sample.room.UserDao
import to.tawk.sample.utils.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
       return  OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()


    @Provides
    @Singleton
    @UserDAO
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.usersDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                appContext.getString(
                        R.string.app_name
                )
        ).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit):ApiService = retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideApiHelper(apiService: ApiService): ApiClient = ApiClient(apiService)

//    @Provides
//    fun providesUserDao(): UserDao? {
//        return DatabUserDao.class
//    }
}