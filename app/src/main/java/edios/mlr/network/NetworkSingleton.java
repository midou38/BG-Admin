package edios.mlr.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkSingleton {


    private static final String TAG = NetworkSingleton.class.getSimpleName();
    //    private static final String SERVER_URL = "http://192.168.5.108:58080/MLR/"; //Local machine
//    private static final String SERVER_URL = "http://192.168.5.129:58080/MLR/"; //Test Server
    private static final String SERVER_URL = "http://192.168.5.129:58080/FoodJunctionMobileAPI/"; //Test Server
//    private static final String SERVER_URL = "http://202.164.43.200:58080/RWP_API/"; //Test Server
//    private static final String SERVER_URL = "http://66.133.97.28:8081/MLR/"; //Production machine

    private static Retrofit retrofit;
    private static Calls retroInterface;


    public static Calls getInstance(Context activity) {
        initializeRetroFit(activity);
        return retroInterface;
    }

    private static void initializeRetroFit(Context activity) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(60, TimeUnit.SECONDS);
            httpClient.connectTimeout(60, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);
            httpClient.addInterceptor(logging);
            retrofit = new Retrofit.Builder().client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create()).baseUrl(SERVER_URL).client(okHttpClient).build();
            retroInterface = retrofit.create(Calls.class);
        }
    }
}

  
