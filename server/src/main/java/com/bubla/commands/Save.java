package com.bubla.commands;

import com.bubla.Application;
import com.bubla.executer.ServerApplication;
import com.bubla.file_managment.Write;

/** Класс команды save
 *
 */
public class Save extends PrimeCommand {
    /** Поле описания комнады*/
    public Save(){super("save : сохранить коллекцию в файл");}

    /** Метод исполнения команды
     *
     * @param args аргумент команды
     * @param application приложение
     */
    @Override
    public String execute(String args, ServerApplication application){
            Write write = new Write(application.getProducts().getProducts(), System.getenv("FILE_PATH"));
            return write.record();
    }
}
