package com.example.appbatch.batch;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommunityDto {

    private String title;
    private String content;
    private Long accountId;
}
