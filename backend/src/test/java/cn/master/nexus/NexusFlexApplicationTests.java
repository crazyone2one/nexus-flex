package cn.master.nexus;

import cn.master.nexus.common.util.Translator;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

@SpringBootTest
class NexusFlexApplicationTests {
    @Resource
    MessageSource messageSource;
    @Resource
    Translator translator;

    @Test
    void testMessage() {
        System.out.println(messageSource.getMessage("user.not.exist", null, "Not Support Key: user.not.exist", Locale.getDefault()));
    }

    @Test
    void contextLoads() {
        messageSource.getMessage("user.not.exist", null, "Not Support Key: user.not.exist", Locale.getDefault());
        System.out.println(translator.get("user.not.exist"));
    }

}
