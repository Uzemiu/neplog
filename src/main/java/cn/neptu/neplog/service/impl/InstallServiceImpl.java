package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.params.InstallParam;
import cn.neptu.neplog.service.*;
import cn.neptu.neplog.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.neptu.neplog.constant.BlogPropertyConstant.*;

@Slf4j
@Service("installService")
@RequiredArgsConstructor
public class InstallServiceImpl implements InstallService{

    private final PropertyService propertyService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ArticleService articleService;
    private final AESUtil aesUtil;

    @Override
    public void installBlog(InstallParam installParam) {

        String plainPassword = aesUtil.decrypt(installParam.getPassword());
        String bcryptedPassword = BCrypt.hashpw(plainPassword,BCrypt.gensalt());
        User user = new User();
        BeanUtils.copyProperties(installParam,user);
        user.setLevel(6);
        user.setPassword(bcryptedPassword);
        user.setNickname("Neptu");
        userService.create(user);

        Category category = new Category(null,"Hello Neplog");
        categoryService.create(category);

        Article article = new Article();
        article.setCategoryId(category.getId());
        article.setTitle("欢迎来到" + installParam.getBlogName());
        article.setContent("开始写起第一篇博客吧");
        article.setHtmlContent("<p>开始写起第一篇博客吧</p>");
        article.setSummary("开始写起第一篇博客吧");
        article.setStatus(4);
        articleService.save(article);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        propertyService.resetProperty();
        propertyService.save(BLOG_NAME, installParam.getBlogName());
        propertyService.save(INSTALL_TIME, format.format(new Date()));
        propertyService.save(INSTALL_STATUS, INSTALLED);
        propertyService.save(VISIT_COUNT,"0");
    }
}
