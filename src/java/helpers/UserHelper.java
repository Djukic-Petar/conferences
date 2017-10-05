/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import com.corejsf.hibernate.HibernateUtil;
import db.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author John
 */
public class UserHelper {
    private org.hibernate.Session session = null;
    private static final ConferenceHelper CONFERENCE_HELPER = new ConferenceHelper();

    public boolean addFavourite(User user1, User user2) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        
        try {
            Favourite fav = new Favourite(new FavouriteId(user1.getIdUser(), user2.getIdUser()), user1, user2);
            session.save(fav);
            tx.commit();
            retVal = true;
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public boolean isInFavs(User user1, User user2) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        
        try {
            Criteria crit = session.createCriteria(Favourite.class);
            crit.add(Restrictions.eq("userIdUserSrc", user1.getIdUser()));
            crit.add(Restrictions.eq("userIdUserDst", user2.getIdUser()));
            Favourite fav = (Favourite)crit.uniqueResult();
            if(fav != null)
                retVal = true;
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public User getUser(String username, String password) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        User retVal = null;
        
        try {
            Criteria crit = session.createCriteria(User.class);
            crit.add(Restrictions.eq("username", username));
            crit.add(Restrictions.eq("password", password));
            retVal = (User)crit.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public int newUser(User user) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        User retVal = null;
        
        try {
            session.save(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return user.getIdUser();
    }

    public User changePassword(String username, String password, String newPassword) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        User retVal = null;
        
        try {
            Criteria crit = session.createCriteria(User.class);
            crit.add(Restrictions.eq("username", username));
            crit.add(Restrictions.eq("password", password));
            retVal = (User)crit.uniqueResult();
            if(retVal != null) {
                retVal.setPassword(newPassword);
                session.update(retVal);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public User getUser(String username) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        User retVal = null;
        
        try {
            Criteria crit = session.createCriteria(User.class);
            crit.add(Restrictions.eq("username", username));
            retVal = (User)crit.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public void addAuthor(User user, Openingorclosingtalk ooc) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            Author newAuth = new Author(ooc, null, user, null, null, false);
            session.save(newAuth);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public void addAuthor(User user1, Talk newTalk) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            Author newAuth = new Author(null, newTalk, user1, null, null, false);
            session.save(newAuth);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public void addMod(User newMod, Conference c) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            
            Criteria crit = session.createCriteria(Subsctiption.class);
            crit.add(Restrictions.eq("user", newMod));
            crit.add(Restrictions.eq("conference", c));
            if(crit.uniqueResult() == null) {
                Subsctiption newSub = new Subsctiption(c, newMod);
                session.save(newSub);
            }
            
            Moderator mod = new Moderator(new ModeratorId(newMod.getIdUser(), c.getIdConference()), c, newMod);
            session.save(mod);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }
    
}
