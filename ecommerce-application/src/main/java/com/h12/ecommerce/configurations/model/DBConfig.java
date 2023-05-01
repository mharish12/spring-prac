package com.h12.ecommerce.configurations.model;

import org.springframework.beans.factory.annotation.Value;

public class DBConfig {
    @Value("${h12.ecommerce.db.url}")
    private String url;
    @Value("${h12.ecommerce.db.username}")
    private String userName;
    @Value("${h12.ecommerce.db.password}")
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
