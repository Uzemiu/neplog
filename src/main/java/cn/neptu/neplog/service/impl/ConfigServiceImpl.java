package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Config;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.params.InstallParam;
import cn.neptu.neplog.model.support.BlogConfig;
import cn.neptu.neplog.repository.ConfigRepository;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.ConfigService;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import cn.neptu.neplog.utils.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.*;

import static cn.neptu.neplog.config.common.BlogConfigConstant.*;

@Slf4j
@Service("configService")
public class ConfigServiceImpl extends AbstractCrudService<Config,Integer> implements ConfigService {

    private final ConfigRepository configRepository;
    private final UserService userService;
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final AESUtil aesUtil;

    private final List<String> blogConfigNames;

    public ConfigServiceImpl(ConfigRepository configRepository,
                             UserService userService,
                             ArticleService articleService,
                             CategoryService categoryService,
                             AESUtil aesUtil) {
        super(configRepository);
        this.configRepository = configRepository;
        this.userService = userService;
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.aesUtil = aesUtil;

        this.blogConfigNames = Arrays.asList(BLOG_NAME, INSTALL_STATUS);
    }

    @Override
    public void installBlog(InstallParam installParam) {
        if(installed()){
            throw new BadRequestException("当前博客已创建完毕，请勿重复创建");
        }

        save(BLOG_NAME, installParam.getBlogName());

        try {
            String plainPassword = aesUtil.decrypt(installParam.getPassword());
            String bcryptedPassword = BCrypt.hashpw(plainPassword,BCrypt.gensalt());
            User user = new User();
            BeanUtils.copyProperties(installParam,user);
            user.setLevel(6);
            user.setPassword(bcryptedPassword);
            userService.save(user);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            log.error("Error in parsing AES encrypted password: " + installParam.getPassword());
            throw new BadRequestException("异常密码");
        }

        Category category = new Category(null,"Hello Neplog");
        categoryService.save(category);

        Article article = new Article();
        article.setCategoryId(category.getId());
        article.setTitle("欢迎来到" + installParam.getBlogName());
        article.setContent("开始写起第一篇博客吧");
        article.setHtmlContent("<p>开始写起第一篇博客吧</p>");
        article.setSummary("开始写起第一篇博客吧");
        article.setStatus(4);
        articleService.save(article);

        save(INSTALL_STATUS, INSTALLED);
    }

    @Override
    public Config save(Config config) {
        return configRepository.save(config);
    }

    @Override
    public List<Config> save(Map<String, String> blogConfig) {
        List<Config> configs = new LinkedList<>();
        blogConfig.forEach((key,value) -> configs.add(new Config(null,key,value)));
        return configRepository.saveAll(configs);
    }

    @Override
    public Map<String, String> getBlogConfig() {
        return listConfigsIn(blogConfigNames);
    }

    @Override
    public Map<String, String> listConfigsIn(Collection<String> keys) {
        List<Config> configs = configRepository.getByKeyIn(blogConfigNames);
        Map<String, String> result = new HashMap<>(keys.size());
        configs.forEach(config -> result.put(config.getKey(),config.getValue()));
        return result;
    }

    @Override
    public Config save(String key, String value) {
        return configRepository.save(new Config(null,key,value));
    }

    @Override
    public boolean installed() {
        Optional<Config> config = configRepository.getByKey(INSTALL_STATUS);
        return config.map(blogConfig -> blogConfig.getValue().equals(INSTALLED)).orElse(false);
    }
}
