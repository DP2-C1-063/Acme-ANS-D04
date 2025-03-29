<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">	
<jstl:if test="${_command == 'show' }">
	<acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
	</jstl:if>  
	<acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.description" path="description"/>
	<acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}"/>
	<acme:input-select code="assistance-agent.claim.form.label.leg" path="leg" choices="${legs}"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.assistanceAgent" path="assistanceAgent" readonly="true"/>
	
	<jstl:choose>	
		
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
			<acme:input-checkbox code="assistance-agent.claim.form.label.confirmation" path="confirmation"/>		
			
		</jstl:when>		
	</jstl:choose>
</acme:form>