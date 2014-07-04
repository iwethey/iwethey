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

import org.iwethey.forums.domain.Quote;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class TerseSerializer extends JacksonSerializer
{
	public TerseSerializer()
	{
		addSerializer(java.util.Date.class, new DateSerializer());

		addSerializer(Quote.class, new QuoteSerializer());
	}

	private class QuoteSerializer extends JsonSerializer<Quote>
	{
		public void serialize(Quote value, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException
		{
			gen.writeStartObject();
			gen.writeNumberField("id", value.getId());
			gen.writeObjectField("quote", value.getQuote());
			gen.writeObjectField("approved", value.isApproved());
			gen.writeEndObject();
		}
	}
}
