
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airport.list.label.name" path="name"/>
	<acme:list-column code="administrator.airport.list.label.IATACode" path="IATACode"/>
	<acme:list-column code="administrator.airport.list.label.operationalScope" path="operationalScope"/>
	<acme:list-column code="administrator.airport.list.label.city" path="city"/>
	<acme:list-column code="administrator.airport.list.label.country" path="country"/>
	<acme:list-column code="administrator.airport.list.label.website" path="website"/>
	<acme:list-column code="administrator.airport.list.label.email" path="email"/>
	<acme:list-column code="administrator.airport.list.label.address" path="address"/>
	<acme:list-column code="administrator.airport.list.label.contactPhoneNumber" path="contactPhoneNumber"/>
	<acme:list-payload path="payload"/>
</acme:list>