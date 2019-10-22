package com.resto.msgc.backend.card;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

import com.resto.core.common.ApolloCfg;
import com.resto.core.common.RestoConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liuzh
 * @since 2015-12-12 18:22
 */
@EnableApolloConfig({ApolloCfg.CONFIG_APPLICATION,
        ApolloCfg.CONFIG_PUBLIC_APPLICATION,
        ApolloCfg.CONFIG_PUBLIC_APPLICATION_JSP,
        ApolloCfg.CONFIG_PUBLIC_MYBATIS,
        ApolloCfg.CONFIG_PUBLIC_DRUID,
        ApolloCfg.CONFIG_PUBLIC_SWAGGER,
        ApolloCfg.CONFIG_PUBLIC_REDIS,
        "pay", "server"
})
@SpringBootApplication(scanBasePackages = {RestoConstant.BASE_PACKAGE})
@MapperScan(basePackages = "com.resto.msgc.backend.card.mapper")
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
