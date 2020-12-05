package cn.neptu.neplog.utils;

import cn.neptu.neplog.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtil {

    private final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public User getCurrentUser(){
        return currentUser.get();
    }

    public void setCurrentUser(User user){
        currentUser.set(user);
    }

    public void removeCurrentUser(){
        currentUser.remove();
    }
}
