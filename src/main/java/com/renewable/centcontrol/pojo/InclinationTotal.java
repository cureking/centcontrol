package com.renewable.centcontrol.pojo;

import java.io.Serializable;
import java.util.Date;

public class InclinationTotal implements Serializable {
    private Long id;

    private Integer terminalId;

    private Integer sensorId;

    private Long originId;

    private Double angleX;

    private Double angleY;

    private Double angleTotal;

    private Double directAngle;

    private Double angleInitTotal;

    private Double directAngleInit;

    private Double temperature;

    private String version;

    private Date createTime;

    public InclinationTotal(Long id, Integer terminalId, Integer sensorId, Long originId, Double angleX, Double angleY, Double angleTotal, Double directAngle, Double angleInitTotal, Double directAngleInit, Double temperature, String version, Date createTime) {
        this.id = id;
        this.terminalId = terminalId;
        this.sensorId = sensorId;
        this.originId = originId;
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleTotal = angleTotal;
        this.directAngle = directAngle;
        this.angleInitTotal = angleInitTotal;
        this.directAngleInit = directAngleInit;
        this.temperature = temperature;
        this.version = version;
        this.createTime = createTime;
    }

    public InclinationTotal() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Double getAngleX() {
        return angleX;
    }

    public void setAngleX(Double angleX) {
        this.angleX = angleX;
    }

    public Double getAngleY() {
        return angleY;
    }

    public void setAngleY(Double angleY) {
        this.angleY = angleY;
    }

    public Double getAngleTotal() {
        return angleTotal;
    }

    public void setAngleTotal(Double angleTotal) {
        this.angleTotal = angleTotal;
    }

    public Double getDirectAngle() {
        return directAngle;
    }

    public void setDirectAngle(Double directAngle) {
        this.directAngle = directAngle;
    }

    public Double getAngleInitTotal() {
        return angleInitTotal;
    }

    public void setAngleInitTotal(Double angleInitTotal) {
        this.angleInitTotal = angleInitTotal;
    }

    public Double getDirectAngleInit() {
        return directAngleInit;
    }

    public void setDirectAngleInit(Double directAngleInit) {
        this.directAngleInit = directAngleInit;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}