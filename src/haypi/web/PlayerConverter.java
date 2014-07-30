package haypi.web;

import java.util.HashMap;
import java.util.Map;

import haypi.model.pojo.Player;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "playerConverter")
public class PlayerConverter implements Converter {

	static Map<String, Player> map = new HashMap<String, Player>();

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String name) {
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		return map.get(name);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		String name = ((Player) object).getName();
		map.put(name, (Player) object);
		return name;
	}

}
