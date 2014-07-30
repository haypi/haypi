package haypi.web;

import haypi.security.HaypiUserDetails;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ManagedBean
public class BasicBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public HaypiUserDetails getPrincipal() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx.getAuthentication() == null) {
			return null;
		}
		Object principal = ctx.getAuthentication().getPrincipal();
		if (principal == null) {
			return null;
		}
		if (principal instanceof HaypiUserDetails) {
			return (HaypiUserDetails) principal;
		} else {
			HaypiUserDetails details = new HaypiUserDetails();
			details.setUsername(principal.toString());
			return details;
		}
	}

	public HttpSession getSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		return session;
	}

	public Object getObjectFromSession(String attributeName) {
		return getSession().getAttribute(attributeName);
	}

	public String getSpringSecurityLastException() {
		Exception e = (Exception) getObjectFromSession("SPRING_SECURITY_LAST_EXCEPTION");
		return (e == null ? null : e.getMessage());
	}
	
	public final static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	protected HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
	
	public boolean isRoleAdmin() {
		return getRequest().isUserInRole("ROLE_ADMIN");
	}

	public boolean isRoleUser() {
		return getRequest().isUserInRole("ROLE_USER");
	}
}
