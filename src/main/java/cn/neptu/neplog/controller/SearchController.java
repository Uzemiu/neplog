package cn.neptu.neplog.controller;

import cn.neptu.neplog.constant.ArticleConstant;
import cn.neptu.neplog.model.dto.ArticleDTO;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.query.ArticleQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.ArticleService;
import cn.neptu.neplog.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ArticleService articleService;

    @ApiOperation("搜索文章")
    @GetMapping
    public BaseResponse<PageDTO<ArticleDTO>> search(String content,
                                                    @PageableDefault(sort = "createTime",
                                          direction = Sort.Direction.DESC) Pageable pageable){
        ArticleQuery query = new ArticleQuery();
        query.setContent(content);
        if(!SecurityUtil.isOwner()){
            // 普通用户默认查询未被删除文章的可见文章
            query.setDeleted(false);
            query.setStatus(ArticleConstant.STATUS_PUBLISHED);
            query.setViewPermission(ArticleConstant.VIEW_PERMISSION_ANYBODY);
        }
        return BaseResponse.ok("ok",articleService.queryBy(query, pageable));
    }
}
