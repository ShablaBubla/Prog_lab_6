package com.bubla.file_managment;

import com.bubla.classes.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;

/** Класс чтения из файла
 *
 */
@Data
public class Read {
    private String file;
    private LinkedHashMap<String, Product> products;

    public Read(String file){
        this.file = file;
        this.products = new LinkedHashMap<>();
    }

    /** Метод наполнения коллекции из файла
     *
     */
    public void refil() {
        try (FileReader reader = new FileReader(this.file);){
            StringBuilder xml = new StringBuilder();
            int sym;
            while((sym = reader.read())!=-1){
                xml.append((char)sym);
            }
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.registerModule(new JavaTimeModule());
            LinkedHashMap mapped = xmlMapper.readValue(xml.toString(), LinkedHashMap.class);
            for(Object key: mapped.keySet()){
                LinkedHashMap newProduct = (LinkedHashMap)mapped.get(key);
                Person owner = null;
                try {
                    LinkedHashMap newOwner = (LinkedHashMap) newProduct.get("owner");
                    owner = new Person(newOwner.get("name").toString(), LocalDateTime.parse(newOwner.get("birthday").toString()), Long.parseLong(newOwner.get("weight").toString()));
                } catch (ClassCastException e){
                    System.out.print("");
                }
                LinkedHashMap newCoordinates = (LinkedHashMap) newProduct.get("coordinates");
                Coordinates coordinates = new Coordinates(Float.parseFloat(newCoordinates.get("x").toString()), Integer.parseInt(newCoordinates.get("y").toString()));
                UnitOfMeasure unitOfMeasure = switch(newProduct.get("unitOfMeasure").toString()){
                    case "CENTIMETERS" -> UnitOfMeasure.CENTIMETERS;
                    case "MILIGRAMS" -> UnitOfMeasure.MILLIGRAMS;
                    default -> UnitOfMeasure.GRAMS;
                };

                String newDate = newProduct.get("creationDate").toString();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(newDate, formatter);
                Date date = Date.from(zonedDateTime.toInstant());

                Product product = new Product(Long.parseLong(newProduct.get("id").toString()), newProduct.get("name").toString(), coordinates, date, Long.parseLong(newProduct.get("price").toString()), unitOfMeasure, owner);
                this.products.put(key.toString(), product);
            }
        } catch (NullPointerException e){
            System.out.println("Переменной FILE_PATH не существует");
        } catch (IOException e){
            System.out.println("Коллекция не записана: " + e.getMessage() + "\n");
        }
    }
}
