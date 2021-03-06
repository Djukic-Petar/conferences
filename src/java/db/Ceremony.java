package db;
// Generated Jun 26, 2017 5:09:54 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Ceremony generated by hbm2java
 */
public class Ceremony  implements java.io.Serializable {


     private int programIdProgram;
     private Program program;
     private char occasion;
     private Set welcomingspeeches = new HashSet(0);
     private Set openingorclosingtalks = new HashSet(0);

    public Ceremony() {
    }

	
    public Ceremony(Program program, char occasion) {
        this.program = program;
        this.occasion = occasion;
    }
    public Ceremony(Program program, char occasion, Set welcomingspeeches, Set openingorclosingtalks) {
       this.program = program;
       this.occasion = occasion;
       this.welcomingspeeches = welcomingspeeches;
       this.openingorclosingtalks = openingorclosingtalks;
    }
   
    public int getProgramIdProgram() {
        return this.programIdProgram;
    }
    
    public void setProgramIdProgram(int programIdProgram) {
        this.programIdProgram = programIdProgram;
    }
    public Program getProgram() {
        return this.program;
    }
    
    public void setProgram(Program program) {
        this.program = program;
    }
    public char getOccasion() {
        return this.occasion;
    }
    
    public void setOccasion(char occasion) {
        this.occasion = occasion;
    }
    public Set getWelcomingspeeches() {
        return this.welcomingspeeches;
    }
    
    public void setWelcomingspeeches(Set welcomingspeeches) {
        this.welcomingspeeches = welcomingspeeches;
    }
    public Set getOpeningorclosingtalks() {
        return this.openingorclosingtalks;
    }
    
    public void setOpeningorclosingtalks(Set openingorclosingtalks) {
        this.openingorclosingtalks = openingorclosingtalks;
    }




}


