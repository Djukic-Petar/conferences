package helpers;

import com.corejsf.hibernate.HibernateUtil;
import db.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class ConferenceHelper {

    private org.hibernate.Session session;
    
    public List<Conference> listConferences() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Conference> retVal = null;
        
        try {
            Criteria crit = session.createCriteria(Conference.class);
            retVal = crit.list();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Field> listFields() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Field> retVal = null;
        
        try {
            Criteria crit = session.createCriteria(Field.class);
            retVal = crit.list();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public void addPhoto(Program program, String link) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Conference> retVal = null;
        
        try {
            Photo newPhoto = new Photo(program, link);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public boolean canUploadPhoto(User user, Program program) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        
        try {
            for(Subsctiption s : new ArrayList<Subsctiption>(user.getSubsctiptions())) {
                for(Agenda a : new ArrayList<Agenda>(s.getAgendas())) {
                    if(a.getProgram().getIdProgram() == program.getIdProgram()) {
                        retVal = true;
                        break;
                    }
                }
            }
            for(Moderator m : new ArrayList<Moderator>(user.getModerators())) {
                if(m.getConference().getIdConference() == program.getConference().getIdConference()) {
                    retVal = true;
                    break;
                }
                    
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public void presentTalk(User user, Talk currentTalk) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            Criteria crit = session.createCriteria(Author.class);
            crit.add(Restrictions.eq("talk", currentTalk));
            crit.add(Restrictions.eq("user", user));
            Author me = (Author)crit.uniqueResult();
            me.setPresenting(true);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public String getTalkPdfLink(Talk currentTalk) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        String retVal = null;
        
        try {
            Criteria crit = session.createCriteria(File.class);
            crit.add(Restrictions.eq("talk", currentTalk));
            crit.add(Restrictions.like("link", "pdf", MatchMode.END));
            retVal = ((File)crit.uniqueResult()).getLink();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public void addFile(Talk currentTalk, String link) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();

        try {
            File newFile = new File(currentTalk, link);
            session.save(newFile);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public boolean isPptUploaded(Talk currentTalk) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        
        try {
            Criteria crit = session.createCriteria(File.class);
            crit.add(Restrictions.eq("talk", currentTalk));
            crit.add(Restrictions.or(Restrictions.like("link", "ppt", MatchMode.END), Restrictions.like("link", "pptx", MatchMode.END)));
            retVal = crit.uniqueResult() != null;
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public boolean isPdfUploaded(Talk currentTalk) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        
        try {
            Criteria crit = session.createCriteria(File.class);
            crit.add(Restrictions.eq("talk", currentTalk));
            crit.add(Restrictions.or(Restrictions.like("link", "pdf", MatchMode.END), Restrictions.like("link", "pdf", MatchMode.END)));
            retVal = crit.uniqueResult() != null;
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public boolean addRating(Talk currentTalk, User user, int rating) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        
        try {
            Rating newRating = new Rating(new RatingId(user.getIdUser(), currentTalk.getIdTalk()), currentTalk, user, rating);
            session.save(newRating);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public boolean isInAgenda(Program currentProgram, User user) {
        return canUploadPhoto(user, currentProgram);
    }

    public List<Talk> getSessionTalks(Session s) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Talk> retVal = null;
        
        try {
            Criteria c = session.createCriteria(Talk.class);
            c.add(Restrictions.eq("session", s));
            retVal = c.list();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Openingorclosingtalk> getSessionOOCTalks(Session s) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Openingorclosingtalk> retVal = null;
        
        try {
            Criteria c = session.createCriteria(Openingorclosingtalk.class);
            c.add(Restrictions.eq("session", s));
            retVal = c.list();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Comment> getComments(Program program) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Comment> retVal = null;
        
        try {
            Criteria c = session.createCriteria(Comment.class);
            c.add(Restrictions.eq("program", program));
            retVal = c.list();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public Comment addComment(User user, Program program, String comment, Boolean liked) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        Comment retVal = null;
        
        try {
            retVal = new Comment(program, user, comment, liked);
            session.save(retVal);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public String authorsToCSV(Collection<Author> authors) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        String retVal = null;
        
        try {
            List<Author> authorList = new ArrayList<>(authors);
            List<String> names = new ArrayList<>();
            for(Author a : authorList) {
                names.add(a.getUser().getUsername());
            }
            retVal = String.join(", ", names);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<User> getConferenceSubs(Conference conference) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<User> retVal = null;
        
        try {
            List<Subsctiption> subs = new ArrayList<>(conference.getSubsctiptions());
            retVal = new ArrayList<>();
            for(Subsctiption s : subs) {
                retVal.add(s.getUser());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Agenda> getUsersAgenda(User user, Conference c) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Agenda> retVal = null;
        
        try {
            Criteria crit = session.createCriteria(Subsctiption.class);
            crit.add(Restrictions.eq("user", user));
            crit.add(Restrictions.eq("conference", c));
            Subsctiption mySub = (Subsctiption)crit.uniqueResult();
            retVal = new ArrayList<>(mySub.getAgendas());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Program> getAgendaPrograms(List<Agenda> myAgenda) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Program> retVal = null;
        
        try {
            retVal = new ArrayList<>();
            for(Agenda a : myAgenda) {
                retVal.add(a.getProgram());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Workshop> getWorkshops(List<Program> agendaPrograms) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Workshop> retVal = null;
        
        try {
            retVal = new ArrayList<>();
            for(Program p : agendaPrograms) {
                if(p.getWorkshop() != null)
                    retVal.add(p.getWorkshop());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Session> getSessions(List<Program> agendaPrograms) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Session> retVal = null;
        
        try {
            retVal = new ArrayList<>();
            for(Program p : agendaPrograms) {
                if(p.getSession() != null)
                    retVal.add(p.getSession());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Ceremony> getCeremonies(List<Program> agendaPrograms) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Ceremony> retVal = null;
        
        try {
            retVal = new ArrayList<>();
            for(Program p : agendaPrograms) {
                if(p.getCeremony() != null)
                    retVal.add(p.getCeremony());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public boolean addProgramToAgenda(Program program, User user) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        
        try {
            Criteria crit = session.createCriteria(Subsctiption.class);
            crit.add(Restrictions.eq("user", user));
            crit.add(Restrictions.eq("conference", program.getConference()));
            Subsctiption mySub = (Subsctiption)crit.uniqueResult();
            crit = session.createCriteria(Agenda.class);
            crit.add(Restrictions.eq("program", program));
            crit.add(Restrictions.eq("subsctiption", mySub));
            Agenda exists = (Agenda)crit.uniqueResult();
            if(exists == null) {
                Agenda newAgenda = new Agenda(program, mySub);
                session.save(newAgenda);
                retVal = true;
            } else
                retVal = false;
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Program> getPrograms(Conference c) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Program> retVal = null;
        
        try {
            Criteria crit = session.createCriteria(Program.class);
            crit.add(Restrictions.eq("conference", c));
            retVal = crit.list();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public List<Conference> getSubscribedConferences(User user) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        List<Conference> retVal = null;
        
        try {
            Criteria c = session.createCriteria(Subsctiption.class);
            c.add(Restrictions.eq("user", user));
            List<Subsctiption> subs = c.list();
            retVal = new ArrayList<>();
            for(Subsctiption s : subs) {
                retVal.add(s.getConference());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public void subscribe(User user, Conference toSubscribe) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            Criteria c = session.createCriteria(Subsctiption.class);
            c.add(Restrictions.eq("user", user));
            c.add(Restrictions.eq("conference", toSubscribe));
            if(c.uniqueResult() == null) {
                Subsctiption newSub = new Subsctiption(toSubscribe, user);
                session.save(newSub);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public boolean updateConference(Conference currentConference) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        
        try {
            session.update(currentConference);
            tx.commit();
            retVal = true;
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }

    public void addSession(Program p, Session s) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            session.save(p);
            session.save(s);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public void addWorkshop(Program p, Workshop w) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            session.save(p);
            session.save(w);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public void addOOCTalk(Openingorclosingtalk ooc) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            session.save(ooc);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public void addTalk(Talk newTalk) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            session.save(newTalk);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public void addConference(Conference conference) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        
        try {
            session.save(conference);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
    }

    public boolean isSubscribed(User user, Conference conference) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        boolean retVal = false;
        try {
            Criteria c = session.createCriteria(Subsctiption.class);
            c.add(Restrictions.eq("user", user));
            c.add(Restrictions.eq("conference", conference));
            retVal = c.uniqueResult() != null;
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e);
        }
        return retVal;
    }
}
