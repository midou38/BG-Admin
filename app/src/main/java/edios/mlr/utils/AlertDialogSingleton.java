package edios.mlr.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import edios.mlr.R;

public class AlertDialogSingleton {
    private static AlertDialog alertDialog;

    public static void alertDialogShow(final Context context, String title, String message, boolean singleButton, final DialogInterface.OnClickListener onClickListener) {
        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setIcon(R.drawable.oasis_logo);
        alertDialog.setTitle(!TextUtils.isEmpty(title) ? title : context.getString(R.string.app_name));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        String negativeButton = "NO";
        if (singleButton) {
            negativeButton = "OK";
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onClickListener != null) {
                        onClickListener.onClick(dialog, which);
                    }
                }
            });
        } else {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onClickListener != null) {
                        onClickListener.onClick(dialog, which);
                    }
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
        }


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
        });
        alertDialog.show();
    }
}