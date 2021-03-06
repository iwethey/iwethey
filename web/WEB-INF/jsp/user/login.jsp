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

<%-- $Id: login.jsp 4 2004-04-20 19:58:10Z anderson $ --%>


<%@ include file="../header.jsp" %>

<form id="loginform" action="login.iwt" method="post">
  <spring:hasBindErrors name="userInfo">
    <spring:bind path="userInfo">
        <c:forEach items="${status.errorMessages}" var="error">
	  <div class="error"><c:out value="${error}"/></div>
        </c:forEach>
    </spring:bind>
  </spring:hasBindErrors>

  <table class="form">
    <jiwt:text msg="user.name" field="userInfo.nickname" req="${true}"/>
    <jiwt:text msg="password" field="userInfo.unencryptedPassword" req="${true}" type="password" />
  </table>

  <div class="submit">
    <input type="submit" name="SUBMIT" value="<fmt:message key="login"/>"/>
    <div class="note">
      <span class="required">&nbsp;&nbsp;&nbsp;&nbsp;</span> - <fmt:message key="required.field"/>
    </div>
  </div>

</form>

<%@ include file="../footer.jsp" %>
