<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Имя</th>
            <th>Телефон</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>

        <%--@elvariable id="resumes" type="java.util.List"--%>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td><%=ContactType.PHONE.toHtml(resume.getContact(ContactType.PHONE)) %>
                </td>
                <td><%=ContactType.EMAIL.toHtml(resume.getContact(ContactType.EMAIL)) %>
                </td>
                <td><a href="resume?uuid=${resume.uuid}&action=delete">Delete</a></td>
                <td><a href="resume?uuid=${resume.uuid}&action=edit">Edit</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<br/><input type="button" onclick="location.href='resume?action=new';" value="Create new"/>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>