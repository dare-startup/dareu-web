/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import com.dareu.web.data.entity.DareUser;
import com.dareu.web.exception.AuthenticationException;
import com.dareu.web.exception.DataAccessException;

/**
 *
 * @author MACARENA
 */
public interface DareUserRepository{
	public boolean isNicknameAvailable(String nickname);
	public void updateSecurityToken(String token, String userId);
    public boolean isEmailAvailable(String email);
    public String registerDareUser(DareUser register) throws DataAccessException;
    public String loginFacebook(String email, String name);
    public DareUser login(String nickname, String pass)throws AuthenticationException;
}
