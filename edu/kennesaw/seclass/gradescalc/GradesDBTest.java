package edu.kennesaw.seclass.gradescalc;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GradesDBTest {

    GradesDB db = null;
    static final String GRADES_DB_GOLDEN = "DB" + File.separator
            + "GradesDatabase-goldenversion.xlsx";
    static final String GRADES_DB = "DB" + File.separator
            + "GradesDatabase.xlsx";

    @Before
    public void setUp() throws Exception {
        FileSystem fs = FileSystems.getDefault();
        Path dbfilegolden = fs.getPath(GRADES_DB_GOLDEN);
        Path dbfile = fs.getPath(GRADES_DB);
        Files.copy(dbfilegolden, dbfile, REPLACE_EXISTING);
        db = new GradesDB();
        db.loadSpreadsheet(GRADES_DB);
    }

    @After
    public void tearDown() throws Exception {
        db = null;
    }

    @Test
    public void testGetNumStudents() {
        int numStudents = db.getNumStudents();
        assertEquals(14, numStudents);
    }

    @Test
    public void testGetNumAssignments() {
        int numAssignments = db.getNumAssignments();
        assertEquals(3, numAssignments);
    }

    @Test
    public void testGetNumProjects() {
        int numProjects;
        numProjects = db.getNumProjects();
        assertEquals(3, numProjects);
    }

    @Test
    public void testGetStudents1() {
        HashSet<Student> students = null;
        students = db.getStudents();
        assertEquals(14, students.size());
    }

    @Test
    public void testGetStudents2() {
        HashSet<Student> students = null;
        students = db.getStudents();
        assertTrue(students.contains(new Student("Cynthia Faast", "1234514", students, db)));
    }

    @Test
    public void testGetStudentsByName1() {
        Student student = null;
        student = db.getStudentByName("Rastus Kight");
        assertTrue(student.getId().compareTo("1234512") == 0);
    }

    @Test
    public void testGetStudentsByName2() {
        Student student = null;
        student = db.getStudentByName("Grier Nehling");
        assertEquals(96, student.getAttendance());
    }

    @Test
    public void testGetStudentsByID() {
        Student student = db.getStudentByID("1234504");
        assertTrue(student.getName().compareTo("Shevon Wise") == 0);
    }

    // Don't change above this point

    // Task 2.1
    // Test average assignment grade
    @Test
    public void testAssignmentAverage1(){
        Student student = db.getStudentByName("Kym Hiles");
        int average = db.getAssignmentAverage(student);
        assertEquals(92, average,0);
    }

    // Task 2.2
    // Test average project grade with contribution
    @Test
    public void testProjectAverage(){
        Student student = db.getStudentByID("1234511");
        int average = db.getProjectAverage(student);
        assertEquals(95, average, 0);
    }

    // Task 3.1
    // Test adding a new assignment
    @Test
    public void testAddingNewAssignment(){
        db.addAssignment();
        assert(db.getNumAssignments() == 4);
    }

    // Test 3.2
    // Test adding a grade to new assignment
    @Test
    public void testAddingNewAssignment2(){
        db.addAssignment();
        assert (db.getNumAssignments() == 4);
        Student student = db.getStudentByName("Genista Parrish");
        db.addAssignmentGrade(student.getId(),65,4);
        double grade = student.getGrades().get(4);
        assertEquals(65, grade, 0);
    }

    // Task 3.3
    // Adds a new Project and Team contribution
    @Test
    public void testAddingContribution(){
        db.addProjectandContribution();
        assert (db.getNumProjects() == 4);
        Student student = db.getStudentByID("1234513");
        db.addIndividualContribution(student.getId(),88,4);
        double grade = student.getProjectGrades().get(4);
        assertEquals(88,grade,0);
    }
    // ----------------
    // Tests for additional tasks 3.4 - 3.6

    // Task 3.4
    // Test adding a new student
    @Test
    public void testAddingNewStudent(){
        Student student = new Student();
        student.setName("Glen Holly");
        student.setId("3324512");
        db.addStudent(student);
        Student check = db.getStudentByID(student.getId());
        assertEquals(student,check);
    }

    // Task 3.5
    // Test adding a new project
    @Test
    public void testAddingNewProject(){
        db.addProject();
        db.addProject();
        assert(db.getNumProjects() == 5);
    }

    // Task 3.6
    // Test adding project grades
    @Test
    public void testAddingProjectGrade(){
        Student student = db.getStudentByID("1234504");
        double preRecordedGrade = student.getProjectGrades().get(0);
        double sampleGrade = 55;
        db.addProjectGrade(student.getId(), sampleGrade, 0);
        double updatedGrade = student.getProjectGrades().get(0);
        assertNotEquals(preRecordedGrade,updatedGrade,0);
    }

}
