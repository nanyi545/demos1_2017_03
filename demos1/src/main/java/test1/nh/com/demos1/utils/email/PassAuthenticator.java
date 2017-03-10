package test1.nh.com.demos1.utils.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


public class PassAuthenticator extends Authenticator
{  
    public PasswordAuthentication getPasswordAuthentication()  
    {
        String username = "webcon_log@163.com"; 
        String pwd = "webcon-log";  
        return new PasswordAuthentication(username,pwd);  
    }

}