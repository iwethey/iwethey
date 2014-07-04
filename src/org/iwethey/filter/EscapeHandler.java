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

package org.iwethey.filter;

/**
 * Single character escape mechanism.
 * <p>
 * $Id: EscapeHandler.java 45 2004-12-04 23:15:56Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class EscapeHandler extends AbstractKeyedHandler
{
	public EscapeHandler(char escape)
		{
			setKey(new String(new char[] { escape }));
		}

	public EscapeHandler()
		{
			this('\\');
		}

	public void process(FilterContext ctx, String text)
		{
			ctx.skip(1);
			ctx.addResults(internalProcess(ctx.getRemaining().substring(0, 1)));
		}

	protected String internalProcess(String escape)
		{
			return escape;
		}
} 
