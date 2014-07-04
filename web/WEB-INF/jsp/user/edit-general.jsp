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

<%-- $Id: edit-general.jsp 4 2004-04-20 19:58:10Z anderson $ --%>


<%@ include file="../header.jsp" %>

<form id="editform" action="edit.iwt" method="post">
  <input type="hidden" name="action" value="general"/>

  <spring:hasBindErrors name="userInfo">
    <spring:bind path="userInfo">
        <c:forEach items="${status.errorMessages}" var="error">
	  <div class="error"><c:out value="${error}"/></div>
        </c:forEach>
    </spring:bind>
  </spring:hasBindErrors>

  <table class="form">
    <jiwt:separator msg="general.controls" />

    <jiwt:text msg="full.name" field="userInfo.fullName"/>

    <jiwt:text msg="email.address" field="userInfo.email"/>

    <jiwt:checkbox msg="show.in.actives" field="userInfo.showInActives"/>

    <jiwt:separator msg="password.again.required"/>

    <jiwt:text msg="password" field="userInfo.unencryptedPassword" type="password"/>

    <jiwt:text msg="password.again" field="userInfo.passwordCheck" type="password" req="${true}"/>

  </table>

  <div class="submit">
    <input type="submit" name="SUBMIT" value="<fmt:message key="save"/>"/>
    <div class="note">
      <span class="required">&nbsp;&nbsp;&nbsp;&nbsp;</span> - <fmt:message key="required.field"/>
    </div>
  </div>

</form>

<%@ include file="../footer.jsp" %>
