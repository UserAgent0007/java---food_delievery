package lab.config;

public final class ConfigKeys {
    private ConfigKeys() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String DATA_PATH_BASE = "data.path.base";
    public static final String DATA_PATH_CUSTOMER_JSON = "data.path.courses.json";
    public static final String DATA_PATH_CUSTOMER_YAML = "data.path.courses.yaml";
    public static final String DATA_PATH_DELIVERY_JSON = "data.path.teachers.json";
    public static final String DATA_PATH_DELIVERY_YAML = "data.path.teachers.yaml";
    

    public static final String TEST_DATA_COUNT = "test.data.count";
}
