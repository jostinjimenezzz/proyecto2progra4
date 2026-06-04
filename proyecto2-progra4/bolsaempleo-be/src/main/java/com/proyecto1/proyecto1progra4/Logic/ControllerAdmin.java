package com.proyecto1.proyecto1progra4.Logic;

import com.proyecto1.proyecto1progra4.Services.AprobacionService;
import com.proyecto1.proyecto1progra4.Services.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ControllerAdmin {

    @Autowired
    private AprobacionService aprobacionService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    @GetMapping("/Administrador/dashboard")
    public String dashboard() {
        return "Administrador/dashboard";
    }

    @GetMapping("/Administrador/empresas")
    public String empresasPendientes(Model model) {
        model.addAttribute("empresas", aprobacionService.empresasPendientes());
        return "Administrador/empresas";
    }

    @PostMapping("/Administrador/empresas/aprobar/{id}")
    public String aprobarEmpresa(@PathVariable Long id) {
        aprobacionService.aprobarEmpresa(id);
        return "redirect:/Administrador/empresas";
    }

    @GetMapping("/Administrador/oferentes")
    public String oferentesPendientes(Model model) {
        model.addAttribute("oferentes", aprobacionService.oferentesPendientes());
        return "Administrador/oferentes";
    }

    @PostMapping("/Administrador/oferentes/aprobar/{id}")
    public String aprobarOferente(@PathVariable Long id) {
        aprobacionService.aprobarOferente(id);
        return "redirect:/Administrador/oferentes";
    }

    @GetMapping("/Administrador/caracteristicas")
    public String caracteristicas(Model model) {
        model.addAttribute("raices", caracteristicaService.caracteristicasRaiz());
        model.addAttribute("todas", caracteristicaService.todasLasCaracteristicas());
        return "Administrador/caracteristicas";
    }

    @PostMapping("/Administrador/caracteristicas/crear")
    public String crearCaracteristica(@RequestParam String nombre,
                                      @RequestParam(required = false) Long idPadre) {
        caracteristicaService.crearCaracteristica(nombre, idPadre);
        return "redirect:/Administrador/caracteristicas";
    }
}