package cn.master.nexus.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author : 11's papa
 * @see <a href="https://github.com/FasterXML/jackson-databind?tab=readme-ov-file#10-minute-tutorial-configuration">Jackson Databind Configuration</a>
 * @since : 2026/3/3, 星期二
 **/
@Configuration
public class JacksonConfig {
    @Bean
    public JsonMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.changeDefaultPropertyInclusion(
                    inclusion -> inclusion.withValueInclusion(JsonInclude.Include.NON_NULL)
            );
            // 自动检测所有类的全部属性
            builder.changeDefaultVisibility(
                    visibility -> visibility.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY));
            builder.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            builder.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            builder.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS);
            builder.enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION);
            // builder.enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.findAndAddModules();
            // builder.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
            // 如果一个对象中没有任何的属性，那么在序列化的时候就会报错
            // builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            // builder.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            // builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // builder.addModule();
        };
    }

    @Bean
    public ObjectMapper mapper() {
        return JsonMapper.builder()
                .changeDefaultVisibility(visibility -> visibility.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY))
                .changeDefaultPropertyInclusion(inclusion -> inclusion.withValueInclusion(JsonInclude.Include.NON_NULL))
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
    }
}
