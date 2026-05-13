package scoremanager.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.School; // 追加
import bean.Student;
import bean.Teacher;
import bean.TestScore;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ScoreServiceAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        
        // 型の使い分けのために準備
        School school = teacher.getSchool();   // Schoolオブジェクト
        String schoolCd = school.getCd();      // 学校コード(String) ※getCd()かgetNo()かはbean定義に合わせる

        // 検索パラメータ取得
        String entYearStr = req.getParameter("f1");
        String classNum = req.getParameter("f2");
        String subjectCd = req.getParameter("f3");
        String numStr = req.getParameter("f4");

        int entYear = (entYearStr != null) ? Integer.parseInt(entYearStr) : 0;
        int num = (numStr != null) ? Integer.parseInt(numStr) : 0;

        // --- 初期表示用データの準備 ---
        StudentDao sDao = new StudentDao();
        SubjectDao subDao = new SubjectDao();

        // StudentDaoは String を引数に取る (前回の修正を維持)
        List<Student> allStudents = sDao.filter(schoolCd);
        Set<Integer> entYearSet = new HashSet<>();
        Set<String> classNumSet = new HashSet<>();
        for (Student s : allStudents) {
            entYearSet.add(s.getEntYear());
            classNumSet.add(s.getClassNum());
        }
        
        List<Integer> entYearList = new ArrayList<>(entYearSet);
        Collections.sort(entYearList);
        List<String> classNumList = new ArrayList<>(classNumSet);
        Collections.sort(classNumList);

        req.setAttribute("ent_year_set", entYearList);
        req.setAttribute("class_num_set", classNumList);

        // 【修正】SubjectDao.filter は Schoolオブジェクトを引数に取る
        req.setAttribute("subjects", subDao.filter(school));

        // 検索実行判定
        if (entYear != 0 && classNum != null && !classNum.equals("0") && subjectCd != null && !subjectCd.equals("0") && num != 0) {
            
            // StudentDao.filter は String(schoolCd) を引数に取る
            List<Student> students = sDao.filter(schoolCd, entYear, classNum, true);
            
            // 既存の得点取得
            TestDao tDao = new TestDao();
            // 【修正】TestDao.filter は Schoolオブジェクトを引数に取る
            List<TestScore> testScores = tDao.filter(school, entYear, classNum, subjectCd, num);
            
            Map<String, Integer> scoreMap = new HashMap<>();
            for (TestScore ts : testScores) {
                scoreMap.put(ts.getStudent().getNo(), ts.getPoint());
            }

            req.setAttribute("f1", entYear);
            req.setAttribute("f2", classNum);
            req.setAttribute("f3", subjectCd);
            req.setAttribute("f4", num);
            req.setAttribute("students", students);
            req.setAttribute("scores", scoreMap);

            // 【修正】SubjectDao.get は (String, School) を引数に取る
            req.setAttribute("subject_name", subDao.get(subjectCd, school).getName());
        } else if (entYearStr != null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("filter", "入学年度とクラスと科目と回数を選択してください");
            req.setAttribute("errors", errors);
        }

        req.getRequestDispatcher("test_regist.jsp").forward(req, res);
    }
}