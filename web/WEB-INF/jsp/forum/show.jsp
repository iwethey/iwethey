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

<%-- $Id: show.jsp 26 2004-06-25 03:12:19Z anderson $ --%>


<%@ include file="../header.jsp" %>

<%-- LAST READ: <fmt:formatDate type="both" dateStyle="medium" timeStyle="full" value="${lastRead}"/> <c:out value="${lastRead.time}"/><br> --%>
  <c:if test="${empty threads}">
    <span id="no-new"><fmt:message key="no.new.posts"/></span> <span id="make-one"><fmt:message key="make.one"/></span>
  </c:if>

  <%@ include file="../pager.jsp" %>

  <c:if test="${!(empty threads)}">
    <c:forEach items="${threads}" var="thread">
      <%@ include file="../post/thread.jsp" %>
    </c:forEach> <%-- threads --%>
  </c:if>

  <%@ include file="../pager.jsp" %>

<%@ include file="../footer.jsp" %>
