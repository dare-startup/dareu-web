package com.dareu.web.data.repository.impl;

import java.util.List;
import java.util.logging.Logger;

import com.dareu.web.core.DareUtils;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.exception.AuthenticationException;
import com.dareu.web.exception.DataAccessException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author MACARENA
 */
@Stateless
public class DareUserRepositoryImpl extends AbstractRepository<DareUser> implements DareUserRepository{
	
	@Inject
	private Logger log; 
	
	@Inject
	private DareUtils utils; 
	
    public DareUserRepositoryImpl(){
        super(DareUser.class); 
    }

	public boolean isNicknameAvailable(String nickname) {
		//get a list of users where the nickname is the same 
		Query q = em.createQuery("SELECT u.nickname FROM User u WHERE u.nickname = :nickname")
				.setParameter("nickname", nickname);
		
		List<String> nicknames = q.getResultList();
		if(nicknames.isEmpty())
			//the nickname is available 
			return true; 
		else return false;
	}

	public boolean isEmailAvailable(String email) {
		Query q = em.createQuery("SELECT u.email FROM User u WHERE u.email = :email")
				.setParameter("email", email); 
		
		List<String> emails = q.getResultList(); 
		if(emails.isEmpty())
			return true; 
		else return false;
	}

	@Transactional
	public String registerDareUser(DareUser register)
			throws DataAccessException {
		persist(register); 
		return register.getId();
	}

	public String loginFacebook(String email, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public DareUser login(String nickname, String pass)throws AuthenticationException {
		DareUser user = null;
		try{
			Query q = em.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname AND u.password = :password")
					.setParameter("nickname", nickname)
					.setParameter("password", pass); 
			
			user = (DareUser)q.getSingleResult();
			if(user == null)
				return null; 
			else
				return user; 
		}catch(NoResultException ex){
			throw new AuthenticationException("Username and/or password are incorrect"); 
		}catch(Exception ex){
			log.severe("An error occured signin in a user: " + ex.getMessage());
			throw new AuthenticationException("Could not authenticate user"); 
		}
	}

	@Transactional
	public void updateSecurityToken(String token, String userId) {
		DareUser user = null; 
		try{
			user = find(userId);
			if(user != null)
				//update 
				user.setSecurityToken(token);
		}catch(DataAccessException ex){
			log.info("User with id " + userId + " not found"); 
		}catch(Exception ex){
			log.info("Exception updating user security token: " + ex.getMessage()); 
		}
	}

	public List<DareUser> findFriends(String userId) throws DataAccessException {
		List<DareUser> users = null; 
		/**try{
			Query q = em.createQuery("SELECT u FROM User u WHERE "); 
		}**/
		return users;
	}

	public DareUser findUserByToken(String token) throws DataAccessException {
		try{
			Query q = em.createQuery("SELECT u FROM User u WHERE u.securityToken = :token")
					.setParameter("token", token); 
			return (DareUser)q.getSingleResult(); 
		}catch(NoResultException ex){
			return null; 
		}catch(Exception ex){
			throw new DataAccessException("Could not get user: " + ex.getMessage()); 
		}
	}

	
}
