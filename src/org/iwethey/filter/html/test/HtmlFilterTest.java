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

package org.iwethey.filter.html.test;

import org.iwethey.filter.*;
import org.iwethey.filter.html.*;

import java.util.*;

import junit.framework.*;

/** 
 * Tests the Filter class.
 * <p>
 * $Id: HtmlFilterTest.java 74 2004-12-14 04:34:10Z anderson $
 * <p>
 * @author Scott Anderson (<a href="mailto:scott@iwethey.org">scott@iwethey.org</a>)
 */
public class HtmlFilterTest extends TestCase
{
	private Filter mHandlers = null;

    public static void main(String args[])
		{
			junit.textui.TestRunner.run(HtmlFilterTest.class);
		}

	public HtmlFilterTest(String name)
		{
			super(name);
		}

	public void setUp()
		throws Exception
		{
			HashSet handlers = new HashSet();
			handlers.add(new NewlineHandler());
			handlers.add(new HtmlEscapeHandler());

			mHandlers = new Filter(handlers);

			mHandlers.setPlaintextHandler(new PassthroughEscapeHandler());
		}

	public void testNewlines()
		{
			FilterResults res = mHandlers.filter("This is a test of the\nemergency goomba system.\n");
			assertEquals("This is a test of the<br>emergency goomba system.<br>", res.getText());
		}

	public void testEscapes()
		{
			FilterResults res = mHandlers.filter("\\& \\<foo> http\\://foo \\\\");
			assertEquals("&amp; &lt;foo&gt; http://foo \\", res.getText());
		}

	public void testAmpersands()
		{
			Filter filter = new Filter();
			filter.addHandler(new AmpersandEscapeHandler());
			filter.setPlaintextHandler(new PassthroughEscapeHandler());

			FilterResults res = filter.filter("&#1; &#12; &#123; &#1234; &amp; &amp no; &;");

			assertEquals("&#1; &#12; &#123; &amp;#1234; &amp; &amp;amp no; &amp;;", res.getText());

			filter = new Filter(false);
			filter.addHandler(new AmpersandEscapeHandler(false));
			filter.setPlaintextHandler(new PassthroughEscapeHandler());

			res = filter.filter("&#1; &#12; &#123; &#1234; &AMP; &amp; &amp no; &;");

			assertEquals("&#1; &#12; &#123; &amp;#1234; &AMP; &amp; &amp;amp no; &amp;;", res.getText());
		}

	public void testSimpleConstant()
		{
			Filter filter = new Filter();
			filter.addHandler(new SimpleConstantTagHandler("b"));
			filter.addHandler(new SimpleConstantTagCloseHandler("b"));
			filter.setPlaintextHandler(new PassthroughEscapeHandler());

			FilterResults res = filter.filter("<b>woot, muffins!</b>");

			assertEquals("<b>woot, muffins!</b>", res.getText());

			try
				{
					res = filter.filter("<b>woot, muffins!</b>ARGH! A snake!</b>");
					fail("Didn't catch mismatched tags.");
				}
			catch (FilterException fe) {}

			filter = new Filter();
			filter.addHandler(new SimpleConstantTagHandler("a", filter));
			filter.addHandler(new SimpleConstantTagHandler("b", filter));
			filter.setPlaintextHandler(new PassthroughEscapeHandler());

			res = filter.filter("<a>woot, <b>muffins</b>!</a>");

			assertEquals("<a>woot, <b>muffins</b>!</a>", res.getText());

			try
				{
					res = filter.filter("<a><b>woot, muffins!</a>ARGH! A snake!</b>");
					fail("Didn't catch mismatched tags.");
				}
			catch (FilterException fe) {}
		}

	public void testCommonConstant()
		{
			Object coverage = new HtmlHelper();

			Filter filter = new Filter();
			filter.setPlaintextHandler(new PassthroughEscapeHandler());

			HtmlHelper.addCommonInlineConstantTags(filter);
			HtmlHelper.addCommonBlockConstantTags(filter);

			FilterResults res = filter.filter("<b><big><code><em><i><small>MOO</small></i></em></code></big></b>");
			assertEquals("<b><big><code><em><i><small>MOO</small></i></em></code></big></b>", res.getText());

			res = filter.filter("<strike><strong><sub><sup><tt><u>MOO</u></tt></sup></sub></strong></strike>");
			assertEquals("<strike><strong><sub><sup><tt><u>MOO</u></tt></sup></sub></strong></strike>", res.getText());


			res = filter.filter("<center><dd><dl><dt><h1><h2>MOO</h2></h1></dt></dl></dd></center>");
			assertEquals("<center><dd><dl><dt><h1><h2>MOO</h2></h1></dt></dl></dd></center>", res.getText());

			res = filter.filter("<h3><h4><h5><h6><li><ol><pre><ul>MOO</ul></pre></ol></li></h6></h5></h4></h3>");
			assertEquals("<h3><h4><h5><h6><li><ol><pre><ul>MOO</ul></pre></ol></li></h6></h5></h4></h3>", res.getText());


			res = filter.filter("<br> <br/> <hr> <hr/> &amp;");
			assertEquals("<br> <br/> <hr> <hr/> &amp;", res.getText());
		}

