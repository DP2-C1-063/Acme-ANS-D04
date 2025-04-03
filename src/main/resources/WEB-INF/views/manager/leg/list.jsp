<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.leg.list.label.flightNumber" path="flightNumber" sortable="false"/>
	<acme:list-column code="manager.leg.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:list-column code="manager.leg.list.label.scheduledArrival" path="scheduledArrival"/>
	<acme:list-column code="manager.leg.list.label.departureAirport" path="departureAirport" sortable="false"/>
	<acme:list-column code="manager.leg.list.label.arrivalAirport" path="arrivalAirport" sortable="false"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list' && flightPublished==false}">
	<acme:button code="manager.leg.list.button.create" action="/manager/leg/create?masterId=${masterId}"/>
</jstl:if>