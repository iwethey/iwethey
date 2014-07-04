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

package org.iwethey.forums.web.json;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class JsonSerializer
{
	private StringBuffer output = null;
	private Map elements = new HashMap();

	private static Logger logger = Logger.getLogger(JsonSerializer.class);

	public JsonSerializer()
	{
		this.elements.put(String.class, new StringSerializerElement());
		this.elements.put(Boolean.class, new BooleanSerializerElement());
		this.elements.put(Double.class, new NumberSerializerElement());
		this.elements.put(Integer.class, new NumberSerializerElement());
		this.elements.put(Long.class, new NumberSerializerElement());
		this.elements.put(Date.class, new DateSerializerElement());
		this.elements.put(Timestamp.class, new DateSerializerElement());
		this.elements.put(HashMap.class, new MapSerializerElement());
		this.elements.put(TreeMap.class, new MapSerializerElement());
		this.elements.put(ArrayList.class, new ListSerializerElement());
		this.elements.put(LinkedList.class, new ListSerializerElement());
		this.elements.put(HashSet.class, new ListSerializerElement());
		this.elements.put(org.hibernate.collection.PersistentSet.class, new ListSerializerElement());
		this.elements.put(org.hibernate.collection.PersistentSortedSet.class, new ListSerializerElement());
		//			this.elements.put(Class.forName("org.apache.catalina.util.ParameterMap"), new MapSerializerElement());

		String[] foo = {"one"};
		this.elements.put(foo.getClass(), new ObjectArraySerializerElement());

	}

	public void addSerializerElement(Class cl, JsonSerializerElement element)
	{
		this.elements.put(cl, element);
	}

	public String serialize(Object obj)
	{
		this.output = new StringBuffer();
		this.output.append("(");
		this.addSerialized(obj);
		this.output.append(")");
		return this.output.toString();
	}

	public JsonSerializer append(String str)
	{
		this.output.append(str == null ? "0" : str);
		return this;
	}

	public JsonSerializer append(Object obj)
	{
		append(obj == null ? "0" : obj.toString());
		return this;
	}

	public void addSerialized(Object obj)
	{
		if (obj != null)
		{
			Class cl = obj.getClass();
			JsonSerializerElement el = (JsonSerializerElement) this.elements.get(cl);
			if (el == null)
			{
				if (obj instanceof JsonSerializerElement)
				{
					el = (JsonSerializerElement) obj;
				}
				else
				{
					throw new IllegalArgumentException("Don't know how to serialize: " + cl.getName());
					//el = (JsonSerializerElement)this.elements.get(String.class);
					//obj = obj.toString();
				}
			}
			el.serialize(obj, this);
		}
		else
		{
			append("0");
		}
	}
}
