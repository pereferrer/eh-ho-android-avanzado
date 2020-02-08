package io.keepcoding.eh_ho.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.keepcoding.eh_ho.BuildConfig
import io.keepcoding.eh_ho.Database.LatestNewsDatabase
import io.keepcoding.eh_ho.data.repository.PREFERENCES_SESSION
import io.keepcoding.eh_ho.data.repository.PREFERENCES_SESSION_USERNAME
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class UtilsModule(private val context: Context) {


    @Singleton
    @Provides
    fun provideLatestNewsDb(): LatestNewsDatabase = Room.databaseBuilder(
        context, LatestNewsDatabase::class.java, "latestNews-database"
    ).build()

    @Singleton
    @Provides
    fun provideApplicationContext(): Context = context

    @Provides
    fun provideSharedPreferences(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREFERENCES_SESSION, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideRetrofit(sharedPreferences: SharedPreferences): Retrofit = Retrofit.Builder()
//        .client(provideOkHttpClient(sharedPreferences))
        .baseUrl("https://${BuildConfig.DiscourseDomain}")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}

fun provideOkHttpClient(sp: SharedPreferences): OkHttpClient {
    val interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()

            val updatedRequest = original.newBuilder()
                .header("Api-Key", BuildConfig.DiscourseApiKey)
                .header("Api-Username", sp.getString(PREFERENCES_SESSION_USERNAME, "") ?: "")
                .method(original.method, original.body)
                .build()

            return chain.proceed(updatedRequest)
        }

    }

    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

}