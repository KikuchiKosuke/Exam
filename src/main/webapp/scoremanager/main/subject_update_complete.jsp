<%-- 科目変更完了JSP --%>
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
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>
            <div class="mx-3">
                <div class="alert alert-success text-center">科目情報を変更しました。</div>
            		<a href="SubjectCreate.action" class="me-3 d-inline-block">戻る</a>
    				<a href="SubjectList.action" class="d-inline-block">科目一覧</a>
            </div>
        </section>
    </c:param>
</c:import>
