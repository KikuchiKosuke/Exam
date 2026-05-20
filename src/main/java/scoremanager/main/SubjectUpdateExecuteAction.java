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

        SubjectDao sDao = new SubjectDao();
        // データベースから現在の科目情報を取得
        Subject subject = sDao.get(cd, teacher.getSchool());

        // 【追加】対象の科目が他から削除されていた場合のチェック
        if (subject == null) {
            errors.put("cd", "科目が存在していません");
            
            // 画面再表示用に、リクエストパラメータを元にした臨時インスタンスを作成
            subject = new Subject();
            subject.setCd(cd);
        }

        // 科目名未入力
        if (name == null || name.isEmpty()) {
            errors.put("name", "このフィールドを入力してください。");
        }

        // エラーがあった場合は変更画面に戻る
        if (!errors.isEmpty()) {
            // 科目が存在していた場合は、入力された新しい科目名をセットして画面に戻す
            if (subject != null && errors.get("cd") == null) {
                subject.setName(name);
            } else if (subject != null) {
                // 科目が存在しない場合も、入力されていた名前を保持させる
                subject.setName(name);
            }
            request.setAttribute("errors", errors);
            request.setAttribute("subject", subject);
            request.getRequestDispatcher("subject_update.jsp").forward(request, response);
            return;
        }

        // 科目情報を更新（正常系：subjectは必ず存在する）
        subject.setName(name);

        // DBに保存
        sDao.save(subject);

        // 変更完了画面へフォワード
        request.getRequestDispatcher("subject_update_complete.jsp").forward(request, response);
    }
}