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

<%-- $Id: postform.jsp 27 2004-12-02 05:12:12Z anderson $ --%>


    <c:if test="${preview}">
    <div id="posts" class="${postInfo.createdBy.nickname}">
      <table>
        <iwt:thread var="postInfo" content="true">
          <%@ include file="body.jsp" %>
        </iwt:thread>
      </table>
	<div class="submit">
	  <input type="submit" name="SUBMIT" value="<fmt:message key='save.without.preview'/>"/>
	</div>
    </div>
    </c:if>
  
    <table class="form">
      <jiwt:text msg="subject" field="postInfo.subject" cols="85" len="${subjectLength}" req="${true}" />
    
      <jiwt:textarea msg="content" field="postInfo.originalContent" rows="12" cols="85" req="${false}" />
    
      <jiwt:textarea msg="signature" field="postInfo.originalSignature" rows="5" cols="85" req="${false}" />
  
      <jiwt:separator msg="posting.options" />

      <jiwt:checkbox msg="posting.convert.newlines" field="postInfo.convertNewlines" />

      <jiwt:checkbox msg="posting.convert.html" field="postInfo.convertHtml" />

      <jiwt:checkbox msg="posting.convert.links" field="postInfo.convertLinks" />

      <jiwt:checkbox msg="posting.convert.codes" field="postInfo.convertCodes" />

      <jiwt:separator/>

      <tr><td><div class="form-label notrequired"><fmt:message key="preview"/></div></td>
          <td><div class="form-field"><input type="checkbox" value="true" <c:if test="${user.previewByDefault}">checked="checked"</c:if> name="preview" /></div></td></tr>
  
    </table>


