package dev.start.init.constants;

/**
 * Holds constants used for Employee-related validation messages.
 *
 * <p>This class centralizes error messages and other constants for the Employee entity to ensure consistency across the application.</p>
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
public final class EmployeeConstants {

    // Prevent instantiation
    private EmployeeConstants() {
        new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
    }

    // Validation Messages
    public static final String BLANK_EMP_CODE = "Employee code must not be blank.";
    public static final String BLANK_EMP_NAME = "Employee name must not be blank.";
    public static final String BLANK_EMP_DESIG = "Employee designation must not be blank.";
    public static final String NULL_JOINING_DATE = "Joining date must not be null.";
    public static final String INVALID_JOINING_DATE = "Joining date must be a past or present date.";
    public static final String INVALID_SALARY = "Employee salary must be non-negative and between 0.000 and 999999999.999";
    public static final String NULL_COMPANY_ID = "Company Id shouldn't be Null or Blank";
}

