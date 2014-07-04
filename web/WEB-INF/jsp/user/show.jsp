<%--
   Copyright 2004 Scott Anderson

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

<%-- $Id: show.jsp 4 2004-04-20 19:58:10Z anderson $ --%>


<%@ include file="../header.jsp" %>

<c:out value="${displayUser.nickname}"/><br>
<c:out value="${displayUser.properties.fullName}"/><br>

<iwt:forEach items="${posts}" var="post">
    <a href="<c:url value='/post/show.iwt?postid=${post.id}'/>"><c:out value="${post.subject}"/></a>

    <c:if test="${empty post.content}">
	<span class="empty-post-marker">-NT</span>
    </c:if>

    <c:if test="${post.replyCount > 0}">
	<span class="thread-post-replies"> - (<c:out value="${post.replyCount}"/>)</span>
    </c:if>

    <span class="thread-post-date"> - <fmt:formatDate type="both" dateStyle="medium" timeStyle="full" value="${post.lastUpdated}"/></span>
	
    <br /><br />
</iwt:forEach>

<%@ include file="../footer.jsp" %>
