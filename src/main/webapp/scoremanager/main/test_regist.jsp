<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3">成績管理</h2>
            
            <%-- 検索条件エリア --%>
            <form action="TestRegist.action" method="get" class="bg-light p-3 rounded mb-4">
                <div class="row g-3">
                    <div class="col-md-2">
                        <label class="form-label">入学年度</label>
                        <select name="f1" class="form-select">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">クラス</label>
                        <select name="f2" class="form-select">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">科目</label>
                        <select name="f3" class="form-select">
                            <option value="0">--------</option>
                            <c:forEach var="subject" items="${subject_name}">
                                <option value="${subject.cd}" <c:if test="${subject.cd == f3}">selected</c:if>>${subject.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">回数</label>
                        <select name="f4" class="form-select">
                            <option value="0">--------</option>
                            <c:forEach var="i" begin="1" end="5">
                                <option value="${i}" <c:if test="${i == f4}">selected</c:if>>${i}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100" id="filter_button">検索</button>
                    </div>
                </div>
                <c:if test="${not empty errors.get('filter')}">
                    <p class="text-danger mt-2 small">入学年度とクラスと科目と回数を選択してください</p>
                </c:if>
            </form>

            <%-- 成績入力エリア (検索成功後のみ表示) --%>
            <c:if test="${not empty students}">
            
                <%-- 上部ヘッダー（科目名と回数） --%>
                <div class="score-info-header" style="margin-top: 20px; margin-bottom: 10px;">
                        科目：
                        <c:forEach var="subject" items="${subject_name}">
                            <c:if test="${subject.cd == f3}"><c:out value="${subject.name}" /></c:if>
                        </c:forEach>
                        （${f4}回）
                </div>
                            
                <form action="TestRegisterExecute.action" method="post">
                    <%-- 検索条件を hidden で引き継ぐ --%>
                    <input type="hidden" name="f1" value="${f1}">
                    <input type="hidden" name="f2" value="${f2}">
                    <input type="hidden" name="f3" value="${f3}">
                    <input type="hidden" name="f4" value="${f4}">

                    <table class="table table-hover mt-3">
                        <thead>
                            <tr>
                                <th>入学年度</th>
                                <th>クラス</th>
                                <th>学生番号</th>
                                <th>氏名</th>
                                <th>点数</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="student" items="${students}">
                                <tr>
                                    <td>${student.entYear}</td>
                                    <td>${student.classNum}</td>
                                    <td>${student.no}</td>
                                    <td>${student.name}</td>
                                    <td>
                                        <%-- 【重要修正】String型として確実にMapから点数を取り出せるように修正 --%>
                                        <input type="text" name="point_${student.no}" 
                                               value="${scores[student.no.toString()]}" 
                                               class="form-control form-control-sm" style="width: 80px;">
                                        <c:if test="${not empty errors.get(student.no)}">
                                            <p class="text-danger small mb-0">0～100の範囲で入力してください</p>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="mt-4">
                        <button type="submit" class="btn btn-success">登録して終了</button>
                    </div>
                </form>
            </c:if>
            
            <c:if test="${not empty f1 && f1 != '0' && empty students}">
                <p>学生情報が存在しませんでした。</p>
            </c:if>
        </section>
    </c:param>
</c:import>