	public void testRawUrl()
		{
			Filter filter = new Filter();
			filter.setPlaintextHandler(new PassthroughEscapeHandler());
			filter.addHandler(new RawUrlHandler("http"));

			FilterResults res = null;

			res = filter.filter("moo http:");
			assertEquals("moo http:", res.getText());

			filter = new Filter();
			filter.setPlaintextHandler(new PassthroughEscapeHandler());
			filter.addHandler(new RawUrlHandler("http:"));

			res = filter.filter("moo http:");
			assertEquals("moo http:", res.getText());

			res = filter.filter("moo http: spork");
			assertEquals("moo http: spork", res.getText());

			res = filter.filter("moo http:/ spork");
			assertEquals("moo http:/ spork", res.getText());

			res = filter.filter("moo http://www.silly-walks.com spork");
			assertEquals("moo <a href=\"http://www.silly-walks.com\">http://www.silly-walks.com</a> spork", res.getText());

			res = filter.filter("moo http://www.silly-walks.com");
			assertEquals("moo <a href=\"http://www.silly-walks.com\">http://www.silly-walks.com</a>", res.getText());

			res = filter.filter("moo http://www.silly-walks.com/ spork");
			assertEquals("moo <a href=\"http://www.silly-walks.com/\">http://www.silly-walks.com/</a> spork", res.getText());

			res = filter.filter("moo http://www.silly-walks.com/");
			assertEquals("moo <a href=\"http://www.silly-walks.com/\">http://www.silly-walks.com/</a>", res.getText());

			res = filter.filter("moo http://www.silly-walks.com. spork");
			assertEquals("moo <a href=\"http://www.silly-walks.com\">http://www.silly-walks.com</a>. spork", res.getText());

			res = filter.filter("moo http://www.silly-walks.com.");
			assertEquals("moo <a href=\"http://www.silly-walks.com\">http://www.silly-walks.com</a>.", res.getText());

			res = filter.filter("moo http://www.silly-walks.com/. spork");
			assertEquals("moo <a href=\"http://www.silly-walks.com/\">http://www.silly-walks.com/</a>. spork", res.getText());

			res = filter.filter("moo http://www.silly-walks.com/.");
			assertEquals("moo <a href=\"http://www.silly-walks.com/\">http://www.silly-walks.com/</a>.", res.getText());

			res = filter.filter("[moo http://www.silly-walks.com] spork");
			assertEquals("[moo <a href=\"http://www.silly-walks.com\">http://www.silly-walks.com</a>] spork", res.getText());

			res = filter.filter("moo http://www.silly-walks.com/zimby?fargle=blancmange!");
			assertEquals("moo <a href=\"http://www.silly-walks.com/zimby?fargle=blancmange\">http://www.silly-wal...fargle=blancmange</a>!", res.getText());

			res = filter.filter("moo http://www.foo.com/zam! spork");
			assertEquals("moo <a href=\"http://www.foo.com/zam\">http://www.foo.com/zam</a>! spork", res.getText());

			res = filter.filter("http://www.foo.com/zam");
			assertEquals("<a href=\"http://www.foo.com/zam\">http://www.foo.com/zam</a>", res.getText());

			res = filter.filter("moo http://www.foo.com/zam/and/company/with/bells/on! spork");
			assertEquals("moo <a href=\"http://www.foo.com/zam/and/company/with/bells/on\">http://www.foo.com/z...any/with/bells/on</a>! spork", res.getText());

			res = filter.filter("http://www.foo.com/zam/and/company/with/bells/on");
			assertEquals("<a href=\"http://www.foo.com/zam/and/company/with/bells/on\">http://www.foo.com/z...any/with/bells/on</a>", res.getText());
		}

	public void testAllRaw()
		{
			Filter filter = new Filter();
			filter.setPlaintextHandler(new PassthroughEscapeHandler());

			HtmlHelper.addRawUrlHandlers(filter);
			
			FilterResults res = filter.filter("http://moo.on.you https://moo.on.you/ ftp://moo.files.for.you/cow gopher://kenny.loggins.net/green/hide telnet://cow.phone.org:4242/ring/ring/ring?tone=moo");
			assertEquals("<a href=\"http://moo.on.you\">http://moo.on.you</a> <a href=\"https://moo.on.you/\">https://moo.on.you/</a> <a href=\"ftp://moo.files.for.you/cow\">ftp://moo.files.for.you/cow</a> <a href=\"gopher://kenny.loggins.net/green/hide\">gopher://kenny.loggins.net/green/hide</a> <a href=\"telnet://cow.phone.org:4242/ring/ring/ring?tone=moo\">telnet://cow.phone.o...ing/ring?tone=moo</a>", res.getText());

			filter = new Filter();
			filter.setPlaintextHandler(new PassthroughEscapeHandler());

			HtmlHelper.addRawUrlHandlers(filter, new MooUrlFormatter());
			
			res = filter.filter("http://moo.on.you https://moo.on.you/ ftp://moo.files.for.you/cow gopher://kenny.loggins.net/green/hide telnet://cow.phone.org:4242/ring/ring/ring?tone=moo");
			assertEquals("MOO:moo.on.you MOO:moo.on.you/ MOO:moo.files.for.you/cow MOO:kenny.loggins.net/green/hide MOO:cow.phone.org:4242/ring/ring/ring?tone=moo", res.getText());
		}
}
