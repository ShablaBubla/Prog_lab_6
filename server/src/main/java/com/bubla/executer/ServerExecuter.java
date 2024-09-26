package com.bubla.executer;

import com.bubla.commands.End;
import com.bubla.commands.PrimeCommand;
import com.bubla.commands.Save;

import java.util.HashMap;

public class ServerExecuter extends Executer{
    public ServerExecuter(){
        super();
        HashMap<String, PrimeCommand> serverCommandsList = new HashMap<>();
        serverCommandsList.put("save", new Save());
        serverCommandsList.put("end", new End());
        this.setCommandsList(serverCommandsList);
    }
}
