package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.model.dto.CategoryDTO;
import cn.neptu.neplog.model.entity.Category;
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

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @GetMapping
    @AnonymousAccess
    public BaseResponse<List<CategoryDTO>> findAll(){
        return BaseResponse.ok("ok",categoryMapper.toDto(categoryService.findAll()));
    }

    @PostMapping
    public BaseResponse<?> createCategory(Category category){
        categoryService.save(category);
        return BaseResponse.ok();
    }

    @DeleteMapping
    public BaseResponse<?> deleteCategory(Integer id){
        return BaseResponse.ok();
    }

}
