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
			
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>
			<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>	
			<acme:button code="assistance-agent.claim.form.button.TrackingLogs" action="/assistance-agent/tracking-log/list?masterId=${id}"/>		
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
			
		</jstl:when>	
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == false}">
			
			<acme:submit code="assistance-agent.claim.form.button.review" action="/assistance-agent/claim/review"/>	
			<acme:button code="assistance-agent.claim.form.button.TrackingLogs" action="/assistance-agent/tracking-log/list?masterId=${id}"/>		
		</jstl:when>	
	</jstl:choose>
	
	
</acme:form>