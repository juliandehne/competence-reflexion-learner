package uzuzjmd.competence.liferay.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;

import uzuzjmd.competence.liferay.util.SOAUtil;
import datastructures.lists.StringList;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@ManagedBean(name="LearningTemplatesFullSet")
@SessionScoped
public class LearningTemplatesFullSet implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private static Log _log = LogFactoryUtil
//			.getLog(LearningTemplatesFullSet.class);
//		
	private List<String> learningTemplates;
	

	public List<String> getLearningTemplates() {
		return learningTemplates;
	}

	public void setLearningTemplates(List<String> learningTemplates) {
		this.learningTemplates = learningTemplates;
	}
	
	

	public List<String> complete(String query) {
		List<String> result = new ArrayList<String>();
//		_log.debug("auto-completing");
		Collection<String> tmp = Collections2.filter(getNotSelectedTemplate(), Predicates.containsPattern(query));	
		result.addAll(tmp);
		
		if(result.size() == 0) {
			FacesContext.getCurrentInstance().addMessage( "autocompleteMessage", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info: ", "Es stellt kein Lernprojekt zur Verfügung!"));
		}
		return result;		
	}
	
	@PostConstruct
	public void init() {
		learningTemplates = new ArrayList<String>();
		accessREST();
	}

	private void accessREST() {
		Client client = Client.create();
		WebResource webResource = client
				.resource(SOAUtil.getRestserverUrl() + "/competences/learningtemplates");
		StringList result = webResource.accept(MediaType.APPLICATION_XML)
				.get(StringList.class);
//		for (String template : result.getData()) {
//			learningTemplates.add(template);
//		}
		final List<String> tmp = result == null ? new ArrayList<String>() : result.getData();
		learningTemplates = CollectionUtils.isEmpty(tmp) ? new ArrayList<String>() : tmp;
		Iterables.removeIf(learningTemplates, Predicates.isNull());
	}

	private List<String> getNotSelectedTemplate() {
		final List<String> notSelectedTemplates = new ArrayList<String>();
		try {
			final List<String> selectedTemplates = SelectedLearningTemplateDAO.findAll()
					.getData();
			
			if(selectedTemplates == null) {
				return learningTemplates;
			}
			
			for (String template : learningTemplates) {
				if(!selectedTemplates.contains(template)) {
					notSelectedTemplates.add(template);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notSelectedTemplates;
	}
}
