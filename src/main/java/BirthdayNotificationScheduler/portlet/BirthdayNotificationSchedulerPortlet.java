package BirthdayNotificationScheduler.portlet;

import BirthdayNotificationScheduler.constants.BirthdayNotificationSchedulerPortletKeys;
import EmployeeManagementSystem2.model.Employee;
import EmployeeManagementSystem2.service.EmployeeLocalService;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author himashu.jha
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=BirthdayNotificationScheduler",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + BirthdayNotificationSchedulerPortletKeys.BIRTHDAYNOTIFICATIONSCHEDULER,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class BirthdayNotificationSchedulerPortlet extends MVCPortlet {
	
//	protected void doReceive(Message message) throws Exception {
//        BirthdayNotifier birthdayNotifier = new BirthdayNotifier();
//        birthdayNotifier.notifyUsersOnBirthday();
//    }
	
	public void notifyUsersOnBirthday(ActionRequest actionRequest, ActionResponse actionResponse) {
        try {
            List<User> users = UserLocalServiceUtil.getUsers(-1, -1);
            Date today = new Date();

            for (User user : users) {
                if (isBirthdayToday(user)) {
                    sendBirthdayEmailNotification(user);
                }
            }
        } catch (Exception e) {
            // Handle exception
        }
    }

    private boolean isBirthdayToday(User user) throws PortalException {
    	
//    	Trying
    	
    	String birthdayString = "2024/03/20";

    	// Parse the birthday string to extract year, month, and day
    	String[] parts = birthdayString.split("/");
    	int year = Integer.parseInt(parts[0]);
    	int month = Integer.parseInt(parts[1]) - 1; // Month is 0-based in Calendar class
    	int day = Integer.parseInt(parts[2]);

    	// Create a Calendar instance and set the year, month, and day
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.YEAR, year);
    	calendar.set(Calendar.MONTH, month);
    	calendar.set(Calendar.DAY_OF_MONTH, day);

    	// Get the Date object from the Calendar
    	Date birthday = calendar.getTime();

//    	trial ends
    	
//    	Main 
//        Date birthday = user.getBirthday();
//    	Date birthday = 2024/03/19;
        System.out.println("Birthday +++++++++++======="+birthday);
        
        if (birthday == null) {
            return false;
        }

        Calendar userCal = Calendar.getInstance();
        userCal.setTime(birthday);

        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(new Date());

        return userCal.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH)
                && userCal.get(Calendar.DAY_OF_MONTH) == todayCal.get(Calendar.DAY_OF_MONTH);
    }

    private void sendBirthdayEmailNotification(User user) {
        try {
            List<User> allUsers = UserLocalServiceUtil.getUsers(-1, -1);

            for (User recipient : allUsers) {
                if (!user.equals(recipient) && !user.getEmailAddress().endsWith("@liferay.com")) { // Skip sending email to the birthday celebrant & demo users
                    InternetAddress toAddress = new InternetAddress(recipient.getEmailAddress());

                    System.out.println("Mail send for birthday");
                    MailMessage mailMessage = new MailMessage();
            		mailMessage.setHTMLFormat(true);
                    mailMessage.setSubject("Birthday Celebration!");
                    mailMessage.setBody("Today is " + user.getFullName() + "'s birthday. Let's celebrate!");
            		mailMessage.setFrom(new InternetAddress("himanshuprincejha2001@gmail.com","Himanshu Jha"));
                    mailMessage.setTo(toAddress);
                    MailServiceUtil.sendEmail(mailMessage);
                }
            }
        } catch (Exception e) {
            // Handle exception
        	System.out.println("Cant sent getting some errros ");
        }
    }
    
}