package teck.getarrays.employeemanager;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import teck.getarrays.employeemanager.domain.model.Employee;
import teck.getarrays.employeemanager.domain.service.EmployeeService;

@AutoConfigureMockMvc //MockMvcを使用するための@
@SpringBootTest(classes = EmployeemanagerApplication.class) // エントリポイントを記述？
public class EmployeeResourceTest {

	private Employee employee;
	private List<Employee> employeeList;


	//@AutoConfigureMockMvcとセット
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService mockEmployeeService;

	@BeforeEach
	public void setup() {

		this.employee = Employee.builder()
				.id((long) 1)
				.name("yugi")
				.email("blackMagic@com")
				.jobTitle("gammer")
				.phone("123-4567-8910")
				.imageUrl("noImage")
				.employeeCode("code")
				.build();

		this.employeeList = new ArrayList<Employee>();
		this.employeeList.add(this.employee);
	}

	@Test
	public void test001_init_200() throws Exception {

		this.mockMvc.perform(get("/employee/init")).andDo(print()) //andDoはオプションで結果をコンソールへ出力しているだけ
			.andExpect(status().isOk());
	}

	//直接JSONで比較。期待値を書くのがめんどくさい。
	@Test
	public void test002_getAllEmployeeEntity_200_assert_json() throws Exception {

		//mock service
		when(mockEmployeeService.findAllEmployees()).thenReturn(this.employeeList);

		String expectedJson = "[{\"id\":1,\"name\":\"yugi\",\"email\":\"blackMagic@com\","
				+ "\"jobTitle\":\"gammer\",\"phone\":\"123-4567-8910\",\"imageUrl\":\"noImage\",\"employeeCode\":\"code\"}]";

		//jsonにして直接確認
		this.mockMvc.perform(get("/employee/all")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(expectedJson));

		verify(mockEmployeeService, times(1)).findAllEmployees();
	}

	//一度MvcResultにいれ、modelへパースして比較。
	@Test
	public void test003_getAllEmployeeEntity_200_assert_object() throws Exception {

		//mock service
		when(mockEmployeeService.findAllEmployees()).thenReturn(this.employeeList);

		//andReturnで結果を戻せる(MvcResult型)
		String resultJson = this.mockMvc.perform(get("/employee/all"))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();

		ObjectMapper objectMapper = new ObjectMapper();

    //JacksonでJavaオブジェクトへ変換。同時にArrays.asListで配列をListへ。
		List<Employee> actual = Arrays.asList(objectMapper.readValue(resultJson, Employee[].class));

		verify(mockEmployeeService, times(1)).findAllEmployees();

		List<Employee> expect = this.employeeList;
		assertThat(actual, is(equalTo(expect))); //importで困窮。org.hamcrest.MatcherAssert.*;からimportする。
	}

	@Test
	public void test004_getEmployeeById_200() throws Exception {

		//mock service
		when(mockEmployeeService.findEmployeeById((long)1)).thenReturn(this.employee);

		//andReturnで結果を戻せる(MvcResult型)
		String resultJson = this.mockMvc.perform(get("/employee/find/1"))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();

		ObjectMapper objectMapper = new ObjectMapper();

    //JacksonでJavaオブジェクトへ変換。同時にArrays.asListで配列をListへ。
		Employee actual = objectMapper.readValue(resultJson, Employee.class);

		verify(mockEmployeeService, times(1)).findEmployeeById((long)1);

		Employee expect = this.employee;
		assertThat(actual, is(equalTo(expect)));
	}

	@Test
	public void test005_addEmployee() {

		Employee request = employee;

		//mock service
		when(mockEmployeeService.addEmployee(request)).thenReturn(employee);
		
		this.mockMvc.perform(
				post("employee/add")
				.content(content)
				)
		

	}
}
