package com.bubla;

import com.bubla.file_managment.Read;

import java.net.InetAddress;

public class Main {
    public static void main(String[] args) {
        Read read = new Read(System.getenv("FILE_PATH"));
        read.refil();
        try {
            Server server = new Server(InetAddress.getLocalHost(), read.getProducts());
            System.out.println("Сервер начал свою работу");
            server.run();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}