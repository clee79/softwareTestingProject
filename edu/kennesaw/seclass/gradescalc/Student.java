package edu.kennesaw.seclass.gradescalc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Student {
    private String name, id, email, team;
    private int c, cpp, java;
    private double attendance;
    private boolean job;
    private List<Double> grades, projectGrades, teamGrades;


    public Student() {
        //Workbook sheet 1
        name = "Jon Arbuckle";
        id = "0";
        email = "test@gmail.com";
        team = "Team 0";
        c = 0;
        cpp = 0;
        java = 0;
        job = false;
        //Workbook sheet 3
        attendance = 0;
        //Workbook sheet 4
        grades = new ArrayList<>();
        //Workbook sheet 5
        projectGrades = new ArrayList<>();
        //Workbook sheet 6
        teamGrades = new ArrayList<>();
    }

    public Student(String n, String i, HashSet<Student> students, GradesDB db) {
        name = n;
        id = i;
        Student temp = db.getStudentByID(i);
        if (temp != null) {
            email = temp.email;
            team = temp.team;
            c = temp.c;
            cpp = temp.cpp;
            java = temp.java;
            job = temp.job;
            //Workbook sheet 3
            attendance = temp.attendance;
            //Workbook sheet 4
            grades = temp.grades;
            //Workbook sheet 5
            projectGrades = temp.projectGrades;
            //Workbook sheet 6
            teamGrades = temp.teamGrades;
        } else {
            email = "test@gmail.com";
            team = "Team 0";
            c = 0;
            cpp = 0;
            java = 0;
            job = false;
            //Workbook sheet 3
            attendance = 0;
            //Workbook sheet 4
            grades = new ArrayList<>();
            //Workbook sheet 5
            projectGrades = new ArrayList<>();
            //Workbook sheet 6
            teamGrades = new ArrayList<>();
        }
        students.add(this);
        db.addStudent(this);
    }

    public void setAssignmentGrade(int assignment, double grade) {
        grades.set(assignment, grade);
    }

    public void setContributionGrade(Student student) {
        //Todo
    }

    public void addAssignment() {
        grades.add(0.0);
    }

    public void addProjectAndTeamGrade(){
        teamGrades.add(0.0);
        projectGrades.add(0.0);
    }

    public void addProject(){
        projectGrades.add(0.0);
    }

    // Gets & Sets
    public String getName() {
        return name;
    }

    public void setName(String nName) {
        name = nName;
    }

    public String getId() {
        return id;
    }

    public void setId(String nID) {
        id = nID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String nMail) {
        email = nMail;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String nteam) {
        team = nteam;
    }

    public int getC() {
        return c;
    }

    public void setC(Integer nC) {
        c = nC;
    }

    public int getCpp() {
        return cpp;
    }

    public void setCpp(Integer nCPP) {
        cpp = nCPP;
    }

    public int getJava() {
        return java;
    }

    public void setJava(Integer nJava) {
        java = nJava;
    }

    public boolean getJob() {
        return job;
    }

    public void setJob(boolean nJob) {
        job = nJob;
    }

    public int getAttendance() {
        return (int) Math.round(attendance);
    }

    public void setAttendance(double nAttendance) {
        attendance = nAttendance;
    }

    public List<Double> getGrades() {
        return grades;
    }

    public void setGrades(List<Double> nGrades) {
        grades = nGrades;
    }

    public List<Double> getProjectGrades() {
        return projectGrades;
    }

    public void setProjectGrades(List<Double> nProjectGrades) {
        projectGrades = nProjectGrades;
    }

    public List<Double> getTeamProjectGrades() {
        return teamGrades;
    }

    public void setTeamGrades(List<Double> nTeamGrades) {
        teamGrades = nTeamGrades;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nID: " + id + "\nEmail: " + email + "\nTeam: " + team + "\nC: " + c + "\nC++: " + cpp + "\nJava: " + java + "\nJob: " + job;
    }
}