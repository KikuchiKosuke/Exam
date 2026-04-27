package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Student;

public class StudentDao extends Dao {

    // フィールド変数 baseSql
    private String baseSql = "SELECT * FROM STUDENT WHERE SCHOOL_CD = ?";

    // postFilterメソッド
    private List<Student> postFilter(ResultSet rs) throws Exception {
        List<Student> list = new ArrayList<Student>();

        while (rs.next()) {
            Student student = new Student();
            student.setNo(rs.getString("NO"));
            student.setName(rs.getString("NAME"));
            student.setEntYear(rs.getInt("ENT_YEAR"));
            student.setClassNum(rs.getString("CLASS_NUM"));
            student.setIsAttend(rs.getBoolean("IS_ATTEND"));
            student.setSchoolCd(rs.getString("SCHOOL_CD"));
            list.add(student);
        }

        return list;
    }
 // filterメソッド④（学校のみ、全件）
    public List<Student> filter(String schoolCd) throws Exception {
        String sql = baseSql;
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, schoolCd);
            try (ResultSet rs = st.executeQuery()) {
                return postFilter(rs);
            }
        }
    }

    // filterメソッド③（学校、在学フラグ）
    public List<Student> filter(String schoolCd, boolean isAttend) throws Exception {
        String sql = baseSql + " AND IS_ATTEND = ?";
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, schoolCd);
            st.setBoolean(2, isAttend);
            try (ResultSet rs = st.executeQuery()) {
                return postFilter(rs);
            }
        }
    }

    // filterメソッド②（学校、入学年度、在学フラグ）
    public List<Student> filter(String schoolCd, int entYear, boolean isAttend) throws Exception {
        String sql = baseSql + " AND ENT_YEAR = ? AND IS_ATTEND = ?";
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, schoolCd);
            st.setInt(2, entYear);
            st.setBoolean(3, isAttend);
            try (ResultSet rs = st.executeQuery()) {
                return postFilter(rs);
            }
        }
    }

    // filterメソッド①（学校、入学年度、クラス番号、在学フラグ）
    public List<Student> filter(String schoolCd, int entYear, String classNum, boolean isAttend) throws Exception {
        String sql = baseSql + " AND ENT_YEAR = ? AND CLASS_NUM = ? AND IS_ATTEND = ?";
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, schoolCd);
            st.setInt(2, entYear);
            st.setString(3, classNum);
            st.setBoolean(4, isAttend);
            try (ResultSet rs = st.executeQuery()) {
                return postFilter(rs);
            }
        }
    }
 // getメソッド
    public Student get(String no) throws Exception {
        String sql = "SELECT * FROM STUDENT WHERE NO = ?";
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, no);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setNo(rs.getString("NO"));
                    student.setName(rs.getString("NAME"));
                    student.setEntYear(rs.getInt("ENT_YEAR"));
                    student.setClassNum(rs.getString("CLASS_NUM"));
                    student.setIsAttend(rs.getBoolean("IS_ATTEND"));
                    student.setSchoolCd(rs.getString("SCHOOL_CD"));
                    return student;
                }
                return null;
            }
        }
    }

    // saveメソッド
    public boolean save(Student student) throws Exception {
        // 既存データの確認
        Student existing = get(student.getNo());

        String sql;
        if (existing != null) {
            // 更新
            sql = "UPDATE STUDENT SET NAME = ?, ENT_YEAR = ?, CLASS_NUM = ?, IS_ATTEND = ?, SCHOOL_CD = ? WHERE NO = ?";
        } else {
            // 登録
            sql = "INSERT INTO STUDENT (NAME, ENT_YEAR, CLASS_NUM, IS_ATTEND, SCHOOL_CD, NO) VALUES (?, ?, ?, ?, ?, ?)";
        }

        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, student.getName());
            st.setInt(2, student.getEntYear());
            st.setString(3, student.getClassNum());
            st.setBoolean(4, student.isAttend());
            st.setString(5, student.getSchoolCd());
            st.setString(6, student.getNo());
            return st.executeUpdate() > 0;
        }
    }
}