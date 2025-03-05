package org.iesvdm.pillowtaskerback.repository;

import org.iesvdm.pillowtaskerback.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {


}
