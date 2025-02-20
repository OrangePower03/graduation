package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    int addRelation(@Param("youngsterId") Long youngsterId, @Param("elderId") Long elderId);

    int affirmRelation(@Param("youngsterId") Long youngsterId, @Param("elderId") Long elderId);

    int removeRelation(@Param("youngsterId") Long youngsterId, @Param("elderId") Long elderId);

    List<Long> getElderIdByYoungsterId(@Param("youngsterId") Long youngsterId, @Param("status") Integer status);

    List<Long> getYoungsterIdByElderId(@Param("elderId") Long elderId, @Param("status") Integer status);

    int containsRelations(@Param("youngsterId") Long youngsterId, @Param("elderId") Long elderId);

}
