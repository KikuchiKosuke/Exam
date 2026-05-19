package bean;

import java.io.Serializable;

public class TestScore implements Serializable {
    private Student student;    // 1学生番号 (STUDENT_NO)
    private Subject subject;    // 2科目コード (SUBJECT_CD)
    private School school;      // 3学校コード (SCHOOL_CD)
    private int no;             // 4回数 (NO)
    private int point;          // 5得点 (POINT)
    private String classNum;    // 6クラス番号 (CLASS_NUM)

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

    public int getNo() { return no; }
    public void setNo(int no) { this.no = no; }

    public int getPoint() { return point; }
    public void setPoint(int point) { this.point = point; }

    public String getClassNum() { return classNum; }
    public void setClassNum(String classNum) { this.classNum = classNum; }
}
