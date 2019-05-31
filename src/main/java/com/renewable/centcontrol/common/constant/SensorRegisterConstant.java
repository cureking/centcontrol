package com.renewable.centcontrol.common.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
public class SensorRegisterConstant {


    // 注册传感器的状态Enum
    public enum SensorRegisterStatusEnum {

        Running(Byte.parseByte(String.valueOf(0)), "正常运行"),
        Alert(Byte.parseByte(String.valueOf(4)), "警报状态"),
        AlertEliminate(Byte.parseByte(String.valueOf(5)), "警报消除"),
        Discard(Byte.parseByte(String.valueOf(7)), "彻底弃用");

        private String value;
        private Byte code;

        SensorRegisterStatusEnum(byte code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public byte getCode() {
            return code;
        }

        public static SensorRegisterStatusEnum codeOf(byte code) {
            for (SensorRegisterStatusEnum sensorRegisterStatusEnum : values()) {
                if (sensorRegisterStatusEnum.getCode() == code) {
                    return sensorRegisterStatusEnum;
                }
            }
            log.warn("没有找到对应的枚举", code);
            throw new RuntimeException("没有找到对应的枚举");    //这里由于是在java程序内，无页面响应。故将相关信息导入到日志中。这里抛出的异常由调用方处理
        }
    }
}
