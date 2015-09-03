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
}
