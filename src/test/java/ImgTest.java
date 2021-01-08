import cn.hutool.core.util.ArrayUtil;
import cn.neptu.neplog.App;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.service.impl.ArticleCommentServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

//@SpringBootTest(classes = App.class)
//@RunWith(SpringRunner.class)
public class ImgTest {

    @Resource
    ArticleCommentServiceImpl commentService;

    @Test
    public void visitTest(){
        long start = System.currentTimeMillis();

        String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57 MicroMessenger/7.0.3(0x17000321) NetType/WIFI Language/zh_CN";
        UserAgent ua = UserAgent.parseUserAgentString(uaString);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
