<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="flight-crew-members.flight-assignment.form.label.duty" path="duty" choices= "${duties}" />	
	<jstl:if test="${_command == 'show' }">
	<acme:input-moment code="flight-crew-members.flight-assignment.form.label.lastUpdate" path="lastUpdate" readonly="true"/>
	</jstl:if>
	<acme:input-select code="flight-crew-members.flight-assignment.form.label.leg" path="leg" choices="${legs}"/>
	<acme:input-select code="flight-crew-members.flight-assignment.form.label.currentStatus" path="currentStatus" choices="${statuses}"/>
	<acme:input-textarea code="flight-crew-members.flight-assignment.form.label.remarks" path="remarks"/>
	<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember" path="flightCrewMember"  readonly="true"/>
			
<jstl:choose>

	<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-members.flight-assignment.form.button.create" action="/flight-crew-members/flight-assignment/create"/>
	</jstl:when>
	
	
	<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true }">
	<jstl:if test="${completed == true }">
		<acme:button code="flight-crew-members.flight-assignments.form.button.logs" action="/flight-crew-members/activity-log/list?masterId=${id}"/>
		</jstl:if>		
		<acme:submit code="flight-crew-members.flight-assignment.form.button.update" action="/flight-crew-members/flight-assignment/update"/>
		<acme:submit code="flight-crew-members.flight-assignment.form.button.publish" action="/flight-crew-members/flight-assignment/publish"/>
		<acme:submit code="flight-crew-members.flight-assignment.form.button.delete" action="/flight-crew-members/flight-assignment/delete"/>	
	</jstl:when>
	
	<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && completed == true}">
			<acme:button code="flight-crew-members.flight-assignments.form.button.logs" action="/flight-crew-members/activity-log/list?masterId=${id}"/>			
	</jstl:when>
		
</jstl:choose>
	
</acme:form>

