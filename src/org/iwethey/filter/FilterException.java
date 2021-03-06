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
 * Contains a group of handlers to apply.
 * <p>
 * $Id: FilterException.java 41 2004-12-03 04:32:33Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class FilterException extends RuntimeException
{
	public FilterException(String message) { super(message); }
	public FilterException(String message, Throwable thr) { super(message, thr); }
} 
