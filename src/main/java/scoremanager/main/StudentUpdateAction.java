package scoremanager.main;

import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        // 学生番号を取得
        String no = request.getParameter("no");

        // DBから学生情報を取得
        StudentDao sDao = new StudentDao();
        Student student = sDao.get(no);

        // クラス番号一覧を全学生から生成
        List<String> classList = new ArrayList<>();
        for (Student s : sDao.filter(teacher.getSchool().getCd())) {
            if (!classList.contains(s.getClassNum())) {
                classList.add(s.getClassNum());
            }
        }

        request.setAttribute("student", student);
        request.setAttribute("class_num_set", classList);

        request.getRequestDispatcher("student_update.jsp").forward(request, response);
    }
}