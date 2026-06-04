package com.proyecto1.proyecto1progra4.Services;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.proyecto1.proyecto1progra4.Data.Empresa;
import com.proyecto1.proyecto1progra4.Data.Oferente;
import com.proyecto1.proyecto1progra4.Repositories.EmpresaRepository;
import com.proyecto1.proyecto1progra4.Repositories.OferenteRepository;
import com.proyecto1.proyecto1progra4.Repositories.PuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReporteAdminService {

    @Autowired private PuestoRepository puestoRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private OferenteRepository oferenteRepository;

    private static final Font H1 = new Font(Font.HELVETICA, 16, Font.BOLD);
    private static final Font H2 = new Font(Font.HELVETICA, 13, Font.BOLD);
    private static final Font NORMAL = new Font(Font.HELVETICA, 10, Font.NORMAL);

    public byte[] pdfPuestosPorMes(int anio) {
        List<Object[]> rows = puestoRepository.contarPuestosPorMes(anio);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.add(new Paragraph("Reporte: Puestos por mes (" + anio + ")", H1));
            doc.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 3f});

            table.addCell(headerCell("Mes"));
            table.addCell(headerCell("Cantidad de puestos"));

            int total = 0;
            for (Object[] r : rows) {
                int mes = ((Number) r[0]).intValue();
                int cantidad = ((Number) r[1]).intValue();
                total += cantidad;

                table.addCell(bodyCell(nombreMes(mes)));
                table.addCell(bodyCell(String.valueOf(cantidad)));
            }

            PdfPCell totalCell = new PdfPCell(new Phrase("Total", new Font(Font.HELVETICA, 10, Font.BOLD)));
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCell.setBackgroundColor(new Color(245, 245, 245));
            totalCell.setPadding(6);
            table.addCell(totalCell);

            PdfPCell totalVal = new PdfPCell(new Phrase(String.valueOf(total), new Font(Font.HELVETICA, 10, Font.BOLD)));
            totalVal.setPadding(6);
            totalVal.setBackgroundColor(new Color(245, 245, 245));
            table.addCell(totalVal);

            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de puestos por mes", e);
        }
    }

    public byte[] pdfEmpresasPendientesYAprobadas() {
        List<Empresa> pendientes = empresaRepository.findByEstadoAprobacion("PENDIENTE");
        List<Empresa> aprobadas = empresaRepository.findByEstadoAprobacion("APROBADO");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.add(new Paragraph("Reporte: Empresas (Pendientes / Aprobadas)", H1));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Pendientes (" + pendientes.size() + ")", H2));
            doc.add(tablaEmpresas(pendientes));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Aprobadas (" + aprobadas.size() + ")", H2));
            doc.add(tablaEmpresas(aprobadas));

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de empresas", e);
        }
    }

    public byte[] pdfOferentesPendientesYAprobados() {
        List<Oferente> pendientes = oferenteRepository.findByEstadoAprobacion("PENDIENTE");
        List<Oferente> aprobados = oferenteRepository.findByEstadoAprobacion("APROBADO");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.add(new Paragraph("Reporte: Oferentes (Pendientes / Aprobados)", H1));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Pendientes (" + pendientes.size() + ")", H2));
            doc.add(tablaOferentes(pendientes));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Aprobados (" + aprobados.size() + ")", H2));
            doc.add(tablaOferentes(aprobados));

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de oferentes", e);
        }
    }

    private PdfPTable tablaEmpresas(List<Empresa> empresas) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 4f, 3f, 2f, 2f});

        table.addCell(headerCell("Correo"));
        table.addCell(headerCell("Nombre"));
        table.addCell(headerCell("Localización"));
        table.addCell(headerCell("Teléfono"));
        table.addCell(headerCell("Estado"));

        for (Empresa e : empresas) {
            table.addCell(bodyCell(e.getUsuario() != null ? e.getUsuario().getUsername() : ""));
            table.addCell(bodyCell(nullSafe(e.getNombre())));
            table.addCell(bodyCell(nullSafe(e.getLocalizacion())));
            table.addCell(bodyCell(nullSafe(e.getTelefono())));
            table.addCell(bodyCell(nullSafe(e.getEstadoAprobacion())));
        }
        return table;
    }

    private PdfPTable tablaOferentes(List<Oferente> oferentes) throws DocumentException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 3f, 3f, 2f, 3f, 2f});

        table.addCell(headerCell("Correo"));
        table.addCell(headerCell("Nombre"));
        table.addCell(headerCell("Identificación"));
        table.addCell(headerCell("Nacionalidad"));
        table.addCell(headerCell("Residencia"));
        table.addCell(headerCell("Estado"));

        for (Oferente o : oferentes) {
            table.addCell(bodyCell(o.getUsuario() != null ? o.getUsuario().getUsername() : ""));
            table.addCell(bodyCell(nullSafe(o.getNombre()) + " " + nullSafe(o.getPrimerApellido())));
            table.addCell(bodyCell(nullSafe(o.getIdentificacion())));
            table.addCell(bodyCell(nullSafe(o.getNacionalidad())));
            table.addCell(bodyCell(nullSafe(o.getLugarResidencia())));
            table.addCell(bodyCell(nullSafe(o.getEstadoAprobacion())));
        }
        return table;
    }

    private PdfPCell headerCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell bodyCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, NORMAL));
        cell.setPadding(6);
        return cell;
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    private String nombreMes(int mes) {
        return switch (mes) {
            case 1 -> "Enero";
            case 2 -> "Febrero";
            case 3 -> "Marzo";
            case 4 -> "Abril";
            case 5 -> "Mayo";
            case 6 -> "Junio";
            case 7 -> "Julio";
            case 8 -> "Agosto";
            case 9 -> "Septiembre";
            case 10 -> "Octubre";
            case 11 -> "Noviembre";
            case 12 -> "Diciembre";
            default -> "Mes " + mes;
        };
    }
}