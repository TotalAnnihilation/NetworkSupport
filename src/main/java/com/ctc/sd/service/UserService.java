package com.ctc.sd.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctc.sd.dao.UserDao;
import com.ctc.sd.entity.User;



/**
 * Created by sang on 17-3-10.
 */
@Service
public class UserService {
	
	@Autowired
    private UserDao userDao;
	
	public User getUser(String account){
		User user = userDao.getUserByAccount(account);
		return user;
	}

    public boolean login(String account, String password) {
        User user = userDao.getUserByAccountPassword(account, password);
        if (user == null) {
            return false;
        }else{
            return true;
        }
    }
    
    public int register(String account, String password,String name,String phone) {
        User user = userDao.getUserByAccount(account);
        if (user == null) {
        	int flag=userDao.insertUser(account, password,name,phone);
        	if(flag>0)
        		return 0;
        	else
        		return 2;
        }else{
            return 1;
        }
    }
    
    public void update(String account, String password, String name, String phone) {
        userDao.update(account, password,name,phone);
    }
}
