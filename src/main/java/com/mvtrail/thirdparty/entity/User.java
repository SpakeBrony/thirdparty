package com.mvtrail.thirdparty.entity;

/**
 * Copyright 2019 bejson.com
 */
import java.util.Date;

/**
 * Auto-generated: 2019-08-14 16:7:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class User {

    private String user_token;
    private Date user_login_time;
    private int id;
    private String password_digest;
    private String account;
    private String e_image;
    private String e_name;
    private String e_sex;
    private String e_birthday;
    private Date created_at;
    private Date updated_at;
    private int e_integral;
    private String standby;
    private String other_account;
    private String login_way;
    private String email;
    private String sent_at;
    private String reset_token;
    private String activate;
    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }
    public String getUser_token() {
        return user_token;
    }

    public void setUser_login_time(Date user_login_time) {
        this.user_login_time = user_login_time;
    }
    public Date getUser_login_time() {
        return user_login_time;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setPassword_digest(String password_digest) {
        this.password_digest = password_digest;
    }
    public String getPassword_digest() {
        return password_digest;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccount() {
        return account;
    }

    public void setE_image(String e_image) {
        this.e_image = e_image;
    }
    public String getE_image() {
        return e_image;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }
    public String getE_name() {
        return e_name;
    }

    public void setE_sex(String e_sex) {
        this.e_sex = e_sex;
    }
    public String getE_sex() {
        return e_sex;
    }

    public void setE_birthday(String e_birthday) {
        this.e_birthday = e_birthday;
    }
    public String getE_birthday() {
        return e_birthday;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    public Date getCreated_at() {
        return created_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
    public Date getUpdated_at() {
        return updated_at;
    }

    public void setE_integral(int e_integral) {
        this.e_integral = e_integral;
    }
    public int getE_integral() {
        return e_integral;
    }

    public void setStandby(String standby) {
        this.standby = standby;
    }
    public String getStandby() {
        return standby;
    }

    public void setOther_account(String other_account) {
        this.other_account = other_account;
    }
    public String getOther_account() {
        return other_account;
    }

    public void setLogin_way(String login_way) {
        this.login_way = login_way;
    }
    public String getLogin_way() {
        return login_way;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setSent_at(String sent_at) {
        this.sent_at = sent_at;
    }
    public String getSent_at() {
        return sent_at;
    }

    public void setReset_token(String reset_token) {
        this.reset_token = reset_token;
    }
    public String getReset_token() {
        return reset_token;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }
    public String getActivate() {
        return activate;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_token='" + user_token + '\'' +
                ", user_login_time=" + user_login_time +
                ", id=" + id +
                ", password_digest='" + password_digest + '\'' +
                ", account='" + account + '\'' +
                ", e_image='" + e_image + '\'' +
                ", e_name='" + e_name + '\'' +
                ", e_sex='" + e_sex + '\'' +
                ", e_birthday='" + e_birthday + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", e_integral=" + e_integral +
                ", standby='" + standby + '\'' +
                ", other_account='" + other_account + '\'' +
                ", login_way='" + login_way + '\'' +
                ", email='" + email + '\'' +
                ", sent_at='" + sent_at + '\'' +
                ", reset_token='" + reset_token + '\'' +
                ", activate='" + activate + '\'' +
                '}';
    }
}