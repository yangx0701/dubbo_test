package com.yx.api.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class Permission implements Serializable {

    private int id;
    private String name;
    private String url;
    private String description;

}
