import cn.neptu.neplog.App;
import cn.neptu.neplog.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class MailTest {

    @Resource
    MailService mailService;

    @Test
    public void testMail(){
        mailService.sendSimpleMail("@qq.com","✧(๑•̀ㅁ•́ฅ早","✧(๑•̀ㅁ•́ฅ早");
    }
}
