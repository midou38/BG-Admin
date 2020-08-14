package edios.mlr.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("ValidFragment")
public class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor objEditor;
    private Activity activity;
    private View component;

    public StartDatePicker(Activity activity, View component) {
        this.activity = activity;
        this.component = component;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog dialog = new DatePickerDialog(activity, this, startYear, startMonth, startDay);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
//        dialog.getDatePicker().setMinDate(new Date().getTime());
        return dialog;

    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        try {
            startDay = dayOfMonth;
            startMonth = monthOfYear + 1;
            startYear = year;

            String dateToBeSet = getMonth(startMonth) + "-" + startDay + "-" + startYear;
//            Date date = new SimpleDateFormat(Constants.db_date_format,Locale.ENGLISH).parse(dateToBeSet);
//            dateToBeSet = new SimpleDateFormat(objSharedPreferences.getString(Constants.selected_date_format, Constants.db_date_format), Locale.ENGLISH).format(date);
            if (component instanceof EditText) {
                ((EditText) component).setText(dateToBeSet);
            } else {
                ((TextView) component).setText(dateToBeSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1].substring(0, 3);
    }


}

