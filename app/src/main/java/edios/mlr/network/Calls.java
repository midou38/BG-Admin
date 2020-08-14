package edios.mlr.network;

import androidx.annotation.Keep;

import edios.mlr.model.ChangePasswordRequest;
import edios.mlr.model.FetchOrderRequest;
import edios.mlr.model.FetchOrderResponse;
import edios.mlr.model.GenericResponse;
import edios.mlr.model.LoginRequest;
import edios.mlr.model.OrderSummaryRequest;
import edios.mlr.model.OrderSummaryResponse;
import edios.mlr.model.RequestDeliveryPartnerRequest;
import edios.mlr.model.SearchOrderRequest;
import edios.mlr.model.SettingsRequest2;
import edios.mlr.model.UpdateOrderRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

@Keep
public interface Calls {
    //    @POST("adminLogin")
//    @POST("adminLoginV2")
    @POST("adminLoginV3")
    Call<GenericResponse> authenticate(@Body LoginRequest loginBean);

//    @POST("adminOrdersV2")
    @POST("adminOrdersV3")
    Call<FetchOrderResponse> fetchOrders(@Body FetchOrderRequest fetchOrderRequest);

    @POST("updateOrderV2")
    Call<FetchOrderResponse> updateOrder(@Body UpdateOrderRequest updateOrderRequest);

    @POST("orderSummaryV2")
    Call<OrderSummaryResponse> orderSummary(@Body OrderSummaryRequest updateOrderRequest);

    @POST("changePassword")
    Call<GenericResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @POST("filterOrdersV2")
    Call<FetchOrderResponse> searchOrders(@Body SearchOrderRequest searchOrderRequest);

    //    @POST("adminSettings")
    @POST("adminSettingsV3")
    Call<GenericResponse> updateSettingsV2(@Body SettingsRequest2 searchOrderRequest);

    @POST("fetchDeliveryPartners")
    Call<FetchOrderResponse> fetchDeliveryPartners(@Body FetchOrderRequest fetchOrderRequest);

    @POST("requestDeliveryPartner")
    Call<GenericResponse> requestDeliveryPartner(@Body RequestDeliveryPartnerRequest requestDeliveryPartnerRequest);

}

