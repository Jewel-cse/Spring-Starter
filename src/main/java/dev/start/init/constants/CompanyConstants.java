package dev.start.init.constants;

public final class CompanyConstants {

    public static final String COMPANY_CODE = "Company Code length should be between 3 to 50";
    public static final String COMPANY_NAME = "Company Name length should be between 3 to 100";
    public static final String COMPANY_ADDRESS = "Company Address length should be between 3 to 100";
    public static final String COMPANY_ESTABLISH_DATE="Company Establish Date must be Past";
    private  CompanyConstants(){
        new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
    }
}
