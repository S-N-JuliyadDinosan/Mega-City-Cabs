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
	public static final String DRIVER = BASE_URL + "/drivers";
	public static final String ASSIGN_CAR_TO_DRIVER = "/{driverId}/assign-car/{carId}";

	//Car
	public static final String CAR = BASE_URL + "/car";
	public static final String CAR_STATUS = "/{id}/status";
	public static final String GET_AVAILABLE_CARS = "/available";
	public static final String SPECIFIC_CAR_AVAILABLE = "/{id}/availability";

	//Customer
	public static final String CUSTOMER = BASE_URL + "/customer";
	public static final String REGISTER = "/register";

	//Booking
	public static final String BOOKING = BASE_URL + "/booking";
	public static final String PENDING_BOOKINGS = "/pending";
	public static final String CUSTOMER_GET_THEIR_BOOKINGS = "/customer/{customerId}";
	public static final String DRIVER_GET_THEIR_BOOKINGS = "/driver/{driverId}";
	public static final String ADMIN_ASSIGN_DRIVER = "/admin/assign/{id}";
	public static final String CUSTOMER_CONFIRM_BOOKING_BY_AMOUNT = "/{id}/confirm";
	public static final String CUSTOMER_OR_ADMIN_CANCEL_BOOKING = "/{id}/cancel";
	public static final String DRIVER_COMPLETE_BOOKING = "/{id}/complete";
	public static final String START_RIDE = "/start/{id}";
	public static final String GET_BILLING_DETAILS = "/billing/{id}";

	//SystemLogs
	public static final String SYSTEM_LOGS = BASE_URL + "/logs";
	public static final String GET_LOGS_BY_CUSTOMER = "/customer/{customerId}";
	public static final String GET_LOGS_BY_ADMIN = "/admin/{adminId}";
	public static final String GET_LOGS_BY_DRIVER = "/driver/{driverId}";
	public static final String GET_LOGS_BY_LEVEL = "/level/{logLevel}";

	//Help
	public static final String HELP = BASE_URL + "/help";
	public static final String GET_HELP_BY_CATEGORY = "/category/{category}";




	//Pricing
	public static final String PRICING = BASE_URL + "/pricing";






	private EndpointBundle() {
	}
}
