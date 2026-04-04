package com.codequest.network;
import com.codequest.util.Constants;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private RetrofitClient() {} 
    public static void init(AuthInterceptor authInterceptor, TokenAuthenticator tokenAuthenticator) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(tokenAuthenticator)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }
    public static ApiService getApi() {
        if (apiService == null) {
            throw new IllegalStateException(
                    "RetrofitClient not initialized. Call RetrofitClient.init() first.");
        }
        return apiService;
    }
    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
