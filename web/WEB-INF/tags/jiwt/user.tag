<%@ include file="common.tagf" %>

<%@ attribute name="user" required="false" type="org.iwethey.forums.domain.User"%>

<a class="nochange username" href="<c:url value='/user/show.iwt?userid=${user.id}' />">${user.nickname}</a>
