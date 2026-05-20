<%-- 学生別成績一覧JSP --%>
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
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（学生）</h2>
            <div class="mx-3 mb-3">

                <%-- 科目情報検索フォーム --%>
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
                                <c:forEach var="s" items="${subjects}">
                                    <option value="${s.cd}">${s.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-auto">
                            <button class="btn btn-secondary" type="submit">検索</button>
                        </div>
                    </div>
                </form>

                <%-- 学生情報検索フォーム --%>
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
                </form>

                <%-- 検索結果 --%>
                <div class="mt-3">
                    <p>氏名：${student.name}（${student.no}）</p>
                    <c:choose>
                        <c:when test="${empty testList}">
                            <p>成績情報が存在しませんでした</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table table-hover">
                                <tr>
                                    <th>科目名</th>
                                    <th>科目コード</th>
                                    <th>回数</th>
                                    <th>点数</th>
                                </tr>
                                <c:forEach var="t" items="${testList}">
                                    <tr>
                                        <td>${t.subjectName}</td>
                                        <td>${t.subjectCd}</td>
                                        <td>${t.num}</td>
                                        <td>${t.point}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>
    </c:param>
</c:import>