package lab.Service;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoadResultTest {

    @Test
    public void testNumberItems(){
        LoadResult loadResult = new LoadResult(5, 10, 1000);
        assertEquals(15, loadResult.getTotalItems());
    }
}
