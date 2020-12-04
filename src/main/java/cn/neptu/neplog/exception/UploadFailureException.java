package cn.neptu.neplog.exception;

public class UploadFailureException extends RuntimeException{

    public UploadFailureException(){super();}

    public UploadFailureException(String message){super(message);}
}
