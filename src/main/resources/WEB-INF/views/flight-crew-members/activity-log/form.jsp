<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="flight-crew-members.activity-log.form.label.registrationMoment" path="registrationMoment"/>	
	<acme:input-textbox code="flight-crew-members.activity-log.form.label.incidentType" path="incidentType"/>
	<acme:input-textarea code="flight-crew-members.activity-log.form.label.description" path="description"/>
	<acme:input-integer code="flight-crew-members.activity-log.form.label.severityLevel" path="severityLevel"/>
	<acme:input-textbox code="flight-crew-members.activity-log.form.label.flightAssignment" path="flightAssignment"/>
	
</acme:form>