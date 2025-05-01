package com.example.backend.utils.web;

import com.example.backend.constants.HttpStatus;
import lombok.Getter;

@Getter
public class AppHttpCode {
    // 成功
    public static final AppHttpCode SUCCESS = new AppHttpCode(HttpStatus.SUCCESS,"操作成功");

    // 错误
    public static final AppHttpCode SYSTEM_ERROR = new AppHttpCode(HttpStatus.SYSTEM_ERROR,"系统内部错误");
    public static final AppHttpCode SYSTEM_BUSY_ERROR = new AppHttpCode(HttpStatus.SYSTEM_BUSY,"系统繁忙，请稍后再试");
    public static final AppHttpCode NETWORK_ERROR = new AppHttpCode(HttpStatus.SYSTEM_ERROR,"网络异常，请稍后再试");

    // 客户端错误
    public static final AppHttpCode INDICATOR_RANGE_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"指标范围的格式错误");
    public static final AppHttpCode PAGE_PARAM_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"分页参数数据范围错误");
    public static final AppHttpCode PHONE_FORMAT_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"手机格式错误");
    public static final AppHttpCode USER_SEX_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"用户性别错误");
    public static final AppHttpCode USERNAME_FORMAT_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"用户名格式错误");
    public static final AppHttpCode PASSWORD_FORMAT_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"密码格式错误，只能输入字母数字和,.-_");
    public static final AppHttpCode ID_NUMBER_FORMAT_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"身份证格式错误");
    public static final AppHttpCode PASSWORD_NOT_EQUALS_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"两次密码不相同");
    public static final AppHttpCode PHONE_EXISTS = new AppHttpCode(HttpStatus.BAD_REQUEST,"手机已被绑定");
    public static final AppHttpCode USERNAME_EXISTS = new AppHttpCode(HttpStatus.BAD_REQUEST,"用户名已存在");
    public static final AppHttpCode ID_NUMBER_EXISTS = new AppHttpCode(HttpStatus.BAD_REQUEST,"身份证已存在");
    public static final AppHttpCode REQUEST_BODY_IS_NULL = new AppHttpCode(HttpStatus.BAD_REQUEST, "请求体为空");
    public static final AppHttpCode REQUEST_DATA_FIELD_IS_NULL = new AppHttpCode(HttpStatus.BAD_REQUEST, "请求数据不完全");
    public static final AppHttpCode USERNAME_OR_PASSWORD_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"用户名或密码错误");
    public static final AppHttpCode RELATION_USER_ROLE_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"关系绑定错误，年轻人和年轻人或者老年人和老年人间不需要绑定关系");
    public static final AppHttpCode REMOVE_MYSELF_ERROR = new AppHttpCode(HttpStatus.BAD_REQUEST,"不能操作自己");

    public static final AppHttpCode UNAUTHORIZED_ERROR = new AppHttpCode(HttpStatus.UNAUTHORIZED,"未授权认证");

    public static final AppHttpCode PERMISSION_DENIED_ERROR = new AppHttpCode(HttpStatus.FORBIDDEN,"权限不足，请向管理员反馈");

    public static final AppHttpCode PAGE_PARAM_FOUND_ERROR = new AppHttpCode(HttpStatus.NOT_FOUND,"分页参数不完全");
    public static final AppHttpCode USER_NOT_FOUND_ERROR = new AppHttpCode(HttpStatus.NOT_FOUND,"不存在该用户");
    public static final AppHttpCode ROLE_NOT_FOUND_ERROR = new AppHttpCode(HttpStatus.NOT_FOUND,"不存在该角色");
    public static final AppHttpCode RELATION_NOT_FOUND_ERROR = new AppHttpCode(HttpStatus.NOT_FOUND,"构建关系失败，未找到对应关系，请询问是否申请");
    public static final AppHttpCode INDICATOR_NOT_FOUND_ERROR = new AppHttpCode(HttpStatus.NOT_FOUND,"不存在该指标");

    public static final AppHttpCode RELATION_EXISTS_ERROR = new AppHttpCode(HttpStatus.CONFLICT,"构建关系失败，可能已申请，请指导长辈确认绑定");


    public static final AppHttpCode TOO_MANY_REQUESTS_ERROR = new AppHttpCode(HttpStatus.TOO_MANY_REQUESTS,"请求过于频繁，请稍后再试");

    public static final AppHttpCode FILE_FORMAT_ERROR = new AppHttpCode(HttpStatus.UNSUPPORTED_TYPE,"文件格式错误");



    private final int code;
    private final String message;

    public AppHttpCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public AppHttpCode(String message){
        this(HttpStatus.SYSTEM_ERROR, message);
    }
}
