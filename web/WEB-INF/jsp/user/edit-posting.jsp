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

<%-- $Id: edit-posting.jsp 4 2004-04-20 19:58:10Z anderson $ --%>


<%@ include file="../header.jsp" %>

<form id="editform" action="edit.iwt" method="post">
  <input type="hidden" name="action" value="posting"/>

  <spring:hasBindErrors name="userInfo">
    <spring:bind path="userInfo">
        <c:forEach items="${status.errorMessages}" var="error">
	  <div class="error"><c:out value="${error}"/></div>
        </c:forEach>
    </spring:bind>
  </spring:hasBindErrors>

  <table class="form">
    <jiwt:separator msg="general.posting.controls"/>

    <jiwt:textarea msg="signature" field="userInfo.signature" req="${false}" />

    <jiwt:text msg="personal.picture.url" field="userInfo.personalPictureUrl" req="${false}" len="200"/>

    <jiwt:checkbox msg="preview.default" field="userInfo.previewByDefault"/>

    <jiwt:checkbox msg="autofill.subject" field="userInfo.autofillSubject"/>

    <jiwt:checkbox msg="convert.newlines" field="userInfo.convertNewlines"/>

    <jiwt:checkbox msg="convert.html" field="userInfo.convertHtml"/>

    <jiwt:checkbox msg="convert.links" field="userInfo.convertLinks"/>

    <jiwt:checkbox msg="convert.codes" field="userInfo.convertCodes"/>

  </table>

  <div class="submit">
    <input type="submit" name="SUBMIT" value="<fmt:message key="save"/>"/>
    <div class="note">
      <span class="required">&nbsp;&nbsp;&nbsp;&nbsp;</span> - <fmt:message key="required.field"/>
    </div>
  </div>

</form>

<%@ include file="../footer.jsp" %>
