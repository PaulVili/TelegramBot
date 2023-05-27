package com.sulikashvili.bot.repositories;

import com.sulikashvili.bot.entities.TeacherOffice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TeacherOfficeRepository extends CrudRepository<TeacherOffice, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM teacher_office_table p WHERE p.first_name = :name")
    List<TeacherOffice> findName(@Param("name") String name);
}
