package com.bubla.message;


import com.bubla.Application;
import com.bubla.classes.Product;
import lombok.Data;

import java.io.Serializable;
@Data
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cmd;
    private String args;
    private Product newProduct;

    public Request(String cmd, String args){
        this.cmd = cmd;
        this.args = args;
    }
}
