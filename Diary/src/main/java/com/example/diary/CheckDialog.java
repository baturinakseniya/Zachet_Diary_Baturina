package com.example.diary;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.fragment.app.DialogFragment;

import com.example.tom.test.R;

/**
 * Создает диалог с двумя кнопками, когда нужно подтвердить действие пользователя
 */
public class CheckDialog extends DialogFragment
{
    private android.app.AlertDialog.Builder builder;
    private DialogClickListener listener;

    /**
     * Создать новый диалог
     * @param listener
     */
    CheckDialog(String title, String msg, Activity activity, DialogClickListener listener)
    {
        this.listener = listener;
        setDialog(title, msg, activity);
    }

    /**
     * Установить диалог
     * @param title Оглаление диалога
     * @param msg Сообщение для пользователя
     * @param activity Текущая активити, в которой нужно показать диалог
     */
    public void setDialog(String title, String msg, Activity activity)
    {
        builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle(title);//Sets title for the dialog

        //Sets messages of buttons
        builder.setMessage(msg).setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                listener.onDialogOkClick();
            }
        }).setNegativeButton(R.string.msg_cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                listener.onDialogCancelClick();
            }
        });

        builder.create();
    }

    /**
     * Показать диалог
     */
    public void show()
    {
        builder.show();
    }
}
