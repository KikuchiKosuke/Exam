package scoremanager.main;

import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.TestScore;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ScoreRegisterExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        
        // 型の使い分けのために準備
        bean.School school = teacher.getSchool();
        String schoolCd = school.getCd(); // 学校コード(String)を取得

        TestDao tDao = new TestDao();
        StudentDao sDao = new StudentDao();
        SubjectDao subDao = new SubjectDao();

        // 検索条件の再取得
        String subjectCd = req.getParameter("f3");
        int num = Integer.parseInt(req.getParameter("f4"));
        int entYear = Integer.parseInt(req.getParameter("f1"));
        String classNum = req.getParameter("f2");

        // SubjectDao.get は (String, School) を期待
        Subject subject = subDao.get(subjectCd, school);
        
        // 【修正】StudentDao.filter は (String, int, String, boolean) を期待
        // 第1引数を school から schoolCd (String型) に変更
        List<Student> students = sDao.filter(schoolCd, entYear, classNum, true);
        
        List<TestScore> targetScores = new ArrayList<>();

        for (Student student : students) {
            String pointStr = req.getParameter("point_" + student.getNo());
            
            if (pointStr != null && !pointStr.isEmpty()) {
                // 登録・更新処理
                int point = Integer.parseInt(pointStr);
                if (point >= 0 && point <= 100) {
                    TestScore ts = new TestScore();
                    ts.setStudent(student);
                    ts.setSubject(subject);
                    ts.setSchool(school);
                    ts.setNo(num);
                    ts.setPoint(point);
                    ts.setClassNum(classNum);
                    targetScores.add(ts);
                }
            } else {
                // ブランク入力時は物理削除
                tDao.deleteScore(student, subject, num); 
            }
        }
        
        // 一括保存実行
        tDao.save(targetScores);

        // 完了画面へ
        req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
    }
}