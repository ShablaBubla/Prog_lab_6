package com.bubla.commands;

import com.bubla.Application;
import com.bubla.executer.ServerApplication;

/** Класс команды remove_key
 *
 */
public class RemoveKey extends PrimeCommand {
    /** Поле описания комнады*/
    public RemoveKey(){super("remove_key null : удалить элемент из коллекции по его ключу");}

    /** Метод исполнения команды
     *
     * @param args аргумент команды
     * @param application приложение
     */
    @Override
    public String execute(String args, ServerApplication application){
        application.getProducts().remove(args);
        return "Объект был удалён";
    }
}
