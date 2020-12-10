package cn.neptu.neplog.model.option;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileOption {

    private String[] path;
    private Boolean compress;
    private Boolean thumbnail;
    private int width;
    private int height;

}
