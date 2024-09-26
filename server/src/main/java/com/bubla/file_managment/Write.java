package com.bubla.file_managment;

import com.bubla.classes.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

/** Класс записии коллекции в файл
 *
 */
@Data
public class Write {
    private LinkedHashMap<String, Product> values;
    private String file;

    public Write(LinkedHashMap<String, Product> products, String file){
        this.file = file;
        this.values = products;
    }

    /** Метод записи коллекции в файл
     *
     */
    public String record() {
        String outputString = "Коллекция успешно записана";
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.registerModule(new JavaTimeModule());
            xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String xml = xmlMapper.writeValueAsString(this.values);
            try (BufferedOutputStream buff = new BufferedOutputStream(new FileOutputStream(this.file))) {
                buff.write(xml.getBytes(StandardCharsets.UTF_8));
                buff.flush();
            } catch (IOException e) {
                outputString = e.getMessage();
            } catch (NullPointerException e) {
                outputString = "Переменной FILE_PATH не существует";
            }
        }catch (JsonProcessingException e){
            outputString = "Не получится запарсить";
        }
        return outputString;
    }
}
