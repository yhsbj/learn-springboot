package com.line;

import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

@Configuration
public class MappingJackson2HttpMessageConverterConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new BeanSerializerModifier() {

            private JsonSerializer<Object> nullJson = new JsonSerializer<Object>() {

                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString("");
                }
            };

            private JsonSerializer<Object> nullArr = new JsonSerializer<Object>() {

                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    if (value == null) {
                        gen.writeStartArray();
                        gen.writeEndArray();
                    }
                }
            };

            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

                for (int i = 0; i < beanProperties.size(); i++) {
                    BeanPropertyWriter writer = beanProperties.get(i);
                    // 判断字段的类型，如果是array，list，set则注册nullSerializer
                    JavaType type = writer.getType();
                    if (type.isArrayType() || type.isCollectionLikeType()) {
                        // 给writer注册一个自己的nullSerializer
                        writer.assignNullSerializer(nullArr);
                    } else {
                        writer.assignNullSerializer(nullJson);
                    }
                }

                return super.changeProperties(config, beanDesc, beanProperties);
            }

        }));

        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
