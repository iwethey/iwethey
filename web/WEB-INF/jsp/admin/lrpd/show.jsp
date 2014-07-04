<%--
   Copyright 2004-2010 Scott Anderson and Mike Vitale

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

<%@ include file="../../common.jsp" %>

<c:set var="admin" value="true" />

<%@ include file="../../header.jsp" %>

<jiwt:javascript file="dynamic" />
<script type="text/javascript" language="javascript">dynamic.baseUri = "<c:url value='/'/>";</script>
<jiwt:javascript file="admin/editLrpds" />

<h1><fmt:message key="admin"/></h1>

<h1>EDIT EL ARRR PEE DEES</h1>

<div id="editor-grid"></div>

<%@ include file="../../footer.jsp" %>
