package com.example.diary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tom.test.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
{
    private ArrayList<UserData> mDataset;//ArrayList с данными пользователя
    private RecyclerView list;
    private RecyclerView.LayoutManager recManager;
    private RecycleItemOnClickListener clickListener;
    private IDatabase db;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView title;
        public TextView date;

        public ViewHolder(View v)
        {
            super(v);

            SharedPreferences shr = PreferenceManager.getDefaultSharedPreferences(MainActivity.getAppContext());
            float titleSize = Float.parseFloat(shr.getString("title_text", "34"));
            float dateSize = Float.parseFloat(shr.getString("date_text", "14"));
            boolean isCentered = shr.getString("gravity_text", "CENTER").equals("CENTER");

            title = (TextView) v.findViewById(R.id.ItemContent);
            title.setTextSize(titleSize);

            if(isCentered)
            {
                title.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            }
            else
            {
                title.setGravity(Gravity.CENTER_HORIZONTAL);
                title.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
            date = (TextView) v.findViewById(R.id.DateText);
            date.setTextSize(dateSize);
            date.setKeyListener(null);

            v.setOnClickListener(this);
        }

        /**
         * On click listener для каждого view
         * @param view
         */
        @Override
        public void onClick(View view)
        {
            if(clickListener != null)
            {
                int pos = list.getChildAdapterPosition(view);
                clickListener.onItemClick(view, pos);
            }
        }
    }

    /**
     * Создает новый ListAdapter, устанавливает RecycleView для их менеджера
     * @param context
     * @param recManager менеджер для RecycleView
     * @param list
     * @throws Exception
     */
    public ListAdapter(Context context, RecyclerView.LayoutManager recManager, RecyclerView list) throws Exception
    {
        this.list = list;
        list.setLayoutManager(recManager);
        list.setAdapter(this);
        list.setHasFixedSize(true);

        db = new Database(context);
        mDataset = db.getContent();
    }

    /**
     * Изменяет соответствующие данные, соответствующие data.getId()
     * @param data
     * @throws Exception
     */
    public void editData(UserData data) throws Exception
    {
        this.removeData(data);
        this.addData(data);
    }

    /**
     * Создает новый ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    /**
     * Биндит holder
     * @param holder
     * @param position позиция холдера
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.title.setText(mDataset.get(position).toString().replace("\n", " "));
        holder.date.setText(mDataset.get(position).getDate());

        //Sets different color for odd and even rows
        if(position % 2 == 0)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#eaeaea"));
        }
    }

    /**
     * Возвращает номер сохранных данных
     * @return
     */
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    /**
     * Добавляет новые данные
     * @param data
     * @throws Exception
     */
    public void addData(UserData data) throws Exception
    {
        db.addData(data);//Adds new data into file
        mDataset.add(data);//Adds data into database
        this.notifyItemInserted(mDataset.size() - 1);//Notify RecyclerView about changes
    }

    /**
     * Устанавливает click listener для RecycleItem
     * @param clickListener
     */
    public void setClickListener(RecycleItemOnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    /**
     * Удаляет данные из RecyclerView и файла
     * @param data
     * @throws Exception
     */
    public void removeData(UserData data) throws Exception
    {
        int pos = getPos(data);
        if(pos < 0)
            throw new Exception("Невозможно найти данные");

        db.removeData(data);
        mDataset.remove(pos);
        this.notifyItemRemoved(pos);
        this.notifyItemRangeChanged(pos, mDataset.size());
    }

    /**
     * Возвращает текщие данные по позиции
     * @param pos позиция элемента
     * @return
     */
    public UserData getData(int pos)
    {
        return mDataset.get(pos);
    }

    /**
     * Возвращает текущуюю позицию данныех, сохраненных в mDataset
     * @param data
     * @return
     */
    private int getPos(UserData data)
    {
        for(int i = 0; i < mDataset.size(); i++)
        {
            if(mDataset.get(i).getId().equals(data.getId()))
                return i;
        }

        return -1;
    }
}
