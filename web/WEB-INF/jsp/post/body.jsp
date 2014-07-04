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

<%-- $Id: body.jsp 27 2004-12-02 05:12:12Z anderson $ --%>


      <tr>
        <td class="<c:out value="${odd}"/> post-info">
	  <a name="<c:out value="${post.id}"/>"></a>
	  <div class="inner <c:out value="${post.createdBy.nickname}"/>-info">
	    <div class="post-id"><fmt:message key="post"/> #<c:if test="${preview && post.id == 0}">------</c:if><c:if test="${empty preview or post.id != 0}"><c:out value="${post.id}"/></c:if></div>
	    <div class="post-created-by"><fmt:message key="by"/> <jiwt:user user="${post.createdBy}"/></div>
	    <c:if test="${user.showPersonalPictures && (not (empty post.createdBy.personalPictureUrl))}">
	      <img src="<c:out value="${post.createdBy.personalPictureUrl}"/>" width="60" alt="<fmt:message key="picture.of"><fmt:param value="${post.createdBy.nickname}"/></fmt:message>">
	    </c:if>

	    <div class="post-last-updated"><fmt:formatDate type="both" dateStyle="short" timeStyle="medium" value="${post.lastUpdated}"/></div>
	    <c:if test="${!(empty post.editHistory)}">
	    <div class="post-created"><fmt:formatDate type="both" dateStyle="short" timeStyle="medium" value="${post.created}"/></div>
	    </c:if>

	    <c:if test="${empty preview}">
	    <div class="post-controls"><a href="<iwt:replyPost/>"><fmt:message key="reply"/></a><c:if test="${post.createdBy.nickname == user.nickname}"> <a href="<iwt:editPost/>"><fmt:message key="edit"/></a></c:if></div>
	    </c:if>
	  </div>
	</td>

        <td class="<c:out value="${odd}"/> post-contents">
	  <div class="<c:out value="${post.createdBy.nickname}"/>-contents" style="padding-left: <c:out value="${post.level - content.level}"/>em">
            <div class="post-subject">
	      <c:choose><c:when test="${postIsNew}">
		<img src="<c:url value='/images/lrpd.gif'/>" width="13" height="12" border="0" alt="<fmt:message key="new"/>"/>
	      </c:when><c:otherwise>
		<img src="<c:url value='/images/blank.gif'/>" width="13" height="12" border="0" alt=""/>
	      </c:otherwise></c:choose>
	      <c:out value="${post.subject}"/>
	    </div>
            <div class="post-content"><c:out value="${post.content}" escapeXml="false"/></div>
            <div class="post-signature"><c:out value="${post.signature}" escapeXml="false"/></div>

	    <c:if test="${!(empty post.editHistory)}">
              <%@ include file="editHistory.jsp" %>
	    </c:if>

	  </div>
	</td>
      </tr>
