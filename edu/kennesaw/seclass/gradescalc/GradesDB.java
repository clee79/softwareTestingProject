package edu.kennesaw.seclass.gradescalc;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class GradesDB {

    private List<Student> enrolledStudents = new ArrayList<>();
    private int numAssignments;
    private int numProjects;

    public GradesDB() {
        numAssignments = 0;
        numProjects = 0;
    }

    public void loadSpreadsheet(String dbLocation) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(dbLocation);
            Workbook workbook = new XSSFWorkbook(fis);

            createStudents(workbook);
            setTeamNames(workbook);
            setAttendanceGrades(workbook);
            setIndividualGrades(workbook);
            setTeamProjectGrades(workbook);
            setProjectContributionGrades(workbook);

            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Compute an average for assignment grades
    // Task 2.1
    public int getAssignmentAverage(Student student) {
        List<Double> g = student.getGrades();
        double temp = 0;
        for(int i = 0; i<g.size();i++){
            temp += g.get(i);
        }
        return (int)Math.round((temp/numAssignments));
    }

    // Compute an average for project based on project grade and contribution
    // Task 2.2
    public int getProjectAverage(Student student) {
        List<Double> g = student.getTeamProjectGrades();
        List<Double> g2 = student.getProjectGrades();
        double temp = 0;
        for(int i = 0; i < g.size(); i++){
            temp += (g.get(i) * 0.8) + (g2.get(i) * 0.2);
        }
        return (int)Math.round((temp/numProjects));
    }

    // Add a new assignment to each student
    // Task 3.1
    public void addAssignment() {
        numAssignments++;
        HashSet<Student> s = getStudents();
        for(Iterator<Student> i = s.iterator(); i.hasNext();)
            i.next().addAssignment();
    }

    // Create a new project and individual contribution field for students
    // Task 3.3
    public void addProjectandContribution(){
        numProjects++;
        HashSet<Student> s = getStudents();
        for(Iterator<Student> i = s.iterator(); i.hasNext();){
            i.next().addProjectAndTeamGrade();
        }
    }

    // Create a new project only
    public void addProject(){
        numProjects++;
        HashSet<Student> s = getStudents();
        for(Iterator<Student> i = s.iterator(); i.hasNext();){
            i.next().addProject();
        }
    }

    // Add a project grade to student
    public void addProjectGrade(String id, double grade, int projectNum){
        Student student = getStudentByID(id);
        List<Double> studentProjects = student.getProjectGrades();
        studentProjects.set(projectNum, grade);
    }


    // Add an assignment grade for student
    // Task 3.2
    public void addAssignmentGrade(String id, double grade, int assignmentNum){
        // Look up student by id
        Student student = getStudentByID(id);
        // Get a reference to grades
        List<Double> studentGrades = student.getGrades();
        studentGrades.add(assignmentNum, grade);
    }

    // Update a contribution grade for project
    // Task 3.3
    public void addIndividualContribution(String id, double contribution, int assignmentNum){
        Student student = getStudentByID(id);
        List<Double> projectContribution = student.getProjectGrades();
        projectContribution.add(assignmentNum, contribution);
    }

    public void addStudent(Student student) {
        enrolledStudents.add(student);
    }

    public int getNumStudents() { return enrolledStudents.size(); }

    public int getNumAssignments() { return numAssignments; }

    public int getNumProjects() { return numProjects; }

    public HashSet<Student> getStudents() {
        // Make a has set of Students.
        // Add all the elements from the list to hashset
        HashSet<Student> students = new HashSet(enrolledStudents);
        students.addAll(enrolledStudents);
        return students;
    }

    public Student getStudentByName(String name) {
        // Type mismatch here
        // Need list of student but construction calls for list of students.
        Iterator<Student> iterator = enrolledStudents.iterator();
        while(iterator.hasNext()){
            Student student = iterator.next();
            if(student.getName().equals(name)){
                return student;
            }
        }
        return null;
    }

    public Student getStudentByID(String s) {
        Iterator<Student> iterator = enrolledStudents.iterator();
        while(iterator.hasNext()){
            Student student = iterator.next();
            if(student.getId().equals(s)){
                return student;
            }
        }
        return null;
    }

    private void createStudents(Workbook workbook) {
        //Creating Student objects from sheet 0
        Sheet sheet = workbook.getSheetAt(0);
        Iterator rowIterator = sheet.iterator();
        rowIterator.next();

        //iterating over each row
        while (rowIterator.hasNext() ) {
            Row row = (Row) rowIterator.next();
            Iterator cellIterator = row.cellIterator();

            if(!cellIterator.hasNext())
                continue;

            Student student = new Student();

            //Iterating over each cell (column wise)  in a particular row.
            while (cellIterator.hasNext()) {

                Cell cell = (Cell) cellIterator.next();

                //System.out.println(cell.getColumnIndex() +" "+ cell.getRowIndex() +" "+ cell.toString());
                if (cell.getColumnIndex() == 0) {
                    student.setName(cell.getStringCellValue());
                } else if (cell.getColumnIndex() == 1) {
                    student.setId(String.valueOf((int)cell.getNumericCellValue()));
                } else if (cell.getColumnIndex() == 2) {
                    student.setEmail(cell.getStringCellValue());
                } else if (cell.getColumnIndex() == 3) {
                    student.setC((int)cell.getNumericCellValue());
                } else if (cell.getColumnIndex() == 4) {
                    student.setCpp((int)cell.getNumericCellValue());
                } else if (cell.getColumnIndex() == 5) {
                    student.setJava((int)cell.getNumericCellValue());
                } else if (cell.getColumnIndex() == 6) {
                    String temp = cell.getStringCellValue();
                    if(temp.equals("Y"))
                        student.setJob(true);
                    else
                        student.setJob(false);
                }
            }
            //end iterating a row, add all the elements of a row in list
            //System.out.println(student.toString()); System.out.println();
            enrolledStudents.add(student);
        }
    }

    private void setTeamNames(Workbook workbook) {
        //Setting team names from sheet 1
        Sheet sheet = workbook.getSheetAt(1);
        Iterator rowIterator = sheet.iterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = (Row) rowIterator.next();
            Iterator cellIterator = row.cellIterator();
            //System.out.println("Row " + row.getRowNum());

            Cell cell = (Cell) cellIterator.next();

            String teamName = cell.getStringCellValue();

            //Iterating over each cell (column wise)  in a particular row.
            while (cellIterator.hasNext()) {

                cell = (Cell) cellIterator.next();
                //System.out.println(cell.getColumnIndex());

                Iterator<Student> iterator = enrolledStudents.iterator();
                while(iterator.hasNext()){
                    Student student = iterator.next();
                    if(student.getName().equals(String.valueOf(cell.getStringCellValue()))){
                        student.setTeam(teamName);
                        //System.out.println(student.toString());
                    }
                }
            }
        }
    }

    private void setAttendanceGrades(Workbook workbook) {
        //Setting attendance grade from sheet 2
        Sheet sheet = workbook.getSheetAt(2);
        Iterator rowIterator = sheet.iterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {

            Row row = (Row) rowIterator.next();
            Iterator cellIterator = row.cellIterator();
            Cell cell = (Cell) cellIterator.next();
            String studentName = cell.getStringCellValue();

            cell = (Cell) cellIterator.next();
            double attendanceGrade = cell.getNumericCellValue();

            Iterator<Student> iterator = enrolledStudents.iterator();
            while(iterator.hasNext()){
                Student student = iterator.next();
                if(student.getName().equals(studentName)){
                    student.setAttendance(attendanceGrade);
                    //System.out.println(studentName +": "+  student.getAttendance());
                    break;
                }
            }
        }
    }

    private void setIndividualGrades(Workbook workbook) {
        //Setting individual grades from sheet 3
        Sheet sheet = workbook.getSheetAt(3);
        Iterator rowIterator = sheet.iterator();
        rowIterator.next();
        numAssignments = 0;

        while (rowIterator.hasNext()) {

            Row row = (Row) rowIterator.next();
            Iterator cellIterator = row.cellIterator();

            Cell cell = (Cell) cellIterator.next();

            String studentName = cell.getStringCellValue();

            //Iterating over each cell (column wise)  in a particular row.
            //Build list of grades
            List<Double> grades = new ArrayList<>();
            while (cellIterator.hasNext()) {
                cell = (Cell) cellIterator.next();
                grades.add(cell.getNumericCellValue());
                numAssignments++;
            }

            //Set grades
            Iterator<Student> iterator = enrolledStudents.iterator();
            while(iterator.hasNext()){
                Student student = iterator.next();
                if(student.getName().equals(studentName)){
                    student.setGrades(grades);
                    //System.out.println(studentName+": "+grades);
                    break;
                }
            }
        }
        numAssignments /= enrolledStudents.size();
    }

    private void setTeamProjectGrades(Workbook workbook) {
        //Setting team project grades from sheet 5 so we know how many projects there are first
        Sheet sheet = workbook.getSheetAt(5);
        Iterator rowIterator = sheet.iterator();
        rowIterator.next();

        int numTeams = 0;
        while (rowIterator.hasNext()) {

            Row row = (Row) rowIterator.next();
            Iterator cellIterator = row.cellIterator();

            Cell cell = (Cell) cellIterator.next();

            String teamName = cell.getStringCellValue();
            numTeams++;

            //Iterating over each cell (column wise)  in a particular row.
            //Build list of grades
            List<Double> grades = new ArrayList<>();
            while (cellIterator.hasNext()) {
                cell = (Cell) cellIterator.next();
                grades.add(cell.getNumericCellValue());
                numProjects++;
            }

            //Set grades
            Iterator<Student> iterator = enrolledStudents.iterator();
            while(iterator.hasNext()){
                Student student = iterator.next();
                if(student.getTeam().equals(teamName)){
                    student.setTeamGrades(grades);
                    //System.out.println(student.getName()+": "+grades);
                }
            }
        }
        numProjects /= numTeams;
    }

    private void setProjectContributionGrades(Workbook workbook) {
        //Setting individual contribution grades from sheet 4 using numProjects calculated above to add zeros when necessary
        Sheet sheet = workbook.getSheetAt(4);
        Iterator rowIterator = sheet.iterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {

            Row row = (Row) rowIterator.next();
            Iterator cellIterator = row.cellIterator();

            Cell cell = (Cell) cellIterator.next();

            String studentName = cell.getStringCellValue();

            //Iterating over each cell (column wise)  in a particular row.
            //Build list of grades
            List<Double> grades = new ArrayList<>();
            while (cellIterator.hasNext()) {
                cell = (Cell) cellIterator.next();
                grades.add(cell.getNumericCellValue());
            }

            while (grades.size() < numProjects){
                grades.add(0.0);
            }

            //Set grades
            Iterator<Student> iterator = enrolledStudents.iterator();
            while(iterator.hasNext()){
                Student student = iterator.next();
                if(student.getName().equals(studentName)){
                    student.setProjectGrades(grades);
                    //System.out.println(studentName+": "+grades);
                    break;
                }
            }
        }

    }
}