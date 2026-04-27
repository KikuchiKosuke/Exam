package scoremanager.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        // リクエストパラメーターの取得
        String no         = request.getParameter("no");
        String name       = request.getParameter("name");
        String classNum   = request.getParameter("class_num");
        String isAttendStr = request.getParameter("is_attend");

        Map<String, String> errors = new HashMap<>();

        // クラス番号一覧を生成（再表示用）
        StudentDao sDao = new StudentDao();
        List<String> classList = new ArrayList<>();
        for (Student s : sDao.filter(teacher.getSchool().getCd())) {
            if (!classList.contains(s.getClassNum())) {
                classList.add(s.getClassNum());
            }
        }

        // DBから既存の学生情報を取得
        Student student = sDao.get(no);

        // バリデーション：氏名未入力
        if (name == null || name.isEmpty()) {
            errors.put("name", "このフィールドを入力してください。");
        }

        // エラーがあった場合は変更画面に戻る
        if (!errors.isEmpty()) {
            student.setName(name);
            student.setClassNum(classNum);
            student.setIsAttend(isAttendStr != null);
            request.setAttribute("errors", errors);
            request.setAttribute("student", student);
            request.setAttribute("class_num_set", classList);
            request.getRequestDispatcher("student_update.jsp").forward(request, response);
            return;
        }

        // 学生情報を更新
        student.setName(name);
        student.setClassNum(classNum);
        student.setIsAttend(isAttendStr != null);

        // DBに保存
        sDao.save(student);

        // 変更完了画面へフォワード
        request.getRequestDispatcher("student_update_complete.jsp").forward(request, response);
    }
}