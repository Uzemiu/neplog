package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.dto.CategoryDTO;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.query.CategoryQuery;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.service.CategoryService;
import cn.neptu.neplog.service.mapstruct.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @AnonymousAccess
    public BaseResponse<List<CategoryDTO>> queryBy(CategoryQuery query){
        return BaseResponse.ok("ok", categoryService.queryBy(query));
    }

    @PostMapping
    public BaseResponse<?> createCategory(@RequestBody Category category){
        return BaseResponse.ok("创建分类成功", categoryService.create(category));
    }

    @PutMapping
    public BaseResponse<?> updateCategory(@RequestBody Category category){
        categoryService.update(category);
        return BaseResponse.ok("更新分类成功");
    }

    @DeleteMapping
    public BaseResponse<?> deleteCategory(@RequestBody Long id){
        categoryService.deleteById(id);
        return BaseResponse.ok("删除分类成功");
    }

}
