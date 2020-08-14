package edios.mlr.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialdialogs.MaterialDialog;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edios.mlr.R;
import edios.mlr.data.db.TOIAdminDB;
import edios.mlr.data.entities.SiteDetailsEntity;
import edios.mlr.interfaces.NotifyFetchOrder;
import edios.mlr.interfaces.UpdateAppDialogListener;
import edios.mlr.model.CustomerOrderItems;
import edios.mlr.model.CustomerOrders;
import edios.mlr.model.FetchOrderRequest;
import edios.mlr.model.FetchOrderResponse;
import edios.mlr.model.ResultOutput;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.ui.fragments.ChangePasswordFragment;
import edios.mlr.ui.fragments.OrderHistoryFragment;
import edios.mlr.ui.fragments.OrderSummaryFragment;
import edios.mlr.ui.fragments.OrdersFragment;
import edios.mlr.ui.fragments.SearchOrdersFragment;
import edios.mlr.ui.fragments.SettingsFragment;
import edios.mlr.utils.printing.BluetoothService;
import edios.mlr.utils.printing.PrinterCommands;
import edios.mlr.utils.printing.SingletonBluetoothService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Utils {
    public static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    AppCompatActivity activity;
    Context context;
    private BluetoothService mService;
    private SharedPreferences pref;
    private SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    public Utils(Object context) {
        if (context instanceof AppCompatActivity) {
            this.activity = (AppCompatActivity) context;
            this.context = activity.getBaseContext();
            pref = SharedPref.getReader(activity);
//        this.context = ((AppCompatActivity) context).getBaseContext();
        } else if (context instanceof Context) {
            this.context = (Context) context;
            pref = SharedPref.getReader(this.context);
            System.out.println("instance of Context");
        }
    }

    public MaterialDialog showProgressDialog(String msg) {
        try {
            return new MaterialDialog.Builder(activity).
                    title(activity.getResources().getString(R.string.app_name)).cancelable(false).content(msg).progress(true, 0).show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }

    public void updateStatusDialog(int selectedIndex, MaterialDialog.ListCallbackSingleChoice callback) {
        new MaterialDialog.Builder(activity).
                title(activity.getResources().getString(R.string.update_status)).items(R.array.status_array).
                cancelable(false).
                itemsCallbackSingleChoice(selectedIndex, callback).positiveText(R.string.submit).negativeText(R.string.cancel).show();
    }

    public List<CustomerOrders> orderList(String orderStatus, List<CustomerOrders> customerOrders) {
        List<CustomerOrders> inKitchenOrders = new ArrayList<>();
        for (CustomerOrders order : customerOrders) {
            if (order.getOrderStatus().equalsIgnoreCase(orderStatus))
                inKitchenOrders.add(order);
        }
        return inKitchenOrders;
    }

    private ResultOutput filterRestaurantOrders(ResultOutput orders) {
        ResultOutput filteredOrders = new ResultOutput();
        List<CustomerOrders> customerOrders = orders.getCustomerOrders();

        String orderType = SharedPref.getReader(context).getString(AppConstants.ORDER_TYPE, "");
        List<CustomerOrders> filteredOrdersList = customerOrders;
        if (!TextUtils.isEmpty(orderType)) {
            if (orderType.equalsIgnoreCase(AppConstants.ORDER_TYPE_RESTAURANT)) {
                filteredOrdersList = filterOrder(customerOrders, true);
            } else if (orderType.equalsIgnoreCase(AppConstants.ORDER_TYPE_TAKE_AWAY)) {
                filteredOrdersList = filterOrder(customerOrders, false);
            } else if (orderType.equalsIgnoreCase(AppConstants.ORDER_TYPE_DELIVERY)) {
                filteredOrdersList = filterOrder(customerOrders, false);
            }
        }

        filteredOrders.setCustomerOrders(new ArrayList());
        filteredOrders.setCustomerOrders(filteredOrdersList);
        return filteredOrders;
    }

    private List<CustomerOrders> filterOrder(List<CustomerOrders> customerOrders, boolean equals) {
        List<CustomerOrders> filteredOrdersList = new ArrayList<>();
        for (CustomerOrders order : customerOrders) {
            if (equals && order.getOrderType().equalsIgnoreCase(context.getString(R.string.order_type_restaurant))) {
                filteredOrdersList.add(order);
            } else if (!equals && !order.getOrderType().equalsIgnoreCase(context.getString(R.string.order_type_restaurant))) {
                filteredOrdersList.add(order);
            }
        }
        return filteredOrdersList;
    }

    public void fetchOrders(final NotifyFetchOrder notifyFetchOrder, final String date, final String from) {
        try {
            if (isNetworkConnected()) {
                final MaterialDialog progressDialog = showProgressDialog(AppConstants.FETCHING_ORDERS_MESSAGE);
                NetworkSingleton.getInstance(activity).fetchOrders(prepareFetchOrderRequest(date)).enqueue(new Callback<FetchOrderResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FetchOrderResponse> call, @NonNull Response<FetchOrderResponse> response) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        if (response.body() != null) {
//                            System.out.println("response = " + response.body());
                            if (response.body().getRestaurant() != null) {
                                context.sendBroadcast(new Intent(context.getString(R.string.header_broadcaster))
                                        .putExtra(context.getString(R.string.header_image_url),
                                                response.body().getRestaurant().getSiteImageUrl()));
                            }
                            TOIAdminDB.getInstance(context).insertSiteDetails(Objects.requireNonNull(response.body()).getSiteDetails());
                            if (validateAppVersion(Objects.requireNonNull(response.body()).getSiteDetails())) {
                                if (date.length() == 0 && SharedPref.getReader(activity).getBoolean(AppConstants.AUTO_PRINT, false)
                                        && !from.equalsIgnoreCase("OrderHistoryFragment")) {
                                    autoPrintBill(Objects.requireNonNull(response.body()).getResultOutput());
                                }
                                notifyFetchOrder.orderFetched(filterRestaurantOrders(response.body().getResultOutput()), true);

                                int totalReceivedOrders = Objects.requireNonNull(response.body()).getResultOutput().getCustomerOrders().size();
                                if (totalReceivedOrders > 0 && !from.equalsIgnoreCase("OrderHistoryFragment")) {
                                    SharedPref.getEditor(activity).putInt(AppConstants.LAST_RECEIVED_ORDER,
                                            Objects.requireNonNull(response.body()).getResultOutput().getCustomerOrders().get(totalReceivedOrders - 1)
                                                    .getCustomerOrderID()).apply();
                                }
                            } else {
                                updateAppDialog(Objects.requireNonNull(response.body()).getSiteDetails(), (UpdateAppDialogListener) activity);
                            }
                        } else {
                            if (activity != null)
                                Toast.makeText(activity, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FetchOrderResponse> call, @NonNull Throwable t) {
                        System.out.println("OrdersFragment.onFailure " + t.getMessage());
                        if (activity != null)
                            Toast.makeText(activity, AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                        notifyFetchOrder.orderFetched(new ResultOutput(), false);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(activity, AppConstants.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void autoPrintBill(ResultOutput resultOutput) {
        int lastReceivedOrder = SharedPref.getReader(activity).getInt(AppConstants.LAST_RECEIVED_ORDER, 0);
        System.out.println("lastReceivedOrder = " + lastReceivedOrder);
        String[] printSize = new String[3];
        if (activity != null)
            printSize = activity.getResources().getStringArray(R.array.print_size_array);
        if (context != null)
            printSize = context.getResources().getStringArray(R.array.print_size_array);

        boolean isNewOrder = false;
        for (CustomerOrders order : resultOutput.getCustomerOrders())
            if (lastReceivedOrder < order.getCustomerOrderID()) {
                System.out.println("NEW order -- " + order.getOrderNumber());
                String selectedPrintSize = SharedPref.getReader(activity).getString(AppConstants.PRINT_SIZE, printSize[0]);
                if (Objects.requireNonNull(selectedPrintSize).equalsIgnoreCase(printSize[0]))
                    print3InchTSPKitchenTicket(order);
                else if (selectedPrintSize.equalsIgnoreCase(printSize[1]))
                    print3InchKitchenTicket(order);
                else print2InchKitchenTicket(order);

                isNewOrder = true;
            }
        if (!isNewOrder) {
            System.out.println("No new order found.");
        }


    }

    public FetchOrderRequest prepareFetchOrderRequest(String date) {
        FetchOrderRequest request = new FetchOrderRequest();
        request.setSignatureKey(AppConstants.SIGNATURE_KEY);
        request.setAccountID(pref.getLong(AppConstants.ACCOUNT_ID, 0L));
        request.setSiteID(pref.getLong(AppConstants.SITE_ID, 0L));
        request.setUserName(SharedPref.getReader(activity).getString(AppConstants.USER_NAME, ""));
        if (date.length() > 0) {
            request.setLastUpdatedDate(date);
        } else {
            Calendar cal = Calendar.getInstance();
            request.setLastUpdatedDate(apiDateFormat.format(cal.getTime()));
        }
        System.out.println("request = " + request);
        return request;

    }

    public void launchFragment(int menuID) {
        Fragment fragment = null;
        switch (menuID) {
            case R.id.nav_order:
                activity.setTitle(R.string.dashboard_menu);
                fragment = new OrdersFragment();
                break;
            case R.id.nav_order_history:
                activity.setTitle(R.string.order_history_menu);
                fragment = new OrderHistoryFragment();
                break;
            case R.id.nav_order_summary:
                activity.setTitle(R.string.order_summary_menu);
                fragment = new OrderSummaryFragment();
                break;
            case R.id.nav_search_order:
                activity.setTitle(R.string.search_orders_menu);
                fragment = new SearchOrdersFragment();
                break;
            case R.id.nav_change_password:
                activity.setTitle(R.string.change_password_menu);
                fragment = new ChangePasswordFragment();
                break;
            case R.id.nav_setting:
                activity.setTitle(R.string.settings_menu);
                fragment = new SettingsFragment();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            System.out.println("Fragment is null ");
        }


        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        System.out.println("DrawerActivity.launchFragment " + menuID);

    }

    public String appDateToApiDate(String apiDate) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
        String apiFormatDate = "";
        try {

            Date date = sdf1.parse(apiDate);

            apiFormatDate = sdf2.format(date);
            System.out.println("apiFormatDate = " + apiFormatDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return apiFormatDate;
    }

    public String getFirstDateOfMonth() {
        String date;
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);

        date = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH).format(c.getTime());
        System.out.println("Date " + date);
        return date;
    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
        Date d = cal.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
        return sdf1.format(d);
    }

    private String[] nameArray(String itemName, int length) {
        String[] nameArray = new String[(int) Math.ceil((float) itemName.length() / length)];
        for (int i = 0; i < nameArray.length; i++) {
            int startLimit = i * length;
            int endLimit = (i + 1) * length > itemName.length() ? itemName.length() : (i + 1) * length;
            nameArray[i] = itemName.substring(startLimit, endLimit);
        }
        return nameArray;
    }

    public void print2InchBill(CustomerOrders order) {
        System.out.println("Utils.print2InchBill");
        mService = SingletonBluetoothService.getInstance(activity, null);
        String itemFormat = "%s %10s"; //39
        printNewLine();
        printCustom(AppConstants.RESTAURANT_NAME, 2, 1);
        printNewLine();
        printCustom(AppConstants.RESTAURANT_WEBSITE, 0, 1);
        printNewLine();
        printCustom(AppConstants.RESTAURANT_CONTACT, 0, 1);
        printNewLine();

        printCustom("ORDER#:  " + order.getOrderNumber(), 0, 0);
        printNewLine();
        printCustom(order.getOrderDateTime(), 0, 0);
        printNewLine();

        printCustom("================================", 1, 0);


        for (CustomerOrderItems item : order.getCustomerOrderItems()) {
            String itemName = item.getItemQuantity() + " " + item.getItemName();
            String[] nameArray = nameArray(itemName, 20);
            if (itemName.length() >= 20) {
//                itemName = itemName.substring(0, Math.min(itemName.length(), 20));
                itemName = nameArray[0];
            } else {
                StringBuilder sd = new StringBuilder();
                for (int i = 0; i < 20 - itemName.length(); i++) {
                    sd.append(" ");
                }
                itemName += sd;
            }


            printCustom(String.format(itemFormat, itemName, "$" +
                            Utils.decimalFormat.format(item.getItemTotalAmount() > 0 ? item.getItemPrice() : item.getItemTotalAmount())),
                    0, 0);

            for (int i = 1; i < nameArray.length; i++) {
                printCustom(String.format(itemFormat, " " + nameArray[i], ""),
                        0, 0);
            }

            printNewLine();
        }
        printCustom("================================", 1, 0);

        System.out.printf("%.2f %n", order.getTotalOrderAmount());

        printCustom(String.format(itemFormat, "Subtotal      ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalPrice())), 0, 0);
        printNewLine();
        printCustom(String.format(itemFormat, "Promo Code    ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getPromoDiscountAmount())), 0, 0);
        printNewLine();
        printCustom(String.format(itemFormat, "Tax           ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalTaxAmount())), 0, 0);
        printNewLine();
        if (order.getOrderType().equalsIgnoreCase(activity.getString(R.string.order_type_delivery))) {
            printCustom(String.format(itemFormat, "Delivery      ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getDeliveryChargesAmount())), 0, 0);
            printNewLine();
        }
        printCustom(String.format(itemFormat, "GRAND TOTAL   ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalOrderAmount())), 1, 0);
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();

    }

    public void print3InchBill(CustomerOrders order) {
        System.out.println("Utils.print3InchBill");
        mService = SingletonBluetoothService.getInstance(activity, null);
        String itemFormat = "%s %15s"; //39
        printNewLine();
        printCustom(AppConstants.RESTAURANT_NAME, 2, 1);
        printNewLine();
        printCustom(AppConstants.RESTAURANT_WEBSITE, 0, 1);
        printNewLine();
        printCustom(AppConstants.RESTAURANT_CONTACT, 0, 1);
        printNewLine();

        printCustom("ORDER#:  " + order.getOrderNumber(), 0, 0);
        printNewLine();
        printCustom(order.getOrderDateTime(), 0, 0);
        printNewLine();

        printCustom("================================================", 1, 0);
        printNewLine();


        for (CustomerOrderItems item : order.getCustomerOrderItems()) {
            String itemName = item.getItemQuantity() + " " + item.getItemName();
            String[] nameArray = nameArray(itemName, 25);
            if (itemName.length() >= 25)
//                itemName = itemName.substring(0, Math.min(itemName.length(), 25));
                itemName = nameArray[0];
            else {
                StringBuilder sd = new StringBuilder();
                for (int i = 0; i < 25 - itemName.length(); i++) {
                    sd.append(" ");
                }
                itemName += sd;
            }
            printCustom(String.format(itemFormat, itemName, "$" +
                            Utils.decimalFormat.format(item.getItemTotalAmount() > 0 ? item.getItemPrice() : item.getItemTotalAmount())),
                    0, 0);

            for (int i = 1; i < nameArray.length; i++) {
                printCustom(String.format(itemFormat, " " + nameArray[i], ""),
                        0, 0);
            }
            printNewLine();
        }
        printCustom("================================================", 1, 0);
        printNewLine();
        System.out.printf("%.2f %n", order.getTotalOrderAmount());

        printCustom(String.format(itemFormat, "Subtotal      ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalPrice())), 0, 0);
        printNewLine();
        printCustom(String.format(itemFormat, "Promo Code    ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getPromoDiscountAmount())), 0, 0);
        printNewLine();
        printCustom(String.format(itemFormat, "Tax           ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalTaxAmount())), 0, 0);
        printNewLine();
        printNewLine();
        if (order.getOrderType().equalsIgnoreCase(activity.getString(R.string.order_type_delivery))) {
            printCustom(String.format(itemFormat, "Delivery      ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getDeliveryChargesAmount())), 0, 0);
            printNewLine();
        }
        printCustom(String.format(itemFormat, "GRAND TOTAL   ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalOrderAmount())), 1, 0);
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();

    }

    public void print3InchTSPBill(CustomerOrders order) {
        System.out.println("Utils.print3InchBill");
        mService = SingletonBluetoothService.getInstance(activity, null);
        String itemFormat = "%s %15s"; //39
        printNewLine();
        printCustom(AppConstants.RESTAURANT_NAME, 2, 1);
        printNewLine();
        printCustom(AppConstants.RESTAURANT_WEBSITE, 0, 1);
        printNewLine();
        printCustom(AppConstants.RESTAURANT_CONTACT, 0, 1);
        printNewLine();

        printCustom("ORDER#:  " + order.getOrderNumber(), 0, 0);
        printNewLine();
        printCustom(order.getOrderDateTime(), 0, 0);
        printNewLine();

        printCustom("==========================================", 1, 0);
        printNewLine();


        for (CustomerOrderItems item : order.getCustomerOrderItems()) {
            String itemName = item.getItemQuantity() + " " + item.getItemName();
            String[] nameArray = nameArray(itemName, 25);
            if (itemName.length() >= 25)
//                itemName = itemName.substring(0, Math.min(itemName.length(), 25));
                itemName = nameArray[0];
            else {
                StringBuilder sd = new StringBuilder();
                for (int i = 0; i < 25 - itemName.length(); i++) {
                    sd.append(" ");
                }
                itemName += sd;
            }
            printCustom(String.format(itemFormat, itemName, "$" +
                            Utils.decimalFormat.format(item.getItemTotalAmount() > 0 ? item.getItemPrice() : item.getItemTotalAmount())),
                    0, 0);
            for (int i = 1; i < nameArray.length; i++) {
                printCustom(String.format(itemFormat, " " + nameArray[i], ""),
                        0, 0);
            }
            printNewLine();
        }
        printCustom("==========================================", 1, 0);
        printNewLine();
        System.out.printf("%.2f %n", order.getTotalOrderAmount());

        printCustom(String.format(itemFormat, "Subtotal      ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalPrice())), 0, 0);
        printNewLine();
        printCustom(String.format(itemFormat, "Promo Code    ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getPromoDiscountAmount())), 0, 0);
        printNewLine();
        printCustom(String.format(itemFormat, "Tax           ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalTaxAmount())), 0, 0);
        printNewLine();
        printNewLine();
        if (order.getOrderType().equalsIgnoreCase(activity.getString(R.string.order_type_delivery))) {
            printCustom(String.format(itemFormat, "Delivery      ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getDeliveryChargesAmount())), 0, 0);
            printNewLine();
        }
        printCustom(String.format(itemFormat, "GRAND TOTAL   ", AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalOrderAmount())), 1, 0);
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        cutPaper();

    }

    public void print2InchKitchenTicket(CustomerOrders order) {
        System.out.println("Utils.printBill");
        mService = SingletonBluetoothService.getInstance(activity, null);
        String itemFormat = "%s %10s"; //39

        printCustom("Kitchen Ticket", 0, 1);
        printNewLine();

        if (order.getOrderType().equalsIgnoreCase((context != null ? context : activity).getString(R.string.order_type_restaurant))
                && !TextUtils.isEmpty(order.getTableNumber())) {
            specifyRedPrinting();
            printCustom("RESTAURANT ORDER", 1, 1);
            cancelRedPrinting();
            printNewLine();
            specifyRedPrinting();
            printCustom("TABLE#:  " + order.getTableNumber(), 1, 1);
            cancelRedPrinting();
        } else {
            specifyRedPrinting();
            printCustom(order.getOrderType().equalsIgnoreCase(
                    (context != null ? context : activity).getString(R.string.order_type_take_away))
                    ? "TAKE AWAY ORDER" : "DELIVERY ORDER", 1, 1);
            cancelRedPrinting();
        }

        printNewLine();
        printCustom("ORDER#:  " + order.getOrderNumber(), 1, 1);
        printNewLine();
        printCustom(order.getOrderDateTime(), 0, 1);
        printNewLine();
        if (order.getOrderType().equalsIgnoreCase((context != null ? context : activity).getString(R.string.order_type_take_away))) {
            String pickupTime[] = order.getOrderPickupDateTime().split(" ");
            DateTime orderDateTime = stringToDateTime(order.getOrderDateTime());
            DateTime orderPickDateTime = stringToDateTime(order.getOrderPickupDateTime());
            String minutes = "(" + Minutes.minutesBetween(orderDateTime, orderPickDateTime).getMinutes() + " Min)";
            printCustom("Pickup Time: " + pickupTime[pickupTime.length - 2] + " " + pickupTime[pickupTime.length - 1] + minutes, 1, 1);
            printNewLine();
        }
        printCustom(order.getCustomer().getUserName() + " - " + order.getCustomer().getMobileNo(), 0, 1);
        printNewLine();


        printCustom("--------------------------------", 1, 0);
        for (CustomerOrderItems item : order.getCustomerOrderItems()) {
            String itemName = item.getItemQuantity() + " " + item.getItemName();
            printCustom(String.format(itemFormat, itemName, ""), 3, 0);
            printNewLine();

            if (!TextUtils.isEmpty(item.getItemSpicyLevel())) {
                printCustom(item.getItemSpicyLevel(), 1, 0);
                printNewLine();
            }
            if (!TextUtils.isEmpty(item.getItemInstructions())) {
                printCustom(item.getItemInstructions(), 1, 0);
                printNewLine();
            }
            printCustom("--------------------------------", 1, 0);
        }

        if (!TextUtils.isEmpty(order.getOrderInstructions())) {
            printCustom("  " + order.getOrderInstructions(), 1, 0);
            printNewLine();
        }
        printCustom("================================", 1, 0);


        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
    }

    public void print3InchKitchenTicket(CustomerOrders order) {
        System.out.println("Utils.printBill");
        mService = SingletonBluetoothService.getInstance(activity, null);
        String itemFormat = "%s %15s"; //39
        printCustom("Kitchen Ticket", 0, 1);
        printNewLine();

        if (order.getOrderType().equalsIgnoreCase((context != null ? context : activity).getString(R.string.order_type_restaurant))
                && !TextUtils.isEmpty(order.getTableNumber())) {
            specifyRedPrinting();
            printCustom("RESTAURANT ORDER", 1, 1);
            cancelRedPrinting();
            printNewLine();
            specifyRedPrinting();
            printCustom("TABLE#:  " + order.getTableNumber(), 1, 1);
            cancelRedPrinting();
        } else {
            specifyRedPrinting();
            printCustom(order.getOrderType().equalsIgnoreCase(
                    (context != null ? context : activity).getString(R.string.order_type_take_away))
                    ? "TAKE AWAY ORDER" : "DELIVERY ORDER", 1, 1);
            cancelRedPrinting();
        }

        printNewLine();
        printCustom("ORDER#:  " + order.getOrderNumber(), 1, 1);
        printNewLine();
        printCustom(order.getOrderDateTime(), 0, 1);
        printNewLine();
        if (order.getOrderType().equalsIgnoreCase((context != null ? context : activity).getString(R.string.order_type_take_away))) {
            String pickupTime[] = order.getOrderPickupDateTime().split(" ");
            DateTime orderDateTime = stringToDateTime(order.getOrderDateTime());
            DateTime orderPickDateTime = stringToDateTime(order.getOrderPickupDateTime());
            String minutes = "(" + Minutes.minutesBetween(orderDateTime, orderPickDateTime).getMinutes() + " Min)";
            printCustom("Pickup Time: " + pickupTime[pickupTime.length - 2] + " " + pickupTime[pickupTime.length - 1] + minutes, 1, 1);
            printNewLine();
        }
        printCustom(order.getCustomer().getUserName() + " - " + order.getCustomer().getMobileNo(), 0, 1);
        printNewLine();


        printCustom("------------------------------------------------", 1, 0);
        printNewLine();
        for (CustomerOrderItems item : order.getCustomerOrderItems()) {
            String itemName = item.getItemQuantity() + " " + item.getItemName();
            printCustom(String.format(itemFormat, itemName, ""), 3, 0);
            printNewLine();


            if (!TextUtils.isEmpty(item.getItemSpicyLevel())) {
                printCustom(item.getItemSpicyLevel(), 1, 0);
                printNewLine();
            }
            if (!TextUtils.isEmpty(item.getItemInstructions())) {
                printCustom(item.getItemInstructions(), 1, 0);
                printNewLine();
            }
            printCustom("------------------------------------------------", 1, 0);
            printNewLine();
        }

        if (!TextUtils.isEmpty(order.getOrderInstructions())) {
            printCustom("  " + order.getOrderInstructions(), 1, 0);
            printNewLine();
        }
        printCustom("================================================", 1, 0);
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
    }

    public void print3InchTSPKitchenTicket(CustomerOrders order) {
        System.out.println("Utils.printBill");
        mService = SingletonBluetoothService.getInstance(activity, null);
        String itemFormat = "%s %15s"; //39
        printCustom("Kitchen Ticket", 0, 1);
        printNewLine();

        if (order.getOrderType().equalsIgnoreCase((context != null ? context : activity).getString(R.string.order_type_restaurant))
                && !TextUtils.isEmpty(order.getTableNumber())) {
            specifyRedPrinting();
            printCustom("RESTAURANT ORDER", 1, 1);
            cancelRedPrinting();
            printNewLine();
            specifyRedPrinting();
            printCustom("TABLE#:  " + order.getTableNumber(), 1, 1);
            cancelRedPrinting();
        } else {
            specifyRedPrinting();
            printCustom(order.getOrderType().equalsIgnoreCase(
                    (context != null ? context : activity).getString(R.string.order_type_take_away))
                    ? "TAKE AWAY ORDER" : "DELIVERY ORDER", 1, 1);
            cancelRedPrinting();
        }

        printNewLine();
        printCustom("ORDER#:  " + order.getOrderNumber(), 1, 1);
        printNewLine();
        printCustom(order.getOrderDateTime(), 0, 1);
        printNewLine();
        if (order.getOrderType().equalsIgnoreCase((context != null ? context : activity).getString(R.string.order_type_take_away))) {
            String pickupTime[] = order.getOrderPickupDateTime().split(" ");
            DateTime orderDateTime = stringToDateTime(order.getOrderDateTime());
            DateTime orderPickDateTime = stringToDateTime(order.getOrderPickupDateTime());
            String minutes = "(" + Minutes.minutesBetween(orderDateTime, orderPickDateTime).getMinutes() + " Min)";
            printCustom("Pickup Time: " + pickupTime[pickupTime.length - 2] + " " + pickupTime[pickupTime.length - 1] + minutes, 1, 1);
            printNewLine();
        }
        printCustom(order.getCustomer().getUserName() + " - " + order.getCustomer().getMobileNo(), 0, 1);
        printNewLine();


        printCustom("------------------------------------------", 1, 0);
        printNewLine();
        for (CustomerOrderItems item : order.getCustomerOrderItems()) {
            String itemName = item.getItemQuantity() + " " + item.getItemName();
            printCustom(String.format(itemFormat, itemName, ""), 5, 0);
            printNewLine();


            if (!TextUtils.isEmpty(item.getItemSpicyLevel())) {
                specifyRedPrinting();
                printCustom(item.getItemSpicyLevel(), 3, 0);
                cancelRedPrinting();
                printNewLine();
            }
            if (!TextUtils.isEmpty(item.getItemInstructions())) {
                specifyRedPrinting();
                printCustom(item.getItemInstructions(), 3, 0);
                cancelRedPrinting();
                printNewLine();
            }
            printCustom("------------------------------------------", 1, 0);
            printNewLine();
        }

        if (!TextUtils.isEmpty(order.getOrderInstructions())) {
            specifyRedPrinting();
            printCustom("" + order.getOrderInstructions(), 3, 0);
            cancelRedPrinting();
            printNewLine();
        }
        printCustom("==========================================", 1, 0);
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();
        cutPaper();
    }

    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
//        byte[] cc = new byte[]{0x1B, 0x21, 0x02};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        byte[] bb4 = new byte[]{0x1B, 0x21, 0x22}; // 3- bold with Medium text spread through out  (Introduce random character in TSP)
        byte[] bb5 = new byte[]{0x1B, 0x21, 0x30}; // 3- bold with More larger text
//        byte[] bb5 = new byte[]{0x1B, 0x21, 0x39}; // 3- bold with More larger text With Bold  (Introduce random character in TSP)
//        byte[] bb6 = new byte[]{0x1B, 0x21, 0x79}; // 3- bold with More larger text Litter Bolder (Introduce random character in TSP)
//        byte[] mFormat = new byte[]{27, 33, 0};
//        byte[] arrayOfByte1 = { 27, 33, 0 };
        byte[] format = {29, 33, 35};
        try {
            switch (size) {
                case 0:
                    mService.write(cc1);
                    break;
                case 1:
                    mService.write(bb);
                    break;
                case 2:
                    mService.write(bb2);
                    break;
                case 3:
                    mService.write(bb3);
                    break;
                case 4:
                    mService.write(bb4);
                    break;
                case 5:
                    mService.write(bb5);
                    break;
                case 99:
                    mService.write(format);
                    break;

            }

            switch (align) {
                case 0:
                    //left align
                    mService.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mService.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            mService.write(msg.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printNewLine() {
        try {
            mService.write(PrinterCommands.FEED_LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void cutPaper() {
        try {
            mService.write(PrinterCommands.CUT_PAPER);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void specifyRedPrinting() {
        try {
            mService.write(PrinterCommands.ENABLE_RED_COLOR_PRINT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelRedPrinting() {
        try {
            mService.write(PrinterCommands.DISABLE_RED_COLOR_PRINT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNetworkConnected() {
        if (activity == null) return true;
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;

    }

    public boolean validateAppVersion(SiteDetailsEntity siteDetails) {
        try {
            Date currentDate = apiDateFormat.parse(apiDateFormat.format(new Date()));
            String lastCheckedAt =
                    pref.getString(AppConstants.APP_VERSION_LAST_CHECKED_AT, "");
            if (TextUtils.isEmpty(lastCheckedAt))
                return siteDetails.getAdminAppVersion() <= getAppVersion();

            long diffHours = (Objects.requireNonNull(currentDate).getTime() - Objects.requireNonNull(apiDateFormat.parse(lastCheckedAt)).getTime()) / (60 * 60 * 1000);
            System.out.println("lastCheckedAt = " + lastCheckedAt);
            System.out.println("diffHours = " + diffHours);


            if (siteDetails.getAdminAppForceUpgrade().equalsIgnoreCase("No")) {
                if (diffHours >= 24) {
                    return siteDetails.getAdminAppVersion() <= getAppVersion();
                } else {
                    return true;
                }
            } else {
                return siteDetails.getAdminAppVersion() <= getAppVersion();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return siteDetails.getAdminAppVersion() <= getAppVersion();
        }
    }

    public void updateAppDialog(SiteDetailsEntity siteDetails, final UpdateAppDialogListener listener) {

        if (activity != null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

            dialog.setCancelable(false);
            dialog.setTitle(activity.getResources().getString(R.string.app_name));

            dialog.setPositiveButton("Update App", (dialog1, which) -> {
                if (listener != null) {
                    listener.onPositiveClick(dialog1, which);
                }

                dialog1.dismiss();

            });
            if (siteDetails.getAdminAppForceUpgrade().equalsIgnoreCase("No")) {
                dialog.setMessage(AppConstants.APP_UPDATE_MESSAGE).setNegativeButton("Skip", (dialog12, which) -> {
                    if (listener != null) {
                        listener.onNegativeClick(dialog12, which);
                    }
                });
            } else {
                dialog.setMessage(AppConstants.FORCED_APP_UPDATE_MESSAGE);
            }
            dialog.create().show();
        }

    }

    public Float getAppVersion() {
        PackageInfo pInfo = null;
        try {
            if (activity != null)
                pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            else
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return Float.parseFloat(Objects.requireNonNull(pInfo).versionName.trim());
    }

    public void saveAppVerLastCheckedAt() {
        SharedPref.getEditor(activity != null ? activity : context).putString(AppConstants.APP_VERSION_LAST_CHECKED_AT, apiDateFormat.format(new Date())).apply();
    }

    private DateTime stringToDateTime(String date) {
        return DateTimeFormat.forPattern("MMM dd, yyyy hh:mm:ss a").parseDateTime(date);
    }

}
