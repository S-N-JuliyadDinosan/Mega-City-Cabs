package com.dino.Mega_City_Cabs.utils;

public class EndpointBundle {
	public static final String BASE_URL = "/api/v1";
	public static final String ID ="/{id}";
	public static final String SEARCH = "/search";

	//UserController
	public static final String USER = BASE_URL + "/users";
	public static final String LOGIN_USER = "/login";
	public static final String LOGOUT_USER = "/logout";
	public static final String EXPORT_USER = "/export";
	public static final String IMPORT_USER = "/import";
	public static final String PASSWORD = "/change-password";
	public static final String PASSWORD_FORGOT = "/forgot-password";
	public static final String PASSWORD_RESET = "/reset-password";

    //Admin
	public static final String ADMIN = BASE_URL + "/admin";

	//Driver
	public static final String DRIVER = BASE_URL + "/driver";
	public static final String ASSIGN_CAR_TO_DRIVER = "/{driverId}/assign-car/{carId}";



	private EndpointBundle() {
	}
}
