package uzuzjmd.competence.liferay.reflexion;

import javax.ws.rs.core.MediaType;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uzuzjmd.competence.shared.StringList;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;

public class SelectedLearningTemplateDAOTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDown() {
		
	}
	
	@Test
	public void testFindAll() {
		Client client = com.sun.jersey.api.client.Client.create();
		client.addFilter(new LoggingFilter(System.out));
		WebResource webResource = client.resource("http://localhost:8084"
				+ "/competences/xml/learningtemplates/selected");
		
		System.out.println("FETCHING FROM_: "+webResource.getURI());

		StringList result = null;
		String user = "xunguyen";
		
		try {

			result = webResource
					.queryParam("userId",
							 user + "")
					.queryParam("groupId", "user")
					.accept(MediaType.APPLICATION_XML).get(StringList.class);
		} finally {
			client.destroy();
		}

		Assert.assertNotNull(result);
	}
}
