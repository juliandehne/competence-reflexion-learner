package uzuzjmd.competence.liferay.reflexion;

import javax.ws.rs.core.MediaType;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uzuzjmd.competence.shared.ReflectiveAssessmentsListHolder;
import uzuzjmd.competence.shared.StringList;
import uzuzjmd.competence.shared.SuggestedCompetenceGrid;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;

public class SelectedLearningTemplateDAOTest {

	private final static String user = "xunguyen";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Client client = com.sun.jersey.api.client.Client.create();
		client.addFilter(new LoggingFilter(System.out));
		WebResource webResource = client.resource("http://localhost:8084"
				+ "/competences/json/user/create/" + user + "/teacher");

		try {
			webResource.queryParam("groupId", "user")
					.accept(MediaType.APPLICATION_JSON).post();
		} finally {
			client.destroy();
		}

	}

	@AfterClass
	public static void tearDown() {

	}

	/**
	 * tests that interface does not through exception may not return data if
	 * user has not previously selected learning templates for himself to learn
	 */
	@Test
	public void testSelectedTemplates() {
		Client client = com.sun.jersey.api.client.Client.create();
		client.addFilter(new LoggingFilter(System.out));
		WebResource webResource = client.resource("http://localhost:8084"
				+ "/competences/xml/learningtemplates/selected");

		System.out.println("FETCHING FROM_: " + webResource.getURI());

		StringList result = null;

		try {

			result = webResource.queryParam("userId", user + "")
					.queryParam("groupId", "user")
					.accept(MediaType.APPLICATION_XML).get(StringList.class);
		} finally {
			client.destroy();
		}

		Assert.assertNotNull(result);
	}
	
	@Test
	public void testUpdateReflexion() {
		Client client1 = com.sun.jersey.api.client.Client.create();
		WebResource webResource2 = client1.resource("http://localhost:8084/competences/xml/learningtemplates/gridview");
		SuggestedCompetenceGrid result = null;
		try {
			result = webResource2
					.queryParam("userId", user)
					.queryParam("groupId","user")
					.queryParam("selectedTemplate", "11 Sprachkompetenz, Univ. (ELC, DE)")
					.accept(MediaType.APPLICATION_XML)
					.get(SuggestedCompetenceGrid.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client1.destroy();
		}
		
		System.out.println(result.getSuggestedCompetenceRows().get(0).getSuggestedCompetenceRowHeader());
		
		ReflectiveAssessmentsListHolder holder = result.getSuggestedCompetenceRows().get(0).getSuggestedCompetenceColumns().get(0).getReflectiveAssessmentListHolder();
		System.out.println("updating: " + holder.getSuggestedMetaCompetence());

		Client client = com.sun.jersey.api.client.Client.create();
		client.addFilter(new LoggingFilter(System.out));
		WebResource webResource = client.resource("http://localhost:8084/competences/xml/learningtemplates/gridview/update");
		try {
			webResource
					.queryParam("userId",user)
					.queryParam("groupId","user")
					.type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
					.post(ReflectiveAssessmentsListHolder.class, holder);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
	}
}
