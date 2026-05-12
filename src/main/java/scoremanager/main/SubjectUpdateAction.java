package scoremanager.main;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        // 科目コードを取得
        String cd = request.getParameter("cd");

        // DBから科目情報を取得
        SubjectDao sDao = new SubjectDao();
        Subject subject = sDao.get(cd, teacher.getSchool());

        request.setAttribute("subject", subject);

        request.getRequestDispatcher("subject_update.jsp").forward(request, response);
    }
}