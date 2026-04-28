package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {

    public Subject get(String cd, School school) throws Exception {
        String sql = "SELECT * FROM subject WHERE CD = ? AND school_cd = ?";
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, cd);
            st.setString(2, school.getCd());
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Subject subject = new Subject();
                    subject.setCd(rs.getString("CD"));
                    subject.setName(rs.getString("NAME"));
                    subject.setSchool(school);
                    return subject;
                }
                return null;
            }
        }
    }

    public List<Subject> filter(School school) throws Exception {
        String sql = "SELECT * FROM subject WHERE school_cd = ?";
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, school.getCd());
            try (ResultSet rs = st.executeQuery()) {
                List<Subject> list = new ArrayList<>();
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setCd(rs.getString("CD"));
                    subject.setName(rs.getString("NAME"));
                    subject.setSchool(school);
                    list.add(subject);
                }
                return list;
            }
        }
    }

    public boolean save(Subject subject) throws Exception {
        Subject existing = get(subject.getCd(), subject.getSchool());

        String sql;
        if (existing != null) {
            sql = "UPDATE subject SET NAME = ? WHERE CD = ? AND school_cd = ?";
        } else {
            sql = "INSERT INTO subject (NAME, CD, school_cd) VALUES (?, ?, ?)";
        }

        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, subject.getName());
            st.setString(2, subject.getCd());
            st.setString(3, subject.getSchool().getCd());
            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(Subject subject) throws Exception {
        String sql = "DELETE FROM subject WHERE CD = ? AND school_cd = ?";
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, subject.getCd());
            st.setString(2, subject.getSchool().getCd());
            return st.executeUpdate() > 0;
           
        }
    }
}
