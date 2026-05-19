package scoremanager.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class TestRegistAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        
        School school = teacher.getSchool();
        String schoolCd = school.getCd();

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

        List<Student> allStudents = sDao.filter(schoolCd);
        Set<Integer> entYearSet = new HashSet<>();
        Set<String> classNumSet = new HashSet<>();
        
        for (Student s : allStudents) {
            // 破損データ（記号交じりなど）がドロップダウンに混ざるのを防ぐ防衛ロジック
            if (s.getClassNum() != null && !s.getClassNum().contains("(")) {
                entYearSet.add(s.getEntYear());
                classNumSet.add(s.getClassNum());
            }
        }
        
        List<Integer> entYearList = new ArrayList<>(entYearSet);
        Collections.sort(entYearList);
        List<String> classNumList = new ArrayList<>(classNumSet);
        Collections.sort(classNumList);

        req.setAttribute("ent_year_set", entYearList);
        req.setAttribute("class_num_set", classNumList);

        // 科目リストのセット
        List<Subject> subject_name = subDao.filter(school);
        req.setAttribute("subject_name", subject_name);
        
        // --- 検索実行判定 ---
        if (entYear != 0 && classNum != null && !classNum.equals("0") && subjectCd != null && !subjectCd.equals("0") && num != 0) {
            
            List<Student> students = sDao.filter(schoolCd, entYear, classNum, true);
            
            TestDao tDao = new TestDao();
            List<TestScore> testScores = tDao.filter(school, entYear, classNum, subjectCd, num);
            
            // JSPへ渡す用：キーをString型（学生番号）にして詰め替え
            Map<String, Integer> scoreMap = new HashMap<>();
            for (TestScore ts : testScores) {
                // ts.getStudent().getNo() の文字列型とJSP側を完全合致させる
                scoreMap.put(String.valueOf(ts.getStudent().getNo()).trim(), ts.getPoint());
            }

            req.setAttribute("f1", entYear);
            req.setAttribute("f2", classNum);
            req.setAttribute("f3", subjectCd);
            req.setAttribute("f4", num);
            req.setAttribute("students", students);
            req.setAttribute("scores", scoreMap);

        } else if (entYearStr != null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("filter", "入学年度とクラスと科目と回数を選択してください");
            req.setAttribute("errors", errors);
        }

        req.getRequestDispatcher("test_regist.jsp").forward(req, res);
    }
}