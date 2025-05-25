<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="technician.task-involves-record.form.label.task" path="task" choices="${tasks}" />
			<acme:submit code="technician.task-involves-record.form.button.create" action="/technician/task-involves-record/create?masterId=${masterId}"/>			
		</jstl:when>
		<jstl:when test="${_command == 'show' && draftMode == true}">
			<acme:input-textbox code="technician.task-involves-record.form.label.record-aircraft" path="maintenanceRecord.relatedAircraft.registrationNumber" readonly="true"/>
			<acme:input-select code="technician.task-involves-record.form.label.task-type" path="task.type" choices="${types}" readonly="true"/>				
			<acme:input-textbox code="technician.task-involves-record.form.label.technician" path="task.technician.licenseNumber" readonly="true"/>
			<acme:input-textarea code="technician.task-involves-record.form.label.description" path="task.description" readonly="true"/>
			<acme:input-integer code="technician.task-involves-record.form.label.priority" path="task.priority" readonly="true"/>
			<acme:input-integer code="technician.task-involves-record.form.label.estimated-duration" path="task.estimatedDuration" readonly="true"/>

			<acme:submit code="technician.task-involves-record.form.button.delete" action="/technician/task-involves-record/delete" />
		</jstl:when>
		<jstl:when test="${_command == 'show'}">
			<acme:input-textbox code="technician.task-involves-record.form.label.record-aircraft" path="maintenanceRecord.relatedAircraft.registrationNumber" readonly="true"/>
			<acme:input-select code="technician.task-involves-record.form.label.task-type" path="task.type" choices="${types}" readonly="true"/>				
			<acme:input-textbox code="technician.task-involves-record.form.label.technician" path="task.technician.licenseNumber" readonly="true"/>
			<acme:input-textarea code="technician.task-involves-record.form.label.description" path="task.description" readonly="true"/>
			<acme:input-integer code="technician.task-involves-record.form.label.priority" path="task.priority" readonly="true"/>
			<acme:input-integer code="technician.task-involves-record.form.label.estimated-duration" path="task.estimatedDuration" readonly="true"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>