package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.tags.shaded.org.apache.xpath.operations.String;

import bean.Student;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {

    private String baseSql = "SELECT t.no, t.point, t.subject_cd, s.name AS subject_name "
            + "FROM test t "
            + "JOIN subject s ON t.subject_cd = s.cd AND t.school_cd = s.school_cd "
            + "WHERE t.student_no = ? AND t.school_cd = ? "
            + "ORDER BY t.subject_cd, t.no";

    private List<TestListStudent> postFilter(ResultSet rs) throws Exception {
        List<TestListStudent> list = new ArrayList<>();
        while (rs.next()) {
            TestListStudent t = new TestListStudent();
            t.setSubjectName(rs.getString("subject_name"));
            t.setSubjectCd(rs.getString("subject_cd"));
            t.setNum(rs.getInt("no"));
            t.setPoint(rs.getInt("point"));
            list.add(t);
        }
        return list;
    }

    public List<TestListStudent> filter(Student student) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(baseSql)) {
            st.setString(1, student.getNo());
            st.setString(2, student.getSchoolCd());
            try (ResultSet rs = st.executeQuery()) {
                return postFilter(rs);
            }
        }
    }
}