<%@ include file="common.tagf" %>
<%@ attribute name="field" required="true"%>
<%@ attribute name="req" required="false" type="java.lang.Boolean"%>
<%@ attribute name="msg" required="true"%>
<%@ attribute name="type" required="false"%>
<%@ attribute name="cols" required="false"%>
<%@ attribute name="len" required="false"%>
  <spring:bind path="${field}">
    <tr>
      <td><div class="form-label <c:if test="${!req}">not</c:if>required<c:if test="${status.error}"> error</c:if>"><fmt:message key="${msg}"/></div></td>
      <td>
        <div class="form-field">
	<input type="<c:out value="${type}" default="text"/>" size="<c:out value="${cols}" default="40"/>" maxlength="<c:out value="${len}" default="40"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}" default=""/>" />
	<c:if test="${status.error}"><span class="error"><c:out value="${status.errorMessage}"/></span></c:if>
	</div>
      </td>
    </tr>
  </spring:bind>
