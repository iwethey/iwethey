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

<%-- $Id: thread.jsp 26 2004-06-25 03:12:19Z anderson $ --%>
    <div class="thread">
      <iwt:thread>

      <div class="thread-post-line ${post.createdBy.nickname}-post">
        <c:forEach var="i" begin="0" end="${(post.level - 1) * 3}">&nbsp;</c:forEach>

        <c:if test="${postIsNew}">
          <img src="<c:url value='/images/lrpd.gif'/>" width="13" height="12" border="0" alt="<fmt:message key="new"/>"/>
        </c:if>
        <c:if test="${not postIsNew}">
          <img src="<c:url value='/images/blank.gif'/>" width="13" height="12" border="0" alt=""/>
        </c:if>

        <a href="<c:url value='/post/show.iwt?postid=${post.id}'/>"><c:out value="${post.subject}"/></a>
	<c:if test="${empty post.content}"><span class="empty-post-marker">-NT</span></c:if>
         - (<a href="<c:url value='/user/show.iwt?userid=${post.createdBy.id}'/>"><c:out value="${post.createdBy.nickname}"/></a>)
        <c:if test="${post.replyCount > 0}">
	  <span class="thread-post-replies"> - (<c:out value="${post.replyCount}"/>)</span>
	</c:if>
        <span class="thread-post-date"> - <fmt:formatDate type="both" dateStyle="medium" timeStyle="full" value="${post.lastUpdated}"/><%-- <c:out value="${post.lastUpdated.time}"/> - <fmt:formatDate type="both" dateStyle="medium" timeStyle="full" value="${post.childrenLastUpdated}"/> <c:out value="${post.childrenLastUpdated.time}"/> --%></span>
	<c:if test="${(not user.anonymous) && user.showThreadReplyLinks}"><span class="thread-links">
	  [<a href="<iwt:replyPost/>"><fmt:message key="reply"/></a><c:if test="${post.createdBy.nickname == user.nickname}">|<a href="<iwt:editPost/>"><fmt:message key="edit"/></a></c:if>]
	</span></c:if>
       </div>

      </iwt:thread>
    </div>
