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

package org.iwethey.crypt.test;

import org.iwethey.crypt.*;

import java.util.*;

import junit.framework.*;
import junit.textui.TestRunner;

/** 
 * Tests the UnixCrypt class.
 * <p>
 * $Id: FilterTest.java 49 2004-12-06 02:15:40Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class UnixCryptTest extends TestCase
{
	public UnixCryptTest(String name)
		{
			super(name);
		}

    public static void main(String args[])
		{
			junit.textui.TestRunner.run(UnixCryptTest.class);
		}

	public void setUp()
		throws Exception
		{
		}

	public void testCrypt()
		{
			assertEquals("galV7hmYaQhCY", UnixCrypt.crypt("ga", "blancmange"));
		}
}
