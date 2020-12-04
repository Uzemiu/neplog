package cn.neptu.neplog.model.option;

import cn.neptu.neplog.model.enums.ThumbnailGenerateStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailOption {

    private boolean generate;
    private ThumbnailGenerateStrategy generateStrategy;
    private int width;
    private int height;
}
