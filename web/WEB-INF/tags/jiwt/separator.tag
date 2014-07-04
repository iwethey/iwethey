<%@ include file="common.tagf" %>
<%@ attribute name="msg" required="false"%>
    <tr><td colspan="2"><div class="form-separator"><c:if test="${not empty msg}"><fmt:message key="${msg}"/></c:if></div></td></tr>
