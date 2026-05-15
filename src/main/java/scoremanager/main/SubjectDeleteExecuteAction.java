package scoremanager.main;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectDeleteExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        // 科目コードを取得
        String cd = request.getParameter("cd");

        // DBから科目情報を取得
        SubjectDao sDao = new SubjectDao();
        Subject subject = sDao.get(cd, teacher.getSchool());

        // DBから削除
        sDao.delete(subject);

        // 削除完了画面へフォワード
        request.getRequestDispatcher("subject_delete_done.jsp").forward(request, response);
        
        
    }
}
