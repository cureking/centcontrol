package com.renewable.centcontrol.common.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
public class WarningEliminateConstant {

    public static final long WARNING_ELIMINATE_VALIDITY = 1000 * 60 * 30;   // 暂定30分钟

    // WarningEliminate中EliminateWay的设置
    public enum EliminateWayEnum {

        Web_Page(0, "网页方式");

        private String value;
        private Integer code;

        EliminateWayEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static EliminateWayEnum codeOf(int code) {
            for (EliminateWayEnum eliminateWayEnum : values()) {
                if (eliminateWayEnum.getCode() == code) {
                    return eliminateWayEnum;
                }
            }
            log.warn("没有找到对应的枚举", code);
            throw new RuntimeException("没有找到对应的枚举");    //这里由于是在java程序内，无页面响应。故将相关信息导入到日志中。这里抛出的异常由调用方处理
        }
    }
}
