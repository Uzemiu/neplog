import cn.hutool.core.util.ArrayUtil;
import cn.neptu.neplog.App;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.service.impl.ArticleCommentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class ImgTest {

    @Resource
    ArticleCommentServiceImpl commentService;

//    private void insert(){
//        for(int i=1;i<=5;i++){
//            ArticleComment articleComment = new ArticleComment();
//            articleComment.setId(i);
//            articleComment.setNickname("");
//            articleComment.setContent("");
//            articleComment.setArticleId(99);
//            commentService.create(articleComment);
//        }
//        Random random = new Random();
//        for(int i=6;i<=20;i++){
//            ArticleComment articleComment = new ArticleComment();
//            articleComment.setId(i);
//            articleComment.setFatherId(random.nextInt(5) + 1);
//            articleComment.setNickname("");
//            articleComment.setContent("");
//            articleComment.setArticleId(99);
//            commentService.create(articleComment);
//        }
//        for(int i=21;i<=100;i++){
//            ArticleComment articleComment = new ArticleComment();
//            articleComment.setId(i);
//            articleComment.setFatherId(random.nextInt(16) + 5);
//            articleComment.setNickname("");
//            articleComment.setContent("");
//            articleComment.setArticleId(99);
//            commentService.create(articleComment);
//        }
//        for(int i=101;i<=2000;i++){
//            ArticleComment articleComment = new ArticleComment();
//            articleComment.setId(i);
//            articleComment.setFatherId(random.nextInt(81) + 20);
//            articleComment.setNickname("");
//            articleComment.setContent("");
//            articleComment.setArticleId(99);
//            commentService.create(articleComment);
//        }
//    }

    @Test
    public void visitTest(){
        long start = System.currentTimeMillis();

        List<CommentDTO> all = commentService.listByArticleId(99L);
        List<CommentDTO> list = commentService.buildSimpleCommentTree(all);
        List<CommentDTO> flat = commentService.flatMap(list);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
