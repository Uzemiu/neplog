package cn.neptu.neplog.service.impl;

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

@Slf4j
@Service("installService")
@RequiredArgsConstructor
public class InstallServiceImpl implements InstallService{

    private final BlogConfigService blogConfigService;
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

        Category category = new Category();
        category.setName("Hello Neplog");
        categoryService.create(category);

        Article article = new Article();
        article.setCategory(category);
        article.setTitle("欢迎来到" + installParam.getBlogName());
        article.setContent("开始写起第一篇博客吧");
        article.setHtmlContent("<p>开始写起第一篇博客吧</p>");
        article.setSummary("开始写起第一篇博客吧");
        article.setStatus(4);
        articleService.create(article);

        blogConfigService.resetConfig();
    }
}
