package com.yx.web.util.log;

/**
 * 日志作用范围枚举类
 */
public enum LogScopeEnum {

    ALL,BEFORE,AFTER;

    public boolean contains(LogScopeEnum scope){
        if(this == ALL){
            return true;
        }else {
            return this == scope;
        }
    }

    @Override
    public String toString(){
        String str = "";
        switch (this){
            case ALL:
                break;
            case BEFORE:
                str = "REQUEST";
                break;
            case AFTER:
                str = "RESPONSE";
                break;
            default:
                break;
        }
        return str;
    }
}
