<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.airlines.form.label.name" path="name"/>	
	<acme:input-textbox code="administrator.airlines.form.label.IATACode" path="IATACode"/>
	<acme:input-url code="administrator.airlines.form.label.web" path="web"/>
	<acme:input-moment code="administrator.airlines.form.label.foundationMoment" path="foundationMoment"/>
	<acme:input-textbox code="administrator.airlines.form.label.email" path="email"/>
	<acme:input-textbox code="administrator.airlines.form.label.phoneNumber" path="phoneNumber"/>
	<acme:input-select path="type" code="administrator.airlines.form.label.type" choices="${types}" />
	
	<jstl:choose>	
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:submit code="administrator.airlines.form.button.update" action="/administrator/airlines/update"/>
			<acme:input-checkbox code="administrator.airlines.form.label.confirmation" path="confirmation"/>			
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.airlines.form.button.create" action="/administrator/airlines/create"/>
			<acme:input-checkbox code="administrator.airlines.form.label.confirmation" path="confirmation"/>		
			
		</jstl:when>		
	</jstl:choose>
	
</acme:form>