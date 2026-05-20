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

public class SubjectCreateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        // リクエストパラメーターの取得
        String cd   = request.getParameter("cd");
        String name = request.getParameter("name");

        Map<String, String> errors = new HashMap<>();

        // 科目コード未入力
        if (cd == null || cd.isEmpty()) {
            errors.put("cd", "このフィールドを入力してください。");
        } else {
            // ★追加：科目コードが半角かつ3文字であるかのチェック
            // 正規表現: ^[a-zA-Z0-9]{3}$ (半角英数字でちょうど3文字)
            // もし数字のみ、あるいは英大文字のみなどルールがある場合は適宜変更してください
            if (!cd.matches("^[a-zA-Z0-9]{3}$")) {
                errors.put("cd", "科目コードは半角3文字にしてください");
            }
        }

        // 科目名未入力
        if (name == null || name.isEmpty()) {
            errors.put("name", "このフィールドを入力してください。");
        }

        // 科目コード重複チェック（エラーがまだ無い場合のみ実行するのが安全です）
        if (errors.get("cd") == null && cd != null && !cd.isEmpty()) {
            SubjectDao sDao = new SubjectDao();
            Subject existing = sDao.get(cd, teacher.getSchool());
            if (existing != null) {
                errors.put("cd", "科目コードが重複しています");
            }
        }

        // エラーがあった場合は登録画面に戻る
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("cd", cd);
            request.setAttribute("name", name);
            request.getRequestDispatcher("subject_create.jsp").forward(request, response);
            return;
        }

        // Subjectインスタンスの生成とセット
        SubjectDao sDao = new SubjectDao();
        Subject subject = new Subject();
        subject.setCd(cd);
        subject.setName(name);
        subject.setSchool(teacher.getSchool());

        // DBに保存
        sDao.save(subject);

        // 登録完了画面へフォワード
        request.getRequestDispatcher("subject_create_complete.jsp").forward(request, response);
    }
}