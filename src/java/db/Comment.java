package db;
// Generated Jun 26, 2017 5:09:54 PM by Hibernate Tools 4.3.1



/**
 * Comment generated by hbm2java
 */
public class Comment  implements java.io.Serializable {


     private Integer idComment;
     private Program program;
     private User user;
     private String content;
     private Boolean liked;

    public Comment() {
    }

	
    public Comment(Program program, User user) {
        this.program = program;
        this.user = user;
    }
    public Comment(Program program, User user, String content, Boolean liked) {
       this.program = program;
       this.user = user;
       this.content = content;
       this.liked = liked;
    }
   
    public Integer getIdComment() {
        return this.idComment;
    }
    
    public void setIdComment(Integer idComment) {
        this.idComment = idComment;
    }
    public Program getProgram() {
        return this.program;
    }
    
    public void setProgram(Program program) {
        this.program = program;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    public Boolean getLiked() {
        return this.liked;
    }
    
    public void setLiked(Boolean liked) {
        this.liked = liked;
    }




}


