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

  <c:if test='${user.hierarchyPosition == "before"}'>
    <div class="top-hierarchy">
    <%@ include file="thread.jsp" %>
    </div>
  </c:if>

  <div id="posts" class="${post.createdBy.nickname}">
    <table>
    <iwt:thread var="content" content="true">
      <%@ include file="body.jsp" %>
    </iwt:thread>
    </table>
  </div>

  <c:if test='${ user.hierarchyPosition == "after" }'>
    <div class="bottom-hierarchy">
    <%@ include file="thread.jsp" %>
    </div>
  </c:if>

<%@ include file="../footer.jsp" %>
