import cn.neptu.neplog.App;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.service.MailService;
import cn.neptu.neplog.utils.SitemapUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.ap.shaded.freemarker.template.utility.StringUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class ImgTest {

    @Resource
    MailService mailService;

    @Resource
    SitemapUtil sitemapUtil;

    @Test
    public void testSitemap() throws JsonProcessingException {

//        sitemapUtil.generateSitemap();

    }
}
