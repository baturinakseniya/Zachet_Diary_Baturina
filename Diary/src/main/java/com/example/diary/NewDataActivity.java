package com.example.diary;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tom.test.R;

public class NewDataActivity extends AppCompatActivity implements DialogClickListener
{
    private EditText etext;
    private FloatingActionButton floatingRemoveButton;
    private IntentOption mode;
    private UserData data;
    private IntentOption dialogOption;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data_activity);//saveButtonClick cancelButtonClick

        SharedPreferences shr = PreferenceManager.getDefaultSharedPreferences(MainActivity.getAppContext());
        float textSize = Float.parseFloat(shr.getString("editor_text", "12"));

        etext = (EditText)findViewById(R.id.newText);
        etext.setTextSize(textSize);

        floatingRemoveButton = (FloatingActionButton)findViewById(R.id.floatingDeleteButton);

        Intent intent = getIntent();
        mode = (IntentOption) intent.getSerializableExtra("MODE");

        switch(mode)
        {
            case NEW:
                floatingRemoveButton.hide();
                break;
            case EDIT:
                String text = intent.getStringExtra("text");
                String id = intent.getStringExtra("id");
                data = new UserData(text, id);
                break;
            default:
                floatingRemoveButton.hide();
                break;
        }

        String text = getIntent().getStringExtra("text");

        if(text != null)
        {
            etext.setText(text, TextView.BufferType.EDITABLE);
        }

    }

    /**
     * Закрывает текущуюю активити и возвращает к родительской
     */
    private void cancelActivity()
    {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * Соответственно текущему моду сохраняет, изменяет или ничего не делает с данными. Затем закрывает
     * текущую активность и возвращает к родительской
     */
    private void reactToUser()
    {
        switch(mode)
        {
            case NEW:
            {
                String text = etext.getText().toString();
                if(!text.isEmpty())
                {
                    Intent intent = new Intent();
                    intent.putExtra("text", text);
                    intent.putExtra("MODE", IntentOption.NEW);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    cancelActivity();
                }
            }
            break;

            case EDIT:
            {
                String text = etext.getText().toString();
                String id = data.getId();

                if(text != null && !text.isEmpty())
                {
                    Intent intent = new Intent();
                    intent.putExtra("text", text);
                    intent.putExtra("id", id);
                    intent.putExtra("MODE", IntentOption.EDIT);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else//Text is empty, delete it instead of overwrite to empty text
                {
                    finishIntentWithDelete();
                }
            }
            break;

            default:
                cancelActivity();
                break;
        }
    }

    /**
     * Соответственно моду, сохраняет или изменяет данные пользователя
     * @param v
     */
    public void saveButtonClick(View v)
    {
        if(mode != IntentOption.NEW)
        {
            CheckDialog dialog = new CheckDialog("Перезаписать?", getString(R.string.msg_check_save), this, this);
            dialog.show();
        }
        else
        {
            reactToUser();
        }
    }

    /**
     * Показывает диалог и спрашивает пользователя, действительно ли он хочет удалить данные
     * @param v
     */
    public void deleteButtonClick(View v)
    {
        CheckDialog dialog = new CheckDialog("Удалить?", getString(R.string.msg_check_delete), this, this);
        dialogOption = IntentOption.DELETE;
        dialog.show();
    }

    /**
     * Закрывает текущуюю активность, не сохраняет изменения
     * @param v
     */
    public void cancelButtonClick(View v)
    {
        CheckDialog dialog = new CheckDialog("Закрыть?", getString(R.string.msg_check_cancel), this, this);
        dialogOption = IntentOption.CANCEL;
        dialog.show();
    }

    /**
     * Реагирует на кноку закрытия в диалоге
     */
    @Override
    public void onDialogCancelClick()
    {
    }

    /**
     * Финиширует intent и устанавливает DELETE мод
     */
    private void finishIntentWithDelete()
    {
        Intent intent = new Intent();
        intent.putExtra("text", data.getText());
        intent.putExtra("id", data.getId());
        intent.putExtra("MODE", IntentOption.DELETE);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Реагирует на кнопку Ок в диалоге
     */
    @Override
    public void onDialogOkClick()
    {
        if(dialogOption == IntentOption.DELETE)
        {
            finishIntentWithDelete();
        }
        else if(dialogOption == IntentOption.CANCEL)
        {
            cancelActivity();
        }
        else
        {
            reactToUser();
        }
    }
}
