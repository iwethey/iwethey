<%--
   Copyright 2004-2010 Scott Anderson and Mike Vitale

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
--%>

<%@ include file="common.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<fmt:setTimeZone value="${user.timezone}"/>

<html>

<head>
    <title>
	<c:choose>
	    <c:when test="${empty title}"><fmt:message key="title"/></c:when>
	    <c:otherwise>
		<fmt:message key="${title.label}">
		    <c:forEach items="${title.args}" var="p"><fmt:param value="${p}"/></c:forEach>
		</fmt:message>
	    </c:otherwise>
	</c:choose>
    </title>

	<c:if test="${admin}">
		<script type="text/javascript" src="http://extjs.cachefly.net/ext-3.2.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="http://extjs.cachefly.net/ext-3.2.1/ext-all.js"></script>
		<jiwt:javascript file="ext/ux/CheckColumn" />
		<link rel="stylesheet" type="text/css" href="http://extjs.cachefly.net/ext-3.2.1/resources/css/ext-all.css" />
	</c:if>

    <meta name="MSSmartTagsPreventParsing" content="TRUE">
    <link rel="icon" href="<c:url value='/images/lrpd.gif' />" type="image/gif">
    <link rel="SHORTCUT ICON" href="/favicon.ico">
    <link rel="stylesheet" href="<c:url value='/style.css'/>" type="text/css">
</head>

<body>

<c:url var="bugsUrl" value="/forum/show.iwt?forumid=3"/>

<div class="site-commands">
    <div class="site-command-box">
	<%--
	<a href="http://twiki.iwethey.org/twiki/bin/view/Main/">twikIWETHEY</a>
	| <a href="http://lists.warhead.org.uk/mailman/listinfo/iwe">Mailing List</a>
	| <a href="telnet://iwethey.org:3000">IWETHEY Interactive</a>

	<br />
	--%>

	<a href="<c:url value='/source/CHANGES'/>"><fmt:message key="version.label"/> <fmt:message key="version.no"/></a>
	<%--(<a href="source">Source</a>) - <fmt:message key="report.bugs"><fmt:param value="${bugsUrl}"/></fmt:message>--%>
	| <a href="<c:url value='/source/TODO' />"> TODO </a>
    </div>

    <%--
    <div class="site-command-box middle">
	<a class="nochange" href="<c:url value="/board/search.iwt?boardid=1"/>"><fmt:message key="search"/></a> |
	<a class="nochange" href="<c:url value="/content/random.iwt"/>"><fmt:message key="random"/></a> |
	<a href="/zlogs"><fmt:message key="usage"/></a>
    </div>
    --%>

    <div class="site-command-box">
	<c:out value="${userCount}"/> <fmt:message key="registered"/> |
	<c:url var="activeUrl" value="/user/active.iwt"/>
	<c:choose>
	    <c:when test="${activeSingle}">
		<fmt:message key="lph"><fmt:param value="${activeUserCount}"/><fmt:param value="${activeUrl}"/><fmt:param value="${lph}"/></fmt:message>
	    </c:when>
	    <c:otherwise>
		<fmt:message key="lphs"><fmt:param value="${activeUserCount}"/><fmt:param value="${activeUrl}"/><fmt:param value="${lph}"/></fmt:message>
	    </c:otherwise>
	</c:choose> |
	<a href="<c:url value="/user/statistics.iwt"/>"><fmt:message key="statistics"/></a>

	<br />

	<c:choose>
	    <c:when test="${user.anonymous}">
		<a href="<c:url value="/user/login.iwt"/>"><fmt:message key="login"/></a> |
		<a href="<c:url value="/user/new.iwt"/>"><fmt:message key="create.user"/></a>
	    </c:when>
	    <c:otherwise>
		<fmt:message key="logged.in"/>
		<jiwt:user user="${user}"/> |
		<a class="nochange" href="<c:url value="/user/edit.iwt"/>">
		    <fmt:message key="edit.preferences"/>
		</a> |
		<a class="nochange" href="<c:url value="/user/logout.iwt"/>">
		    <fmt:message key="logout"/>
		</a>
	    </c:otherwise>
	</c:choose>

	<c:if test="${user.admin}">
		<br />

		<a class="nochange" href="<c:url value="/admin/main.iwt"/>">
			<fmt:message key="admin"/>
		</a>
	</c:if>
    </div>
</div>

<div class="site-welcome">
    <c:if test="${(empty forum) || (empty forum.headerImage)}">
    <img src="<c:url value='/images/iwethey-lrpd-small.png'/>" alt="<fmt:message key="alt.banner"/>">
    </c:if>
    <c:if test="${!((empty forum) || (empty forum.headerImage))}">
    <img src="<c:url value='/images/${forum.headerImage}'/>" alt="<fmt:message key="alt.banner"/>">
    </c:if>
    <h2><fmt:message key="welcome"/></h2>
</div>

<c:if test="${!(empty notice)}"><div class="blockbox notice"><h1><fmt:message key="notice"/>:</h1><c:out value="${notice}"/></div></c:if>

<div class="blockbox navbar">
    <c:forEach items="${navigation}" var="n" varStatus="c">
	<c:if test="${!c.last}"><a href="<c:url value="${n.uri}"/>"></c:if><span class="barlink"><fmt:message key="${n.label}"><c:forEach items="${n.args}" var="p"><fmt:param value="${p}"/></c:forEach></fmt:message></span><c:if test="${!c.last}"></a><span class="divider"> / </span></c:if>
    </c:forEach>
</div>

<c:if test="${!(empty commands) && (user.forumControlLocation == 'top' || user.forumControlLocation == 'both')}">
    <div class="blockbox command-bar command-bar-top">
	<c:forEach items="${commands}" var="n" varStatus="c">
	    <c:if test="${!n.current}"><a class="nochange" href="<c:url value="${n.uri}"/>"></c:if><span class="barlink"><fmt:message key="${n.label}"><c:forEach items="${n.args}" var="p"><fmt:param value="${p}"/></c:forEach></fmt:message></span><c:if test="${!n.current}"></a></c:if>
	    <c:if test="${!c.last}"><span class="divider"> | </span></c:if>
	</c:forEach>
    </div>
</c:if>

<c:if test="${not (empty message)}">
    <div id="message"><h3><fmt:message key="${message}"/></h3></div>
</c:if>

<div id="window">
