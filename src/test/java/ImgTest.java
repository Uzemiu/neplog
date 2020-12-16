import cn.neptu.neplog.App;
import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.enums.ThumbnailGenerateStrategy;
import cn.neptu.neplog.utils.RedisUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    RedisUtil redisUtil;

    @Test
    public void visitTest(){
        redisUtil.hset("visit","1",1);
        redisUtil.hset("visit","2",2);
        redisUtil.hset("visit","3",3);
        redisUtil.hincr("visit","2",6);
        redisUtil.hincr("visit","3",6);
        Map<Object,Object> map = redisUtil.hmget("visit");
        map.forEach((k,v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }
}
