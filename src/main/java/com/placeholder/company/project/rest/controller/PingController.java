package com.placeholder.company.project.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController extends AbstractController {

	@RequestMapping( value = "/ping", method = RequestMethod.POST )
	public ResponseEntity ping() {
		return ResponseEntity.ok().body( "Pong!" );
	}
}
