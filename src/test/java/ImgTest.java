import cn.neptu.neplog.App;
import cn.neptu.neplog.model.dto.CommentDTO;
import cn.neptu.neplog.model.entity.ArticleComment;
import cn.neptu.neplog.service.CommentService;
import cn.neptu.neplog.service.impl.CommentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class ImgTest {

    @Resource
    CommentServiceImpl commentService;

    private void insert(){
        for(int i=1;i<=5;i++){
            ArticleComment articleComment = new ArticleComment();
            articleComment.setId(i);
            articleComment.setNickname("");
            articleComment.setContent("");
            articleComment.setArticleId(99);
            commentService.create(articleComment);
        }
        Random random = new Random();
        for(int i=6;i<=20;i++){
            ArticleComment articleComment = new ArticleComment();
            articleComment.setId(i);
            articleComment.setFatherId(random.nextInt(5) + 1);
            articleComment.setNickname("");
            articleComment.setContent("");
            articleComment.setArticleId(99);
            commentService.create(articleComment);
        }
        for(int i=21;i<=40;i++){
            ArticleComment articleComment = new ArticleComment();
            articleComment.setId(i);
            articleComment.setFatherId(random.nextInt(16) + 5);
            articleComment.setNickname("");
            articleComment.setContent("");
            articleComment.setArticleId(99);
            commentService.create(articleComment);
        }
        for(int i=41;i<=60;i++){
            ArticleComment articleComment = new ArticleComment();
            articleComment.setId(i);
            articleComment.setFatherId(random.nextInt(21) + 20);
            articleComment.setNickname("");
            articleComment.setContent("");
            articleComment.setArticleId(99);
            commentService.create(articleComment);
        }
    }

    @Test
    public void visitTest(){
        long start = System.currentTimeMillis();
//        insert();
        List<CommentDTO> all = commentService.listByArticleId(99);
        List<CommentDTO> list = commentService.buildSimpleCommentTree(all);
        List<CommentDTO> flat = commentService.flatMap(list);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
