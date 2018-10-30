package com.demo.zuul.entity;

/**
 *
 * @author admin
 * @param <T>
 */
public class Result<T> {


    /**
     * status 状态码
     */
    private boolean status;

    /**
     *  data 泛型类型的data数据
     */
    private T data;

    /**
     *  msg json返回的信息
     */
    private String msg;

    /**
     *  code 错误码
     */
    private int code;

    /**
    *
    * @Description: 无参构造
    * @auther: 快乐水 樱桃可乐
    * @date: 8:38 2018/9/28
    * @param: []
    * @return:
    *
    */
    public Result() {
    }


    /**
      *
      * @Description:
      * @auther: 快乐水 零度可乐
      * @date: 16:48 2018/9/20
      * @param: [status, data]
      * @return:
     */
    public Result(boolean status, T data) {
        this.status = status;
        this.data = data;
    }

    /**
     *
     * @Description:
     * @auther: 快乐水 零度可乐
     * @date: 16:46 2018/9/20
     * @param: [status, Msg, Code]
     * @return:
     */
    public Result(boolean status, String Msg, int Code) {
        this.status = status;
        this.msg = Msg;
        this.code = Code;
    }

    /**
    *
    * @Description:
    * @auther: 快乐水 樱桃可乐
    * @date: 8:39 2018/9/28
    * @param: [status, msg]
    * @return:
    *
    */
    public Result(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        code = code;
    }

}
