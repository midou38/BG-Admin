package edios.mlr.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {

    private static SharedPreferences objSharedPreferences;
    private static SharedPreferences.Editor objEditor;


    private static void initializeMemory(Context context) {
        if (objSharedPreferences == null) {
            objSharedPreferences = context.getSharedPreferences("TOI", Context.MODE_PRIVATE);
            objEditor = objSharedPreferences.edit();
            objEditor.apply();
        }
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        if (objEditor == null) {
            initializeMemory(context);
        }
        return objEditor;
    }

    public static SharedPreferences getReader(Context context) {
        initializeMemory(context);
        return objSharedPreferences;
    }
}