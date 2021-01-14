import cn.hutool.core.util.ArrayUtil;
import cn.neptu.neplog.App;
import cn.neptu.neplog.config.FileServiceFactory;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.model.property.TencentCosProperty;
import cn.neptu.neplog.service.CosService;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.service.impl.ArticleCommentServiceImpl;
import cn.neptu.neplog.service.impl.TencentCloudFileServiceImpl;
import cn.neptu.neplog.utils.RedisUtil;
import cn.neptu.neplog.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    RedisUtil redisUtil;

    @Test
    public void visitTest() throws JsonProcessingException {
        long start = System.currentTimeMillis();

        System.out.println();

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
