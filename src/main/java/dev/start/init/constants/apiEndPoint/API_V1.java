package dev.start.init.constants.apiEndPoint;

/**
 * V1 api endpoints are define here
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
public interface API_V1 {
    //THIS IS THE BASE WHICH ARE COMMON FOR ALL THE ENDPOINTS
    String BASE_ENDPOINT = "/api/v1";

    String AUTH_URL = BASE_ENDPOINT + "/user";
    String EMPLOYEE_URL = BASE_ENDPOINT+"/employees";
    String COMPANY_URL = BASE_ENDPOINT+"/companies";
    String API_V1_ADDRESS_URL = BASE_ENDPOINT+"/addresses";


    String ROLE_PERMISSION_URL = BASE_ENDPOINT+"/permissions";
}

