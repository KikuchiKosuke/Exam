package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.TestListStudent;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestListStudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListStudentExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        String no = request.getParameter("no");

        // 入学年度リスト生成
        int year = LocalDate.now().getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i < year + 1; i++) {
            entYearSet.add(i);
        }

        // クラス番号一覧生成
        StudentDao sDao = new StudentDao();
        List<String> classList = new ArrayList<>();
        for (Student s : sDao.filter(teacher.getSchool().getCd())) {
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
        request.setAttribute("no", no);

        // バリデーション
        if (no == null || no.isEmpty()) {
            request.setAttribute("error_student", "このフィールドを入力してください。");
            request.getRequestDispatcher("test_list.jsp").forward(request, response);
            return;
        }

        // 学生取得
        Student student = sDao.get(no);
        if (student == null) {
            request.setAttribute("error_student", "学生情報が存在しませんでした");
            request.getRequestDispatcher("test_list.jsp").forward(request, response);
            return;
        }

        // 成績データ取得
        TestListStudentDao dao = new TestListStudentDao();
        List<TestListStudent> list = dao.filter(student);

        request.setAttribute("student", student);
        request.setAttribute("testList", list);
        request.getRequestDispatcher("test_list_student.jsp").forward(request, response);
    }
}