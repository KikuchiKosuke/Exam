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

public class StudentListAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();//セッション
        Teacher teacher = (Teacher)session.getAttribute("user");

        String entYearStr = "";// 入力された入学年度
        String classNum = "";//入力されたクラス番号
        String isAttendStr = "";//入力された在学フラグ
        int entYear = 0;// 入学年度
        boolean isAttend = false;// 在学フラグ
        List<Student> students = null;// 学生リスト
        LocalDate todaysDate = LocalDate.now();// LocalDateインスタンスを取得
        int year = todaysDate.getYear();// 現在の年を取得
        StudentDao sDao = new StudentDao();//学生Dao
        Map<String, String> errors = new HashMap<>();// エラーメッセージ

        //リクエストパラメーターの取得 2
        entYearStr = request.getParameter("f1");
        classNum = request.getParameter("f2");
        isAttendStr = request.getParameter("f3");

        // 在学フラグが送信されていた場合、先にtrueにする
        if (isAttendStr != null) {
            isAttend = true;
        }

        // 入学年度を数値に変換
        if (entYearStr != null) {
            entYear = Integer.parseInt(entYearStr);
        }

        //DBからデータ取得 3
        if (entYear != 0 && classNum != null && !classNum.equals("0")) {
            // 入学年度とクラス番号を指定
            students = sDao.filter(teacher.getSchool().getCd(), entYear, classNum, isAttend);
        } else if (entYear != 0 && (classNum == null || classNum.equals("0"))) {
            // 入学年度のみ指定
            students = sDao.filter(teacher.getSchool().getCd(), entYear, isAttend);
        } else if (entYear == 0 && (classNum == null || classNum.equals("0"))) {
            // 指定なしの場合は全件取得
            if (isAttend) {
                students = sDao.filter(teacher.getSchool().getCd(), isAttend);
            } else {
                students = sDao.filter(teacher.getSchool().getCd());
            }
        } else {
            errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
            request.setAttribute("errors", errors);
            students = sDao.filter(teacher.getSchool().getCd());
        }

        // クラス番号一覧を全学生から生成
        List<String> classList = new ArrayList<>();
        for (Student s : sDao.filter(teacher.getSchool().getCd())) {
            if (!classList.contains(s.getClassNum())) {
                classList.add(s.getClassNum());
            }
        }

        // リストを初期化
        List<Integer> entYearSet = new ArrayList<>();
        // 10年前から1年後まで年をリストに追加
        for (int i = year - 10; i < year + 1; i++) {
            entYearSet.add(i);
        }

        //レスポンス値をセット 6
        request.setAttribute("f1", entYear);
        request.setAttribute("f2", classNum);
        if (isAttendStr != null) {
            request.setAttribute("f3", isAttendStr);
        }
        request.setAttribute("students", students);
        request.setAttribute("class_num_set", classList);
        request.setAttribute("ent_year_set", entYearSet);

        //JSPへフォワード 7
        request.getRequestDispatcher("student_list.jsp").forward(request, response);
        
    }
}