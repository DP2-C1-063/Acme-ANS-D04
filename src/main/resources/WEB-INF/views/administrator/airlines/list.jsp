<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airlines.list.label.name" path="name" width="20%"/>
	<acme:list-column code="administrator.airlines.list.label.IATACode" path="IATACode" width="10%"/>
	<acme:list-column code="administrator.airlines.list.label.web" path="web" width="70%"/>
	<acme:list-payload path="payload"/>
</acme:list>