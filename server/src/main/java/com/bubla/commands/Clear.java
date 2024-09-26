package com.bubla.commands;

import com.bubla.Application;
import com.bubla.classes.LinkedHashMapOfProducts;
import com.bubla.executer.ServerApplication;


/** Класс команды clear
 * @author ShablsBubla
 */
public class Clear extends PrimeCommand {
    /** Поле описания комнады*/
    public Clear(){super("clear : очистить коллекцию");}

     /** Метод исполнения команды
      *
      * @param args аргумент команды
      * @param application приложение
      */
     @Override
    public String execute(String args, ServerApplication application){
         if(!args.isBlank()){
             return "Недопустимый аргумент: " + args;
         }
         application.setProducts(new LinkedHashMapOfProducts(application.getProducts().getCreationDate()));
         return "Коллекция очищена";
    }
}
