package com.renewable.centcontrol.common.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
public class TerminalConstant {

    public enum TerminalStatusEnum {

        Running(0, "正常运行"),
        Alert(2, "警报状态"),
        AlertEliminate(3, "警报消除"),
        Discard(7, "彻底弃用");

        private String value;
        private Integer code;

        TerminalStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static TerminalStatusEnum codeOf(int code) {
            for (TerminalStatusEnum terminalStatusEnum : values()) {
                if (terminalStatusEnum.getCode() == code) {
                    return terminalStatusEnum;
                }
            }
            log.warn("没有找到对应的枚举", code);
            throw new RuntimeException("没有找到对应的枚举");    //这里由于是在java程序内，无页面响应。故将相关信息导入到日志中。这里抛出的异常由调用方处理
        }
    }
}
