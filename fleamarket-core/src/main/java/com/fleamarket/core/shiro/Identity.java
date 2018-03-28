package com.fleamarket.core.shiro;

/**
 * 自定义枚举，用来唯一标示用户身份
 * Created By Gss in 2018/3/22
 */
public enum Identity {
    /**
     * 自定义Token的type
     */
    USER("user"), ADMIN("admin");
    private String value;

    Identity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
