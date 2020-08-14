package edios.mlr.utils;

public interface AppConstants {
    String AUTHENTICATE_MESSAGE = "Authenticating... Please wait";
    String FETCHING_ORDERS_MESSAGE = "Refreshing orders...";
    String ORDER_SUMMARY_MESSAGE = "Generating orders summary...";
    String SEARCH_ORDER_MESSAGE = "Searching orders...";
    String PASSWORD_CHANGE_MESSAGE = "Changing password... Please wait";
    String UPDATE_SETTING_MESSAGE = "Updating settings... Please wait";
    String BLUETOOTH_UNAVAILABLE_MESSAGE = "Unable to print as Printer is not connected.";
    String SETTINGS_SAVED_MESSAGE = "Settings updated successfully.";
    String INTERNET_CONNECTION_MESSAGE = "Please check your internet connection.";
    String SERVER_NOT_RESPONDING_MESSAGE = "Server not responding.";
    String SERVER_NOT_CONNECTED = "Couldn't connect to server, please try again after sometime.";
    String APP_UPDATE_MESSAGE = "You're using older version of Edios TOI ADMIN. There is a newer version of the app"
            + " available on Play Store.";
    String FORCED_APP_UPDATE_MESSAGE = "You're using older version of Edios TOI ADMIN. Please update the app "
            + "to newer version to proceed further.";
    int DEFAULT_REFRESH_TIME = 15;
    String UPDATE_ORDERS = "UPDATE_ORDERS ";


    //API Constants
    String SIGNATURE_KEY = "1234";
    String APP_END = "ADMIN";
    String ACCOUNT_ID = "ACCOUNT_ID";
    String SITE_ID = "SITE_ID";
    String DEFAULT_ORDER_READY_TIME = "25";
    String LAST_RECEIVED_ORDER = "LAST_RECEIVED_ORDER";
    //API Constants

    //Shared Pref Info
    String USER_NAME = "USER_NAME";
    String LOGIN_STATUS = "LOGIN_STATUS";
    String ORDER_READY_TIME = "ORDER_READY_TIME";
    String ORDER_REFRESH_TIME = "ORDER_REFRESH_TIME";
    String ORDER_NO_PREFIX = "ORDER_NO_PREFIX";
    String SITE_CURRENCY = "SITE_CURRENCY";
    String SITE_TIME_ZONE = "SITE_TIME_ZONE";

    String AUTO_PRINT = "AUTO_PRINT";
    String PRINT_SIZE = "PRINT_SIZE";
    String SHUTDOWN_APP = "SHUTDOWN_APP";
    String APP_VERSION_LAST_CHECKED_AT = "APP_VERSION_LAST_CHECKED_AT";
    String ORDER_TYPE = "ORDER_TYPE";
    String ORDER_TYPE_SETTINGS = "ORDER_TYPE_SETTINGS";
    String ORDER_TYPE_RESTAURANT = "RESTAURANT_ORDER";
    String ORDER_TYPE_TAKE_AWAY = "TAKE_AWAY_ORDER";
    String ORDER_TYPE_DELIVERY = "DELIVERY_ORDER";
    String DELIVERY_CHARGES = "DELIVERY_CHARGES";
    String DELIVERY_CHARGES_AMOUNT = "DELIVERY_CHARGES_AMOUNT";
    String DELIVERY_RADIUS = "DELIVERY_RADIUS";
    String DELIVERY_PICKUP_TIME = "DELIVERY_PICKUP_TIME";

    //Shared Pref Info

    //order status
    String PENDING_ORDER_STATUS = "PENDING";
    String IN_KITCHEN_ORDER_STATUS = "IN-KITCHEN";
    String READY_FOR_PICKUP_ORDER_STATUS = "READY FOR PICKUP";
    String COMPLETED_ORDER_STATUS = "COMPLETED";
    //order status

    //Printing Constants
    String RESTAURANT_NAME = "Multi Location Restaurant";
    String RESTAURANT_WEBSITE = "edios.global";
    String RESTAURANT_CONTACT = "999-999-9999";
    String CURRENCY = "$";
    //Printing Constants

}
