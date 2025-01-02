package qingcloud.service;

import qingcloud.dto.LoginDTO;
import qingcloud.dto.Result;

import javax.mail.MessagingException;

public interface UserService {
     Result login(LoginDTO loginDTO);

    Result sendCode(String email) throws MessagingException;
}
