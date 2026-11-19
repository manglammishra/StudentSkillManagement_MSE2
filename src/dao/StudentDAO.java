package dao;


import model.Student;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Add student
    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getStudentId());
            ps.setString(2, s.getStudentName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getCourseName());
            ps.setDouble(5, s.getProgrammingScore());
            ps.setDouble(6, s.getMathematicsScore());

            ps.executeUpdate();

            // Insert skills
            for (String skill : s.getTechnicalSkills()) {
                addSkill(s.getStudentId(), skill);
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    // Add skill
    private void addSkill(int studentId, String skill) throws SQLException {
        String sql = "INSERT INTO student_skills VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setString(2, skill);
            ps.executeUpdate();
        }
    }

    // Get skills of student
    private ArrayList<String> getSkills(int studentId) {
        ArrayList<String> skills = new ArrayList<>();

        String sql = "SELECT skill_name FROM student_skills WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                skills.add(rs.getString("skill_name"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return skills;
    }

    // Convert ResultSet into Student object
    private Student extractStudent(ResultSet rs) throws SQLException {
        int id = rs.getInt("student_id");

        return new Student(
                id,
                rs.getString("student_name"),
                rs.getString("email"),
                rs.getString("course_name"),
                rs.getDouble("programming_score"),
                rs.getDouble("mathematics_score"),
                getSkills(id)
        );
    }

    // View all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(extractStudent(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return students;
    }

    // Search by ID
    public Student searchById(int id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractStudent(rs);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Search by email
    public List<Student> searchByEmail(String email) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE email LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + email + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                students.add(extractStudent(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return students;
    }

    // Search by course
    public List<Student> searchByCourse(String course) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE course_name LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + course + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                students.add(extractStudent(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return students;
    }

    // Merit list in descending order of average
    public List<Student> getMeritList() {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students " +
                "ORDER BY (programming_score + mathematics_score)/2 DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(extractStudent(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return students;
    }

    // Students needing programming improvement but maths >= 75
    public List<Student> getImprovementStudents() {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students " +
                "WHERE programming_score < 50 AND mathematics_score >= 75";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(extractStudent(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return students;
    }

    // Find students who share at least one technical skill
    public List<String> getStudentsWithSameSkills() {
        List<String> result = new ArrayList<>();

        String sql = """
                SELECT s1.student_name, s2.student_name, sk1.skill_name
                FROM student_skills sk1
                JOIN student_skills sk2
                    ON sk1.skill_name = sk2.skill_name
                    AND sk1.student_id < sk2.student_id
                JOIN students s1
                    ON s1.student_id = sk1.student_id
                JOIN students s2
                    ON s2.student_id = sk2.student_id
                ORDER BY sk1.skill_name
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(rs.getString(1) + " and " +
                        rs.getString(2) + " share skill: " +
                        rs.getString(3));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    // Update student
    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET student_name=?, email=?, course_name=?, " +
                "programming_score=?, mathematics_score=? WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getStudentName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getCourseName());
            ps.setDouble(4, s.getProgrammingScore());
            ps.setDouble(5, s.getMathematicsScore());
            ps.setInt(6, s.getStudentId());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                // Replace old skills with new skills
                String deleteSql = "DELETE FROM student_skills WHERE student_id=?";

                try (PreparedStatement deletePs = con.prepareStatement(deleteSql)) {
                    deletePs.setInt(1, s.getStudentId());
                    deletePs.executeUpdate();
                }

                for (String skill : s.getTechnicalSkills()) {
                    try (PreparedStatement skillPs =
                                 con.prepareStatement("INSERT INTO student_skills VALUES (?, ?)")) {
                        skillPs.setInt(1, s.getStudentId());
                        skillPs.setString(2, skill);
                        skillPs.executeUpdate();
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
        }

        return false;
    }

    // Delete student
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
        }

        return false;
    }
}
