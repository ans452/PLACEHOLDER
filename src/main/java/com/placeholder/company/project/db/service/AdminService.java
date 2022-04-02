package com.placeholder.company.project.db.service;

import com.placeholder.company.project.db.model.Admin;
import com.placeholder.company.project.rest.GeneralHttpException;

public interface AdminService {

	Admin getAdminByUsername( String username );

	Admin getAdminByToken( String token );

	void createAdmin( Admin admin ) throws GeneralHttpException;
}
