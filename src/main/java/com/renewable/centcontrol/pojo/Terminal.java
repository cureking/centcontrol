package com.renewable.centcontrol.pojo;

import java.util.Date;

public class Terminal {
    private Integer id;

    private Integer productId;

    private String ip;

    private String mac;

    private String name;

    private String mark;

    private Date createTime;

    private Date updateTime;

    public Terminal(Integer id, Integer productId, String ip, String mac, String name, String mark, Date createTime, Date updateTime) {
        this.id = id;
        this.productId = productId;
        this.ip = ip;
        this.mac = mac;
        this.name = name;
        this.mark = mark;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Terminal() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}