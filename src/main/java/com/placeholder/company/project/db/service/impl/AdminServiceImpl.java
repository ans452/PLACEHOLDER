package com.placeholder.company.project.db.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.placeholder.company.project.db.model.Admin;
import com.placeholder.company.project.db.repository.AdminRepository;
import com.placeholder.company.project.db.service.AdminService;
import com.placeholder.company.project.rest.GeneralHttpException;
import com.placeholder.company.project.rest.api.constants.ErrorCode;

@Service( "adminService" )
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;

	public AdminServiceImpl( @Qualifier( "adminRepository" ) AdminRepository adminRepository ) {
		this.adminRepository = adminRepository;
	}

	@Override
	public Admin getAdminByUsername( String username ) {
		return adminRepository.findOne( username );
	}

	@Override
	public Admin getAdminByToken( String token ) {
		return adminRepository.findAll().stream().filter( e -> e.getToken().equals( token ) ).findFirst().orElse( null );
	}

	@Override
	public void createAdmin( Admin admin ) throws GeneralHttpException {
		Admin existingEntry = adminRepository.findOne( admin.getUsername() );

		if ( existingEntry != null ) {
			// Generally, shouldn't happen, unless implemented incorrectly.
			throw new GeneralHttpException( ErrorCode.SYSTEM, "Something went wrong." );
		}

		adminRepository.save( admin );
	}
}
