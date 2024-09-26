package com.bubla.classes;


import java.io.Serializable;

/** Класс описывает меру измерения
 *
 */
public enum UnitOfMeasure implements Serializable {
    CENTIMETERS("Centimeters"),
    GRAMS("Grams"),
    MILLIGRAMS("Miligrams");
    private static final long serialVersionUID = 1L;
    private String desc;
    UnitOfMeasure(String desc){
        this.desc = desc;
    }

    @Override
    public String toString(){return desc;}

}
