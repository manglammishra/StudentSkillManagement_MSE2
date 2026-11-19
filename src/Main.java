
import dao.StudentDAO;
import model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static StudentDAO dao = new StudentDAO();

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== STUDENT PROFILE & SKILL MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Search Student by Email");
            System.out.println("5. Search Student by Course");
            System.out.println("6. Generate Merit List");
            System.out.println("7. Find Students with Same Skills");
            System.out.println("8. Programming Needs Improvement (Math >= 75)");
            System.out.println("9. Update Student");
            System.out.println("10. Delete Student");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = readInt();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayStudents(dao.getAllStudents());
                case 3 -> searchById();
                case 4 -> searchByEmail();
                case 5 -> searchByCourse();
                case 6 -> displayStudents(dao.getMeritList());
                case 7 -> displaySameSkills();
                case 8 -> displayStudents(dao.getImprovementStudents());
                case 9 -> updateStudent();
                case 10 -> deleteStudent();
                case 0 -> System.out.println("Exiting application...");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 0);
    }

    // Read integer safely
    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid integer: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    // Validate scores from 0 to 100
    static double readScore(String subject) {
        double score;

        do {
            System.out.print("Enter " + subject + " score (0-100): ");
            score = sc.nextDouble();
            sc.nextLine();

            if (score < 0 || score > 100) {
                System.out.println("Score must be between 0 and 100!");
            }

        } while (score < 0 || score > 100);

        return score;
    }

    // Read skills separated by commas
    static ArrayList<String> readSkills() {
        System.out.print("Enter technical skills (comma-separated): ");
        String input = sc.nextLine();

        ArrayList<String> skills = new ArrayList<>();

        if (!input.isBlank()) {
            String[] arr = input.split(",");

            for (String skill : arr) {
                if (!skill.trim().isEmpty()) {
                    skills.add(skill.trim());
                }
            }
        }

        return skills;
    }

    // Add student
    static void addStudent() {
        System.out.print("Student ID: ");
        int id = readInt();

        System.out.print("Student Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Course Name: ");
        String course = sc.nextLine();

        double programming = readScore("Programming");
        double maths = readScore("Mathematics");

        ArrayList<String> skills = readSkills();

        Student s = new Student(id, name, email, course,
                programming, maths, skills);

        if (dao.addStudent(s)) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Failed to add student. ID or email may already exist.");
        }
    }

    // Display students
    static void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student s : students) {
            System.out.println("--------------------------------");
            System.out.println(s);
        }
    }

    // Search by ID
    static void searchById() {
        System.out.print("Enter student ID: ");
        int id = readInt();

        Student s = dao.searchById(id);

        if (s != null) {
            System.out.println(s);
        } else {
            System.out.println("Student not found.");
        }
    }

    // Search by email
    static void searchByEmail() {
        System.out.print("Enter email: ");
        String email = sc.nextLine();

        displayStudents(dao.searchByEmail(email));
    }

    // Search by course
    static void searchByCourse() {
        System.out.print("Enter course name: ");
        String course = sc.nextLine();

        displayStudents(dao.searchByCourse(course));
    }

    // Find shared technical skills
    static void displaySameSkills() {
        List<String> result = dao.getStudentsWithSameSkills();

        if (result.isEmpty()) {
            System.out.println("No common skills found.");
        } else {
            for (String s : result) {
                System.out.println(s);
            }
        }
    }

    // Update student
    static void updateStudent() {
        System.out.print("Enter ID of student to update: ");
        int id = readInt();

        Student old = dao.searchById(id);

        if (old == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println("Enter new details:");

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Course: ");
        String course = sc.nextLine();

        double programming = readScore("Programming");
        double maths = readScore("Mathematics");

        ArrayList<String> skills = readSkills();

        Student updated = new Student(id, name, email, course,
                programming, maths, skills);

        if (dao.updateStudent(updated)) {
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Update failed.");
        }
    }

    // Delete student
    static void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        int id = readInt();

        if (dao.deleteStudent(id)) {
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student not found.");
        }
    }
}
