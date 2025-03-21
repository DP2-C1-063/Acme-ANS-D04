<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="flight-crew-members.flight-assignment.form.label.duty" path="duty"/>	
	<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.lastUpdate" path="lastUpdate"/>
	<acme:input-textarea code="flight-crew-members.flight-assignment.form.label.leg" path="leg"/>
	<acme:input-integer code="flight-crew-members.flight-assignment.form.label.currentStatus" path="currentStatus"/>
	<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.remarks" path="remarks"/>
	<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember" path="flightCrewMember"/>

	
</acme:form>