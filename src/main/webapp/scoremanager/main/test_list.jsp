<%-- 成績参照JSP --%>
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
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績参照</h2>
            <div class="mx-3 mb-3">

                <%-- 科目情報検索 --%>
                <form method="get" action="TestListSubjectExecute.action">
                    <div class="row align-items-end mb-2">
                        <div class="col-auto">
                            <label class="form-label">科目情報</label>
                        </div>
                        <div class="col-auto">
                            <label class="form-label">入学年度</label>
                            <select class="form-select" name="ent_year">
                                <option value="0">--------</option>
                                <c:forEach var="year" items="${ent_year_set}">
                                    <option value="${year}" <c:if test="${year==ent_year}">selected</c:if>>${year}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-auto">
                            <label class="form-label">クラス</label>
                            <select class="form-select" name="class_num">
                                <option value="0">--------</option>
                                <c:forEach var="num" items="${class_num_set}">
                                    <option value="${num}" <c:if test="${num==class_num}">selected</c:if>>${num}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-auto">
                            <label class="form-label">科目</label>
                            <select class="form-select" name="subject_cd">
                                <option value="0">--------</option>
                                <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject.cd}" <c:if test="${subject.cd==subject_cd}">selected</c:if>>${subject.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-auto">
                            <button class="btn btn-secondary" type="submit">検索</button>
                        </div>
                    </div>
                    <div class="text-warning mb-2">${error}</div>
                </form>

                <%-- 学生情報検索 --%>
                <form method="get" action="TestListStudentExecute.action">
                    <div class="row align-items-end mb-2">
                        <div class="col-auto">
                            <label class="form-label">学生情報</label>
                        </div>
                        <div class="col-auto">
                            <label class="form-label">学生番号</label>
                            <input class="form-control" type="text" name="no"
                                placeholder="学生番号を入力してください"
                                value="${no}" required />
                        </div>
                        <div class="col-auto">
                            <button class="btn btn-secondary" type="submit">検索</button>
                        </div>
                    </div>
                    <div class="text-warning mb-2">${error_student}</div>
                </form>

                <%-- 利用方法案内メッセージ --%>
                <div class="text-info mb-3">
                    <c:choose>
                        <%-- 修正箇所：科目コードが存在し、かつ「検索結果の成績リスト(tests)」が空ではない場合のみ表示 --%>
                        <c:when test="${not empty subject_cd and subject_cd != '0' and not empty tests}">
                            <c:forEach var="subject" items="${subjects}">
                                <c:if test="${subject.cd == subject_cd}">
                                    ${subject.name}（${subject.cd}）
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            科目情報を選択または学生情報を入力して検索ボタンをクリックしてください
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="text-warning">${error_common}</div>
            </div>
        </section>
    </c:param>
</c:import>