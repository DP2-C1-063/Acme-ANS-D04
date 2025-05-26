<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${_command == 'show' }">
	<acme:input-moment code="flight-crew-members.activity-log.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
	</jstl:if>
	<acme:input-textbox code="flight-crew-members.activity-log.form.label.incidentType" path="incidentType"/>
	<acme:input-textarea code="flight-crew-members.activity-log.form.label.description" path="description"/>
	<acme:input-integer code="flight-crew-members.activity-log.form.label.severityLevel" path="severityLevel"/>
	<acme:input-textbox code="flight-crew-members.activity-log.form.label.flightAssignment" path="flightAssignment" readonly="true"/>
	


<jstl:choose>
	<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-members.activity-log.form.button.create" action="/flight-crew-members/activity-log/create?masterId=${masterId}"/>
	</jstl:when>
	
	<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:submit code="flight-crew-members.activity-log.form.button.update" action="/flight-crew-members/activity-log/update"/>
			<acme:submit code="flight-crew-members.activity-log.form.button.publish" action="/flight-crew-members/activity-log/publish"/>
			<acme:submit code="flight-crew-members.activity-log.form.button.delete" action="/flight-crew-members/activity-log/delete"/>	
			
	</jstl:when>
</jstl:choose>
</acme:form>