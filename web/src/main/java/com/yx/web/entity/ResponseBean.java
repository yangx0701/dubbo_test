package com.yx.web.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.HashMap;

@Data
public class ResponseBean implements Serializable {
    private String code;
    private String msg;
    private Object data;

    public ResponseBean(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }



    public static ResponseBean success_nodata(String msg) {
        return new ResponseBean(CommonParam.SUCCESS_CODE,msg,null);
    }

    public static ResponseBean fail_nodata(String msg) {

        return new ResponseBean(CommonParam.FAIL_CODE,msg,null);
    }

    public static ResponseBean success_withdata(String msg, HashMap<String,Object> data) {
        return new ResponseBean(CommonParam.SUCCESS_CODE,msg,data);
    }

    public static ResponseBean fail_withdata(String msg,HashMap<String,Object> data) {

        return new ResponseBean(CommonParam.FAIL_CODE,msg,data);
    }
}
