package com.dareu.web.data.repository.impl;

import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.data.entity.DareUser;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.data.repository.FriendshipRepository;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.response.entity.FriendSearchDescription;
import com.dareu.web.dto.response.entity.Page;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class FriendshipRepositoryImpl extends AbstractRepository<FriendshipRequest> implements FriendshipRepository {

    private static final Logger log = Logger.getLogger(FriendshipRepositoryImpl.class.getName());

    @Inject
    private DareuAssembler assembler;

    public FriendshipRepositoryImpl() {
        super(FriendshipRequest.class);
    }

    @Override
    public FriendshipRequest findFriendship(String userId, String requestedUserId)
            throws DataAccessException {
        Query q = em.createQuery("SELECT f FROM Friendship f WHERE f.user.id = :userId AND f.requestedUser.id = :requestedUserId")
                .setParameter("requestedUserId", requestedUserId)
                .setParameter("userId", userId);

        FriendshipRequest f = null;
        try {
            f = (FriendshipRequest) q.getSingleResult();
            return f;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void updateFriendhip(boolean approved, String friendhipId)
            throws DataAccessException {
        //create a query 
        Query q = em.createQuery("UPDATE Friendship SET accepted = :approved WHERE id = :friendshipId")
                .setParameter("approved", approved)
                .setParameter("friendshipId", friendhipId);

        q.executeUpdate();
    }

    @Override
    public List<DareUser> discoverUsers(String id, int pageNumber) throws DataAccessException {
        try {
            Query q = em.createNativeQuery("select u.* from dareu_user u "
                    + "inner join friendship request on u.id <> request.user_id "
                    + "inner join friendship requested on requested.requested_user_id <> u.id "
                    + "where u.id = ?1")
                    .setParameter(1, id)
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));
            return q.getResultList();
        } catch (Exception ex) {
            throw new DataAccessException("Could not fetch friendships: " + ex.getMessage());
        }
    }

    @Override
    public boolean areUsersFriends(String userId, String requestedUserId) throws DataAccessException {
        Long count = -1L;
        try {
            Query q = em.createQuery("SELECT count(f) from Friendship f WHERE f.requestedUser.id = :requestedUserId AND f.user.id = :user and f.accepted = true")
                    .setParameter("requestedUserId", requestedUserId)
                    .setParameter("user", userId);
            count = (Long) q.getSingleResult();
            if (count > 0) {
                return true;
            } else {
                q = em.createQuery("SELECT count(f) from Friendship f WHERE f.requestedUser.id = :requestedUserId AND f.user.id = :user and f.accepted = true")
                        .setParameter("requestedUserId", userId)
                        .setParameter("user", requestedUserId);
                count = (Long) q.getSingleResult();
                return count > 0;
            }
        } catch (Exception ex) {
            log.severe("Exception checking if users are friends: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean isRequestSent(String userId, String requestedUserId) throws DataAccessException {
        Long count = 0L;
        try {
            Query q = em.createQuery("SELECT COUNT(f) FROM Friendship f WHERE f.user.id = :userId AND f.requestedUser.id = :requestedUserId AND f.accepted = false")
                    .setParameter("userId", userId)
                    .setParameter("requestedUserId", requestedUserId);
            count = (Long) q.getSingleResult();
            return count.intValue() > 0;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get request sent property: " + ex.getMessage());
        }
    }

    @Override
    public boolean isRequestReceived(String userId, String requestedUserId) throws DataAccessException {
        Long count = 0L;
        try {
            Query q = em.createQuery("SELECT COUNT(f) FROM Friendship f WHERE f.user.id = :requestedUserId AND f.requestedUser.id = :userId AND f.accepted = false")
                    .setParameter("userId", userId)
                    .setParameter("requestedUserId", requestedUserId);
            count = (Long) q.getSingleResult();
            return count.intValue() > 0;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get request sent property: " + ex.getMessage());
        }
    }

    @Override
    public Page<FriendSearchDescription> findFriendDescriptions(String userId, int pageNumber) throws DataAccessException {
        Page<FriendSearchDescription> page = new Page<FriendSearchDescription>();
        List<DareUser> users = null;
        try {
            Query q = em.createNativeQuery("SELECT u.* FROM friendship f INNER JOIN dareu_user u "
                    + "ON f.user_id = u.id OR f.requested_user_id = u.id "
                    + "WHERE (f.requested_user_id = ?1 OR f.user_id = ?2 ) "
                    + "AND f.accepted = true AND u.id <> ?3", DareUser.class)
                    .setParameter(1, userId)
                    .setParameter(2, userId)
                    .setParameter(3, userId)
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));
            users = q.getResultList();
            //get count 
            BigInteger count = (BigInteger) em.createNativeQuery("SELECT count(u.id) FROM friendship f INNER JOIN dareu_user u "
                    + "ON f.user_id = u.id OR f.requested_user_id = u.id "
                    + "WHERE (f.requested_user_id = ?1 OR f.user_id = ?2 ) "
                    + "AND f.accepted = true AND u.id <> ?3")
                    .setParameter(1, userId)
                    .setParameter(2, userId)
                    .setParameter(3, userId)
                    .getSingleResult();
            page.setItems(assembler.transformFriendRequests(users, count.longValue()));
            page.setPageNumber(pageNumber);
            page.setPageSize(DEFAULT_PAGE_NUMBER);
            page.setPagesAvailable(getPagesAvailable(pageNumber, count.intValue()));
            return page;
        } catch (Exception ex) {
            throw new DataAccessException(String.format("%s: %s", ex.getClass().getName(), ex.getMessage()));
        }
    }

    public Page<FriendSearchDescription> findFriendDescriptions(String userId, int pageNumber, String query) throws DataAccessException {
        Page<FriendSearchDescription> page = new Page<FriendSearchDescription>();
        List<DareUser> users = null;
        try {
            Query q = em.createNativeQuery("SELECT u.* FROM friendship f INNER JOIN dareu_user u "
                    + "ON f.user_id = u.id OR f.requested_user_id = u.id "
                    + "WHERE (f.requested_user_id = ?1 OR f.user_id = ?2 ) "
                    + "AND f.accepted = true AND u.id <> ?3 AND u.name LIKE ?4", DareUser.class)
                    .setParameter(1, userId)
                    .setParameter(2, userId)
                    .setParameter(3, userId)
                    .setParameter(4, "%" + query + "%")
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));

            users = q.getResultList();
            BigInteger count = null; 
            q = em.createNativeQuery("SELECT count(u.id) FROM friendship f INNER JOIN dareu_user u "
                    + "ON f.user_id = u.id OR f.requested_user_id = u.id "
                    + "WHERE (f.requested_user_id = ?1 OR f.user_id = ?2 ) "
                    + "AND f.accepted = true AND u.id <> ?3 AND u.name LIKE ?4")
                    .setParameter(1, userId)
                    .setParameter(2, userId)
                    .setParameter(3, userId)
                    .setParameter(4, "%" + query + "%");
            count = (BigInteger) q.getSingleResult();
            page.setItems(assembler.transformFriendRequests(users, count.longValue()));
            page.setPageNumber(pageNumber);
            page.setPageSize(DEFAULT_PAGE_NUMBER);
            page.setPagesAvailable(getPagesAvailable(pageNumber, count.intValue()));
            return page;
        } catch (Exception ex) {
            throw new DataAccessException(String.format("%s: %s", ex.getClass().getName(), ex.getMessage()));
        }

    }

}
