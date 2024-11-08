package dev.start.init.controller.v1.identityCard;


import dev.start.init.annotation.IgnoreResponseBinding;
import dev.start.init.constants.apiEndPoint.API_V1;
import dev.start.init.dto.EmployeeDto;
import dev.start.init.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(API_V1.IT_CARD)
public class RoportController {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping()
    public ResponseEntity<Resource> generateIdCard(@RequestParam long empId) {
        try {
            EmployeeDto employee = employeeService.getEmployeeById(empId);
            if (employee == null) {
                LOG.warn("Employee with ID {} not found.", empId);
                return ResponseEntity.notFound().build();
            }

            // Load JRXML file from resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("identityCard/emp_id_card.jrxml");
            if (inputStream == null) {
                LOG.error("JRXML file 'identityCard/emp_id_card.jrxml' not found in classpath.");
                return ResponseEntity.status(500).body(null);
            }
            LOG.debug("JRXML file loaded successfully.");

            // Compile the JRXML template
            JasperReport jasperReport;
            try {
                jasperReport = JasperCompileManager.compileReport(inputStream);
                LOG.debug("JRXML compiled successfully.");
            } catch (JRException e) {
                LOG.error("Error compiling JRXML file.", e);
                return ResponseEntity.status(500).body(null);
            }

            // Create parameters map
            Map<String, Object> parameters = new HashMap<>();

            // Load Company Logo as InputStream
            InputStream logoStream = getClass().getClassLoader().getResourceAsStream("identityCard/image/CompanyLogo.png");
            if (logoStream == null) {
                LOG.error("Company logo 'identityCard/image/CompanyLogo.png' not found in classpath.");
                return ResponseEntity.status(500).body(null);
            }
            parameters.put("CompanyLogo", logoStream);
            LOG.debug("CompanyLogo parameter set.");

            // Load Employee Photo as InputStream
            InputStream photoStream = getClass().getClassLoader().getResourceAsStream("identityCard/image/EmployeePhoto.jpg");
            if (photoStream == null) {
                LOG.error("Employee photo 'identityCard/image/EmployeePhoto.jpg' not found in classpath.");
                return ResponseEntity.status(500).body(null);
            }
            parameters.put("EmployeePhoto", photoStream);
            LOG.debug("EmployeePhoto parameter set.");

            // Add other parameters as needed
            // For example, if you have company name or other static data
            parameters.put("CompanyName", "VECTOR"); // Example static parameter
            LOG.debug("CompanyName parameter set.");
            parameters.put("empName",employee.getEmpName());
            parameters.put("empCode",employee.getEmpCode());
            parameters.put("empDesignation",employee.getEmpDesignation());
            parameters.put("joiningDate",employee.getJoiningDate());


            // Create a connection from DataSource
            try (Connection connection = dataSource.getConnection()) {
                LOG.debug("Database connection established.");

                // Fill the report with parameters and data source (JDBC)
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
                LOG.debug("Report filled successfully.");

                // Export the report to a byte array (PDF)
                byte[] pdfReport = JasperExportManager.exportReportToPdf(jasperPrint);
                LOG.debug("Report exported to PDF successfully.");

                // Prepare HTTP response
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", "identityCard.pdf");

                ByteArrayResource resource = new ByteArrayResource(pdfReport);

                return ResponseEntity.ok().headers(headers).body(resource);

            } catch (Exception e) {
                LOG.error("Error filling the report or exporting to PDF.", e);
                return ResponseEntity.status(500).body(null);
            }

        } catch (Exception e) {
            LOG.error("Unexpected error occurred while generating ID card.", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
