<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.task-involves-record.form.label.task-type" path="task.type" width="20%"/>
	<acme:list-column code="technician.task-involves-record.list.label.priority" path="task.priority" width="20%"/>
	<acme:list-column code="technician.task-involves-record.list.label.estimated-duration" path="task.estimatedDuration" width="20%"/>
	<acme:list-column code="technician.task-involves-record.list.label.description" path="task.description" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list' && draftMode == true}">
	<acme:button code="technician.task-involves-record.list.button.create" action="/technician/task-involves-record/create?masterId=${masterId}"/>
</jstl:if>	