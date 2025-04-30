package com.example.backend.utils.bean;

import com.example.backend.domain.dto.BaseDTO;
import com.example.backend.domain.entity.BaseEntity;
import com.example.backend.domain.vo.BaseVO;
import com.example.backend.utils.AssertUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将entity对象转换为vo(view object)对象的工具类
 * 将dto对象转换为entity对象的工具类
 */
public class BeanCopyUtils {
    private BeanCopyUtils(){
    }

    /**
     * 将entity对象转换为vo对象
     * @param source 数据库实体类
     * @param targetClass vo类
     * @return 返回创建完毕的vo对象
     */
    public static <V extends BaseVO> V copyBean(BaseEntity source, Class<V> targetClass) {
        try {
            V target = targetClass.getConstructor().newInstance();
            return doCopy(source, target);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 将dto对象转换为entity对象
     * @param source dto对象
     * @param targetClass entity对象
     * @return entity对象
     */
    public static <V extends BaseEntity> V copyBean(BaseDTO source, Class<V> targetClass) {
        try {
            V target = targetClass.getConstructor().newInstance();
            return doCopy(source, target);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 复制多个数据库实体类至vo类,调用该方法表示vo类未创建
     */
    public static <V extends BaseVO> List<V> copyBeans(Collection<? extends BaseEntity> sourceList, Class<V> targetClass) {

        return sourceList.stream()
                .map(baseEntity -> copyBean(baseEntity, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * 复制多个数据库实体类至vo类,调用该方法表示vo类已创建
     */
    public static void copyBeans(List<? extends BaseEntity> source, List<? extends BaseVO> target) {
        AssertUtils.isEquals(source.size(), target.size(), "source and target size must be equal");
        int size = source.size();
        for (int i = 0; i < size; i++) {
            doCopy(source.get(i), target.get(i));
        }
    }

    public static <V extends BaseEntity> V doCopy(BaseDTO dto, V entity) {
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static <V extends BaseVO> V doCopy(BaseEntity entity, V vo) {
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
