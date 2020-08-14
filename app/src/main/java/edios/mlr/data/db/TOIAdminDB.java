package edios.mlr.data.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edios.mlr.data.entities.SiteDetailsEntity;


public class TOIAdminDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TOI_admin_db.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TAG = TOIAdminDB.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static TOIAdminDB dbOperation;
    private SQLiteDatabase db;


    private TOIAdminDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TOIAdminDB getInstance(Context context) {
        if (dbOperation == null) {
            dbOperation = new TOIAdminDB(context);
        }
        return dbOperation;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SiteDetailsTable.INSTANCE.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void writeDB() {
        db = this.getWritableDatabase();
    }

    private void readDB() {
        db = this.getReadableDatabase();
    }


    public void insertSiteDetails(SiteDetailsEntity details) {
        writeDB();
        ContentValues values = new ContentValues();
        values.put(SiteDetailsTable.COLUMN_SITE_DETAIL_ID.name(), details.getSiteDetailId());
        values.put(SiteDetailsTable.COLUMN_ORDER_PREFIX.name(), details.getOrderPrefix());
        values.put(SiteDetailsTable.COLUMN_ORDER_READY_TIME.name(), details.getOrderReadyTime());
        values.put(SiteDetailsTable.COLUMN_SITE_CURRENCY.name(), details.getSiteCurrency());
        values.put(SiteDetailsTable.COLUMN_TIME_ZONE.name(), details.getTimeZone());
        values.put(SiteDetailsTable.COLUMN_ITEM_WISE_SPICE_LEVELS.name(), details.getItemWiseSpiceLevels());
        values.put(SiteDetailsTable.COLUMN_ORDER_CANCELLATION_IN_MIN.name(), details.getOrderCancellationInMin());
        values.put(SiteDetailsTable.COLUMN_ADMIN_APP_VERSION.name(), details.getAdminAppVersion());
        values.put(SiteDetailsTable.COLUMN_ADMIN_APP_FORCE_UPGRADE.name(), details.getAdminAppForceUpgrade());
        values.put(SiteDetailsTable.COLUMN_EU_APP_SHUTDOWN.name(), details.getEuAppShutdown());
        values.put(SiteDetailsTable.COLUMN_EU_APP_SHUTDOWN_MSG.name(), details.getEuAppShutdownMsg());
        values.put(SiteDetailsTable.COLUMN_OPENING_DAYS_TIMES.name(), details.getOpeningDaysTimes());
        values.put(SiteDetailsTable.COLUMN_CLOSING_DATES.name(), details.getClosingDates());
        values.put(SiteDetailsTable.COLUMN_ORDER_DELIVERY_TYPE.name(), details.getOrderDeliveryType());
        values.put(SiteDetailsTable.COLUMN_DELIVERY_CHARGES_TYPE.name(), details.getDeliveryChargesType());
        values.put(SiteDetailsTable.COLUMN_DELIVERY_CHARGES.name(), details.getDeliveryCharges());
        values.put(SiteDetailsTable.COLUMN_DELIVERY_RADIUS.name(), details.getDeliveryRadius());
        values.put(SiteDetailsTable.COLUMN_DELIVERY_RESTAURANT_TIMES.name(), details.getDeliveryRestaurantTime());
        values.put(SiteDetailsTable.COLUMN_TAKE_AWAY_RESTAURANT_TIMES.name(), details.getTakeAwayRestaurantTime());
        values.put(SiteDetailsTable.COLUMN_ORDER_DELIVERY_TIME.name(), details.getOrderDeliveryTime());
        values.put(SiteDetailsTable.COLUMN_DELIVERY_TYPE.name(), details.getDeliveryType());
        db.insertWithOnConflict(SiteDetailsTable.TABLE_SITE_DETAILS.name(), null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public SiteDetailsEntity getSiteDetails() {
        readDB();
        SiteDetailsEntity details = new SiteDetailsEntity();
        Cursor cursor = db.rawQuery(SiteDetailsTable.INSTANCE.siteDetails(), null);
        if (cursor.moveToFirst()) {
            do {
                details.setSiteDetailId(cursor.getLong(cursor.getColumnIndex(SiteDetailsTable.COLUMN_SITE_DETAIL_ID.name())));
                details.setOrderPrefix(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_ORDER_PREFIX.name())));
                details.setOrderReadyTime(cursor.getLong(cursor.getColumnIndex(SiteDetailsTable.COLUMN_ORDER_READY_TIME.name())));
                details.setSiteCurrency(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_SITE_CURRENCY.name())));
                details.setTimeZone(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_TIME_ZONE.name())));
                details.setItemWiseSpiceLevels(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_ITEM_WISE_SPICE_LEVELS.name())));
                details.setOrderCancellationInMin(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_ORDER_CANCELLATION_IN_MIN.name())));
                details.setAdminAppVersion(cursor.getFloat(cursor.getColumnIndex(SiteDetailsTable.COLUMN_ADMIN_APP_VERSION.name())));
                details.setAdminAppForceUpgrade(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_ADMIN_APP_FORCE_UPGRADE.name())));
                details.setEuAppShutdown(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_EU_APP_SHUTDOWN.name())));
                details.setEuAppShutdownMsg(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_EU_APP_SHUTDOWN_MSG.name())));
                details.setOpeningDaysTimes(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_OPENING_DAYS_TIMES.name())));
                details.setClosingDates(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_CLOSING_DATES.name())));
                details.setOrderDeliveryType(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_ORDER_DELIVERY_TYPE.name())));
                details.setDeliveryChargesType(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_DELIVERY_CHARGES_TYPE.name())));
                details.setDeliveryCharges(cursor.getFloat(cursor.getColumnIndex(SiteDetailsTable.COLUMN_DELIVERY_CHARGES.name())));
                details.setDeliveryRadius(cursor.getLong(cursor.getColumnIndex(SiteDetailsTable.COLUMN_DELIVERY_RADIUS.name())));
                details.setDeliveryRestaurantTime(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_DELIVERY_RESTAURANT_TIMES.name())));
                details.setTakeAwayRestaurantTime(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_TAKE_AWAY_RESTAURANT_TIMES.name())));
                details.setOrderDeliveryTime(cursor.getLong(cursor.getColumnIndex(SiteDetailsTable.COLUMN_ORDER_DELIVERY_TIME.name())));
                details.setDeliveryType(cursor.getString(cursor.getColumnIndex(SiteDetailsTable.COLUMN_DELIVERY_TYPE.name())));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return details;
    }
}
