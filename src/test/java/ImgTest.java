import cn.neptu.neplog.App;
import cn.neptu.neplog.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class ImgTest {

    @Resource
    MailService mailService;

    @Test
    public void visitTest() throws JsonProcessingException {
        long start = System.currentTimeMillis();


        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
