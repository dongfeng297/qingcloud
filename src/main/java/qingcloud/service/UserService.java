package qingcloud.service;

import qingcloud.dto.LoginDTO;
import qingcloud.dto.Result;

import javax.mail.MessagingException;
import java.math.BigDecimal;

public interface UserService {
     Result login(LoginDTO loginDTO);

    Result sendCode(String email) throws MessagingException;

    Result getVouchers(Long userId);

    boolean deductBalance(Long userId, BigDecimal payAmount);
}
