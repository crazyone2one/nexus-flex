package cn.master.nexus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.master.nexus.modules.**.mapper")
public class NexusFlexApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusFlexApplication.class, args);
    }

}
