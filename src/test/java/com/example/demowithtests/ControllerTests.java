package com.example.demowithtests;

import com.example.demowithtests.domain.Address;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.EmployeeDto;
import com.example.demowithtests.dto.EmployeeReadDto;
import com.example.demowithtests.service.EmployeeService;
import com.example.demowithtests.service.EmployeeServiceEM;
import com.example.demowithtests.service.document.DocumentService;
import com.example.demowithtests.util.mappers.DocumentMapper;
import com.example.demowithtests.util.mappers.EmployeeMapper;
import com.example.demowithtests.web.EmployeeController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
@DisplayName("Employee Controller Tests")
@Disabled
public class ControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    EmployeeService service;

    @MockBean
    DocumentService documentService;

    @MockBean
    EmployeeServiceEM serviceEM;

    @MockBean
    EmployeeMapper employeeMapper;

    @MockBean
    DocumentMapper documentMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST API -> /api/users")
    @WithMockUser(roles = "ADMIN")
    public void createPassTest() throws Exception {

        EmployeeDto response = new EmployeeDto(
                1, "Mike", "England", "mail@mail.com",
                null, null, null);

        var employee = Employee.builder()
                .id(1)
                .name("Mike")
                .email("mail@mail.com").build();

        when(employeeMapper.toEmployee(any(EmployeeDto.class))).thenReturn(employee);
        when(employeeMapper.toEmployeeDto(any(Employee.class))).thenReturn(response);
        when(service.create(any(Employee.class))).thenReturn(employee);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name").value("Mike"));

        verify(service).create(any());
    }

    @Test
    @DisplayName("POST API -> /api/users/jpa")
    @WithMockUser(roles = "USER")
    public void testSaveWithJpa() throws Exception {

        var employeeToBeReturn = Employee.builder()
                .id(1)
                .name("Mark")
                .country("France").build();

        doReturn(employeeToBeReturn).when(serviceEM).createWithJpa(any());
        when(this.serviceEM.createWithJpa(any(Employee.class))).thenReturn(employeeToBeReturn);
        // Execute the POST request
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/api/users/jpa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employeeToBeReturn))
                .with(csrf());
        mockMvc
                .perform(builder)
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        verify(this.serviceEM, times(1)).createWithJpa(any(Employee.class));
        verifyNoMoreInteractions(this.serviceEM);
    }

    @Test
    @DisplayName("GET API -> /api/users/{id}")
    @WithMockUser(roles = "USER")
    public void getPassByIdTest() throws Exception {

        var response = new EmployeeReadDto();
        response.id = 1;
        response.name = "Mike";

        var employee = Employee.builder()
                .id(1)
                .name("Mike")
                .build();

        when(employeeMapper.toEmployeeReadDto(any(Employee.class))).thenReturn(response);
        when(service.getById(1)).thenReturn(employee);

        MockHttpServletRequestBuilder mockRequest = get("/api/users/1");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name", is("Mike")));

        verify(service).getById(1);
    }

    @Test
    @DisplayName("PUT API -> /api/users/{id}")
    @WithMockUser(roles = "ADMIN")
    public void updatePassByIdTest() throws Exception {
        var response = new EmployeeReadDto();
        response.id = 1;
        var employee = Employee.builder().id(1).build();

        when(employeeMapper.toEmployee(any(EmployeeDto.class))).thenReturn(employee);
        when(service.updateById(eq(1), any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toEmployeeReadDto(any(Employee.class))).thenReturn(response);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/users/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(service).updateById(eq(1), any(Employee.class));
    }

    @Test
    @DisplayName("DELETE API -> /api/users/{id}")
    @WithMockUser(roles = "ADMIN")
    public void deletePassTest() throws Exception {

        doNothing().when(service).removeById(1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/api/users/1")
                .with(csrf());

        mockMvc.perform(mockRequest)
                .andExpect(status().isNoContent());

        verify(service).removeById(1);
    }

    @Test
    @DisplayName("GET API -> /api/users/pages")
    @WithMockUser(roles = "USER")
    public void getUsersPageTest() throws Exception {

        Address address1 = new Address(2L, false, "Vanuatu", "Mellissastad", "42269 Keith Wells");
        Address address2 = new Address(3L, false, "Uruguay", "New Jodytown", "507 Pamala Mountains");
        Employee employee1 = new Employee(4, "Phebe Lehner", "Ukraine", "teodoratoy@mail.com", new HashSet<>(Arrays.asList(address1, address2)), null, null);
        Employee employee2 = new Employee(5, "Phebe Lehner", "Ukraine", "teodoratoy@mail.com", new HashSet<>(Arrays.asList(address1, address2)), null, null);
        Employee employee3 = new Employee(6, "Phebe Lehner", "Ukraine", "teodoratoy@mail.com", new HashSet<>(Arrays.asList(address1, address2)), null, null);
        Employee employee4 = new Employee(7, "Phebe Lehner", "Ukraine", "teodoratoy@mail.com", new HashSet<>(Arrays.asList(address1, address2)), null, null);
        Employee employee5 = new Employee(8, "Phebe Lehner", "Ukraine", "teodoratoy@mail.com", new HashSet<>(Arrays.asList(address1, address2)), null, null);

        List<Employee> employees = Arrays.asList(employee1, employee2, employee3, employee4, employee5);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Employee> page = new PageImpl<>(employees, pageable, 2000);


        when(service.getAllWithPagination(any(Pageable.class))).thenReturn(page);

        EmployeeReadDto dto = new EmployeeReadDto();
        EmployeeReadDto dtoTwo = new EmployeeReadDto();
        EmployeeReadDto dtoThree = new EmployeeReadDto();
        EmployeeReadDto dtoFour = new EmployeeReadDto();
        EmployeeReadDto dtoFive = new EmployeeReadDto();

        when(employeeMapper.toEmployeeReadDto(employee1)).thenReturn(dto);
        when(employeeMapper.toEmployeeReadDto(employee2)).thenReturn(dtoTwo);
        when(employeeMapper.toEmployeeReadDto(employee3)).thenReturn(dtoThree);
        when(employeeMapper.toEmployeeReadDto(employee4)).thenReturn(dtoFour);
        when(employeeMapper.toEmployeeReadDto(employee5)).thenReturn(dtoFive);


        MvcResult result = mockMvc.perform(get("/api/users/pages")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();


        verify(service).getAllWithPagination(eq(pageable));
        verify(employeeMapper, times(1)).toEmployeeReadDto(employee1);
        verify(employeeMapper, times(1)).toEmployeeReadDto(employee2);
        verify(employeeMapper, times(1)).toEmployeeReadDto(employee3);

        String contentType = result.getResponse().getContentType();
        assertNotNull(contentType);
        assertTrue(contentType.contains(MediaType.APPLICATION_JSON_VALUE));
        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);

    }

    @Test
    @DisplayName("GET API -> /api/employees/city")
    @WithMockUser(roles = "USER")
    public void findEmployeesByCityTest() throws Exception {
        Employee employee1 = Employee.builder()
                .name("John")
                .country("England")
                .gender(Gender.M)
                .build();
        Employee employee2 = Employee.builder()
                .name("Jane")
                .country("USA")
                .gender(Gender.F)
                .build();

        List<Employee> employeesInLondon = List.of(employee1);
        List<Employee> employeesInNewYork = List.of(employee2);

        when(service.findEmployeesByCity("London")).thenReturn(employeesInLondon);
        when(service.findEmployeesByCity("New York")).thenReturn(employeesInNewYork);

        mockMvc.perform(get("/api/employees/city")
                        .param("city", "London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John")));

        mockMvc.perform(get("/api/employees/city")
                        .param("city", "New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Jane")));

        verify(service, times(1)).findEmployeesByCity("London");
        verify(service, times(1)).findEmployeesByCity("New York");
    }

    @Test
    @DisplayName("GET API -> /api/employees/street")
    @WithMockUser(roles = "USER")
    public void findEmployeesByStreetAndGenderTest() throws Exception {
        Employee employee1 = Employee.builder()
                .name("John")
                .country("England")
                .gender(Gender.M)
                .build();
        Employee employee2 = Employee.builder()
                .name("Jane")
                .country("USA")
                .gender(Gender.F)
                .build();

        List<Employee> employeesInBakerStreet = List.of(employee1);
        List<Employee> employeesInFifthAvenue = List.of(employee2);

        when(service.findEmployeesByStreetAndGender("Baker Street", Gender.M)).thenReturn(employeesInBakerStreet);
        when(service.findEmployeesByStreetAndGender("Fifth Avenue", Gender.F)).thenReturn(employeesInFifthAvenue);

        mockMvc.perform(get("/api/employees/street")
                        .param("street", "Baker Street")
                        .param("gender", "M"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John")));

        mockMvc.perform(get("/api/employees/street")
                        .param("street", "Fifth Avenue")
                        .param("gender", "F"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Jane")));

        verify(service, times(1)).findEmployeesByStreetAndGender("Baker Street", Gender.M);
        verify(service, times(1)).findEmployeesByStreetAndGender("Fifth Avenue", Gender.F);
    }

}