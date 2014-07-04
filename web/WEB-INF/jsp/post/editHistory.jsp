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

<%-- $Id: editHistory.jsp 27 2004-12-02 05:12:12Z anderson $ --%>

	    <div class="post-edit-history">
	      <iwt:postHistory>
	      <div class="edit-line">
	        <a href="<c:out value="expandHistory.iwt?postid=${post.id}&index=${histIndex}"/>">
	        <c:choose><c:when test="${expanded}">
		  <img class="expander" src="<c:url value="/images/minus.png"/>" alt="<fmt:message key="collapse.history"/>">
	        </c:when><c:otherwise>
		  <img class="expander" src="<c:url value="/images/plus.png"/>" alt="<fmt:message key="expand.history"/>">
	        </c:otherwise></c:choose>
	        </a>
	        
		<fmt:message key="edited.by"/> <span class="edit-user"><jiwt:user user="${hist.createdBy}"/></span>
		<span class="edit-time">
		  <fmt:formatDate type="both" dateStyle="short" timeStyle="medium" value="${hist.created}"/>
		</span>
              </div>
	      
	      <c:if test="${expanded}">
	      <div class="edit-expanded-line <c:out value="${even}"/>">
	        <div class="post-subject"><c:out value="${hist.subject}"/></div>
		<div class="post-content"><c:out value="${hist.content}" escapeXml="false"/></div>
		<div class="post-signature"><c:out value="${hist.signature}" escapeXml="false"/></div>
	      </div>
	      </c:if>

	      </iwt:postHistory>

	      <div class="history-control">
	      <c:if test="${someCollapsed}">
	        <a class="nochange" href="<c:out value="expandAllHistory.iwt?postid=${post.id}"/>"><fmt:message key="expand.all.history"/></a>
	        <c:if test="${someExpanded}"> | </c:if>
	      </c:if>
	      <c:if test="${someExpanded}">
	        <a class="nochange" href="<c:out value="collapseAllHistory.iwt?postid=${post.id}"/>"><fmt:message key="collapse.all.history"/></a>
	      </c:if>
	      </div>
	    </div>
