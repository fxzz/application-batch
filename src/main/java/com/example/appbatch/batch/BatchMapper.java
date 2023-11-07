package com.example.appbatch.batch;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BatchMapper {
    void insertCommunities(List<CommunityDto> communityDtoList);
}
