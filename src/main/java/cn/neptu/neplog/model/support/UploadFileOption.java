package cn.neptu.neplog.model.support;

import cn.neptu.neplog.model.entity.Storage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileOption {

    /**
     * 相对上传文件根目录下的路径
     * root: .neplog/uploads
     * path: [img]
     * 对应 .neplog/uploads/img
     */
    private String[] path;

    /**
     * 描述存储文件的类型(头像，封面，一般文件等)
     * 对应{@link Storage#getType()}
     */
    private String type;

    /**
     * 是否压缩原图
     */
    private Boolean compress;

    /**
     * 是否生成缩略图
     */
    private Boolean thumbnail;

    /**
     * 如果生成缩略图，缩略图的宽度
     */
    private int width;

    /**
     * 如果生成缩略图，缩略图的高度
     */
    private int height;

}
