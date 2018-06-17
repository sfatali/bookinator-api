package com.bookinator.api.resources.util;

/**
 * Created by Sabina on 6/13/2018.
 */
public class RequestTemplateItem {
    private String field;
    private String type;
    private boolean required;
    private Integer minLength;
    private Integer maxLength;

    public RequestTemplateItem(String field, String type, boolean required) {
        this.field = field;
        this.type = type;
        this.required = required;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
