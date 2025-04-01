<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">	
     <jstl:if test="${_command == 'show' }">
	<acme:input-moment code="assistance-agent.tracking-logs.form.label.lastUpdateMoment" path="lastUpdateMoment"  readonly="true"/>  
	</jstl:if>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.step" path="step"/>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:input-select code="assistance-agent.tracking-logs.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.resolution" path="resolution"/>
	<acme:input-select code="assistance-agent.tracking-logs.form.label.claim" path="claim" choices="${claims}"/>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.assistanceAgent" path="assistanceAgent"  readonly="true"/>
	
	<jstl:choose>	
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
			<acme:submit code="assistance-agent.tracking-logs.form.button.update" action="/assistance-agent/tracking-log/update"/>
			<acme:submit code="assistance-agent.tracking-logs.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
			<acme:submit code="assistance-agent.tracking-logs.form.button.delete" action="/assistance-agent/tracking-log/delete"/>			
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.tracking-logs.form.button.create" action="/assistance-agent/tracking-log/create"/>
			<acme:input-checkbox code="assistance-agent.tracking-logs.form.label.confirmation" path="confirmation"/>		
			
		</jstl:when>		
	</jstl:choose>
</acme:form>

