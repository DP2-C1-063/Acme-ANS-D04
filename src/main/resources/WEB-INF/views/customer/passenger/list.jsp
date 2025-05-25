<%@page%>
 
 <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
 <acme:list>
     <acme:list-column code="customer.passenger.list.label.name" path="name" />
 	<acme:list-column code="customer.passenger.list.label.passport" path="passport" />
 	<acme:list-column code="customer.passenger.list.label.birth" path="birth" />
 	<acme:list-column code="customer.passenger.list.label.draftmode" path="draftMode" />
     <acme:list-payload path="payload"/>	
 </acme:list>
 
 
     <acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
