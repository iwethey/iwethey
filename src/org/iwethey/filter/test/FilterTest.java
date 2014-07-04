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

import java.util.*;

import junit.framework.*;
import junit.textui.TestRunner;

/** 
 * Tests the Filter class.
 * <p>
 * $Id: FilterTest.java 73 2004-12-13 22:24:55Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class FilterTest extends TestCase
{
	private Filter mFilter = null;
	private Filter mFilterInsens = null;
	private Filter mMuffinFilter = null;

	public FilterTest(String name)
		{
			super(name);
		}

    public static void main(String args[])
		{
			junit.textui.TestRunner.run(FilterTest.class);
		}

	public void setUp()
		throws Exception
		{
			HashSet handlers = new HashSet();
			handlers.add(new TestSporkReverser());
			handlers.add(new TestFarbleReverser());
			handlers.add(new ConstantHandler("goomba"));
			handlers.add(new EscapeHandler('+'));
			handlers.add(new EscapeHandler());
			handlers.add(new EscapeHandler('\\'));
			handlers.add(new NewlineCounterHandler());
			handlers.add(new StackingMooHandler());

			mFilter = new Filter();
			mFilter.addHandlers(handlers, true);

			mFilterInsens = new Filter(handlers, false);

			mMuffinFilter = new Filter();
			mMuffinFilter.addHandler(new StackingMuffinHandler());
		}

	public void testFilterException()
		{
			FilterException fe1 = new FilterException("moo");
			FilterException fe2 = new FilterException("blancmange", fe1);

			assertEquals("moo", fe1.getMessage());
			assertEquals("blancmange", fe2.getMessage());
			assertEquals(fe1, fe2.getCause());
		}

	public void testHandlerStackEntry()
		{
			StackableHandler hand = new StackingMooHandler();
			Map map = new HashMap();
			HandlerStackEntry entry = new HandlerStackEntry(hand, 42);

			assertEquals(hand, entry.getHandler());
			assertNull(entry.getInterim());
			assertEquals(42, entry.getPosition());


			entry = new HandlerStackEntry(hand);

			assertEquals(hand, entry.getHandler());
			assertNull(entry.getInterim());
			assertEquals(0, entry.getPosition());


			entry = new HandlerStackEntry(hand, map);

			assertEquals(hand, entry.getHandler());
			assertEquals(map, entry.getInterim());
			assertEquals(0, entry.getPosition());


			entry = new HandlerStackEntry(hand, map, 42);

			assertEquals(hand, entry.getHandler());
			assertEquals(map, entry.getInterim());
			assertEquals(42, entry.getPosition());


			try
				{
					entry = new HandlerStackEntry(null);
					fail("Failed to detect null handler");
				}
			catch (FilterException fe) {}

			try
				{
					entry = new HandlerStackEntry(null, null);
					fail("Failed to detect null handler");
				}
			catch (FilterException fe) {}

			try
				{
					entry = new HandlerStackEntry(null, 0);
					fail("Failed to detect null handler");
				}
			catch (FilterException fe) {}

			try
				{
					entry = new HandlerStackEntry(null, null, 0);
					fail("Failed to detect null handler");
				}
			catch (FilterException fe) {}

		}

	public void testKeyedHandler()
		{
			KeyedHandler k = new TestFarbleReverser();

			assertEquals("farble", k.getKey());
			assertEquals("farble", k.getPattern());

			k.setPattern("moo");
			assertEquals("farble", k.getKey());
			assertEquals("moo", k.getPattern());

			k.setKey("zing");
			assertEquals("zing", k.getKey());
			assertEquals("moo", k.getPattern());
		}

	public void testNull()
		{
			FilterResults res = mFilter.filter(null);
			assertEquals("", res.getText());
		}

	public void testEmpty()
		{
			FilterResults res = mFilter.filter("");
			assertEquals("", res.getText());
		}

	public void testSimplePassthrough()
		{
			FilterResults res = mFilter.filter("This is a test of the emergency blancmange system.");
			assertEquals("This is a test of the emergency blancmange system.", res.getText());
		}

	public void testReplaceBeginning()
		{
			FilterResults res = mFilter.filter("farble this is a test of the emergency.");
			assertEquals("elbraf this is a test of the emergency.", res.getText());
		}

	public void testReplaceEnd()
		{
			FilterContext ctx = new FilterContext();
			FilterResults res = ctx.filter("This is a test of the emergency farble", mFilter);
			assertEquals("This is a test of the emergency elbraf", res.getText());
			assertEquals("farble", ctx.getMatched());
		}
	public void testReplaceAndSkip()
		{
			FilterContext ctx = new FilterContext();
			FilterResults res = ctx.filter("This is a test of the emergency spork system.", mFilter);
			assertEquals("This is a test of the emergency krops SYStem.", res.getText());
			assertEquals("tem.", ctx.getSkipped());
		}

	public void testReplaceAndSkipBeginning()
		{
			FilterResults res = mFilter.filter("spork system this is a test of the emergency.");
			assertEquals("krops SYStem this is a test of the emergency.", res.getText());
		}

	public void testReplaceAndSkipEnd()
		{
			FilterResults res = mFilter.filter("This is a test of the emergency spork sys");
			assertEquals("This is a test of the emergency krops SYS", res.getText());
		}

	public void testConstant()
		{
			FilterResults res = mFilter.filter("This is a test of the emergency goomba system.");
			assertEquals("This is a test of the emergency goomba system.", res.getText());
		}

	public void testEscapes()
		{
			FilterResults res = mFilter.filter("spork ah + ++ +- +& \\ \\\\ \\- \\& blancmange");
			assertEquals("krops AH  + - &  \\ - & blancmange", res.getText());
		}

	public void testInsensitive()
		{
			FilterResults res = mFilter.filter("moo");
			assertEquals("moo on you", res.getText());

			res = mFilter.filter("MOO");
			assertEquals("MOO on you", res.getText());

			res = mFilterInsens.filter("moo");
			assertEquals("moo on you", res.getText());

			res = mFilterInsens.filter("MOO");
			assertEquals("MOO", res.getText());
		}

	public void testNewlineCount()
		{
			FilterContext ctx = new FilterContext();
			ctx.filter("one\ntwo\nthree\nfour\n", mFilter);
			// Count starts at 1, increments for each newline found.
			assertEquals(5, ctx.getLine());
			ctx.incrementLine(2);
			assertEquals(7, ctx.getLine());
		}

	public void testStackingHandler()
		{
			FilterContext ctx = new FilterContext();
			FilterResults res = ctx.filter("moo zoo poo", mFilter);

			assertEquals("moo on you zoo poo", res.getText());
			try
				{
					ctx.peekHandler();
					fail("No filter exception generated for empty stack.");
				}
			catch (FilterException fe) {}

			try
				{
					ctx.popHandler();
					fail("No filter exception generated for empty stack.");
				}
			catch (FilterException fe) {}
		}

	public void testFilterStacking()
		{
			FilterContext ctx = new FilterContext();

			try
				{
					ctx.popFilter();
					fail("No filter exception generated for empty stack.");
				}
			catch (FilterException fe) {}

			ctx = new FilterContext("moo zoo poo", mFilter);
			assertEquals(mFilter, ctx.peekFilter());

			try
				{
					ctx.popFilter();
					fail("No filter exception generated for empty stack.");
				}
			catch (FilterException fe) {}

			ctx = new FilterContext("blancmange muffin muffin blancmange blancmange", mMuffinFilter);
			FilterResults res = ctx.filter();

			assertEquals("blancmange muffins are cosmic! muffin blancmanges are on the Hertzprung-Russel chart! Next to 'M'! blancmange", res.getText());

		}

	public void testBrokenFilterStacking()
		{
			FilterContext ctx = new FilterContext();

			Filter muffin = new Filter();
			muffin.addHandler(new BrokenStackingMuffinHandler());

			ctx = new FilterContext("blancmange muffin muffin blancmange blancmange", muffin);
			try
				{
					FilterResults res = ctx.filter();
					fail("Sabotage incomplete; the filter wasn't wrong.");
				}
			catch (FilterException fe) {}
		}

	public void testBrokenHandlerStacking()
		{
			FilterContext ctx = new FilterContext();

			Filter muffin = new Filter();
			StackableHandler throwaway = new StackingMuffinHandler();
			PoppingBlancmangeHandler popcorn = new PoppingBlancmangeHandler(muffin, throwaway);

			assertEquals("muffin", popcorn.getMatchingKey());

			muffin.addHandler(popcorn);

			ctx = new FilterContext("blancmange muffin muffin blancmange blancmange", muffin);

			// Ensure that the popping handler won't match when it pops.
			StackableHandler baddie = new StackingMuffinHandler();
			baddie.setKey("moo");
			ctx.pushHandler(baddie);

			try
				{
					FilterResults res = ctx.filter();
					fail("Sabotage incomplete; the handler wasn't wrong.");
				}
			catch (FilterException fe) {}
		}

	public void testBrokenInterimResults()
		{
			FilterContext ctx = new FilterContext();

			Filter muffin = new Filter();
			StackableHandler throwaway = new StackingMuffinHandler("doberman");
			PoppingBlancmangeHandler popcorn = new PoppingBlancmangeHandler(muffin, throwaway);

			muffin.addHandler(popcorn);

			ctx = new FilterContext("blancmange muffin muffin blancmange blancmange", muffin);

			try
				{
					FilterResults res = ctx.filter();
					fail("Sabotage incomplete; the handler wasn't a doberman.");
				}
			catch (FilterException fe) {}
		}

	public void testNonProcessingHandler()
		{
			Filter filter = new Filter();

			KeyedHandler moo = new FilteringHandler("moo[a-z ]+");
			filter.addHandler(moo);
			filter.addHandler(new TestFarbleReverser());
			
			// Mainly for coverage
			moo.process(null, null);

			FilterResults res = filter.filter("moo in da farble!");

			// Since the moo filters everything to the !, the farble handler will never be called.
			assertEquals("moo in da farble!", res.getText());
		}

	public void testAbort()
		{
			Filter filter = new Filter();
			filter.addHandler(new AbortingMooHandler());
			filter.setPlaintextHandler(new L33tMooHandler());

			FilterResults res = filter.filter("mooZUM in da mooBAM!");

			assertEquals("mooZAM in da m00!!1!BAM!", res.getText());
		}
}
