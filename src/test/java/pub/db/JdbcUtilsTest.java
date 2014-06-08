package pub.db;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/applicationContext.xml")
@ActiveProfiles("jdbc")
public class JdbcUtilsTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(
				this.webApplicationContext).build();
	}

	@Test
	public void testJdbcv() throws Exception {
		DataSource dataSource = (DataSource) webApplicationContext
				.getBean("dataSource");
		try {
			List<Map<String, Object>> list = JdbcUtils.executeQuery(dataSource,
					"SELECT id, name,  address, telephone,email FROM account");
			for (int i = 0, size = list.size(); i < size; ++i) {
				Map<String, Object> map = list.get(i);
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					System.out.println(entry.getKey() + "=>" + entry.getValue()
							+ ", ");
				}
				System.out.println();
			}
		} catch (Exception e) {
		}
	}

	@Test
	public void getVisitsXml() throws Exception {
		ResultActions actions = this.mockMvc.perform(get("/mobile/accounts/list2")
				.accept(MediaType.APPLICATION_JSON_VALUE));
		actions.andDo(print()); // action is logged into the console
		actions.andExpect(status().isOk());
	}
}
