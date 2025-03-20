<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">	
	<acme:input-textbox code="administrator.airport.form.label.name" path="name"/>
	<acme:input-textbox code="administrator.airport.form.label.IATACode" path="IATACode"/>
	<acme:input-textbox code="administrator.airport.form.label.operationalScope" path="operationalScope"/>
	<acme:input-textbox code="administrator.airport.form.label.city" path="city"/>
	<acme:input-textbox code="administrator.airport.form.label.country" path="country"/>
	<acme:input-textbox code="administrator.airport.form.label.website" path="website"/>
	<acme:input-textbox code="administrator.airport.form.label.email" path="email"/>
	<acme:input-textbox code="administrator.airport.form.label.address" path="address"/>
	<acme:input-textbox code="administrator.airport.form.label.contactPhoneNumber" path="contactPhoneNumber" />
	<jstl:if test="${!readonly}">
	<acme:input-select code="administrator.airport.form.label.operationalScope" path="operationalScope" choices="${operationalScopes}"/>
	</jstl:if>
</acme:form>