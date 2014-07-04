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

package org.iwethey.filter.test;

import org.iwethey.filter.*;

public class TestSporkReverser extends AbstractKeyedHandler
{
	public TestSporkReverser()
		{
			setKey("spork");
		}

	public void process(FilterContext ctx, String text)
		{
			ctx.addResults( (new StringBuffer(text)).reverse().toString() );

			String foo = ctx.getRemaining().substring(0, 4);

			ctx.addResults( foo.toUpperCase() );
			ctx.skip(4);
		}
}
