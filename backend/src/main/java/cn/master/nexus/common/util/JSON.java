package cn.master.nexus.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.function.Function;

/**
 * @author : 11's papa
 * @since : 2026/3/4, 星期三
 **/
public class JSON {
    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .changeDefaultVisibility(visibilityChecker ->
                    visibilityChecker.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY))
            .configure(JsonReadFeature.ALLOW_JAVA_COMMENTS, true)
            .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    public static String toJSONString(Object value) {
        return mapper.writeValueAsString(value);
    }

    public static byte[] toJSONBytes(Object value) {
        return mapper.writeValueAsBytes(value);
    }

    public static <T> T parseObject(String content, Class<T> valueType) {
        return mapper.readValue(content, valueType);
    }

    public static Object parseObject(String content) {
        return mapper.readValue(content, Object.class);
    }

    public static Map<String, Object> parseMap(String jsonObject) {
        return mapper.readValue(jsonObject, new TypeReference<>() {
        });
    }

    public static <T> Function<Object, T> objectToType(TypeReference<T> targetType) {
        return obj -> mapper.convertValue(obj, targetType);
    }
}
