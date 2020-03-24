package com.yx.web.util.log;

/**
 * 日志类型枚举类
 */
public enum LogTypeEnum {

    WEB("-1"), DUBBO("1"), MQ("2");

    private final String value;

    LogTypeEnum(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }
}
