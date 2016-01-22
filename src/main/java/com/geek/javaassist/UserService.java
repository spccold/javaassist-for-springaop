package com.geek.javaassist;
/**
 * 
 * 
 * @author jileng
 * @version $Id: UserService.java, v 0.1 2016年1月22日 下午2:41:02 jileng Exp $
 */
public interface UserService {
    /**
     * 添加用户
     * 
     * @param user
     */
    void addUser(User user);
    
    /**
     * 根据id删除用户
     * 
     * @param id
     */
    void deleteUserById(int id);
    
    /**
     * 根据id获取用户
     * 
     * @param id
     * @return
     */
    User getUserById(int id);
}
