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

<%-- $Id: new.jsp 26 2004-06-25 03:12:19Z anderson $ --%>


<%@ include file="../header.jsp" %>

<!--
  <c:if test='${user.hierarchyPosition == "before"}'>
    <div class="top-hierarchy">
    <%@ include file="thread.jsp" %>
    </div>
  </c:if>
-->

  <form id="newpostform" action="new.iwt" method="post">
    <spring:hasBindErrors name="postInfo">
      <spring:bind path="postInfo">
          <c:forEach items="${status.errorMessages}" var="error">
  	  <div class="error"><c:out value="${error}"/></div>
          </c:forEach>
      </spring:bind>
    </spring:hasBindErrors>

    <%@ include file="postform.jsp" %>
    <input value="<c:out value="${forum.id}"/>" name="forumid" type="hidden"/>  
  
    <div class="submit">
      <input type="submit" name="SUBMIT" value="<fmt:message key="save"/>"/>
      <div class="note">
        <span class="required">&nbsp;&nbsp;&nbsp;&nbsp;</span> - <fmt:message key="required.field"/>
      </div>
    </div>
  </form>

<!--
  <c:if test='${ user.hierarchyPosition == "after" }'>
    <div class="bottom-hierarchy">
    <%@ include file="thread.jsp" %>
    </div>
  </c:if>
-->

<%@ include file="../footer.jsp" %>
