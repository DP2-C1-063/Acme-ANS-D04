<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${acme:anyOf(_command, 'show|update|publish')}">
		<acme:input-moment code="technician.maintenance-record.form.label.maintenance-moment" path="maintenanceMoment" readonly="true"/>
	</jstl:if>
	<acme:input-moment code="technician.maintenance-record.form.label.next-inspection" path="nextInspection" />
	<acme:input-select code="technician.maintenance-record.form.label.status" path="status" choices="${statuses}" />
	<acme:input-select code="technician.maintenance-record.form.label.aircraft" path="relatedAircraft" choices="${aircrafts}" />
	<acme:input-money code="technician.maintenance-record.form.label.estimated-cost" path="estimatedCost" />
	<acme:input-textarea code="technician.maintenance-record.form.label.notes" path="notes" />
	<jstl:if test="${acme:anyOf(_command, 'show|update|publish') }">
		<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task-involves-record/list?masterId=${id}"/>
	</jstl:if>
	<jstl:choose>
	
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
				<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>
				<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish"/>
		</jstl:when>
		
	</jstl:choose>
</acme:form>