package com.placeholder.company.project.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.placeholder.company.project.db.model.Admin;

@Repository( "adminRepository" )
public interface AdminRepository extends JpaRepository<Admin, String> {

}
