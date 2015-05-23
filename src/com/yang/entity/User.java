package com.yang.entity;

/**
 * 
 * ClassName: User
 * @Description: 用户实体
 * @author: fengmengyang
 * @date: 2015-5-15
 */
public class User {
	private Long userId;
	private String userName;
	private String password;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
