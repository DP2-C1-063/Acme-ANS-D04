<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-members.activity-log.list.label.registrationMoment" path="registrationMoment" width="20%"/>
	<acme:list-column code="flight-crew-members.activity-log.list.label.incidentType" path="incidentType" width="10%"/>
	<acme:list-column code="flight-crew-members.activity-log.list.label.severityLevel" path="severityLevel" width="70%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="flight-crew-members.activity-log.list.button.create" action="/flight-crew-members/activity-log/create"/>
</jstl:if>	