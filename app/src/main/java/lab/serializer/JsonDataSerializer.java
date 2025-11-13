package lab.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonDataSerializer<T> extends AbstractDataSerializer<T> {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataSerializer.class);

    
    public JsonDataSerializer() {
        super(createDefaultObjectMapper());
        logger.debug("JsonDataSerializer initialized with pretty printing enabled");
    }

    public JsonDataSerializer(ObjectMapper objectMapper) {
        super(objectMapper);
        logger.debug("JsonDataSerializer initialized with custom ObjectMapper");
    }

    private static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    @Override
    public String getFormat() {
        return "JSON";
    }
}
