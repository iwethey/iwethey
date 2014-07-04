/**
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
*/

package org.iwethey.forums.web.user.test;

import org.iwethey.forums.db.*;
import org.iwethey.forums.db.test.*;
import org.iwethey.forums.domain.*;
import org.iwethey.forums.web.user.*;

import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.validation.*;

import java.util.*;

import junit.framework.TestCase;

/**
 * Tests the EditUserValidator object.
 * <p>
 * $Id: EditUserValidatorTest.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class EditUserValidatorTest extends TestCase
{
    private EditUserValidator mVal;

    public void setUp()
		throws Exception
		{
			mVal = new EditUserValidator();

			ApplicationContext mContext = new ClassPathXmlApplicationContext("test-context.xml");
			mVal.setUserManager((UserManager) mContext.getBean("userManager"));

			List l = new ArrayList();
   			l.add("nbsp");
			l.add("bullet");
			l.add("number");
			l.add("none");

			mVal.setAllowedIndentationFormats(l);

			l = new ArrayList();
			l.add("above");
			l.add("below");

			mVal.setAllowedHierarchyPositions(l);

			l = new ArrayList();
			l.add("samewindow");
			l.add("newwindow");
			l.add("asterisk");
			l.add("split");

			mVal.setAllowedLinkFormats(l);

			l = new ArrayList();
			l.add("inline");
			l.add("link");

			mVal.setAllowedImageFormats(l);

			l = new ArrayList();
			l.add("top");
			l.add("bottom");

			mVal.setAllowedForumControlLocations(l);
		}

	public void testSupports()
		{
			assertTrue(mVal.supports(User.class));
		}

	public void testValidateIndentationFormat()
		{
			User u = new User();
			BindException errors = new BindException(u, "editInfo");
			u.setNickname("ut_spork96");
			u.setUnencryptedPassword("itchy96");
			u.setPasswordCheck("itchy96");
			u.setIndentationFormat("bad");

			mVal.validate(u, errors);

			assertEquals("indentationFormat", 1, errors.getFieldErrorCount("indentationFormat"));
			FieldError err = errors.getFieldError("indentationFormat");
			assertEquals("indentation format", "error.indentation.format", err.getCode());

			errors = new BindException(u, "editInfo");
			u.setIndentationFormat("nbsp");
			mVal.validate(u, errors);

			assertEquals("indentationFormat good", 0, errors.getFieldErrorCount("indentationFormat"));
		}

	public void testValidateHierarchyPosition()
		{
			User u = new User();
			BindException errors = new BindException(u, "editInfo");
			u.setNickname("ut_spork96");
			u.setUnencryptedPassword("itchy96");
			u.setPasswordCheck("itchy96");
			u.setIndentationFormat("nbsp");
			u.setHierarchyPosition("bad");

			mVal.validate(u, errors);

			assertEquals("hierarchyPosition", 1, errors.getFieldErrorCount("hierarchyPosition"));
			FieldError err = errors.getFieldError("hierarchyPosition");
			assertEquals("hierarchy position", "error.hierarchy.position", err.getCode());

			errors = new BindException(u, "editInfo");
			u.setHierarchyPosition("above");
			mVal.validate(u, errors);

			assertEquals("hierarchyPosition good", 0, errors.getFieldErrorCount("hierarchyPosition"));
		}

	public void testValidateLinkFormat()
		{
			User u = new User();
			BindException errors = new BindException(u, "editInfo");
			u.setNickname("ut_spork96");
			u.setUnencryptedPassword("itchy96");
			u.setPasswordCheck("itchy96");
			u.setIndentationFormat("nbsp");
			u.setHierarchyPosition("above");
			u.setLinkFormat("bad");

			mVal.validate(u, errors);

			assertEquals("linkFormat", 1, errors.getFieldErrorCount("linkFormat"));
			FieldError err = errors.getFieldError("linkFormat");
			assertEquals("link format", "error.link.format", err.getCode());

			errors = new BindException(u, "editInfo");
			u.setLinkFormat("split");
			mVal.validate(u, errors);

			assertEquals("linkFormat good", 0, errors.getFieldErrorCount("linkFormat"));
		}

	public void testValidateImageFormat()
		{
			User u = new User();
			BindException errors = new BindException(u, "editInfo");
			u.setNickname("ut_spork96");
			u.setUnencryptedPassword("itchy96");
			u.setPasswordCheck("itchy96");
			u.setIndentationFormat("nbsp");
			u.setHierarchyPosition("above");
			u.setLinkFormat("inline");
			u.setImageFormat("bad");

			mVal.validate(u, errors);

			assertEquals("imageFormat", 1, errors.getFieldErrorCount("imageFormat"));
			FieldError err = errors.getFieldError("imageFormat");
			assertEquals("image format", "error.image.format", err.getCode());

			errors = new BindException(u, "editInfo");
			u.setImageFormat("inline");
			mVal.validate(u, errors);

			assertEquals("imageFormat good", 0, errors.getFieldErrorCount("imageFormat"));
		}

	public void testValidateForumControlLocation()
		{
			User u = new User();
			BindException errors = new BindException(u, "editInfo");
			u.setNickname("ut_spork96");
			u.setUnencryptedPassword("itchy96");
			u.setPasswordCheck("itchy96");
			u.setIndentationFormat("nbsp");
			u.setHierarchyPosition("above");
			u.setLinkFormat("split");
			u.setImageFormat("inline");
			u.setForumControlLocation("bad");

			mVal.validate(u, errors);

			assertEquals("forumControlLocation", 1, errors.getFieldErrorCount("forumControlLocation"));
			FieldError err = errors.getFieldError("forumControlLocation");
			assertEquals("forumControlLocation code", "error.forum.control.location", err.getCode());

			errors = new BindException(u, "editInfo");
			u.setForumControlLocation("top");
			mVal.validate(u, errors);

			assertEquals("forumControlLocation good", 0, errors.getFieldErrorCount("forumControlLocation"));
		}

	public void testValidateForumBatchSize()
		{
			User u = new User();
			BindException errors = new BindException(u, "editInfo");
			u.setNickname("ut_spork96");
			u.setUnencryptedPassword("itchy96");
			u.setPasswordCheck("itchy96");
			u.setIndentationFormat("nbsp");
			u.setHierarchyPosition("above");
			u.setLinkFormat("split");
			u.setImageFormat("inline");
			u.setForumControlLocation("inline");
			u.setForumBatchSize(0);

			mVal.validate(u, errors);

			assertEquals("forumBatchSize min", 1, errors.getFieldErrorCount("forumBatchSize"));
			FieldError err = errors.getFieldError("forumBatchSize");
			assertEquals("forum batch size min type", "error.forum.batch.size.min", err.getCode());

			errors = new BindException(u, "editInfo");
			u.setForumBatchSize(2000);
			mVal.validate(u, errors);

			assertEquals("forumBatchSize max", 1, errors.getFieldErrorCount("forumBatchSize"));
			err = errors.getFieldError("forumBatchSize");
			assertEquals("forum batch size max type", "error.forum.batch.size.max", err.getCode());

			errors = new BindException(u, "editInfo");
			u.setForumBatchSize(50);
			mVal.validate(u, errors);

			assertEquals("forumBatchSize good", 0, errors.getFieldErrorCount("forumBatchSize"));
		}
}
