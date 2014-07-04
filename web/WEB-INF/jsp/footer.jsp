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

<%-- $Id: footer.jsp 26 2004-06-25 03:12:19Z anderson $ --%>


</div>

<br />

<c:if test="${!(empty commands) && (user.forumControlLocation == 'bottom' || user.forumControlLocation == 'both')}">
  <div class="blockbox command-bar command-bar-bottom">
    <c:forEach items="${commands}" var="n" varStatus="c">
      <c:if test="${!n.current}"><a class="nochange" href="<c:url value="${n.uri}"/>"></c:if><span class="barlink"><fmt:message key="${n.label}"><c:forEach items="${n.args}" var="p"><fmt:param value="${p}"/></c:forEach></fmt:message></span><c:if test="${!n.current}"></a></c:if>
      <c:if test="${!c.last}"><span class="divider"> | </span></c:if>
    </c:forEach>
  </div>
</c:if>

<div class="iwethey-logo">
  <a href="http://www.iwethey.org"><span id="i">i</span><span id="we">we</span><span id="they">they</span><span id="org">.org</span></a>
</div>
<div class="lrpdism"><c:out value="${lrpd}" escapeXml="false"/>
<br><%= (new java.util.Date()).getTime() - ((Long)request.getAttribute("start")).longValue()%> ms</div>
</body>

</html>