package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.tags.shaded.org.apache.xpath.operations.String;

import bean.School;
import bean.Subject;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao {

    private String baseSql = "SELECT st.no AS student_no, st.name AS student_name, st.ent_year, st.class_num, "
            + "t.no AS test_no, t.point "
            + "FROM student st "
            + "LEFT JOIN test t ON st.no = t.student_no AND t.subject_cd = ? AND t.school_cd = ? "
            + "WHERE st.school_cd = ? AND st.ent_year = ? AND st.class_num = ? "
            + "ORDER BY st.no, t.no";

    private List<TestListSubject> postFilter(ResultSet rs) throws Exception {
        Map<String, TestListSubject> map = new LinkedHashMap<>();
        while (rs.next()) {
            String studentNo = rs.getString("student_no");
            if (!map.containsKey(studentNo)) {
                TestListSubject t = new TestListSubject();
                t.setStudentNo(studentNo);
                t.setStudentName(rs.getString("student_name"));
                t.setEntYear(rs.getInt("ent_year"));
                t.setClassNum(rs.getString("class_num"));
                map.put(studentNo, t);
            }
            int testNo = rs.getInt("test_no");
            int point = rs.getInt("point");
            if (testNo != 0) {
                map.get(studentNo).putPoint(testNo, point);
            }
        }
        return new ArrayList<>(map.values());
    }

    public List<TestListSubject> filter(int entYear, String classNum, Subject subject, School school) throws Exception {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(baseSql)) {
            st.setString(1, subject.getCd());
            st.setString(2, school.getCd());
            st.setString(3, school.getCd());
            st.setInt(4, entYear);
            st.setString(5, classNum);
            try (ResultSet rs = st.executeQuery()) {
                return postFilter(rs);
            }
        }
    }
}