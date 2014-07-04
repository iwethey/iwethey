package org.iwethey.forums.web;

import org.springframework.context.MessageSource;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** 
 * General page utilities that Velocity doesn't provide.
 * <p>
 * UNUSED. It turns out that Velocity templates are about
 * twice as slow as JSP (9ms vs. 5ms for the main page on 
 * this machine). I never throw anything away, though...
 * <p>
 * $Id: ContextTool.java 55 2004-12-07 21:53:42Z anderson $
 * <p>
 * @author Scott Anderson
 */
public class ContextTool
{
	private HttpServletRequest mRequest = null;
	private HttpServletResponse mResponse = null;
	private Locale mLocale = null;
	private MessageSource mMessages = null;

	public ContextTool(HttpServletRequest req, HttpServletResponse resp, Locale loc, MessageSource msgs)
		{
			mRequest = req;
			mResponse = resp;
			mLocale = loc;
			mMessages = msgs;
		}

	public String url(String url)
		{
			String result = null;

			if (url.startsWith("/"))
				result = mRequest.getContextPath() + url;
			else
				result = url;

			return mResponse.encodeURL(result);
		}

	public String msg(String key)
		{
			return mMessages.getMessage(key, null, mLocale);
		}

	public String msg(String key, List args)
		{
			return mMessages.getMessage(key, args.toArray(), mLocale);
		}

	public String msg(String key, String arg)
		{
			return mMessages.getMessage(key, new Object[] { arg }, mLocale);
		}

	public long timerEnd()
		{
			return new Date().getTime() - ((Long)mRequest.getAttribute("start")).longValue();
		}

	public boolean last(List list, int count)
		{
			return count == list.size();
		}

	public void setRequest(HttpServletRequest request) { mRequest = request; }
	public HttpServletRequest getRequest() { return mRequest; }

	public void setResponse(HttpServletResponse response) { mResponse = response; }
	public HttpServletResponse getResponse() { return mResponse; }

	public void setMessageSource(MessageSource messageSource) { mMessages = messageSource; }
	public MessageSource getMessageSource() { return mMessages; }

	public void setLocale(Locale locale) { mLocale = locale; }
	public Locale getLocale() { return mLocale; }

}
