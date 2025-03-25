<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">	
	<acme:input-textbox code="manager.flight.form.label.tag" path="tag"/>
	<acme:input-select code="manager.flight.form.label.indication" path="indication" choices="${indications}"/>
	<acme:input-money code="manager.flight.form.label.cost" path="cost"/>
	<acme:input-textarea code="manager.flight.form.label.description" path="description"/>
	
	<jstl:if test="${readonly}">
	<acme:input-moment code="manager.flight.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="manager.flight.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-textbox code="manager.flight.form.label.originCity" path="originCity"/>
	<acme:input-textbox code="manager.flight.form.label.destinationCity" path="destinationCity"/>
	<acme:input-integer code="manager.flight.form.label.numberOfLayovers" path="numberOfLayovers"/>
	
	</jstl:if>
	
	<jstl:if test="${!readonly}">
		<acme:input-checkbox code="manager.flight.form.label.confirmation" path="confirmation"/>

	</jstl:if>
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && published == true}">
			<acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?masterId=${id}"/>
			<acme:submit code="manager.flight.form.button.update" action="/manager/flight/update"/>
			<acme:submit code="manager.flight.form.button.delete" action="/manager/flight/delete"/>
			<acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.flight.form.button.create" action="/manager/flight/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>