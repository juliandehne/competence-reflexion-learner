package uzuzjmd.competence.liferay.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

public class SOAUtil {
	/**
	 * This is a utility class to get the endpoint of the competence server api calls.
	 * The endpoint should be changed in portlet.properties
	 * 
	 * @return
	 */
	public static final String getRestserverUrl() {
		String competenceRestServerUrl = "";
		try {
			competenceRestServerUrl = GetterUtil.getString(PortletProps
					.get("competenceRestServerUrl"));			
		} catch (Exception ex) {
			System.err.println(ex);			
		}
		System.out.println("using: " + competenceRestServerUrl);
		return competenceRestServerUrl;
	}
}
