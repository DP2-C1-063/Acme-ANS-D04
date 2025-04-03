<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
<acme:list-column code="technician.maintenance-record.list.label.aircraft" path="relatedAircraft" width="20%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="20%"/>
	<acme:list-column code="technician.maintenance-record.list.label.maintenance-moment" path="maintenanceMoment" width="20%"/>
	<acme:list-column code="technician.maintenance-record.list.label.estimated-cost" path="estimatedCost" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>
</jstl:if>	