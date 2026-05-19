package scoremanager.main;

import java.util.ArrayList;
import java.util.HashMap; // 追加
import java.util.List;
import java.util.Map; // 追加

import bean.School;
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

public class TestRegisterExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        School school = teacher.getSchool();
        String schoolCd = school.getCd();

        TestDao tDao = new TestDao();
        StudentDao sDao = new StudentDao();
        SubjectDao subDao = new SubjectDao();

        String subjectCd = req.getParameter("f3");
        int num = Integer.parseInt(req.getParameter("f4"));
        int entYear = Integer.parseInt(req.getParameter("f1"));
        String classNum = req.getParameter("f2");

        Subject subject = subDao.get(subjectCd, school);
        List<Student> students = sDao.filter(schoolCd, entYear, classNum, true);
        
        List<TestScore> targetScores = new ArrayList<>();
        Map<String, String> errors = new HashMap<>(); // 💡 エラー格納用のマップを用意

        for (Student student : students) {
            String pointStr = req.getParameter("point_" + student.getNo());
            
            if (pointStr != null && !pointStr.isEmpty()) {
                try {
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
                    } else {
                        // 💡 0〜100の範囲外の場合にエラーを設定
                        errors.put(student.getNo(), "0〜100の範囲で入力してください");
                    }
                } catch (NumberFormatException e) {
                    // 💡 数字以外が入力された場合の考慮
                    errors.put(student.getNo(), "数値を入力してください");
                }
            } else {
                tDao.deleteScore(student, subject, school, num); 
            }
        }
        
        // 💡 エラーが1件でもある場合は、保存せずに検索条件とエラーを保持して入力画面に戻す
        if (!errors.isEmpty()) {
            // 初期表示用データの再取得が必要な場合は TestRegistAction のロジックに合わせるか、
            // 今回はリクエストパラメータを引き継いで再表示させます
            req.setAttribute("errors", errors);
            req.setAttribute("f1", entYear);
            req.setAttribute("f2", classNum);
            req.setAttribute("f3", subjectCd);
            req.setAttribute("f4", num);
            req.setAttribute("students", students);
            
            // 再検索時と同様に、入力されていた（正しい）点数をマップに戻して画面に再表示させる処理
            Map<String, Integer> scoreMap = new HashMap<>();
            for (TestScore ts : targetScores) {
                scoreMap.put(ts.getStudent().getNo(), ts.getPoint());
            }
            req.setAttribute("scores", scoreMap);
            
            // 科目名などの再セット（TestRegistActionを参考に必要に応じて実装）
            req.setAttribute("subject_name", subDao.filter(school));

            req.getRequestDispatcher("test_regist.jsp").forward(req, res);
            return; // 処理を終了
        }
        
        // エラーがなければ一括保存
        tDao.save(targetScores);

        req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
    }
}