package cl.sebastian.chadoskyx.cli.run;

import cl.chadoskyx.titulo.modelo.Atencion;
import cl.chadoskyx.titulo.modelo.Categoria;
import cl.chadoskyx.titulo.modelo.Diagnostico;
import cl.chadoskyx.titulo.modelo.Ficha;
import cl.chadoskyx.titulo.modelo.Medico;
import cl.chadoskyx.titulo.modelo.Paciente;
import cl.chadoskyx.titulo.modelo.Sucursal;
import cl.chadoskyx.titulo.utils.FechaUtils;
import cl.sebastian.chadoskyx.cli.servicio.ServicioCli;
import cl.sebastian.chadoskyx.cli.utils.CliUtils;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
            ServicioCli servicio = context.getBean("servicioCli", ServicioCli.class);

            Sucursal sucursal = servicio.consultarSucursal(1);
            Medico medico = servicio.consultarMedico(1);
            Paciente paciente = servicio.consultarPaciente(1L);
            List<Categoria> categorias = servicio.consultarCategorias(sucursal.getVeterinaria());
            List<Diagnostico> diagnosticos = servicio.consultarDiagnosticos();

            consola.println(String.format("Cargando datos para la veterinaria '%s'", sucursal.getVeterinaria().getRazonSocial()));

            // Ciclo de Atenciones
            for (int d = 1; d <= 31; d++) {
                for (int m = 1; m <= 12; m++) {
                    int numero = RandomUtils.nextInt(1, 7);
                    for (int i = 0; i < numero; i++) {
                        Date fecha = CliUtils.crearFecha(2015, m, d);
                        if (fecha != null) {
                            if (!CliUtils.esDomingo(fecha)) {

                                Categoria categoria = CliUtils.obtenerAlAzar(categorias);
                                if (categoria != null) {

                                    Atencion atencion = new Atencion();
                                    atencion.setCategoria(categoria);
                                    atencion.setFecha(fecha);
                                    atencion.setPaciente(paciente);
                                    atencion.setPrecio(categoria.getPrecio());
                                    atencion.setRutProfesional(CliUtils.rutAlAzar());
                                    atencion.setSucursal(sucursal);

                                    if (StringUtils.containsIgnoreCase(categoria.getNombre(), "médica")) {
                                        Ficha ficha = new Ficha();
                                        ficha.setDiagnostico(CliUtils.obtenerAlAzar(diagnosticos));
                                        ficha.setFecha(fecha);
                                        ficha.setMedico(medico);
                                        ficha.setPaciente(paciente);
                                        Ficha fichaGuardada = servicio.guardar(ficha);
                                        if (fichaGuardada != null) {
                                            consola.println(String.format("Ficha %d guardada", ficha.getId()));
                                        }

                                        atencion.setRutProfesional(medico.getRut());
                                    }

                                    Atencion atencionGuardada = servicio.guardar(atencion);
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
