package cn.neptu.neplog.model.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private Integer status;
    private String message;
    private T data;

    public static <T> BaseResponse<T> ok(String message, T data){
        return new BaseResponse<>(200,message,data);
    }

    public static <T> BaseResponse<T> ok(String message){
        return BaseResponse.ok(message,null);
    }

    public static <T> BaseResponse<T> error(String message, T data){
        return new BaseResponse<>(400,message,data);
    }

    public static <T> BaseResponse<T> error(String message){
        return BaseResponse.error(message,null);
    }
}
