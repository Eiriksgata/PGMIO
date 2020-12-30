# PGM图片格式工具类演示代码

> 在项目： [file-pgmio-java](https://gist.github.com/armanbilge/3276d80030d1caa2ed7c#file-pgmio-java-L2) 的基础上进行了完善

项目说明文章:
[CSND-JPG等图片格式转成PGM](https://blog.csdn.net/Keith_Prime/article/details/106403147)

## 测试案例:
```java
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
```
