package com.example.diary;

import java.util.ArrayList;


/**
 * Интерфейс для базы данныех, содержит базовые операции для добавления, удаления, восстановления данных пользователя.
 * Методы бросают исключения, когда пользовательский ввод не корректный
 * Исключения содержат сообщения для пользователя.
 */
public interface IDatabase
{
    /**
     * Добавляет новые данные пользователя
     * @param data
     * @throws Exception если что-то не так, содержит сообщение для пользователя
     */
    void addData(UserData data) throws Exception;

    /**
     * Заменяет старые данные новыми.
     * @param newData Новые данные
     * @throws Exception если что-то не так, содержит сообщение для пользователя
     */
    void editData(UserData newData) throws Exception;

    /**
     * Удаляет данные пользователя.
     * @param data данные для удаления
     * @throws Exception если что-то не так, содержит сообщение для пользователя
     */
    void removeData(UserData data) throws Exception;

    ArrayList<UserData> getContent() throws Exception;
}
