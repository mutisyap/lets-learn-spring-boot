# Beyond Basic CRUD
- Our needs will definitely go beyond basic CRUD. For example, we will need to create multiple employees, get employees in department, ...
- Our entity class.
```
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    private Department department;
    @ManyToMany
    private List<Employee> supervisors;

    private LocalDate dateOfBirth;

    private LocalDate dateOfEmployment;

    private Integer salary;

    @Enumerated(EnumType.STRING)
    private EmployeeRole role;
}
```

Some queries:
### Get employees who are supervisors as List and Page
- Query Method
```java
List<Employee> findAllByRole(EmployeeRole role);
Page<Employee> findAllByRole(EmployeeRole role, Pageable pageable);
```
- JPQL
```java 
@Query("SELECT e FROM Employee e WHERE e.role = 'SUPERVISOR'")
List<Employee> findAllSupervisors(); // Here, the name doesn't matter

@Query("SELECT e FROM Employee e WHERE e.role = 'SUPERVISOR'")
Page<Employee> findAllSupervisors(Pageable pageable);
```

- Native SQL => Specific to the storage engine. In this case, MySQL.
```java
@Query(value = "SELECT * FROM employees WHERE role = 'SUPERVISOR'", nativeQuery = true)
List<Employee> findAllSupervisors();

// Page - We need to provide the count query
@Query(value = "SELECT * FROM employees WHERE role = 'SUPERVISOR'", countQuery = "SELECT COUNT(*) FROM employees WHERE role = 'SUPERVISOR'", nativeQuery = true)
Page<Employee> findAllSupervisors(Pageable pageable);
```

### Search employees by name, or department name, or company name
+ Here, the user gives us just one name. We need to search in all three fields.
- Query Method
```java
List<Employee> findAllByNameContainingOrDepartmentNameContainingOrDepartmentCompanyCompanyNameContaining(String name, String departmentName, String companyName);
Page<Employee> findAllByNameContainingOrDepartmentNameContainingOrDepartmentCompanyCompanyNameContaining(String name, String departmentName, String companyName, Pageable pageable);

// The client will be as follows
Page<Employee> searchByName(String name, Pageable pageable){
    return employeeRepository.findAllByNameContainingOrDepartmentNameContainingOrDepartmentCompanyCompanyNameContaining(name, name, name, pageable);
}
```

- JPQL
```java
@Query("SELECT e FROM Employee e WHERE e.name LIKE %:name% OR e.department.name LIKE %:name% OR e.department.company.companyName LIKE %:name%")
List<Employee> searchByName(@Param("name") String name);
// page
@Query("SELECT e FROM Employee e WHERE e.name LIKE %:name% OR e.department.name LIKE %:name% OR e.department.company.companyName LIKE %:name%")
Page<Employee> searchByName(@Param("name") String name, Pageable pageable);

// or using parameter position
@Query("SELECT e FROM Employee e WHERE e.name LIKE %?1% OR e.department.name LIKE %?1% OR e.department.company.companyName LIKE %?1%")
List<Employee> searchByName(String name);

// page
@Query("SELECT e FROM Employee e WHERE e.name LIKE %?1% OR e.department.name LIKE %?1% OR e.department.company.companyName LIKE %?1%")
Page<Employee> searchByName(String name, Pageable pageable);
```

+ Native SQL
```java
@Query(value = "SELECT * FROM employees WHERE name LIKE %:name% OR department_name LIKE %:name% OR company_name LIKE %:name%", nativeQuery = true)
List<Employee> searchByName(@Param("name") String name);

// page
@Query(value = "SELECT * FROM employees WHERE name LIKE %:name% OR department_name LIKE %:name% OR company_name LIKE %:name%", countQuery = "SELECT COUNT(*) FROM employees WHERE name LIKE %:name% OR department_name LIKE %:name% OR company_name LIKE %:name%", nativeQuery = true)
Page<Employee> searchByName(@Param("name") String name, Pageable pageable);
```
_Since java 8, you can decide to remove the @Param annotation and use the parameter name directly._

+ Query by example
- Here, the client does the hard work, and supplies us with an example object. We just need to use it. Remember, our EmployeeRepository interface extends JpaRespository, which extends QueryByExampleExecutor, which enables us run this query.
```java
public Page<Employee> search(String name, Pageable pageable) {
    // first, create an example object
    Employee employee = new Employee();
    employee.setName(name);
    Department department = new Department();
    department.setName(name);
    Company company = new Company();
    company.setCompanyName(name);
    department.setCompany(company);
    employee.setDepartment(department);
    // then, create an example matcher
    ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher("department.name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher("department.company.companyName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
    // then, create an example
    Example<Employee> example = Example.of(employee, matcher);
    // then, use it
    return employeeRepository.findAll(example, pageable);
}
```
+ Specifications
- Here, we create a specification object, and use it.
```java
public Page<Employee> search(String name, Pageable pageable) {
    Specification<Employee> specification = (Specification<Employee>) (root, criteriaQuery, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("department").get("name")), "%" + name.toLowerCase() + "%"));
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("department").get("company").get("companyName")), "%" + name.toLowerCase() + "%"));
        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    };

    return employeeRepository.findAll(specification, pageable);
}
```