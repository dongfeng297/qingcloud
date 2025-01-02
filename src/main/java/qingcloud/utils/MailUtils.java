package qingcloud.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MailUtils {
    public static void main(String[] args) throws MessagingException {
        sendMail("2978618707@qq.com",new MailUtils().achieveCode());
    }
    public static void sendMail(String email,String code)throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth",true);
        properties.put("mail.smtp.host","smtp.qq.com");
        properties.put("mail.smtp.port",465);
        properties.put("mail.user","2978618707@qq.com");
        properties.put("mail.password","xmgcozhoiydodfdh");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = properties.getProperty("mail.user");
                String password = properties.getProperty("mail.password");
                return new PasswordAuthentication(userName,password);
            }
        };
        Session mailSession = Session.getInstance(properties,authenticator);
        MimeMessage message = new MimeMessage(mailSession);
        InternetAddress  form =new InternetAddress(properties.getProperty("mail.user"));
        message.setFrom(form);
        InternetAddress to = new InternetAddress(email);
        message.setRecipient(MimeMessage.RecipientType.TO,to);
        message.setSubject("来自qingcloud的邮件");
        message.setContent("尊敬的用户:你好!\n注册验证码为:" + code + "(有效期为一分钟,请勿告知他人)", "text/html;charset=UTF-8");
        Transport.send(message);
    }
    public static String achieveCode() {  //由于数字 1 、 0 和字母 O 、l 有时分不清楚，所以，没有数字 1 、 0
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z"};
        List<String> list = Arrays.asList(beforeShuffle);//将数组转换为集合
        Collections.shuffle(list);  //打乱集合顺序
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s); //将集合转化为字符串
        }
        return sb.substring(3, 9);
    }
}

