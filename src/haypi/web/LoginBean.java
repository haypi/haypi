package haypi.web;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
@ManagedBean
public class LoginBean extends BasicBean {

	private static final long serialVersionUID = 1L;

	protected final Log log = LogFactory.getLog(getClass());

	private String username = "andrem";
	private String password = "";
//	private String deviceId = "89f0f2d8c9b8735735d222adcfc111cfe68a3844";
	private String deviceId = "414831e95c10a96f92f56220004fb9454f1fe8f4";
//	private String deviceId = RandomStringUtils.random(40, "0123456789abcdef");
	private Boolean pvp = false;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDeviceId() {
		return deviceId;
	}
	
	public Boolean getPvp() {
		return pvp;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public void setPvp(Boolean pvp) {
		this.pvp = pvp;
	}

	public String login() throws ServletException, IOException {
		log.debug("login username=" + username);
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		HttpServletRequest request = (HttpServletRequest) context.getRequest();

		RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");

		dispatcher.forward(request, (ServletResponse) context.getResponse());
		FacesContext.getCurrentInstance().responseComplete();
		return "";
	}

}
