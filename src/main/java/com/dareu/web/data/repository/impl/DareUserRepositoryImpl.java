package com.dareu.web.data.repository.impl;

import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.core.service.FileService;
import java.util.List;

import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.data.exception.AuthenticationException;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.data.repository.FriendshipRepository;
import com.dareu.web.dto.response.entity.*;
import org.apache.log4j.Logger;

import java.math.BigInteger;

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
public class DareUserRepositoryImpl extends AbstractRepository<DareUser> implements DareUserRepository {

    @Inject
    private Logger log;

    @Inject
    private DareRepository dareRepository; 
    
    @Inject
    private FileService fileService; 
    
    @Inject
    private DareResponseRepository dareResponseRepository;

    @Inject
    private FriendshipRepository friendshipRepository;
    
    @Inject
    private DareuAssembler assembler;
    
    public DareUserRepositoryImpl() {
        super(DareUser.class);
    }


    @Override
    public boolean isEmailAvailable(String email) {
        log.info("Checking email existence");
        Query q = em.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.email = :email")
                .setParameter("email", email);

        Long count = (Long)q.getSingleResult();
        if(count.intValue() == 0)
            return true;
        return false;
    }

    @Override
    @Transactional
    public String registerDareUser(DareUser register)
            throws DataAccessException {
        persist(register);
        return register.getId();
    }

