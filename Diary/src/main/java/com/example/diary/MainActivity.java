package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.example.tom.test.R;

import static com.example.diary.IntentOption.DELETE;
import static com.example.diary.IntentOption.EDIT;
import static com.example.diary.IntentOption.NEW;

public class MainActivity extends AppCompatActivity implements RecycleItemOnClickListener
{
    private static Context context;
    private ListAdapter lad;
    private FloatingActionButton floatingNewButton;
    private FloatingActionButton floatingRemoveButton;
    private FloatingActionButton floatingSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        PreferenceManager.setDefaultValues(MainActivity.context, R.xml.root_preferences, false);

        floatingNewButton = (FloatingActionButton)findViewById(R.id.floatingSaveButton);
        floatingSettingsButton = (FloatingActionButton)findViewById(R.id.floatingSettingsButton);
        floatingRemoveButton = (FloatingActionButton)findViewById(R.id.floatingDeleteInListButton);
        floatingRemoveButton.hide();

        try
        {
            lad = new ListAdapter
                    (
                            MainActivity.getAppContext(),
                            new LinearLayoutManager(this),
                            (RecyclerView) findViewById(R.id.list)
                    );
        }
        catch(Exception e)
        {
            Messenger.showError("", e.getMessage(), this);
        }

        lad.setClickListener(this);
    }

    /**
     * Реагирует на нажатие новой кнопки и создает новую активити
     * @param v
     */
    public void newButtonClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, NewDataActivity.class);
        intent.putExtra("MODE", NEW);
        this.startActivityForResult(intent, 1);
    }

    /**
     * Метод реагирует на результат активити, соответственно данному результату добавляет, удаляет или изменяет данные
     * @param requestCode
     * @param resultCode
     * @param data результаты активити: modes: 'new', 'edit', 'delete'
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                IntentOption mode = (IntentOption) data.getSerializableExtra("MODE");

                if(mode == NEW)
                {
                    try
                    {
                        String text = data.getStringExtra("text");
                        lad.addData(new UserData(text));
                    }
                    catch(Exception e)
                    {
                        Messenger.showError("", e.getMessage(), this);
                    }
                }
                else if(mode == EDIT)
                {
                    String text = data.getStringExtra("text");
                    String id = data.getStringExtra("id");

                    try
                    {
                        lad.editData(new UserData(text, id));
                    }
                    catch(Exception e)
                    {
                        Messenger.showError("", e.getMessage(), this);
                    }
                }
                else if(mode == DELETE)
                {
                    String text = data.getStringExtra("text");
                    String id = data.getStringExtra("id");

                    try
                    {
                        lad.removeData(new UserData(text, id));
                    }
                    catch(Exception e)
                    {
                        Messenger.showError("", e.getMessage(), this);
                    }
                }
            }

            finish();
            startActivity(getIntent());
        }
    }

    public void SettingsButtonClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        this.startActivityForResult(intent, 1);
    }

    public void deleteInListButtonClick(View v)
    {//to be done...
    }

    /**
     * Возвращает контекст приложения
     * @return
     */
    public static Context getAppContext()
    {
        return MainActivity.context;
    }

    /**
     * Реагирует на нажатие на элемента, пропихивает данные в новую активити с помощью Intent mode -> 'edit' extras -> 'text', 'id'
     * @param v
     * @param position
     */
    @Override
    public void onItemClick(View v, int position)
    {
        UserData data = lad.getData(position);

        Intent intent = new Intent(MainActivity.this, NewDataActivity.class);
        intent.putExtra("text", data.getText());
        intent.putExtra("id", data.getId());
        intent.putExtra("MODE", EDIT);
        this.startActivityForResult(intent, 1);
    }
}
