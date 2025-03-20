
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airport.list.label.name" path="name" width="40%"/>
	<acme:list-column code="administrator.airport.list.label.IATACode" path="IATACode" width="60%"/>
	<acme:list-payload path="payload"/>
</acme:list>