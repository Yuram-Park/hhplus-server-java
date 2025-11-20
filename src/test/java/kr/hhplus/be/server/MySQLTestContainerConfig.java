package kr.hhplus.be.server;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class MySQLTestContainerConfig {

    private static final MySQLContainer<?> MYSQL_CONTAINTER;

    static {
        MYSQL_CONTAINTER = new MySQLContainer<>("mysql:8")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        MYSQL_CONTAINTER.start();
    }

    public static MySQLContainer<?> getMysqlContainer() {
        return MYSQL_CONTAINTER;
    }
}
