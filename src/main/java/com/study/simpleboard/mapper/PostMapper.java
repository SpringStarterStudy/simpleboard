package com.study.simpleboard.mapper;

import com.study.simpleboard.domain.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {
    void save(@Param("post") Post post);
}
