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

<%-- $Id: active.jsp 26 2004-06-25 03:12:19Z anderson $ --%>


<%@ include file="../header.jsp" %>

<p><fmt:message key="active.users"/>:</p>
<table>
    <c:forEach items="${userList}" var="u">
	<tr>
	    <td>
		<c:if test="${u.nickname != 'UQC'}"><a href="<c:url value="/user/show.iwt?userid=${u.id}"/>"></c:if><c:out value="${u.nickname}"/><c:if test="${u.nickname != 'UQC'}"></a></c:if>
	    </td>
	    <td><fmt:formatDate type="both" dateStyle="short" timeStyle="medium" value="${u.lastPresent}"/></td>
	</tr>
    </c:forEach>
</table>

<%@ include file="../footer.jsp" %>
