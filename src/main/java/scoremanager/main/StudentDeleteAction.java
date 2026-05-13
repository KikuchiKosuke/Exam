package scoremanager.main;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentDeleteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        String no = request.getParameter("no");

        StudentDao sDao = new StudentDao();
        Student student = sDao.get(no);

        request.setAttribute("student", student);

        request.getRequestDispatcher("student_delete.jsp").forward(request, response);
    }
}
