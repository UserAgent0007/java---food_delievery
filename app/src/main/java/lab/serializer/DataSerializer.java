package lab.serializer;

import java.util.List;
import lab.exceptions.DataSerializationException;

public interface DataSerializer<T> {
    void serialize(List<T> items, String filePath) throws DataSerializationException;

    List<T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException;
    
    String getFormat();

    /**
     * Serialize a single item to String
     * @param item Item to serialize
     * @return Serialized string representation
     * @throws DataSerializationException if serialization fails
     */
    String toString(T item) throws DataSerializationException;

    /**
     * Serialize a list of items to String (for HTTP responses)
     * @param items List of items to serialize
     * @return Serialized string representation of the list
     * @throws DataSerializationException if serialization fails
     */
    String listToString(List<T> items) throws DataSerializationException;

    /**
     * Deserialize a single item from String
     * @param str Serialized string
     * @param clazz Class type for deserialization
     * @return Deserialized item
     * @throws DataSerializationException if deserialization fails
     */
    T fromString(String str, Class<T> clazz) throws DataSerializationException;

}
