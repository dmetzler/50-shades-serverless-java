package org.dmetzler.serverless.movie;

import java.util.HashMap;
import java.util.Map;

import org.dmetzler.serverless.movie.service.impl.DynamoDBDaoIT;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import cloud.localstack.Localstack;
import cloud.localstack.docker.LocalstackDocker;
import cloud.localstack.docker.annotation.LocalstackDockerConfiguration;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AwsMovieServiceIT.class, DynamoDBDaoIT.class })
public class AwsSuiteIT {

    private static LocalstackDocker localstackDocker = LocalstackDocker.INSTANCE;

    @ClassRule
    public static ExternalResource testRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            Localstack.teardownInfrastructure();
            try {
                final Map<String, String> environmentVariables = new HashMap<>();
                environmentVariables.put("SERVICES", "dynamodb");
                final LocalstackDockerConfiguration dockerConfig = LocalstackDockerConfiguration.builder()
                                                                                                .randomizePorts(true)
                                                                                                .environmentVariables(
                                                                                                        environmentVariables)
                                                                                                .build();
                localstackDocker.startup(dockerConfig);
            } finally {

            }
        };

        @Override
        protected void after() {
            localstackDocker.stop();
        };
    };
}
