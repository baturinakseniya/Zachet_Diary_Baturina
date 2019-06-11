package com.example.diary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Класс исрользуется для упаковки данных пользователя
 */
public class UserData
{
    private String text;
    private String id;
    private static final int MAX_TITLE_LENGHT = 25;

    /**
     * Конструктор, который устанавливает название данных и текст
     * @param text String контент
     */
    public UserData(String text, String id)
    {
        this.text = text;
        this.id = id;
    }

    /**
     * Создает новую UserData и генерирует уникальный ID
     * @param text
     */
    public UserData(String text)
    {
        this.text = text;
        this.id = genId();
    }

    /**
     * Вохвращает сохраненный текст
     * @return text
     */
    public String getText()
    {
        return text;
    }

    /**
     * Возвращает ID
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * Возвращает текст до определенной буквы
     * @param end
     * @return
     */
    public String getText(int end)
    {
        return getText(0, end);
    }

    /**
     * Возвращает текст от начала до конца
     * @param start
     * @param end
     * @return
     */
    public String getText(int start, int end)
    {
        if(text.length() < end)
            return text;

        return text.substring(start, end);
    }

    /**
     * Устанавливает текст
     * @param text
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * Устанавливает дату данныех в формате 'HH:mm:ss dd/MM/yyyy'
     * @return
     */
    public String getDate()
    {
        int pos = id.indexOf('#');
        return id.substring(0, pos);
    }

    /**
     * Возвращает предстваление данныех, максимум 25 букв
     * @return
     */
    @Override
    public String toString()
    {
        return getText(MAX_TITLE_LENGHT);
    }

    /**
     * Генерирует уникальный ID для каждого обънета данныех, на основе времени
     * @return
     */
    private String genId()
    {
        Random rand = new Random();
        int num = rand.nextInt(10001);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();

        String atrb = dateFormat.format(date) + "#" + num;

        return atrb;
    }
}
