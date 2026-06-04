package com.proyecto1.proyecto1progra4.Logic;

import com.proyecto1.proyecto1progra4.Services.ReporteAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@Controller
public class ControllerReportesAdmin {

    @Autowired private ReporteAdminService reporteAdminService;

    @GetMapping("/Administrador/reportes")
    public String reportes(Model model) {
        model.addAttribute("anioActual", Year.now().getValue());
        return "Administrador/reportes";
    }

    @GetMapping("/Administrador/reportes/puestos-por-mes.pdf")
    public ResponseEntity<byte[]> puestosPorMes(@RequestParam("anio") int anio) {
        byte[] pdf = reporteAdminService.pdfPuestosPorMes(anio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"puestos_por_mes_" + anio + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/Administrador/reportes/empresas.pdf")
    public ResponseEntity<byte[]> empresas() {
        byte[] pdf = reporteAdminService.pdfEmpresasPendientesYAprobadas();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"empresas_pendientes_y_aprobadas.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/Administrador/reportes/oferentes.pdf")
    public ResponseEntity<byte[]> oferentes() {
        byte[] pdf = reporteAdminService.pdfOferentesPendientesYAprobados();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"oferentes_pendientes_y_aprobados.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}