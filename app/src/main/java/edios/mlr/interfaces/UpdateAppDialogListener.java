package edios.mlr.interfaces;

import android.content.DialogInterface;

public interface UpdateAppDialogListener {

    void onPositiveClick(DialogInterface dialog, int which);

    void onNegativeClick(DialogInterface dialog, int which);

}