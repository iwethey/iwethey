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

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;

import org.codehaus.jackson.impl.DefaultPrettyPrinter;

import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

import org.hibernate.proxy.CGLIBLazyInitializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.util.Map;

public class JacksonSerializer
{
	private CustomSerializerFactory serializerFactory = null;
	private JsonFactory factory = null;

	public JacksonSerializer()
	{
		this.serializerFactory = new CustomSerializerFactory();
		this.serializerFactory.addGenericMapping(CGLIBLazyInitializer.class, new CGLIBLazyInitializerHandler());

		this.factory = new MappingJsonFactory(new ObjectMapper(this.serializerFactory));
		this.factory.setGeneratorFeature(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
	}

	public void addSerializer(Class c, JsonSerializer ser)
	{
		this.serializerFactory.addGenericMapping(c, ser);
	}

	public void addSerializers(Map<Class, JsonSerializer> extra)
	{
		if (extra == null) return;

		for (Class c : extra.keySet())
		{
			addSerializer(c, extra.get(c));
		}
	}

	public void serialize(Object obj, OutputStream out)
	{
		this.serialize(obj, new OutputStreamWriter(out));
	}

	public void serialize(Object obj, Writer out)
	{
		try
		{
			JsonGenerator gen = this.factory.createJsonGenerator(out);
			gen.setPrettyPrinter(new DefaultPrettyPrinter());
			gen.writeObject(obj);
			gen.flush();
			out.flush();
		}
		catch (IOException e)
		{
			throw new JsonException(e);
		}
	}

	public String serializeToString(Object obj)
	{
		StringWriter out = new StringWriter();
		serialize(obj, out);
		return out.toString();
	}

	public void serializeFragment(Object obj, OutputStream out)
	{
		this.serializeFragment(obj, new OutputStreamWriter(out));
	}

	public void serializeFragment(Object obj, Writer out)
	{
		try
		{
			JsonGenerator gen = this.factory.createJsonGenerator(out);
			gen.writeObject(obj);
			gen.flush();
		}
		catch (IOException e)
		{
			throw new JsonException(e);
		}
	}

	public String serializeToFragment(Object obj)
	{
		StringWriter out = new StringWriter();
		serializeFragment(obj, out);
		return out.toString();
	}

	/* Fixes problems with Jackson trying to serialize Hibernate lazy-load objects. */
	private class CGLIBLazyInitializerHandler extends JsonSerializer<CGLIBLazyInitializer>
	{
		public CGLIBLazyInitializerHandler() {}

		public void serialize(CGLIBLazyInitializer value, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException
		{
			//			gen.writeObject(value.getImplementation());
			gen.writeObject(null);
		}

	}

}
