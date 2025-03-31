<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
 <acme:form >
 	 <acme:input-textbox code="customer.passenger.list.label.name" path="name" />
     <acme:input-email code="customer.passenger.list.label.email" path="email"/>
     <acme:input-textbox code="customer.passenger.list.label.passport" path="passport" />
     <acme:input-moment code="customer.passenger.list.label.birth" path="birth" />
     <acme:input-textarea code="customer.passenger.list.label.needs" path="needs"/>
 
 	<jstl:choose>
 		<jstl:when test="${acme:anyOf(_command, 'show|update|publish')}">
 		<jstl:if test="${draftMode}">
 			<acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update"/>
 			<acme:submit code="customer.passenger.form.button.publish" action="/customer/passenger/publish"/>
 		</jstl:if>
 		</jstl:when>
 		<jstl:when test="${_command == 'create'}">
 			<acme:submit code="customer.passenger.form.button.create" action="/customer/passenger/create"/>
 		</jstl:when>		
 	</jstl:choose>	
 
 </acme:form>