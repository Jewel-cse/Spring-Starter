package dev.start.init.controller.v1.report;


import dev.start.init.constants.apiEndPoint.API_V1;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(API_V1.Report)
public class RoportController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/identity-card")
    public ResponseEntity<Resource> generateReport(@RequestParam("") long billNo) {
        try {
            // Load JRXML file from resources
            ClassPathResource jrxmlFile = new ClassPathResource("report/wasaBill.jrxml");
            InputStream inputStream = jrxmlFile.getInputStream();

            // Compile the JRXML template
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // Create parameters map
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("BILL_NO", billNo);

            // Create a connection from DataSource
            try (Connection connection = dataSource.getConnection()) {

                // Fill the report with parameters and data source (JDBC)
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                        connection);

                // Export the report to a byte array
                byte[] pdfReport = JasperExportManager.exportReportToPdf(jasperPrint);

                // Return the PDF as a response entity
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", "bill_report.pdf");
//
//        return ResponseEntity.ok()
//            .headers(headers)
//            .body(pdfReport);
//      }

//      =============================================

                ByteArrayResource resource = new ByteArrayResource(pdfReport);

                return ResponseEntity.ok().headers(headers).body(resource);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            }
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
