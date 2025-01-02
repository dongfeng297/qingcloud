package qingcloud.utils;

import qingcloud.dto.UserDTO;

public class UserHolder {
    public static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();
    public static UserDTO getUser()
    {
        return tl.get();
    }
    public static void setUser(UserDTO user){
        tl.set(user);
    }
    public static void removeUser(){
        tl.remove();
    }
}
