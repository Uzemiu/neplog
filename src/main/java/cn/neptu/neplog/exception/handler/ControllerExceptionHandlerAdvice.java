package cn.neptu.neplog.exception.handler;

import cn.neptu.neplog.exception.*;
import cn.neptu.neplog.model.support.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public BaseResponse<?> validExceptionHandler(BindException exception) {
        return BaseResponse.error(exception.getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public BaseResponse<?> resourceNotFoundExceptionHandler(Exception exception){
        return BaseResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class,IllegalArgumentException.class})
    public BaseResponse<?> badRequestExceptionHandler(Exception exception){
        return BaseResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public BaseResponse<?> unauthenticatedExceptionHandler(UnauthenticatedException exception){
        return BaseResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UploadFailureException.class)
    public BaseResponse<?> uploadFailureExceptionHandler(UploadFailureException exception){
        return BaseResponse.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalException.class)
    public BaseResponse<?> internalExceptionHandler(InternalException exception){
        return BaseResponse.error(exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace()));
    }

}
