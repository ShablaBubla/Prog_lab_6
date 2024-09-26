package com.bubla.classes;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;

/** Класс описывает коллекцию продуктов
 *
 */
@Data
public class LinkedHashMapOfProducts implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedHashMap<String, Product> products;
    private String creationDate;
    private int size;
    private String type;
    private String modDate;

    public LinkedHashMapOfProducts(LinkedHashMap<String, Product> map){
        Date current = new Date();
        this.creationDate = current.toString();
        this.size = map.size();
        this.type = "LinkedHashMap";
        this.modDate = current.toString();
        this.products = map;
    }
    public LinkedHashMapOfProducts(){
        Date current = new Date();
        this.creationDate = current.toString();
        this.size = 0;
        this.type = "LinkedHashMap";
        this.modDate = current.toString();
        this.products = new LinkedHashMap<>();
    }

    public LinkedHashMapOfProducts(String creationDate){
        Date current = new Date();
        this.creationDate = creationDate;
        this.size = 0;
        this.type = "LinkedHashMap";
        this.modDate = current.toString();
        this.products = new LinkedHashMap<>();
    }

    public void add(String key, Product prod){
        this.size += 1;
        Date current = new Date();
        this.modDate = current.toString();
        this.products.put(key, prod);
    }

    public void update(String key, Product prod){
        this.products.replace(key, prod);
        this.size += 1;
        Date current = new Date();
        this.modDate = current.toString();
    }
    public void remove(String key){
        this.size -= 1;
        Date current = new Date();
        this.modDate = current.toString();
        this.products.remove(key);
    }
    public ArrayList<Product> sort(){
        ArrayList<Product> values = new ArrayList<>(this.products.values());
        Collections.sort(values);
        return values;
    }
}
