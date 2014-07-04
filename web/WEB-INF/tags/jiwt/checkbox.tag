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
	<input type="checkbox" value="true" <c:if test="${status.value}">checked="checked"</c:if> name="<c:out value="${status.expression}"/>" />
	<c:if test="${status.error}"><span class="error"><c:out value="${status.errorMessage}"/></span></c:if>
	</div>
      </td>
    </tr>
  </spring:bind>
