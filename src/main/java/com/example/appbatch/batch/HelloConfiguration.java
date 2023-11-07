package com.example.appbatch.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class HelloConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BatchMapper batchMapper;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .incrementer(new RunIdIncrementer())
                .start(helloStep())
                .build();
    }

    @Bean
    public Step helloStep() {
        return stepBuilderFactory.get("helloStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("hello spring batch");
                    return RepeatStatus.FINISHED;
                }).build();
    }


    @Bean
    public Job communityInsertJob() {
        return jobBuilderFactory.get("helloJob")
                .incrementer(new RunIdIncrementer())
                .start(communityInsertStep())
                .build();
    }


    @Bean
    public Step communityInsertStep() {
        return stepBuilderFactory.get("helloStep")
                .tasklet((contribution, chunkContext) -> {
                    List<CommunityDto> communityDtoList = new ArrayList<>();

                    for (int i = 0; i < 10; i++) {
                        CommunityDto communityDto = CommunityDto.builder()
                                .content("테스트")
                                .title("테스트")
                                .accountId(1L)
                                .build();
                        communityDtoList.add(communityDto);
                    }


                    batchMapper.insertCommunities(communityDtoList);
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
