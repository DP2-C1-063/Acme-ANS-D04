<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">	
	<acme:input-moment code="assistance-agent.tracking-logs.form.label.lastUpdateMoment" path="lastUpdateMoment"/>  
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.step" path="step"/>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.status" path="status"/>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.resolution" path="resolution"/>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.claim" path="claim"/>
	<acme:input-textbox code="assistance-agent.tracking-logs.form.label.assistanceAgent" path="assistanceAgent"/>
</acme:form>