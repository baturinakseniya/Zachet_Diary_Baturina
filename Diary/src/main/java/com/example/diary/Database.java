package com.example.diary;

import android.content.Context;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *  Сохраняет, изменяет и удаляет данные пользователя из xml файла
 */
public class Database implements IDatabase
{
    private String fpath;
    private Context cont;

    public Database(Context cont)
    {
        this.cont = cont;
        fpath = cont.getFilesDir().getPath() + "/torar.xml";
        init();
    }

    /**
     * Создает xml ФАЙЛ  с корневым элементом если файла с данными еще не существует
     */
    private void init()
    {
        try
        {
            File fl = new File(fpath);
            if(!fl.exists())
            {
                fl.createNewFile();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("records");
                doc.appendChild(rootElement);

                saveXMLToFile(doc);
            }
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Сохранить документ в xml файл
     * @param doc Документ с данными
     * @throws Exception
     */
    private void saveXMLToFile(Document doc) throws Exception
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fpath));
        transformer.transform(source, result);
    }

    /**
     * Возвращает корневой документ из xml файла с данными пользователя
     * @return
     * @throws Exception Содержит сообщение для пользователя
     */
    private Document getXMLDoc() throws Exception
    {
        File fXmlFile = new File(fpath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        return dBuilder.parse(fXmlFile);
    }

    /**
     * Сохраняет новые данные в файл
     * @param data Содержит новые данные для сохранения
     * @throws Exception Содержит сообщение для пользователя
     */
    @Override
    public void addData(UserData data) throws Exception
    {
        try
        {
            Document doc = getXMLDoc();
            Element rootElement = doc.getDocumentElement();

            Attr attr = doc.createAttribute("date");
            attr.setValue(data.getId());

            Element record = doc.createElement("record");

            record.setAttributeNode(attr);
            Element text = (Element)record.appendChild(doc.createElement("text"));
            text.appendChild(doc.createTextNode(data.getText()));

            rootElement.appendChild(record);

            saveXMLToFile(doc);
        }
        catch(Exception e)
        {
            System.err.println("Невозможно добавить данные" + e.getMessage());
        }
    }

    /**
     * Возвращает соответствующие данные из файла, соответствующие уникальному идентификатору data.getId()
     * @param data
     * @throws Exception Содержит сообщение для пользователя
     */
    @Override
    public void removeData(UserData data) throws Exception
    {
        Document doc = getXMLDoc();
        Element rootElement = doc.getDocumentElement();
        NodeList list = doc.getElementsByTagName("record");

        for(int i = 0; i < list.getLength(); i++)
        {
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                if(data.getId().equals(element.getAttribute("date")))
                {
                    rootElement.removeChild(element);
                    saveXMLToFile(doc);
                    break;
                }
            }
            else
            {
                System.err.println("Невозможно удалить данные");
            }
        }
    }

    /**
     * Замещает соответствующие данные (data.getId()) в файле с ноым текстом
     * @param data
     * @throws Exception Contains message for user.
     */
    @Override
    public void editData(UserData data) throws Exception
    {
        removeData(data);
        addData(data);
    }

    /**
     * Загружает ArrayList с данными из файла и затем возвращает его
     * @return ArrayList заполненный данными из файла
     * @throws Exception Содержит сообщение для пользователя
     */
    @Override
    public ArrayList<UserData> getContent() throws Exception
    {
        ArrayList<UserData> it = new ArrayList<UserData>();

        Document doc = getXMLDoc();

        NodeList list = doc.getElementsByTagName("record");
        for(int i = 0; i < list.getLength(); i++)
        {
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                String date = element.getAttribute("date");
                String text = element.getElementsByTagName("text").item(0).getTextContent();
                it.add(new UserData(text, date));
            }
            else
            {
                System.err.println("Невозможно сохранить данные");
            }
        }

        return it;
    }
}
