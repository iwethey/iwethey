package org.iwethey.conversions;

import java.util.List;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.iwethey.forums.domain.AdminManager;
import org.iwethey.forums.domain.Quote;
import org.iwethey.forums.domain.OldQuote;
import org.iwethey.forums.domain.UserManager;

/**
 * <p>Conversion program for converting the old Quote data to new Quote data.</p>
 *
 * <p>@author Mike Vitale (<a href="mailto:iwethey@mikevitale.com">iwethey@mikevitale.com</a>)</p>
 */
public class ConvertQuote {
    private static AdminManager adminManager;
    private static UserManager userManager;
	protected static ApplicationContext context = null;

	public static void main(String[] args) throws Exception {
		System.out.println("Converting Old Quotes to New LERPADISMS!");
		context = new ClassPathXmlApplicationContext("test-context.xml");
		System.out.println("1");
		SessionFactory sf = (SessionFactory) context.getBean("sessionFactory");
		System.out.println("2");
		Session s = sf.openSession();
		System.out.println("3");
		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(s));
		System.out.println("4");

		adminManager = (AdminManager) context.getBean("adminManager");
		System.out.println("5");
		userManager = (UserManager) context.getBean("userManager");
		System.out.println("5.5");

		List<OldQuote> oldQuotes = adminManager.getAllOldLRPDs();
		System.out.println("6");

		System.out.println("# Old Quotes = [" + oldQuotes.size() + "]");

		for (OldQuote oq : oldQuotes)
		{
			System.out.println("Working on Quote = [" + oq.getQuote() + "]");
			Quote q = new Quote();

			q.setBoard(oq.getBoard());
			q.setQuote(oq.getQuote());
			q.setCreated(oq.getCreated());
			q.setApproved(true);
			q.setApprovedBy(userManager.getUserById(1));
			q.setApprovedDate(new Date());
			q.setCreatedBy(userManager.getUserByNickname(oq.getCreatedBy()));


			System.out.println("Saving New Quote = [" + q.getQuote() + "]");
			adminManager.saveQuote(q);
			System.out.println("-----------------------------");
		}
		System.out.println("7");

		s.flush();
		System.out.println("8");
		TransactionSynchronizationManager.unbindResource(sf);
		System.out.println("9");
		SessionFactoryUtils.releaseSession(s, sf);
		System.out.println("10");
	}
}
