package com.edu.studentprofile.util;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结果封装
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码: 200-成功, 其他-失败 */
    private int code;

    /** 消息 */
    private String msg;

    /** 数据 */
    private T data;

    /** 时间戳 */
    private long timestamp;

    private R() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok(String msg, T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static <T> R<T> fail(String msg) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public boolean isSuccess() {
        return this.code == 200;
    }
}
