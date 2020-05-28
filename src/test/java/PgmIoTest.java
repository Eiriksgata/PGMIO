import org.junit.Test;
import utlis.PgmIo;

import java.io.File;
import java.io.IOException;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: PACKAGE_NAME
 * @date:2020/5/28
 **/
public class PgmIoTest {

    @Test
    public void pgmWriteTest() {

        try {
            PgmIo.write(new File("image/sa1.jpg"), new File("image/sa1.pgm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
