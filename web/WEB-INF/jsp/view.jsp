<%@ page import="com.urise.webapp.util.JsonParser" %>
<%@ page import="com.urise.webapp.model.*" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.function.Consumer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>

<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit">Edit</a>&nbsp<a
            href="resume?uuid=${resume.uuid}&action=delete">Delete</a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">

            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>

    <h3><%=sectionEntry.getKey().getTitle()%>
    </h3>

    <c:choose>
    <c:when test="${sectionEntry.key eq SectionType.OBJECTIVE || sectionEntry.key eq SectionType.PERSONAL}">
        <%=((TextSection) sectionEntry.getValue()).getText()%>
    </c:when>

    <c:when test="${sectionEntry.key eq SectionType.ACHIEVEMENT || sectionEntry.key eq SectionType.QUALIFICATION}">
        <%=String.join("<br/>", ((ListSection) sectionEntry.getValue()).getList()) %>
    </c:when>

    <c:when test="${sectionEntry.key eq SectionType.EDUCATION || sectionEntry.key eq SectionType.EXPERIENCE}">
    <c:forEach var="organisations"
               items="<%=((OrganisationSection) sectionEntry.getValue()).getOrganisationList()%>">
    <jsp:useBean id="organisations"
                 type="com.urise.webapp.model.Organisation"/>
        ${organisations.company}<br/>
    <%=organisations.getUrlOfHomepage()%><br/>
    <c:forEach var="positions" items="<%=organisations.getPosition()%>">
    <jsp:useBean id="positions"
                 type="com.urise.webapp.model.Organisation.Position"/>
        ${positions.startDate}
        ${positions.finishDate}
    <p> ${positions.title}
            ${positions.description}
        </c:forEach>
        </c:forEach>
        </c:when>
        </c:choose>
        </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
