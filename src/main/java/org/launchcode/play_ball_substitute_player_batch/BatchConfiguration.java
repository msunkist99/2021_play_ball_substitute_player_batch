package org.launchcode.play_ball_substitute_player_batch;

import org.launchcode.play_ball_substitute_player_batch.fieldMappers.SubstitutePlayerFieldSetMapper;
import org.launchcode.play_ball_substitute_player_batch.listeners.JobCompletionNotificationListener;
import org.launchcode.play_ball_substitute_player_batch.models.SubstitutePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing      // adds many critical beans that support jobs
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("classpath*:1982/Series/*.SUB")
    private Resource[] inputFiles;

    private static final int STARTING_PLAYER = 0;
    private static final int SUBSTITUTE_PLAYER = 1;
    private static final int VISITING_PLAYER = 0;
    private static final int HOME_PLAYER = 1;

    @Bean
    public MultiResourceItemReader<SubstitutePlayer> multiResourceItemReader(){
        MultiResourceItemReader<SubstitutePlayer> reader = new MultiResourceItemReader<>();
        reader.setDelegate(reader());
        reader.setResources(inputFiles);
        return reader;
    }

    @Bean
    // create an ItemReader that reads in a flat file
    // https://www.programcreek.com/java-api-examples/?api=org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
    public FlatFileItemReader<SubstitutePlayer> reader() {
        DefaultLineMapper<SubstitutePlayer> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer("|"));
        lineMapper.setFieldSetMapper(new SubstitutePlayerFieldSetMapper());

        return new FlatFileItemReaderBuilder<SubstitutePlayer>()
                .name("substitutePLayerItemReader")
                .lineMapper(lineMapper)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<SubstitutePlayer> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<SubstitutePlayer>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql( "INSERT INTO game_player (game_id, player_id, start_sub, visit_home, batting_order, field_position, offense_defense) VALUES " +
                        "(:gameId, :playerId, " + SUBSTITUTE_PLAYER + ", :visit_home, :battingOrder, :fieldPosition, :offenseDefense); ")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<SubstitutePlayer> writer) {
        return stepBuilderFactory.get("step1")
                .<SubstitutePlayer,SubstitutePlayer> chunk(10)
                .reader(multiResourceItemReader())
                .writer(writer)
                .faultTolerant()
                .build();
    }

    @Bean
    public Job importSubstitutePlayerJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importSubstitutePlayerJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
