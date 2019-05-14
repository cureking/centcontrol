package com.renewable.centcontrol.pojo;

import java.util.Date;

public class Project {
    private Integer id;

    private String name;

    private String mark;

    private Date createTime;

    private Date updateTime;

    public Project(Integer id, String name, String mark, Date createTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.mark = mark;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Project() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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