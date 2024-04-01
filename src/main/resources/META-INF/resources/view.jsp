<%@ include file="/init.jsp" %>

<p>
	<b><liferay-ui:message key="birthdaynotificationscheduler.caption"/></b>
</p>

<portlet:actionURL  name = "notifyUsersOnBirthday" var="mailURL" />
<a href = "${mailURL}">Birthday</a>


<portlet:actionURL  name = "WorkAniversaryNotifier" var="WorkAniversaryNotifierURL" />
<a href = "${WorkAniversaryNotifierURL}">Work Aniversary Notifier</a>



