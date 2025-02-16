package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    int addRelation(@Param("youngsterId") Long youngsterId, @Param("elderId") Long elderId);

    int affirmRelation(@Param("youngsterId") Long youngsterId, @Param("elderId") Long elderId);

    int removeRelation(@Param("youngsterId") Long youngsterId, @Param("elderId") Long elderId);
}
