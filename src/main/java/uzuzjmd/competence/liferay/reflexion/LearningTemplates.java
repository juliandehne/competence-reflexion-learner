package uzuzjmd.competence.liferay.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.TabChangeEvent;

import uzuzjmd.competence.shared.SuggestedCompetenceGrid;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

@ManagedBean(name = "LearningTemplates")
@ViewScoped
public class LearningTemplates implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> learningTemplates;
	private String selectedCompetence; // the one to persist in lernprojekte tab
	private String selectedLearningTemplate; // the one to select the grid for
	
	private SuggestedCompetenceGrid suggestedCompetenceGrid;

	public List<String> getLearningTemplates() {
		return learningTemplates;
	}

	public void setLearningTemplates(List<String> learningTemplates) {
		this.learningTemplates = learningTemplates;
	}

	@PostConstruct
	public void init() {
		learningTemplates = new ArrayList<String>();
		List<String> selectedTemplates = null;
		try {
			selectedTemplates = SelectedLearningTemplateDAO.findAll().getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!CollectionUtils.isEmpty(selectedTemplates)) {
			learningTemplates.addAll(selectedTemplates);
			selectedLearningTemplate = learningTemplates.get(0);
		}
		
		if (!StringUtils.isEmpty(selectedLearningTemplate)) {	
			SuggestedCompetenceGrid data = SuggestedCompetenceGridDAO
					.getGrid(selectedLearningTemplate);	
			setSuggestedCompetenceGrid(data);
		}
	}

	public void updateGrid(ComponentSystemEvent event) throws UniformInterfaceException, ClientHandlerException, PortalException, SystemException {
		System.out.println("updateing grid ........ ");
		System.out.println(event.getComponent().getId());
	}
	
	public void onTabChange(TabChangeEvent event) throws UniformInterfaceException, ClientHandlerException, PortalException, SystemException {
		learningTemplates.clear();
		List<String> selectedTemplates = SelectedLearningTemplateDAO.findAll().getData();
		if (!CollectionUtils.isEmpty(selectedTemplates)) {
			learningTemplates.addAll(selectedTemplates);
			selectedLearningTemplate = learningTemplates.get(0);
		} else {
			selectedLearningTemplate = null;
		}
		
		
		if(StringUtils.equals(event.getTab().getId(), "learningProjectGridTab")) {
			SuggestedCompetenceGrid data = null;
			if (!StringUtils.isEmpty(selectedLearningTemplate)) {	
				data = SuggestedCompetenceGridDAO.getGrid(selectedLearningTemplate);	
			}
			
			setSuggestedCompetenceGrid(data);
		}
    }
	
	public void handleChange(){
	    if (!StringUtils.isEmpty(selectedLearningTemplate)) {	
			SuggestedCompetenceGrid data = SuggestedCompetenceGridDAO
					.getGrid(selectedLearningTemplate);	
			setSuggestedCompetenceGrid(data);
		}
	}

	public void setSuggestedCompetenceGrid(
			SuggestedCompetenceGrid suggestedCompetenceGrid) {
		this.suggestedCompetenceGrid = suggestedCompetenceGrid;
	}

	public SuggestedCompetenceGrid getSuggestedCompetenceGrid() {
		return suggestedCompetenceGrid;
	}

	public void addTemplate(ActionEvent e) throws UniformInterfaceException, ClientHandlerException, PortalException, SystemException {
		if(SelectedLearningTemplateDAO.persist(getSelectedCompetence())) {
			learningTemplates.add(getSelectedCompetence());
			setSelectedCompetence(null);
		}
//		while(learningTemplates.contains(selectedCompetence)) {
//			learningTemplates.remove(selectedCompetence);
//		}
//		learningTemplates.add(selectedCompetence);
		//learningTemplates = new ArrayList<String>(new LinkedHashSet<String>(learningTemplates));		
	}

	public void deleteTemplate1(String todelete) {
		if(SelectedLearningTemplateDAO.delete(todelete)){
			learningTemplates.remove(todelete);
		}
//		while(learningTemplates.contains(selectedCompetence)) {
//			learningTemplates.remove(todelete);
//		}				
	}

	public String getSelectedCompetence() {
		return selectedCompetence;
	}

	public void setSelectedCompetence(String selectedCompetence) {
		this.selectedCompetence = selectedCompetence;
	}

	public void setSelectedLearningTemplate(String selectedLearningTemplate) {
		this.selectedLearningTemplate = selectedLearningTemplate;
	}

	public String getSelectedLearningTemplate() {
		return selectedLearningTemplate;
	}

	// public void processValueChange(AjaxBehaviorEvent event)
	// throws AbortProcessingException {
	// System.out.println("value change listener called");
	// System.out.println("selectedItem: " + selectedLearningTemplate);
	// setSuggestedCompetenceGrid(SuggestedCompetenceGridDAO.getGrid(selectedLearningTemplate));
	// RequestContext.getCurrentInstance().update("gridView");
	// }

	// @Override
	// public void processValueChange(ValueChangeEvent event)
	// throws AbortProcessingException {
	// System.out.println("prozessing event change: " + event.getNewValue());
	// System.out.println("selectedItem: " + selectedLearningTemplate);
	// }
}
