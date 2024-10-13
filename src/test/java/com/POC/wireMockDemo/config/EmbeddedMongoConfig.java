//package com.POC.wireMockDemo.config;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import de.flapdoodle.embed.mongo.MongodExecutable;
//import de.flapdoodle.embed.mongo.MongodStarter;
//import de.flapdoodle.embed.mongo.config.IMongodConfig;
//import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
//import de.flapdoodle.embed.mongo.config.Net;
//import de.flapdoodle.embed.mongo.distribution.Version;
//
//import java.io.IOException;
//
//@TestConfiguration
//public class EmbeddedMongoConfig {
//
//    @Bean
//    public MongodExecutable embeddedMongoServer() throws IOException {
//        IMongodConfig mongodConfig = new MongodConfigBuilder()
//                .version(Version.Main.PRODUCTION)
//                .net(new Net("localhost", 27017, true))
//                .build();
//
//        MongodStarter starter = MongodStarter.getDefaultInstance();
//        return starter.prepare(mongodConfig);
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate() throws IOException {
//        embeddedMongoServer().start();
//        return new MongoTemplate(new SimpleMongoClientDbFactory("mongodb://localhost:27017/test"));
//    }
//}
