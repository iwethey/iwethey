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

<%-- $Id: pager.jsp 26 2004-06-25 03:12:19Z anderson $ --%>


  <c:if test="${pageCount > 1}">
    <div class="pager">
      <div class="pager-header">
        <fmt:message key="pager.viewing">
          <fmt:param value="${pageNumber}"/>
          <fmt:param value="${pageCount}"/>
          <fmt:param value="${itemCount}"/>
        </fmt:message>
      </div>
    
      <div class="pager-bar">
    
        <c:if test="${pageNumber > 1}">
          <span class="pager-prev">[<a href="<c:url value="/forum/show.iwt?forumid=${forum.id}&pageNumber=${pageNumber - 1}"/>"><fmt:message key="pager.prev"/></a>]</span>
        </c:if>
      
        <c:if test="${pageNumber != 1}"><a href="<c:url value='/forum/show.iwt?forumid=${forum.id}'/>">1</a></c:if>
        <c:if test="${pageNumber == 1}">1</c:if>
      
        <c:if test="${pageBarStart > 2}">...</c:if>
      
        <c:if test="${pageCount > 2}">
          <c:forEach begin="${pageBarStart}" end="${pageBarEnd}" var="pg">
            <c:if test="${pg != pageNumber}">
              <a href="<c:url value='/forum/show.iwt?forumid=${forum.id}&pageNumber=${pg}'/>"><c:out value="${pg}"/></a>
            </c:if>
            <c:if test="${pg == pageNumber}"><c:out value="${pg}"/></c:if>
          </c:forEach>
        </c:if>
      
        <c:if test="${pageBarEnd < pageCount-1}">...</c:if>
      
        <c:if test="${pageNumber < pageCount}"><a href="<c:url value='/forum/show.iwt?forumid=${forum.id}&pageNumber=${pageCount}'/>"><c:out value="${pageCount}"/></a></c:if>
        <c:if test="${pageNumber == pageCount}"><c:out value="${pageCount}"/></c:if>
      
        <c:if test="${pageNumber < pageCount}">
          <span class="pager-next">[<a href="<c:url value='/forum/show.iwt?forumid=${forum.id}&pageNumber=${pageNumber + 1}'/>"><fmt:message key="pager.next"/></a>]</span>
        </c:if>
      
      </div>
    </div>

  </c:if>
