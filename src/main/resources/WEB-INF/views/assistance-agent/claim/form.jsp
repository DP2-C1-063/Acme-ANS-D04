<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">	
	<acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment"/>  
	<acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.description" path="description"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.type" path="type"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.leg" path="leg"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.assistanceAgent" path="assistanceAgent"/>
	
</acme:form>