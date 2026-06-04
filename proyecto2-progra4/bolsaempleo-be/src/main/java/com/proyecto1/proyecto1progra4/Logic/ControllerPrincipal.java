package com.proyecto1.proyecto1progra4.Logic;

import java.util.List;

import com.proyecto1.proyecto1progra4.Data.Puesto;
import com.proyecto1.proyecto1progra4.Services.CaracteristicaService;
import com.proyecto1.proyecto1progra4.Services.PuestoPublicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("ControllerPrincipal")
public class ControllerPrincipal {

    @Autowired
    private PuestoPublicoService puestoPublicoService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Puesto> listaPuestos = puestoPublicoService.top5PuestosPublicosRecientes();
        model.addAttribute("puestos", listaPuestos);
        model.addAttribute("requisitos", puestoPublicoService.requisitosPorPuestos(listaPuestos));
        return "login/puestos";
    }

    @GetMapping("/puestos/buscar")
    public String buscarPuestos(@RequestParam(required = false) List<Long> caracteristicaIds,
                                Model model) {

        model.addAttribute("raices", caracteristicaService.caracteristicasRaiz());
        model.addAttribute("todas", caracteristicaService.todasLasCaracteristicas());

        if (caracteristicaIds != null && !caracteristicaIds.isEmpty()) {
            model.addAttribute("resultados", puestoPublicoService.buscarPuestosPublicos(caracteristicaIds));
            model.addAttribute("buscado", true);
        } else {
            model.addAttribute("resultados", List.of());
            model.addAttribute("buscado", false);
        }

        return "login/buscar";
    }

    @GetMapping("/puestos/detalle/{id}")
    public String detallePuesto(@PathVariable Long id, Model model) {
        Puesto puesto = puestoPublicoService.buscarPuestoPorId(id);
        model.addAttribute("puesto", puesto);
        model.addAttribute("requisitos", puestoPublicoService.requisitosDelPuesto(id));
        return "login/detalle";
    }
}