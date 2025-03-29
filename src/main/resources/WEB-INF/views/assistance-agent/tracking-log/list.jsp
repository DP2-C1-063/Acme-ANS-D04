<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.tracking-logs.list.label.resolutionPercentage" path="resolutionPercentage" width="20%"/>
	<acme:list-column code="assistance-agent.tracking-logs.list.label.step" path="step" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-logs.list.label.lastUpdateMoment" path="lastUpdateMoment" width="70%"/>
	<acme:list-payload path="payload"/>
</acme:list>