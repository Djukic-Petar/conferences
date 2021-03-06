package db;
// Generated Jun 26, 2017 5:09:54 PM by Hibernate Tools 4.3.1



/**
 * Favourite generated by hbm2java
 */
public class Favourite  implements java.io.Serializable {


     private FavouriteId id;
     private User userByUserIdUserSrc;
     private User userByUserIdUserDst;
     private Integer pad;

    public Favourite() {
    }

	
    public Favourite(FavouriteId id, User userByUserIdUserSrc, User userByUserIdUserDst) {
        this.id = id;
        this.userByUserIdUserSrc = userByUserIdUserSrc;
        this.userByUserIdUserDst = userByUserIdUserDst;
    }
    public Favourite(FavouriteId id, User userByUserIdUserSrc, User userByUserIdUserDst, Integer pad) {
       this.id = id;
       this.userByUserIdUserSrc = userByUserIdUserSrc;
       this.userByUserIdUserDst = userByUserIdUserDst;
       this.pad = pad;
    }
   
    public FavouriteId getId() {
        return this.id;
    }
    
    public void setId(FavouriteId id) {
        this.id = id;
    }
    public User getUserByUserIdUserSrc() {
        return this.userByUserIdUserSrc;
    }
    
    public void setUserByUserIdUserSrc(User userByUserIdUserSrc) {
        this.userByUserIdUserSrc = userByUserIdUserSrc;
    }
    public User getUserByUserIdUserDst() {
        return this.userByUserIdUserDst;
    }
    
    public void setUserByUserIdUserDst(User userByUserIdUserDst) {
        this.userByUserIdUserDst = userByUserIdUserDst;
    }
    public Integer getPad() {
        return this.pad;
    }
    
    public void setPad(Integer pad) {
        this.pad = pad;
    }




}


