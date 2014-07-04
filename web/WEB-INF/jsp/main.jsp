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

<%-- $Id: main.jsp 80 2005-11-29 20:30:10Z anderson $ --%>


<%@ include file="header.jsp" %>

<c:if test="${not empty boards}">
<h1 id="pageheading"><fmt:message key="board.list"/></h1>
<table id="boards" width="100%">
<c:forEach items="${boards}" var="b">
<tr>
	<td width="15%"><div class="boardname"><a href="<c:url value="/board/show.iwt?boardid=${b.id}"/>"><c:out value="${b.displayName}"/></a></div></td>
	<td width="50%"><div class="boarddesc"><c:out value="${b.description}"/></div></td>
	<td width="10%"><div class="post-count"><fmt:formatNumber value="${b.postCount}" pattern="#,##0"/></div></td>
	<td width="40%"><div class="last-post"><fmt:formatDate type="both" dateStyle="medium" timeStyle="full" value="${b.lastUpdated}"/></div></td>
</tr>
</c:forEach>
</table>
</c:if>

<%@ include file="footer.jsp" %>
