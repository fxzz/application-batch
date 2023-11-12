package com.example.appbatch.batch;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BatchMapper {
    List<LikesDto> selectLikesAllWithStatus(Map map);
    void updateLikesCount(Map map);
    void updateLikesBatchStatus(Map map);
}
