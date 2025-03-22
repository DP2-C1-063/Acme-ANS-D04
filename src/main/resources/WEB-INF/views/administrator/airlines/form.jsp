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
	
</acme:form>