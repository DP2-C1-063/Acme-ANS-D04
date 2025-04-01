<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-members.flight-assignment.list.label.duty" path="duty" width="20%"/>
	<acme:list-column code="flight-crew-members.flight-assignment.list.label.lastUpdate" path="lastUpdate" width="10%"/>
	<acme:list-column code="flight-crew-members.flight-assignment.list.label.leg" path="leg" width="70%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${acme:anyOf(_command, 'list-planned|list-completed')}">
	<acme:button code="flight-crew-members.flight-assignment.list.button.create" action="/flight-crew-members/flight-assignment/create"/>
</jstl:if>