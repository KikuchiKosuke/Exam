package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import bean.Student;
import dao.StudentDao;
import tool.Action;

public class StudentDeleteExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String no = request.getParameter("no");

        StudentDao sDao = new StudentDao();
        Student student = sDao.get(no);

        // DBから削除
        sDao.delete(student);

        request.getRequestDispatcher("student_delete_done.jsp").forward(request, response);
    }
}
