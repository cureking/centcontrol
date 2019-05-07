package com.renewable.centcontrol.pojo;

import java.util.Date;

public class SensorRegister {
    private Integer id;

    private Integer terminalId;

    private Integer sensorId;

    private String nickName;

    private String port;

    private String address;

    private Integer type;

    private Integer model;

    private Integer zero;

    private Integer baudrate;

    private Byte cleanType;

    private String cleanKey;

    private Long cleanInterval;

    private Long cleanLastId;

    private String remake;

    private Date createTime;

    private Date updateTime;

    public SensorRegister(Integer id, Integer terminalId, Integer sensorId, String nickName, String port, String address, Integer type, Integer model, Integer zero, Integer baudrate, Byte cleanType, String cleanKey, Long cleanInterval, Long cleanLastId, String remake, Date createTime, Date updateTime) {
        this.id = id;
        this.terminalId = terminalId;
        this.sensorId = sensorId;
        this.nickName = nickName;
        this.port = port;
        this.address = address;
        this.type = type;
        this.model = model;
        this.zero = zero;
        this.baudrate = baudrate;
        this.cleanType = cleanType;
        this.cleanKey = cleanKey;
        this.cleanInterval = cleanInterval;
        this.cleanLastId = cleanLastId;
        this.remake = remake;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public SensorRegister() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Integer getZero() {
        return zero;
    }

    public void setZero(Integer zero) {
        this.zero = zero;
    }

    public Integer getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(Integer baudrate) {
        this.baudrate = baudrate;
    }

    public Byte getCleanType() {
        return cleanType;
    }

    public void setCleanType(Byte cleanType) {
        this.cleanType = cleanType;
    }

    public String getCleanKey() {
        return cleanKey;
    }

    public void setCleanKey(String cleanKey) {
        this.cleanKey = cleanKey == null ? null : cleanKey.trim();
    }

    public Long getCleanInterval() {
        return cleanInterval;
    }

    public void setCleanInterval(Long cleanInterval) {
        this.cleanInterval = cleanInterval;
    }

    public Long getCleanLastId() {
        return cleanLastId;
    }

    public void setCleanLastId(Long cleanLastId) {
        this.cleanLastId = cleanLastId;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake == null ? null : remake.trim();
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