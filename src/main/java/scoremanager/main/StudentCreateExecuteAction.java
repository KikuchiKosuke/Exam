package scoremanager.main;

import java.time.LocalDate;
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

public class StudentCreateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        // リクエストパラメーターの取得
        String entYearStr = request.getParameter("ent_year");
        String no         = request.getParameter("no");
        String name       = request.getParameter("name");
        String classNum   = request.getParameter("class_num");

        Map<String, String> errors = new HashMap<>();

        // 入学年度リストを生成（再表示用）
        LocalDate todaysDate = LocalDate.now();
        int year = todaysDate.getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i < year + 1; i++) {
            entYearSet.add(i);
        }

        // クラス番号一覧を生成（再表示用）
        StudentDao sDao = new StudentDao();
        List<String> classList = new ArrayList<>();
        for (Student s : sDao.filter(teacher.getSchool().getCd())) {
            if (!classList.contains(s.getClassNum())) {
                classList.add(s.getClassNum());
            }
        }

        // 入学年度未選択
        if (entYearStr == null || entYearStr.equals("0")) {
            errors.put("ent_year", "入学年度を選択してください");
        }

        // 学生番号重複チェック
        if (no != null && !no.isEmpty()) {
            Student existing = sDao.get(no);
            if (existing != null) {
                errors.put("no", "学生番号が重複しています");
            }
        }

        // エラーがあった場合は登録画面に戻る
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("ent_year_set", entYearSet);
            request.setAttribute("class_num_set", classList);
            request.setAttribute("no", no);
            request.setAttribute("name", name);
            request.setAttribute("class_num", classNum);
            request.setAttribute("ent_year", entYearStr);
            request.getRequestDispatcher("student_create.jsp").forward(request, response);
            return;
        }

        // Studentインスタンスの生成とセット
        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setEntYear(Integer.parseInt(entYearStr));
        student.setClassNum(classNum);
        student.setIsAttend(true);
        student.setSchoolCd(teacher.getSchool().getCd());

        // DBに保存
        sDao.save(student);

        // 登録完了画面へフォワード
        request.getRequestDispatcher("student_create_complete.jsp").forward(request, response);
    }
}