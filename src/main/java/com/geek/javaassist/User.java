package com.geek.javaassist;
/**
 * 
 * 
 * @author jileng
 * @version $Id: User.java, v 0.1 2016年1月22日 下午2:40:07 jileng Exp $
 */
public class User {
    private int id;
    
    public User(){}
    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
