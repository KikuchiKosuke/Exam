package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.TestScore;

public class TestDao extends Dao {

    /**
     * 条件に合致する成績一覧を取得する
     */
    public List<TestScore> filter(School school, int entYear, String classNum, String subjectCd, int num) throws Exception {
        List<TestScore> list = new ArrayList<>();
        Connection con = getConnection();
        
        // 学生テーブルと結合し、検索条件に一致するデータを取得 [cite: 2]
        String sql = "SELECT t.STUDENT_NO, t.SUBJECT_CD, t.SCHOOL_CD, t.NO, t.POINT, t.CLASS_NUM " +
                     "FROM TEST t " +
                     "JOIN STUDENT s ON t.STUDENT_NO = s.NO " +
                     "WHERE t.SCHOOL_CD = ? AND s.ENT_YEAR = ? AND t.CLASS_NUM = ? AND t.SUBJECT_CD = ? AND t.NO = ?";
        
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, school.getCd());
        st.setInt(2, entYear);
        st.setString(3, classNum);
        st.setString(4, subjectCd);
        st.setInt(5, num);
        
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            TestScore ts = new TestScore();
            // 簡易化のためIDからBeanを生成（本来は各Daoで取得）
            Student s = new Student(); s.setNo(rs.getString("STUDENT_NO"));
            Subject sub = new Subject(); sub.setCd(rs.getString("SUBJECT_CD"));
            
            ts.setStudent(s);
            ts.setSubject(sub);
            ts.setSchool(school);
            ts.setNo(rs.getInt("NO"));
            ts.setPoint(rs.getInt("POINT"));
            ts.setClassNum(rs.getString("CLASS_NUM"));
            list.add(ts);
        }
        
        st.close();
        con.close();
        return list;
    }

    /**
     * 成績情報を一括保存する（UPSERT処理）
     */
    public boolean save(List<TestScore> list) throws Exception {
        Connection con = getConnection();
        con.setAutoCommit(false); // トランザクション開始
        
        try {
            for (TestScore ts : list) {
                // 既にデータが存在するか確認
                String checkSql = "SELECT COUNT(*) FROM TEST WHERE STUDENT_NO=? AND SUBJECT_CD=? AND SCHOOL_CD=? AND NO=?";
                PreparedStatement checkSt = con.prepareStatement(checkSql);
                checkSt.setString(1, ts.getStudent().getNo());
                checkSt.setString(2, ts.getSubject().getCd());
                checkSt.setString(3, ts.getSchool().getCd());
                checkSt.setInt(4, ts.getNo());
                ResultSet rs = checkSt.executeQuery();
                
                boolean exists = false;
                if (rs.next()) {
                    exists = (rs.getInt(1) > 0);
                }
                
                PreparedStatement saveSt;
                if (exists) {
                    // 更新 (UPDATE) 
                    saveSt = con.prepareStatement("UPDATE TEST SET POINT=?, CLASS_NUM=? WHERE STUDENT_NO=? AND SUBJECT_CD=? AND SCHOOL_CD=? AND NO=?");
                    saveSt.setInt(1, ts.getPoint());
                    saveSt.setString(2, ts.getClassNum());
                    saveSt.setString(3, ts.getStudent().getNo());
                    saveSt.setString(4, ts.getSubject().getCd());
                    saveSt.setString(5, ts.getSchool().getCd());
                    saveSt.setInt(6, ts.getNo());
                } else {
                    // 新規登録 (INSERT) 
                    saveSt = con.prepareStatement("INSERT INTO TEST (STUDENT_NO, SUBJECT_CD, SCHOOL_CD, NO, POINT, CLASS_NUM) VALUES (?, ?, ?, ?, ?, ?)");
                    saveSt.setString(1, ts.getStudent().getNo());
                    saveSt.setString(2, ts.getSubject().getCd());
                    saveSt.setString(3, ts.getSchool().getCd());
                    saveSt.setInt(4, ts.getNo());
                    saveSt.setInt(5, ts.getPoint());
                    saveSt.setString(6, ts.getClassNum());
                }
                saveSt.executeUpdate();
                saveSt.close();
                checkSt.close();
            }
            con.commit();
            return true;
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }

    /**
     * 成績情報を物理削除する（ブランク保存用）
     */
    public boolean deleteScore(Student student, Subject subject, int num) throws Exception {
        Connection con = getConnection();
        PreparedStatement st = con.prepareStatement("DELETE FROM TEST WHERE STUDENT_NO=? AND SUBJECT_CD=? AND NO=?");
        st.setString(1, student.getNo());
        st.setString(2, subject.getCd());
        st.setInt(3, num);
        
        int line = st.executeUpdate();
        st.close();
        con.close();
        return line > 0;
    }
}