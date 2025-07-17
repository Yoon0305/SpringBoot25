package org.mbc.board.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration // 환경 설정 용이다.
public class RootConfig {

    @Bean // 환경 설정용 객체로 지정
    public ModelMapper getMapper() {
        //implementation 'org.modelmapper:modelmapper:3.1.0'

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

                return modelMapper;
                // Entity를 DTO로 변환하게 환경설정함.
                // https://velog.io/@hjhearts/SpringModelMapper-ModelMapper%EC%82%AC%EC%9A%A9%EB%B2%95-%EC%B4%9D%EC%A0%95%EB%A6%AC

    }
}
