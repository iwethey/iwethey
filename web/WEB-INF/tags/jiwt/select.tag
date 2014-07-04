<%@ include file="common.tagf" %>
<%@ attribute name="field" required="true"%>
<%@ attribute name="req" required="false" type="java.lang.Boolean"%>
<%@ attribute name="msg" required="true"%>
<%@ attribute name="value" required="true" type="java.util.Collection" %>
  <spring:bind path="${field}">
    <tr>
      <td><div class="form-label <c:if test="${!req}">not</c:if>required<c:if test="${status.error}"> error</c:if>"><fmt:message key="${msg}"/></div></td>
      <td>
        <div class="form-field">

          <select size="1" name="<c:out value="${status.expression}"/>" >
            <c:forEach items="${value}" var="sel" varStatus="c">
              <option <c:if test="${status.value == sel.value}">selected="selected" </c:if>value="<c:out value="${sel.value}"/>"><c:if test="${sel.translate}"><fmt:message key="${sel.label}"/></c:if><c:if test="${not sel.translate}"><c:out value="${sel.label}"/></c:if></option>
	    </c:forEach>
          </select>

	<c:if test="${status.error}"><span class="error"><c:out value="${status.errorMessage}"/></span></c:if>
	</div>
      </td>
    </tr>
  </spring:bind>
