package com.example.backend.domain.entity;

import com.example.backend.utils.object.ObjectUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 114514L;

    //主键
    private Long id;

    //创建时间，日期时间类型，默认当前时间
    private Date createTime;

    //更新时间，日期时间类型，默认当前时间，每次更新自动更新
    private Date updateTime;

    //是否删除，一位字符存储，删除为1，未删除为0，默认未删除
    private Integer delFlag;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return ObjectUtils.isEquals(id, ((BaseEntity) obj).id);
    }

    @Override
    public int hashCode() {
        if (ObjectUtils.isNull(id)) {
            throw new IllegalArgumentException("id is null");
        }
        return id.hashCode();
    }
}
