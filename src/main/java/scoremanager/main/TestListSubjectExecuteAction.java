package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.Subject;
import bean.Teacher;
import bean.TestListSubject;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListSubjectExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        String entYearStr  = request.getParameter("ent_year");
        String classNum    = request.getParameter("class_num");
        String subjectCd   = request.getParameter("subject_cd");

        // 入学年度リスト生成
        int year = LocalDate.now().getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i < year + 1; i++) {
            entYearSet.add(i);
        }

        // クラス番号一覧生成
        StudentDao sDao = new StudentDao();
        List<String> classList = new ArrayList<>();
        for (bean.Student s : sDao.filter(teacher.getSchool().getCd())) {
            if (!classList.contains(s.getClassNum())) {
                classList.add(s.getClassNum());
            }
        }

        // 科目一覧取得
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> subjects = subjectDao.filter(teacher.getSchool());

        request.setAttribute("ent_year_set", entYearSet);
        request.setAttribute("class_num_set", classList);
        request.setAttribute("subjects", subjects);
        request.setAttribute("ent_year", entYearStr);
        request.setAttribute("class_num", classNum);
        request.setAttribute("subject_cd", subjectCd);

        // バリデーション
        if (entYearStr == null || entYearStr.equals("0")
                || classNum == null || classNum.equals("0")
                || subjectCd == null || subjectCd.equals("0")) {
            request.setAttribute("error", "入学年度とクラスと科目を選択してください。");
            request.getRequestDispatcher("test_list.jsp").forward(request, response);
            return;
        }

        // 科目取得
        Subject subject = subjectDao.get(subjectCd, teacher.getSchool());

        // 成績データ取得
        TestListSubjectDao dao = new TestListSubjectDao();
        List<TestListSubject> list = dao.filter(Integer.parseInt(entYearStr), classNum, subject, teacher.getSchool());

        if (list.isEmpty()) {
            request.setAttribute("error", "学生情報が存在しませんでした");
            request.getRequestDispatcher("test_list.jsp").forward(request, response);
            return;
        }

        request.setAttribute("testList", list);
        request.setAttribute("subject", subject);
        request.getRequestDispatcher("test_list_subject.jsp").forward(request, response);
    }
}