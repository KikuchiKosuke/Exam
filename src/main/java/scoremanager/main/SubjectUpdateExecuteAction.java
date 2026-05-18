package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        // リクエストパラメーターの取得
        String cd   = request.getParameter("cd");
        String name = request.getParameter("name");

        Map<String, String> errors = new HashMap<>();

        //科目名未入力
        if (name == null || name.isEmpty()) {
            errors.put("name", "このフィールドを入力してください。");
        }

        // エラーがあった場合は変更画面に戻る
        if (!errors.isEmpty()) {
            SubjectDao sDao = new SubjectDao();
            Subject subject = sDao.get(cd, teacher.getSchool());
            subject.setName(name);
            request.setAttribute("errors", errors);
            request.setAttribute("subject", subject);
            request.getRequestDispatcher("subject_update.jsp").forward(request, response);
            return;
        }

        // 科目情報を更新
        SubjectDao sDao = new SubjectDao();
        Subject subject = sDao.get(cd, teacher.getSchool());
        subject.setName(name);

        // DBに保存
        sDao.save(subject);

        // 変更完了画面へフォワード
        request.getRequestDispatcher("subject_update_complete.jsp").forward(request, response);
    }
}
