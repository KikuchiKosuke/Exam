<%-- 学生登録JSP --%>
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
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報登録</h2>
            <form method="post" action="StudentCreateExecute.action">
                <div class="mx-3 mb-3">
                    <div class="mb-3">
                        <label class="form-label" for="ent-year-select">入学年度</label>
                        <select class="form-select" id="ent-year-select" name="ent_year">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year==ent_year}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                        <div class="text-warning">${errors.get("ent_year")}</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="no-input">学生番号</label>
                        <input class="form-control" type="text" id="no-input" name="no"
                            maxlength="10" required placeholder="学生番号を入力してください"
                            value="${no}" />
                        <div class="text-warning">${errors.get("no")}</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="name-input">氏名</label>
                        <input class="form-control" type="text" id="name-input" name="name"
                            maxlength="30" required placeholder="氏名を入力してください"
                            value="${name}" />
                        <div class="text-warning">${errors.get("name")}</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="class-num-select">クラス</label>
                        <select class="form-select" id="class-num-select" name="class_num">
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num==class_num}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <button class="btn btn-secondary" type="submit" name="end">登録して終了</button>
                </div>
            </form>
            <div class="mx-3">
                <a href="StudentList.action">戻る</a>
            </div>
        </section>
    </c:param>
</c:import>