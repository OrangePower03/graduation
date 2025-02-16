package com.example.backend.aop;

import com.example.backend.annotation.FieldNullable;
import com.example.backend.domain.dto.BaseDTO;
import com.example.backend.exception.GlobalException;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.web.AppHttpCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.util.annotation.NonNull;

import java.lang.reflect.Field;

@Aspect
@Component
public class VerifyRequestData {
    private final Logger log = LoggerFactory.getLogger(VerifyRequestData.class);

    @Before("@annotation(com.example.backend.annotation.VerifyRequestBody)")
    public void verifyRequestBody(JoinPoint jp) throws IllegalAccessException {
        Object[] args = jp.getArgs();
        // 打上注解的方法没参数，抛异常
        AssertUtils.isTrue(args.length > 0, "request no args here, please check your annotation configuration");
        Object dto = args[0];
        AssertUtils.nonNull(dto, AppHttpCode.REQUEST_BODY_IS_NULL);
        doVerify(dto.getClass().getName(), dto);
    }

    /**
     * 递归查看dto中的所有java内置的字段，并判断是否都不为空
     * @param fieldPrefix 当前字段的唯一标识前缀，方便后续根据日志来定为到对应的字段
     */
    private void doVerify(String fieldPrefix, Object obj) throws IllegalAccessException{
        Class<?> clazz = obj.getClass();
        AssertUtils.isTrue(BaseDTO.class.isAssignableFrom(clazz), "request body is not a dto");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if(isNullable(field)) continue;
            if(isBaseType(field)) {
                boolean judge = ObjectUtils.isNull(field.get(obj));
                if (judge) {
                    logNullField(fieldPrefix, field);
                    throw new GlobalException(AppHttpCode.REQUEST_DATA_FIELD_IS_NULL);
                }
            } else {
                Object fieldObj = field.get(obj);
                AssertUtils.nonNull(fieldObj, AppHttpCode.REQUEST_DATA_FIELD_IS_NULL);
                doVerify(fieldPrefix + "#" +  fieldObj.getClass().getName(), fieldObj);
            }
        }
    }

    /**
     * 字段是否为java内置数据类型
     * 注意，dto类中不能有基本数据类型，请均使用包装类！
     */
    private boolean isBaseType(@NonNull Field field) {
        Class<?> clazz = field.getType();
        return clazz.getPackage().getName().startsWith("java");
    }

    /**
     * 字段上是否标识有{@link FieldNullable}注解
     */
    private boolean isNullable(@NonNull Field field) {
        return ObjectUtils.nonNull(field.getAnnotation(FieldNullable.class));
    }

    /**
     * 打印日志，处理空字段的业务代码
     */
    private void logNullField(String fieldPrefix, Field field) {
        log.error("请求体中数据不完整，空数据出现在{}#{}", fieldPrefix, field.getName());
    }
}
