package model;

import java.util.ArrayList;

public class Student {

    private int studentId;
    private String studentName;
    private String email;
    private String courseName;
    private double programmingScore;
    private double mathematicsScore;
    private ArrayList<String> technicalSkills;

    // Constructor
    public Student(int studentId, String studentName, String email,
                   String courseName, double programmingScore,
                   double mathematicsScore, ArrayList<String> technicalSkills) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        this.technicalSkills = technicalSkills;
    }

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getEmail() {
        return email;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getProgrammingScore() {
        return programmingScore;
    }

    public double getMathematicsScore() {
        return mathematicsScore;
    }

    public ArrayList<String> getTechnicalSkills() {
        return technicalSkills;
    }

    // Setters
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setProgrammingScore(double programmingScore) {
        this.programmingScore = programmingScore;
    }

    public void setMathematicsScore(double mathematicsScore) {
        this.mathematicsScore = mathematicsScore;
    }

    public void setTechnicalSkills(ArrayList<String> technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    // Calculate average score
    public double calculateAverage() {
        return (programmingScore + mathematicsScore) / 2.0;
    }

    // Performance category
    public String getPerformanceCategory() {
        double avg = calculateAverage();

        if (avg >= 85)
            return "Excellent";
        else if (avg >= 70)
            return "Good";
        else if (avg >= 50)
            return "Satisfactory";
        else
            return "Needs Improvement";
    }

    // Stronger subject
    public String getStrongerSubject() {
        if (programmingScore > mathematicsScore)
            return "Programming";
        else if (mathematicsScore > programmingScore)
            return "Mathematics";
        else
            return "Both Equal";
    }

    // To display student details
    @Override
    public String toString() {
        return "\nStudent ID: " + studentId +
                "\nName: " + studentName +
                "\nEmail: " + email +
                "\nCourse: " + courseName +
                "\nProgramming Score: " + programmingScore +
                "\nMathematics Score: " + mathematicsScore +
                "\nAverage: " + calculateAverage() +
                "\nCategory: " + getPerformanceCategory() +
                "\nStronger Subject: " + getStrongerSubject() +
                "\nTechnical Skills: " + technicalSkills;
    }
}
