package com.geek.javaassist;

/**
 * 
 * 
 * @author jileng
 * @version $Id: UserServiceImpl.java, v 0.1 2016年1月22日 下午2:41:59 jileng Exp $
 */
public class UserServiceImpl implements UserService{
    
    @JavaAssistEnhancer
    public void addUser(User user) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("User["+user.getId()+"] be added!");
    }

    @JavaAssistEnhancer
    public void deleteUserById(int id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("User["+id+"] be deleted!");
    }

    @JavaAssistEnhancer
    public User getUserById(int id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User(id);
    }
}
