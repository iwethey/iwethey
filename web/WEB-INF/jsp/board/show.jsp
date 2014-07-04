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

  <c:if test="${empty categories}">
    <span id="no-new"><fmt:message key="no.new.forums"/></span> <span id="make-one"><fmt:message key="make.one"/></span>
  </c:if>

  <c:if test="${!(empty categories)}">
    <c:forEach items="${categories}" var="c" varStatus="cStatus">
      <c:if test="${cStatus.first}">
        <table class="forum-list">
          <tr>
            <th class="lrpd-col"></th>
            <th class="forum-name"><fmt:message key="forum"/></th>
            <th class="post-count"><fmt:message key="number.posts"/></th>
            <th class="last-post"><fmt:message key="last.post"/></th>
          </tr>
  
      </c:if> <%-- cStatus.first --%>
    
      <tr><td colspan="4">
        <div class="category"><c:out value="${c.displayName}"/></div>
      </td></tr>
  
      <c:forEach items="${c.forums}" var="f">
        <iwt:forumNew var="f">
  
          <tr>
            <td class="lrpd-col"><div class="lrpd-col">
              <c:if test="${forumIsNew}">
                <img src="<c:url value='/images/lrpd.gif'/>" alt="<fmt:message key="new"/>">
              </c:if>
            </div></td>
        
            <td class="forum-name">
              <div class="forum-name">
                <a href="<c:url value="/forum/show.iwt?forumid=${f.id}"/>"><c:out value="${f.displayName}"/></a>
				<c:if test="${user.showForumMiniControl}">
					<c:if test="${!user.anonymous}">
						<span class="add-topic mini-control"><a href="<iwt:newPost var="f"/>"><fmt:message key="add.topic"/></a></span>
						<span class="mark-read mini-control"><a href="<iwt:markRead var="f"/>"><fmt:message key="mark.read"/></a></span>
					</c:if>
				</c:if>
              </div>
              <div class="forum-desc"><c:out value="${f.description}"/></div>
            </td>
        
            <td><div class="post-count"><fmt:formatNumber value="${f.postCount}" pattern="#,##0"/></div></td>
        
            <td><div class="last-post">
              <fmt:formatDate type="both" dateStyle="medium" timeStyle="full" value="${f.lastUpdated}"/>
            </div></td>
          </tr>
   
       </iwt:forumNew>
  
      </c:forEach> <%-- c.forums --%>
    
    </c:forEach> <%-- categories --%>

    </table>
  </c:if>

<%@ include file="../footer.jsp" %>
