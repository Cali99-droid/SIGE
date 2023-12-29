<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date" %>
<%
String nombre = (String)request.getParameter("nombre");
String apellido = (String)request.getParameter("apellido");
String email = (String)request.getParameter("email");
String importe = (String)request.getParameter("importe");
String token = (String)request.getAttribute("token");
String body = (String)request.getAttribute("json");
String sess = (String)request.getAttribute("session");
String merchantId = (String)request.getAttribute("merchantId");
String currency = (String)request.getAttribute("currency");
Date hoy = new Date();
Long purchaseNumber = hoy.getTime()/100000;
String purchase = purchaseNumber.toString();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Integraciones VisaNet</title>
</head>
<body>
<form action="BotonServlet?importe=<%=importe%>&purchaseNumber=<%=purchase%>&merchantId=<%=merchantId%>&currency=<%=currency%>" method='post'>
	<script src="https://static-content-qas.vnforapps.com/v2/js/checkout.js?qa=true" type="text/javascript"
		data-sessiontoken="<%=sess%>"
		data-merchantid="<%=merchantId%>"
		data-channel="web"
		data-merchantname="Prueba Java"
		data-purchasenumber="<%=purchase%>"
		data-cardholdername="<%=nombre%>"
		data-cardholderlastname="<%=apellido%>"
		data-cardholderemail="<%=email%>"
		data-amount="<%=importe%>"
		data-timeouturl="http://127.0.0.1:8080/demo_boton/"
	>
	</script>
</form>
</body>
</html>
