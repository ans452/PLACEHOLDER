package com.placeholder.company.project.rest.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.placeholder.company.project.db.model.Admin;
import com.placeholder.company.project.db.service.AdminService;
import com.placeholder.company.project.rest.GeneralHttpException;
import com.placeholder.company.project.rest.api.AdminAuthenticationResponse;
import com.placeholder.company.project.rest.api.constants.ErrorCode;
import com.placeholder.company.project.rest.controller.tools.BasicAccessAuthentication;

import java.security.NoSuchAlgorithmException;

@RestController
public class AdminAuthenticationController extends AbstractController {

	public static final String authorizationToken = "#12345ASDqwerty";

	private final AdminService adminService;

	public AdminAuthenticationController( AdminService adminService ) {
		this.adminService = adminService;
	}

	@RequestMapping( value = "/admin/authentication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity register( @RequestHeader( value = HttpHeaders.AUTHORIZATION, required = false ) String authorization, @RequestBody String request ) {
		try {
			validateAuthorizationHeader( authorization );
			BasicAccessAuthentication authentication = BasicAccessAuthentication.parseAuthorizationHeader( authorization );


			Admin admin = this.adminService.getAdminByUsername( authentication.getUsername() );
			if(admin == null){
				adminService.createAdmin(new Admin(authentication.getUsername(),
						authentication.getPassword(), request));
			}
			else {
				if (!admin.isAdminAuthenticated(authentication)) {
					throw new GeneralHttpException(HttpStatus.NOT_FOUND, ErrorCode.INVALID, "Invalid username or password.");
				}
			}
			AdminAuthenticationResponse response = new AdminAuthenticationResponse();
			response.setErrorCode(ErrorCode.OK);
			// Not sure how to get the same token next time...
      // response.setToken( RandomStringUtils.randomAlphanumeric(36) );
      String token = authentication.getToken();
      response.setToken( token );
      // admin.setToken( token );
			return ResponseEntity
					.ok()
					.body( response );
		} catch ( GeneralHttpException exception ) {
			return exception.createErrorResponse();
		}
	}
}
