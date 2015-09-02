package cl.sebastian.chadoskyx.cli.run;

import cl.chadoskyx.titulo.modelo.Atencion;
import cl.chadoskyx.titulo.modelo.Categoria;
import cl.chadoskyx.titulo.modelo.Diagnostico;
import cl.chadoskyx.titulo.modelo.Ficha;
import cl.chadoskyx.titulo.modelo.Medico;
import cl.chadoskyx.titulo.modelo.Paciente;
import cl.chadoskyx.titulo.modelo.Sucursal;
import cl.chadoskyx.titulo.servicio.ServicioAtencion;
import cl.chadoskyx.titulo.servicio.ServicioDiagnostico;
import cl.chadoskyx.titulo.servicio.ServicioEmail;
import cl.chadoskyx.titulo.servicio.ServicioEspecie;
import cl.chadoskyx.titulo.servicio.ServicioGeografico;
import cl.chadoskyx.titulo.servicio.ServicioPaciente;
import cl.chadoskyx.titulo.servicio.ServicioSQL;
import cl.chadoskyx.titulo.servicio.ServicioUsuario;
import cl.chadoskyx.titulo.servicio.ServicioVeterinaria;
import cl.chadoskyx.titulo.utils.FechaUtils;
import cl.sebastian.chadoskyx.cli.CliUtils;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.tools.Diagnostic;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Sebastián Salazar Molina <sebasalazar@gmail.com>
 */
public class App implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PrintStream consola = System.out;

        try {
            ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"chadoskyx-cli-context.xml", "modelo-context.xml"});
            // Propiedades dependientes del sistema operativo
            String cwd = System.getProperty("user.dir");
            String separador = System.getProperty("file.separator");

            String inicio = "Inicio de la Aplicación: " + FechaUtils.crearFechaStrCL(new Date());
            consola.println(inicio);
            logger.info(inicio);

            // Instancio Servicios, súper lento :P
            ServicioAtencion servicioAtencion = context.getBean("servicioAtencion", ServicioAtencion.class);
            ServicioDiagnostico servicioDiagnostico = context.getBean("servicioDiagnostico", ServicioDiagnostico.class);
            ServicioEmail servicioEmail = context.getBean("servicioEmail", ServicioEmail.class);
            ServicioEspecie servicioEspecie = context.getBean("servicioEspecie", ServicioEspecie.class);
            ServicioGeografico servicioGeografico = context.getBean("servicioGeografico", ServicioGeografico.class);
            ServicioPaciente servicioPaciente = context.getBean("servicioPaciente", ServicioPaciente.class);
            ServicioSQL servicioSQL = context.getBean("servicioSQL", ServicioSQL.class);
            ServicioUsuario servicioUsuario = context.getBean("servicioUsuario", ServicioUsuario.class);
            ServicioVeterinaria servicioVeterinaria = context.getBean("servicioVeterinaria", ServicioVeterinaria.class);

            Sucursal sucursal = servicioVeterinaria.consultarSucursal(1);
            Medico medico = servicioVeterinaria.consultarMedico(1);
            Paciente paciente = servicioPaciente.consultarPaciente(1L);
            List<Categoria> categorias = servicioAtencion.consultarCategorias(sucursal.getVeterinaria());
            List<Diagnostico> diagnosticos = servicioDiagnostico.consultarDiagnosticos();

            consola.println(String.format("Cargando datos para la veterinaria '%s'", sucursal.getVeterinaria().getRazonSocial()));

            // Ciclo de Atenciones
            for (int d = 1; d <= 31; d++) {
                for (int m = 1; m <= 12; m++) {
                    Date fecha = FechaUtils.crearFecha(2015, m, d);
                    if (fecha != null) {
                        if (!CliUtils.esDomingo(fecha)) {
                            int numero = RandomUtils.nextInt(0, 7);
                            for (int i = 0; i < numero; i++) {
                                Categoria categoria = CliUtils.obtenerAlAzar(categorias);
                                if (categoria != null) {

                                    Atencion atencion = new Atencion();
                                    atencion.setCategoria(categoria);
                                    atencion.setFecha(fecha);
                                    atencion.setPaciente(paciente);
                                    atencion.setPrecio(categoria.getPrecio());
                                    atencion.setRutProfesional(12345678);
                                    atencion.setSucursal(sucursal);
                                    
                                    if (StringUtils.containsIgnoreCase(categoria.getNombre(), "médica")) {
                                        Ficha ficha = new Ficha();
                                        ficha.setDiagnostico(CliUtils.obtenerAlAzar(diagnosticos));
                                        ficha.setFecha(fecha);
                                        ficha.setMedico(medico);
                                        ficha.setPaciente(paciente);
                                        Ficha fichaGuardada = servicioAtencion.guardar(ficha);
                                        if (fichaGuardada != null) {
                                            consola.println(String.format("Ficha %d guardada", ficha.getId()));
                                        }
                                        
                                        atencion.setRutProfesional(medico.getRut());
                                    }
                                    
                                    Atencion atencionGuardada = servicioAtencion.guardar(atencion);
                                    if (atencionGuardada != null) {
                                        consola.println(String.format("Atención %d guardad", atencionGuardada.getId()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            consola.println("Error en la aplicación por: " + e.toString());
            logger.error(e.toString());
            logger.debug("Error en aplicación principal", e);
        } finally {
            String fin = "Fin de la Aplicación: " + FechaUtils.crearFechaStrCL(new Date());
            consola.println(fin);
            logger.info(fin);
        }
    }
}
