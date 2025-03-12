package com.dino.Mega_City_Cabs.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Contains custom error codes loaded from the properties file.
 */
@Getter
@Setter
@Component
@PropertySource("classpath:ErrorMessages.properties")
public class ErrorCodes {

	@Value("${validation.user.notExists}")
	private String userNotExist;

	@Value("${validation.email.alreadyExist}")
	private String emailAlreadyExist;

	@Value("${validation.id.notExist}")
	private String idNotExist;

	@Value("${validation.entry.alreadyExist}")
	private String entryAlreadyExist;

	@Value("${validation.entry.notValid}")
	private String inputNotValid;

}
