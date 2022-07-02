package io.datalake.controller.sys.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailInfo implements Serializable{

    private String host;
    private String port;
    private String account;
    private String password;
    private String ssl;
    private String tls;
    private String recipient;
    
}