    @Override
    public String loginFacebook(String email, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DareUser login(String nickname, String pass) throws AuthenticationException {
        DareUser user = null;
        try {
            Query q = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
                    .setParameter("email", nickname)
                    .setParameter("password", pass);

            user = (DareUser) q.getSingleResult();
            if (user == null) {
                return null;
            } else {
                return user;
            }
        } catch (NoResultException ex) {
            throw new AuthenticationException("Username and/or password are incorrect");
        } catch (Exception ex) {
            log.error("An error occured signin in a user: " + ex.getMessage());
            throw new AuthenticationException("Could not authenticate user");
        }
    }

    @Transactional
    @Override
    public void updateSecurityToken(String token, String userId) {
        DareUser user = null;
        try {
            user = find(userId);
            if (user != null) //update 
            {
                user.setSecurityToken(token);
            }
        } catch (DataAccessException ex) {
            log.info("User with id " + userId + " not found");
        } catch (Exception ex) {
            log.info("Exception updating user security token: " + ex.getMessage());
        }
    }

    @Override
    public List<DareUser> findFriends(String userId) throws DataAccessException {
        List<DareUser> users = null;
        /**
         * try{ Query q = em.createQuery("SELECT u FROM User u WHERE "); }*
         */
        return users;
    }

    @Override
    public DareUser findUserByToken(String token) throws DataAccessException {
        try {
            Query q = em.createQuery("SELECT u FROM User u WHERE u.securityToken = :token")
                    .setParameter("token", token);
            return (DareUser) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get user: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateFcmRegId(String regId, String token)
            throws DataAccessException {
        try {
            Query q = em.createQuery("SELECT u FROM User u WHERE u.securityToken = :token")
                    .setParameter("token", token);
            DareUser user = (DareUser) q.getSingleResult();
            if (user != null) {
                //update 
                user.setGCM(regId);
            }
        } catch (NoResultException ex) {
            log.info("User with token " + token + " not found");
        } catch (Exception ex) {
            throw new DataAccessException("Could not update user FCM: " + ex.getMessage());
        }
    }

    @Override
    public boolean isUserFriend(String userId, String anotherUserId) throws DataAccessException {
        try {
            Query q = em.createQuery("SELECT count(c) FROM Friendship c WHERE c.user_id = :userId AND u.requested_user_id = :anotherUserId AND u.accepted = 1")
                    .setParameter("userId", userId)
                    .setParameter("anotherUserId", anotherUserId);
            Integer count = (Integer) q.getSingleResult();
            return count > 0;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get count: " + ex.getMessage());
        }
    }

    @Override
    public DareUser findUserByEmail(String email) throws DataAccessException {
        try {
            Query q = em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", email);
            DareUser user = (DareUser) q.getSingleResult();
            return user;
        } catch (NoResultException ex) {

            return null;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get user by email: " + ex.getMessage());
        }
    }

    @Override
    public List<DareUser> findUsersByPage(int pageNumber, boolean excludePrincipal, String userId) throws DataAccessException {
        List<DareUser> users = null;
        try {
            Query query = null;
            if (excludePrincipal) {
                query = em.createQuery("SELECT u FROM User u WHERE u.id != :id")
                        .setParameter("id", userId);
            } else {
                query = em.createQuery("SELECT u FROM User u");
            }
            query.setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));
            users = query.getResultList();
            return users;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get users by page: " + ex.getMessage());
        }
    }

    @Override
    public Page<DareUser> discoverUsers(int pageNumber, String userId) throws DataAccessException {
        try {
            //list
            Query q = em.createNativeQuery("select * from dareu_user "
                    + "where id not in (select (case ?1 when user_id then requested_user_id else user_id end) id from friendship "
                    + "where user_id = ?2 or requested_user_id = ?3) AND id <> ?4", DareUser.class)
                    .setParameter(1, userId)
                    .setParameter(2, userId)
                    .setParameter(3, userId)
                    .setParameter(4, userId)
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));
            List<DareUser> users = q.getResultList();
            //count
            q = em.createNativeQuery("select count(*) from dareu_user "
                    + "where id not in (select (case ?1 when user_id then requested_user_id else user_id end) id from friendship "
                    + "where user_id = ?2 or requested_user_id = ?3) AND id <> ?4")
                    .setParameter(1, userId)
                    .setParameter(2, userId)
                    .setParameter(3, userId)
                    .setParameter(4, userId);
            BigInteger count = (BigInteger) q.getSingleResult();

            //creates a new page 
            Page<DareUser> page = new Page<DareUser>();
            page.setItems(users);
            page.setPageNumber(pageNumber);
            page.setPageSize(DEFAULT_PAGE_NUMBER);
            page.setPagesAvailable(getPagesAvailable(pageNumber, count.intValue()));
            return page;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public String getUserFcmToken(String userId) throws DataAccessException {
        try {
            Query q = em.createQuery("SELECT u.GCM FROM User u WHERE u.id = :id")
                    .setParameter("id", userId);

            String fcm = (String) q.getSingleResult();
            return fcm;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void updateImageUrl(String id, String url) throws DataAccessException {
        try {
            Query q = em.createQuery("UPDATE User u SET u.imageUrl = :url WHERE u.id = :id")
                    .setParameter("id", id)
                    .setParameter("url", url);

            q.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public AccountProfile getAccountProfile(String id) throws DataAccessException {
        AccountProfile profile = null; 
        
        try{
            Query q = em.createQuery("SELECT d FROM User d WHERE d.id = :id")
                    .setParameter("id", id);
            DareUser user = (DareUser)q.getSingleResult(); 
            Page<CreatedDare> dares = dareRepository.findCreatedDares(id, 1);
            Page<DareResponseDescription> descs = dareResponseRepository.getResponses(id, 1);
            Page<FriendSearchDescription> contacts = friendshipRepository.findFriendDescriptions(id, 1);
            return assembler.getAccountProfile(user, dares, descs, contacts);
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage()); 
        }
        
    }

    @Override
    public void changeEmailAddress(String newEmail, String token) throws DataAccessException {
        try{
            Query q = em.createQuery("UPDATE User u SET u.email = :email WHERE u.securityToken = :token")
                    .setParameter("email", newEmail)
                    .setParameter("token", token); 
            q.executeUpdate(); 
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage()); 
        }
    }

    @Override
    public DareUser loginGoogle(String googleId, String user) throws DataAccessException {
        try{
            DareUser u = (DareUser) em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.googleId = :gid")
                    .setParameter("email", user)
                    .setParameter("gid", googleId)
                    .getSingleResult();
            return u;
        }catch(NoResultException ex){
            return null;
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

}
