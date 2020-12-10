import cn.neptu.neplog.model.entity.Tag;
import cn.neptu.neplog.model.enums.ThumbnailGenerateStrategy;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ImgTest {

    public static void main(String[] args) throws IOException {
        Tag tag1 = new Tag(5,33,"nihao");
        tag1.setCreateTime(new Date());
        Tag tag2 = new Tag(6,33,"tag");
        List<Tag> tags = Arrays.asList(new Tag(null,33,"nihao"),new Tag(null,33,"tag"));
        Set<Tag> tag = new HashSet<>();
        tag.add(tag1);
        tag.add(tag2);
        tag.addAll(tags);
        System.out.println(tag);
    }
}
