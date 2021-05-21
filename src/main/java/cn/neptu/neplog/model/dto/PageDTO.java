package cn.neptu.neplog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<DTO> {

    private List<DTO> content;

    private Long total;

    public PageDTO(Page<DTO> page){
        content = page.getContent();
        total = page.getTotalElements();
    }
}
