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

public class NumberSerializerElement implements JsonSerializerElement
{
    public void serialize(Object object, JsonSerializer serializer)
    {
		if (object == null)
		{
			return;
		}

		Number num = (Number) object;

		serializer.append(num.toString());
    }
}
