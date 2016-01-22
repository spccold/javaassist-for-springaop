package com.geek.javaassist;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * base on spring 3.2.12
 * 
 * @author jileng
 * @version $Id: App.java, v 0.1 2016年1月22日 下午7:28:09 jileng Exp $
 */
public class App {
    public static void main(String[] args) throws Throwable {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        UserServiceImpl service1 = context.getBean(UserServiceImpl.class);
        System.out.println("**********************addUser**************************");
        service1.addUser(new User(1));
        System.out.println("********************deleteUserById***************************");
        service1.deleteUserById(1);
        System.out.println("*******************getUserById*****************************");
        service1.getUserById(1);
        context.close();
    }
}
