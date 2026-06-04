package com.proyecto1.proyecto1progra4.Logic;

import com.proyecto1.proyecto1progra4.Data.Empresa;
import com.proyecto1.proyecto1progra4.Data.Oferente;
import com.proyecto1.proyecto1progra4.Data.Usuario;
import com.proyecto1.proyecto1progra4.Services.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registro")
public class ControllerRegistro {

    @Autowired private RegistroService registroService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping({"", "/"})
    public String index() {
        return "registro/index";
    }

    @GetMapping("/empresa")
    public String formEmpresa() {
        return "registro/empresa";
    }

    @PostMapping("/empresa")
    public String registrarEmpresa(@RequestParam("correo") String correo,
                                   @RequestParam("clave") String clave,
                                   @RequestParam("nombre") String nombre,
                                   @RequestParam("localizacion") String localizacion,
                                   @RequestParam("telefono") String telefono,
                                   @RequestParam(value = "descripcion", required = false) String descripcion,
                                   Model model) {
        try {
            Usuario u = new Usuario();
            u.setUsername(correo);
            u.setClave(passwordEncoder.encode(clave));

            Empresa e = new Empresa();
            e.setNombre(nombre);
            e.setLocalizacion(localizacion);
            e.setTelefono(telefono);
            e.setDescripcion(descripcion);

            registroService.registrarEmpresa(u, e);
            return "redirect:/registro/exito?tipo=empresa";

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "registro/empresa";
        }
    }


    @GetMapping("/oferente")
    public String formOferente() {
        return "registro/oferente";
    }

    @PostMapping("/oferente")
    public String registrarOferente(@RequestParam("correo") String correo,
                                    @RequestParam("clave") String clave,
                                    @RequestParam("identificacion") String identificacion,
                                    @RequestParam("nombre") String nombre,
                                    @RequestParam("primerApellido") String primerApellido,
                                    @RequestParam("nacionalidad") String nacionalidad,
                                    @RequestParam("telefono") String telefono,
                                    @RequestParam("lugarResidencia") String lugarResidencia,
                                    Model model) {
        try {
            Usuario u = new Usuario();
            u.setUsername(correo);
            u.setClave(passwordEncoder.encode(clave));

            Oferente o = new Oferente();
            o.setIdentificacion(identificacion);
            o.setNombre(nombre);
            o.setPrimerApellido(primerApellido);
            o.setNacionalidad(nacionalidad);
            o.setTelefono(telefono);
            o.setLugarResidencia(lugarResidencia);

            registroService.registrarOferente(u, o);
            return "redirect:/registro/exito?tipo=oferente";

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "registro/oferente";
        }
    }

    @GetMapping("/exito")
    public String exito(@RequestParam("tipo") String tipo, Model model) {
        model.addAttribute("tipo", tipo);
        return "registro/exito";
    }
}