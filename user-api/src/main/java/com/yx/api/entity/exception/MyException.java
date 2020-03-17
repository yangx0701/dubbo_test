package com.yx.api.entity.exception;

import lombok.Data;

@Data
public class MyException extends RuntimeException {
    private String msg;
    private String causeMsg;

    public MyException(String msg,String causeMsg) {
        this.msg = msg;
        this.causeMsg = causeMsg;
    }

}
