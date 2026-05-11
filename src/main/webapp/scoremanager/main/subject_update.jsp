<%-- 科目変更JSP --%>
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
            <form method="post" action="SubjectUpdateExecute.action">
                <input type="hidden" name="cd" value="${subject.cd}" />
            ・    <div class="mx-3 mb-3">
                    <div class="mb-3">
                        <label class="form-label">科目コード</label>
                        <div>${subject.cd}</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="name-input">科目名</label>
                        <input class="form-control" type="text" id="name-input" name="name"
                            required placeholder="科目名を入力してください"
                            value="${subject.name}" />
                        <div class="text-warning">${errors.get("name")}</div>
                    </div>
                    <button class="btn btn-primary" type="submit">変更</button>
                </div>
            </form>
            <div class="mx-3">
                <a href="SubjectList.action">戻る</a>
            </div>
        </section>
    </c:param>
</c:import>
