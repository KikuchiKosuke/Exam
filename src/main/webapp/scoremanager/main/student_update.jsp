<%-- 学生変更JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">
        得点管理システム
    </c:param>

    <c:param name="scripts"></c:param>

    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報変更</h2>
            <form method="post" action="StudentUpdateExecute.action">
                <input type="hidden" name="no" value="${student.no}" />
                <div class="mx-3 mb-3">
                    <div class="mb-3">
                        <label class="form-label">入学年度</label>
                        <div>${student.entYear}</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">学生番号</label>
                        <div>${student.no}</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="name-input">氏名</label>
                        <input class="form-control" type="text" id="name-input" name="name"
                            maxlength="30" required placeholder="氏名を入力してください"
                            value="${student.name}" />
                        <div class="text-warning">${errors.get("name")}</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="class-num-select">クラス</label>
                        <select class="form-select" id="class-num-select" name="class_num">
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num==student.classNum}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3 form-check">
                        <label class="form-check-label" for="is-attend-check">在学中
                            <input class="form-check-input" type="checkbox"
                                id="is-attend-check" name="is_attend" value="t"
                                <c:if test="${student.isAttend()}">checked</c:if> />
                        </label>
                    </div>
                    <button class="btn btn-primary" type="submit">変更</button>
                </div>
            </form>
            <div class="mx-3">
                <a href="StudentList.action">戻る</a>
            </div>
        </section>
    </c:param>
</c:import>