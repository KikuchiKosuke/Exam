<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../../common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3">成績管理</h2> <%-- [cite: 1977] --%>
            
            <%-- 検索条件エリア --%>
            <form action="ScoreService.action" method="get" class="bg-light p-3 rounded mb-4">
                <div class="row g-3">
                    <div class="col-md-2">
                        <label class="form-label">入学年度</label> <%-- [cite: 1977] --%>
                        <select name="f1" class="form-select"> <%-- [cite: 1977] --%>
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">クラス</label> <%-- [cite: 1977] --%>
                        <select name="f2" class="form-select"> <%-- [cite: 1977] --%>
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">科目</label> <%-- [cite: 1977] --%>
                        <select name="f3" class="form-select"> <%-- [cite: 1977] --%>
                            <option value="0">--------</option>
                            <c:forEach var="subject" items="${subjects}">
                                <option value="${subject.cd}" <c:if test="${subject.cd == f3}">selected</c:if>>${subject.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">回数</label> <%-- [cite: 1977] --%>
                        <select name="f4" class="form-select"> <%-- [cite: 1977] --%>
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
                <%-- 代替フロー：未入力エラーメッセージ [cite: 1980, 1985] --%>
                <c:if test="${not empty errors.get('filter')}">
                    <p class="text-danger mt-2 small">入学年度とクラスと科目と回数を選択してください</p>
                </c:if>
            </form>

            <%-- 成績入力エリア (検索成功後のみ表示) [cite: 1978] --%>
            <c:if test="${not empty students}">
                <h3 class="h5 mt-4">${subject_name}（${f4}回目）</h3> <%-- 科目名と回数を表示 [cite: 1978] --%>
                <form action="ScoreRegister.action" method="post">
                    <%-- 検索条件を hidden で引き継ぐ --%>
                    <input type="hidden" name="f1" value="${f1}">
                    <input type="hidden" name="f2" value="${f2}">
                    <input type="hidden" name="f3" value="${f3}">
                    <input type="hidden" name="f4" value="${f4}">

                    <table class="table table-hover mt-3"> <%-- [cite: 1978] --%>
                        <thead>
                            <tr>
                                <th>入学年度</th> <%-- [cite: 1978] --%>
                                <th>クラス</th> <%-- [cite: 1978] --%>
                                <th>学生番号</th> <%-- [cite: 1978] --%>
                                <th>氏名</th> <%-- [cite: 1978] --%>
                                <th>点数</th> <%-- [cite: 1978] --%>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="student" items="${students}">
                                <tr>
                                    <td>${student.entYear}</td> <%-- [cite: 1978] --%>
                                    <td>${student.classNum}</td> <%-- [cite: 1978] --%>
                                    <td>${student.no}</td> <%-- [cite: 1978] --%>
                                    <td>${student.name}</td> <%-- [cite: 1978] --%>
                                    <td>
                                        <%-- name属性をpoint_{学生番号}に設定 [cite: 1978] --%>
                                        <input type="text" name="point_${student.no}" 
                                               value="${scores.get(student.no)}" 
                                               class="form-control form-control-sm" style="width: 80px;">
                                        <%-- 代替フロー：0～100のバリデーション [cite: 1980, 1985] --%>
                                        <c:if test="${not empty errors.get(student.no)}">
                                            <p class="text-danger small mb-0">0～100の範囲で入力してください</p>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="mt-4">
                        <%-- 登録して終了ボタン [cite: 1978, 1980] --%>
                        <button type="submit" class="btn btn-success">登録して終了</button>
                    </div>
                </form>
            </c:if>
            
            <%-- 検索したが学生が見つからない場合 [cite: 1985] --%>
            <c:if test="${not empty f1 && f1 != '0' && empty students}">
                <p>学生情報が存在しませんでした。</p>
            </c:if>
        </section>
    </c:param>
</c:import>