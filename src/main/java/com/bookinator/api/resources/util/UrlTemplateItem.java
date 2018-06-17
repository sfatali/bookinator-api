package com.bookinator.api.resources.util;

/**
 * Created by Sabina on 6/14/2018.
 */
public class UrlTemplateItem {
    private String param;
    private String type;
    private boolean required;

    public UrlTemplateItem(String param, String type, boolean required) {
        this.param = param;
        this.type = type;
        this.required = required;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
