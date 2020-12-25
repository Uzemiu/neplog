import cn.neptu.neplog.App;
import cn.neptu.neplog.model.entity.Friend;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.enums.ThumbnailGenerateStrategy;
import cn.neptu.neplog.service.FriendService;
import cn.neptu.neplog.utils.RedisUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class ImgTest {

    @Resource
    FriendService friendService;

    @Test
    public void visitTest(){
        long start = System.currentTimeMillis();
        friendService.countByLabel();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
