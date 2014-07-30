<% 
if ( request.getHeader("user-agent").contains("iPhone") || request.getHeader("user-agent").contains("Android")) {
	response.sendRedirect("pages/home.html"); 
} else {
	response.sendRedirect("pages/home.html"); 
}
%>