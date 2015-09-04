package cl.sebastian.chadoskyx.cli.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastián Salazar Molina <ssalazarm.externo@bancofalabella.cl>
 */
public class CliUtils implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(CliUtils.class);

    private CliUtils() {
        throw new AssertionError();
    }

    public static <T> T obtenerAlAzar(List<T> objetos) {
        T entidad = null;
        try {
            if (objetos != null) {
                if (!objetos.isEmpty()) {
                    int total = objetos.size();
                    int azar = RandomUtils.nextInt(0, total - 1);
                    entidad = objetos.get(azar);
                }
            }
        } catch (Exception e) {
            entidad = null;
            logger.error("Error al obtener al azar: {}", e.toString());
        }
        return entidad;
    }

    public static boolean esDomingo(Date fecha) {
        boolean ok = false;
        try {
            if (fecha != null) {
                Calendar calendario = new GregorianCalendar();
                calendario.setTime(fecha);
                int dow = calendario.get(Calendar.DAY_OF_WEEK);
                if (Calendar.SUNDAY == dow) {
                    ok = true;
                }
            }
        } catch (Exception e) {
            ok = false;
            logger.error("Error al determinar si es Domingo: {}", e.toString());
        }
        return ok;
    }

    public static Integer rutAlAzar() {
        return RandomUtils.nextInt(10000000, 20000000);
    }

    public static Date crearFecha(int anio, int mes, int dia) {
        Date fecha = new Date();
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fecha);
            calendar.set(Calendar.YEAR, anio);
            calendar.set(Calendar.MONTH, mes - 1);
            calendar.set(Calendar.DATE, dia);
            calendar.set(Calendar.HOUR_OF_DAY, RandomUtils.nextInt(8, 20));
            calendar.set(Calendar.MINUTE, RandomUtils.nextInt(0, 59));
            calendar.set(Calendar.SECOND, RandomUtils.nextInt(0, 59));
            calendar.set(Calendar.MILLISECOND, RandomUtils.nextInt(0, 999));
            fecha = calendar.getTime();
        } catch (Exception e) {
            fecha = null;
            logger.error("Error al obtener fecha: {}", e.toString());
        }
        return fecha;
    }
}
