package com.example.appbatch.batch;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class LikesBatchConfiguration {

    private final int CHUNK_SIZE = 10;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job communityLikesUpdateJob() throws Exception {
        return jobBuilderFactory.get("communityLikesUpdateJob")
                .incrementer(new RunIdIncrementer())
                .start(communityLikesUpdateStep())
                .build();
    }

    @Bean
    @JobScope
    public Step communityLikesUpdateStep() throws Exception {
        return stepBuilderFactory.get("communityLikesUpdateStep")
                .<LikesDto, LikesDto>chunk(CHUNK_SIZE)
                .reader(likesWithStatusFalseReader())
                .writer(compositeItemWriter())
                .build();
    }

    @Bean
    public CompositeItemWriter<LikesDto> compositeItemWriter() throws Exception {
        final CompositeItemWriter<LikesDto> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(communityLikesUpdateWriter(), likesStatusUpdateWriter()));
        return compositeItemWriter;
    }


    @Bean
    @StepScope
    public MyBatisBatchItemWriter<LikesDto> likesStatusUpdateWriter() throws Exception {
        return new MyBatisBatchItemWriterBuilder<LikesDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.example.appbatch.batch.BatchMapper.updateLikesBatchStatus")
                .itemToParameterConverter(likesDto -> {
                    log.info("LikesId: {}", likesDto.getLikesId());
                    Map<String, Object> parameter = new HashMap<>();
                    parameter.put("likesId", likesDto.getLikesId());
                    return parameter;
                })
                .build();
    }


    @Bean
    @StepScope
    public MyBatisBatchItemWriter<LikesDto> communityLikesUpdateWriter() throws Exception {
        return new MyBatisBatchItemWriterBuilder<LikesDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.example.appbatch.batch.BatchMapper.updateLikesCount")
                .itemToParameterConverter(likesDto -> {
                    log.info("id: {}", likesDto.getCommunityId());
                    Map<String, Object> parameter = new HashMap<>();
                    parameter.put("communityId", likesDto.getCommunityId());
                    return parameter;
                })
                .build();
    }


    @Bean
    @StepScope
    public MyBatisPagingItemReader<LikesDto> likesWithStatusFalseReader() throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("status", false);

        return new MyBatisPagingItemReaderBuilder<LikesDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.example.appbatch.batch.BatchMapper.selectLikesAllWithStatus")
                .parameterValues(parameterValues)
                .build();
    }



}
