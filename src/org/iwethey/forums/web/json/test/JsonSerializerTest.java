/**
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
*/

package org.iwethey.forums.web.json.test;

import org.iwethey.forums.web.json.*;

import java.util.*;

import junit.framework.TestCase;

/**
 * <p>
 * $Id: JsonSerializerTest.java 9 2005-09-06 03:21:50Z  $
 * <p>
 * @author Scott Anderson (<a href="mailto:scottanderson@comcast.net">scottanderson@comcast.net</a>)
 */
public class JsonSerializerTest extends TestCase
{
	public void testSimpleSerialize()
		throws Exception
	{
		JsonSerializer ser = new JsonSerializer();

		assertEquals("(\"foodle\")", ser.serialize("foodle"));
		assertEquals("(\"\\\"foodle\\\"\")", ser.serialize("\"foodle\""));
		assertEquals("(42)", ser.serialize(new Long(42)));
		assertEquals("(42)", ser.serialize(new Integer(42)));
		assertEquals("(42.42)", ser.serialize(new Double(42.42)));

	}

	public void testListSerialize()
		throws Exception
	{
		JsonSerializer ser = new JsonSerializer();

		ArrayList list = new ArrayList();
		list.add("foodle");
		list.add("\"foodle\"");
		list.add(new Long(42));
		list.add(new Integer(42));
		list.add(new Double(42.42));

		ArrayList moo = new ArrayList();
		moo.add(new Integer(1));
		moo.add(new Integer(2));
		moo.add(new Integer(3));

		list.add(moo);

		String str = ser.serialize(list);
		assertEquals("([\"foodle\",\"\\\"foodle\\\"\",42,42,42.42,[1,2,3]])", str);
	}

	public void testMapSerialize()
		throws Exception
	{
		JsonSerializer ser = new JsonSerializer();

		HashMap map = new HashMap();

		HashMap foo = new HashMap();
		foo.put("one", new Integer(1));
		foo.put("two", new Integer(2));
		foo.put("three", new Integer(3));

		map.put("quark", foo);
		map.put("spork", "\"foodle\"");
		map.put("fingle", new Long(42));
		map.put("dangle", new Integer(42));
		map.put("rhubarb", new Double(42.42));

		String str = ser.serialize(map);
		assertEquals("({\"dangle\":42,\"rhubarb\":42.42,\"spork\":\"\\\"foodle\\\"\",\"fingle\":42,\"quark\":{\"two\":2,\"one\":1,\"three\":3}})", str);
	}
}
