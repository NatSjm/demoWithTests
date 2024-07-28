package com.example.demowithtests;

import com.example.demowithtests.domain.Address;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Employee Repository Tests")
public class RepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("Save employee test")
    public void saveEmployeeTest() {

        var employee = Employee.builder()
                .name("Mark")
                .country("England")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("UK")
                                .build())))
                .gender(Gender.M)
                .build();

        employeeRepository.save(employee);

        Assertions.assertThat(employee.getId()).isGreaterThan(0);
        Assertions.assertThat(employee.getId()).isEqualTo(1);
        Assertions.assertThat(employee.getName()).isEqualTo("Mark");
    }

    @Test
    @Order(2)
    @DisplayName("Get employee by id test")
    public void getEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        Assertions.assertThat(employee.getId()).isEqualTo(1);
        Assertions.assertThat(employee.getName()).isEqualTo("Mark");
    }

    @Test
    @Order(3)
    @DisplayName("Get employees test")
    public void getListOfEmployeeTest() {

        var employeesList = employeeRepository.findAll();

        Assertions.assertThat(employeesList.size()).isGreaterThan(0);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("Update employee test")
    public void updateEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        employee.setName("Martin");
        var employeeUpdated = employeeRepository.save(employee);

        Assertions.assertThat(employeeUpdated.getName()).isEqualTo("Martin");

    }

    @Test
    @Order(5)
    @DisplayName("Find employee by gender test")
    public void findByGenderTest() {

        var employees = employeeRepository.findByGender(Gender.M.toString(), "UK");

        assertThat(employees.get(0).getGender()).isEqualTo(Gender.M);
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @DisplayName("Delete employee test")
    public void deleteEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        employeeRepository.delete(employee);

        Employee employeeNull = null;

        var optionalEmployee = Optional.ofNullable(employeeRepository.findByName("Martin"));

        if (optionalEmployee.isPresent()) {
            employeeNull = optionalEmployee.orElseThrow();
        }

        Assertions.assertThat(employeeNull).isNull();
    }

    @Test
    @Order(7)
    @Rollback(value = false)
    public void whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given
        Employee bob = new Employee();
        bob.setName("Bob");
        entityManager.persistAndFlush(bob);
        String newName = "Boob";
        // When
        employeeRepository.updateEmployeeByName(newName, bob.getId());
        // Then
        Employee found = employeeRepository.findByName(newName);
        assertThat(found.getName()).isEqualTo(newName);
    }

    @Test
    @Order(8)
    @DisplayName("Find employees by city test")
    public void findEmployeesByCityTest() {
        Address address1 = Address.builder()
                .city("London")
                .country("UK")
                .build();
        Address address2 = Address.builder()
                .city("New York")
                .country("USA")
                .build();
        Employee employee1 = Employee.builder()
                .name("John")
                .country("England")
                .addresses(new HashSet<>(Set.of(address1)))
                .gender(Gender.M)
                .build();
        Employee employee2 = Employee.builder()
                .name("Jane")
                .country("USA")
                .addresses(new HashSet<>(Set.of(address2)))
                .gender(Gender.F)
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> employeesInLondon = employeeRepository.findEmployeesByCity("London");
        assertThat(employeesInLondon).hasSize(1);
        assertThat(employeesInLondon.get(0).getName()).isEqualTo("John");

        List<Employee> employeesInNewYork = employeeRepository.findEmployeesByCity("New York");
        assertThat(employeesInNewYork).hasSize(1);
        assertThat(employeesInNewYork.get(0).getName()).isEqualTo("Jane");

        List<Employee> employeesInNonExistingCity = employeeRepository.findEmployeesByCity("NonExistingCity");
        assertThat(employeesInNonExistingCity).isEmpty();
    }

    @Test
    @Order(10)
    @DisplayName("Find employees by street and gender test")
    public void findEmployeesByStreetAndGenderTest() {
        Address address1 = Address.builder()
                .street("Baker Street")
                .country("UK")
                .build();
        Address address2 = Address.builder()
                .street("Fifth Avenue")
                .country("USA")
                .build();
        Employee employee1 = Employee.builder()
                .name("John")
                .country("England")
                .addresses(new HashSet<>(Set.of(address1)))
                .gender(Gender.M)
                .build();
        Employee employee2 = Employee.builder()
                .name("Jane")
                .country("USA")
                .addresses(new HashSet<>(Set.of(address2)))
                .gender(Gender.F)
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> employeesInBakerStreet = employeeRepository.findEmployeesByStreetAndGender("Baker Street", Gender.M);
        assertThat(employeesInBakerStreet).hasSize(1);
        assertThat(employeesInBakerStreet.get(0).getName()).isEqualTo("John");

        List<Employee> employeesInFifthAvenue = employeeRepository.findEmployeesByStreetAndGender("Fifth Avenue", Gender.F);
        assertThat(employeesInFifthAvenue).hasSize(1);
        assertThat(employeesInFifthAvenue.get(0).getName()).isEqualTo("Jane");
    }

    @Test
    public void testFindEmployeeByEmailNotNull() {

        Employee employee1 = new Employee();
        employee1.setName("John");
        employee1.setEmail("john@gmail.com");

        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setName("Jane");
        employee2.setEmail(null);  // not setting email

        employeeRepository.save(employee2);

        Employee foundEmployee = employeeRepository.findEmployeeByEmailNotNull();
        assertNotNull(foundEmployee.getEmail() != null);
    }

}
