<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.claim.list.label.passengerEmail" path="passengerEmail" width="20%"/>
	<acme:list-column code="assistance-agent.claim.list.label.type" path="type" width="10%"/>
	<acme:list-column code="assistance-agent.claim.list.label.description" path="description" width="70%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${acme:anyOf(_command, 'list-completed|list-pending')}">
	<acme:button code="assistance-agent.claim.list.button.create" action="/assistance-agent/claim/create"/>
</jstl:if>