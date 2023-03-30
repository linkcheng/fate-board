/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.ai.fate.board.bootstrap;


import org.apache.catalina.Context;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.websocket.server.WsSci;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
@ComponentScan(basePackages = {"com.webank.*"})
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@Configuration
@EnableScheduling
public class Bootstrap {
    public static void main(String[] args) {
//        long timestamp = 1680078127L;
//        long timestamp1 = 1680078732L;
//        long timestampValue = new Timestamp(System.currentTimeMillis()).getTime();
//        System.out.println(timestampValue);
//        if (timestamp < timestampValue - 60000 || timestamp > timestampValue + 60000) {
//            System.out.println("time error");
//        }
//
//        String nonce = "HYbBIn8wWSSK";
//        String passwordValue = "49df4a4c0ba6c9d7a55bf60ccf4d57b30b263d9d";
//        String digest = "";
//        try {
//            digest = computeDigest(nonce + timestamp, passwordValue);
//        } catch (Exception e) {
//            System.out.println("passwd error");
//            System.out.println(digest);
//        }

        try {
            ConfigurableApplicationContext context = SpringApplication.run(Bootstrap.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String computeDigest(String message, String secret) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);

        byte[] digest = mac.doFinal(message.getBytes());
        byte[] hexDigest = new Hex().encode(digest);

        return new String(hexDigest, StandardCharsets.ISO_8859_1);
    }

    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.addServletContainerInitializer(new WsSci(), null);
            }
        };
    }
}
