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

public class L33tMooHandler extends AbstractHandler
{
	public void process(FilterContext ctx, String text)
		{
			if (text.equals("moo"))
				{
					ctx.addResults("m00!!1!");
				}
			else
				{
					ctx.addResults(text);
				}
		}
}
