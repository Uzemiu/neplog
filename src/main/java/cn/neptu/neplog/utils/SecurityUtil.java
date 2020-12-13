package cn.neptu.neplog.utils;

import cn.neptu.neplog.model.entity.User;

public class SecurityUtil {

    private final static ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static User getCurrentUser(){
        return currentUser.get();
    }

    public static void setCurrentUser(User user){
        currentUser.set(user);
    }

    public static void removeCurrentUser(){
        currentUser.remove();
    }
}
