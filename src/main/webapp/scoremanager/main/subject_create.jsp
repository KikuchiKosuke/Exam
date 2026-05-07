<%-- 科目登録JSP --%>
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
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報登録</h2>
            <form method="post" action="SubjectCreateExecute.action">
                <div class="mx-3 mb-3">
                    <div class="mb-3">
                        <label class="form-label" for="cd-input">科目コード</label>
                        <input class="form-control" type="text" id="cd-input" name="cd"
                            required placeholder="科目コードを入力してください"
                            value="${cd}" />
                        <div class="text-warning">${errors.get("cd")}</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="name-input">科目名</label>
                        <input class="form-control" type="text" id="name-input" name="name"
                            required placeholder="科目名を入力してください"
                            value="${name}" />
                        <div class="text-warning">${errors.get("name")}</div>
                    </div>
                    <button class="btn btn-primary" type="submit">登録</button>
                </div>
            </form>
            <div class="mx-3">
                <a href="SubjectList.action">戻る</a>
            </div>
        </section>
    </c:param>
</c:import>
