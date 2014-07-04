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

<%-- $Id: edit-display.jsp 17 2004-04-28 22:13:29Z anderson $ --%>


<%@ include file="../header.jsp" %>

<form id="editform" action="edit.iwt" method="post">
  <input type="hidden" name="action" value="display"/>

  <spring:hasBindErrors name="userInfo">
    <spring:bind path="userInfo">
        <c:forEach items="${status.errorMessages}" var="error">
	  <div class="error"><c:out value="${error}"/></div>
        </c:forEach>
    </spring:bind>
  </spring:hasBindErrors>

  <table class="form">
    <jiwt:separator msg="general.display.controls"/>

    <jiwt:checkbox msg="use.css" field="userInfo.useCss"/>

    <jiwt:select msg="time.zone" field="userInfo.timezone" value="${allowedTimeZones}" />

    <jiwt:checkbox msg="show.mark.all.read" field="userInfo.showMarkAllRead"/>

    <jiwt:checkbox msg="invert.lpms" field="userInfo.invertLpms"/>


    <jiwt:separator msg="board.display.controls"/>
    <jiwt:select msg="default.board" field="userInfo.defaultBoard" value="${boards}"/>

    <jiwt:checkbox msg="show.new.forums.only" field="userInfo.showNewForumsOnly"/>

    <jiwt:checkbox msg="show.forum.mini" field="userInfo.showForumMiniControl"/>


    <jiwt:separator msg="forum.display.controls"/>

    <jiwt:checkbox msg="show.new.posts.only" field="userInfo.showNewPostsOnly"/>

    <jiwt:checkbox msg="show.threads.collapsed" field="userInfo.showThreadsCollapsed"/>

    <jiwt:checkbox msg="sort.last.modified" field="userInfo.sortByLastModified"/>

    <jiwt:checkbox msg="show.post.ids" field="userInfo.showPostIds"/>

    <jiwt:checkbox msg="show.thread.reply.links" field="userInfo.showThreadReplyLinks"/>

    <jiwt:select msg="thread.indentation" field="userInfo.indentationFormat" value="${allowedIndentationFormats}" />

    <jiwt:select msg="link.format" field="userInfo.linkFormat" value="${allowedLinkFormats}" />

    <jiwt:select msg="image.format" field="userInfo.imageFormat" value="${allowedImageFormats}" />

    <jiwt:select msg="forum.control" field="userInfo.forumControlLocation" value="${allowedForumControlLocations}" />

    <jiwt:text msg="threads.per.page" field="userInfo.forumBatchSize"/>


    <jiwt:separator msg="content.display.controls" />

    <jiwt:select msg="hierarchy.position" field="userInfo.hierarchyPosition" value="${allowedHierarchyPositions}" />

    <jiwt:checkbox msg="show.personal.pictures" field="userInfo.showPersonalPictures"/>

    <jiwt:checkbox msg="show.jump.tags" field="userInfo.showJumpTags"/>

  </table>

  <div class="submit">
    <input type="submit" name="SUBMIT" value="<fmt:message key="save"/>"/>
    <div class="note">
      <span class="required">&nbsp;&nbsp;&nbsp;&nbsp;</span> - <fmt:message key="required.field"/>
    </div>
  </div>

</form>

<%@ include file="../footer.jsp" %>
