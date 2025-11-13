package lab.serializer;

import java.util.List;
import lab.exceptions.DataSerializationException;

public interface DataSerializer<T> {
    void serialize(List<T> items, String filePath) throws DataSerializationException;

    List<T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException;
    
    String getFormat();

}
