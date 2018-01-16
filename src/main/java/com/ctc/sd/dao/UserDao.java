package com.ctc.sd.dao;



import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ctc.sd.entity.User;


public interface UserDao {
	@Select("SELECT * FROM users WHERE account = #{account} AND password = #{password} ")
	public User getUserByAccountPassword(@Param("account") String account, @Param("password") String password);
	
	@Select("SELECT * FROM users WHERE account = #{account}  ")
	public User getUserByAccount(@Param("account") String account);
	
	@Insert("insert into users(account,password,name,phone) "+
            "values(#{account},#{password},#{name},#{phone})")
    public int insertUser(@Param("account") String account, @Param("password") String password,@Param("name") String name,@Param("phone") String phone);
	
	@Update("UPDATE users SET password=#{password},name=#{name},phone=#{phone} WHERE account =#{account}")
	public void update(@Param("account") String account,@Param("password") String password,@Param("name") String name,@Param("phone") String phone);
	
}

