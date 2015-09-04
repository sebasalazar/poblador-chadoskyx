package cl.sebastian.chadoskyx.cli.servicio.impl;

import cl.chadoskyx.titulo.modelo.Atencion;
import cl.chadoskyx.titulo.modelo.Categoria;
import cl.chadoskyx.titulo.modelo.Diagnostico;
import cl.chadoskyx.titulo.modelo.Ficha;
import cl.chadoskyx.titulo.modelo.Medico;
import cl.chadoskyx.titulo.modelo.Paciente;
import cl.chadoskyx.titulo.modelo.Sucursal;
import cl.chadoskyx.titulo.modelo.Veterinaria;
import cl.chadoskyx.titulo.servicio.ServicioAtencion;
import cl.chadoskyx.titulo.servicio.ServicioDiagnostico;
import cl.chadoskyx.titulo.servicio.ServicioEmail;
import cl.chadoskyx.titulo.servicio.ServicioEspecie;
import cl.chadoskyx.titulo.servicio.ServicioGeografico;
import cl.chadoskyx.titulo.servicio.ServicioPaciente;
import cl.chadoskyx.titulo.servicio.ServicioSQL;
import cl.chadoskyx.titulo.servicio.ServicioUsuario;
import cl.chadoskyx.titulo.servicio.ServicioVeterinaria;
import cl.sebastian.chadoskyx.cli.servicio.ServicioCli;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sebastián Salazar Molina <ssalazarm.externo@bancofalabella.cl>
 */
@Service("servicioCli")
public class ServicioCliImpl implements ServicioCli, Serializable {

    @Resource(name = "servicioAtencion")
    ServicioAtencion servicioAtencion;
    @Resource(name = "servicioDiagnostico")
    ServicioDiagnostico servicioDiagnostico;
    @Resource(name = "servicioEmail")
    ServicioEmail servicioEmail;
    @Resource(name = "servicioEspecie")
    ServicioEspecie servicioEspecie;
    @Resource(name = "servicioGeografico")
    ServicioGeografico servicioGeografico;
    @Resource(name = "servicioPaciente")
    ServicioPaciente servicioPaciente;
    @Resource(name = "servicioSQL")
    ServicioSQL servicioSQL;
    @Resource(name = "servicioUsuario")
    ServicioUsuario servicioUsuario;
    @Resource(name = "servicioVeterinaria")
    ServicioVeterinaria servicioVeterinaria;
    @Resource(name = "dataSource")
    private DataSource dataSource;
    private SessionFactory sessionFactory = null;
    private final static Logger logger = LoggerFactory.getLogger(ServicioCliImpl.class);

    private void crearSession() {
        if (sessionFactory.isClosed()) {
            LocalSessionFactoryBuilder factory = new LocalSessionFactoryBuilder(dataSource);
            sessionFactory = factory.buildSessionFactory();
        }
    }

    @PostConstruct
    public void iniciar() {
        try {
            LocalSessionFactoryBuilder factory = new LocalSessionFactoryBuilder(dataSource);
            sessionFactory = factory.buildSessionFactory();
        } catch (Exception e) {
            logger.error("Error al iniciar: {}", e.toString());
            logger.debug("Error al iniciar: {}", e.toString(), e);
        }
    }

    @Override
    public Sucursal consultarSucursal(Integer id) {
        Sucursal sucursal = null;
        try {
            if (id != null) {
                sucursal = servicioVeterinaria.consultarSucursal(id);
            }
        } catch (Exception e) {
            sucursal = null;
            logger.error("Error al consultar Sucursal {}", e.toString());
            logger.debug("Error al consultar Sucursal {}", e.toString(), e);
        }
        return sucursal;
    }

    @Override
    public Medico consultarMedico(Integer id) {
        Medico medico = null;
        try {
            if (id != null) {
                medico = servicioVeterinaria.consultarMedico(id);
            }
        } catch (Exception e) {
            medico = null;
            logger.error("Error al consultar médico: {}", e.toString());
            logger.debug("Error al consultar médico: {}", e.toString(), e);
        }
        return medico;
    }

    @Override
    public Paciente consultarPaciente(Long id) {
        Paciente paciente = null;
        try {
            if (id != null) {
                paciente = servicioPaciente.consultarPaciente(id);
            }
        } catch (Exception e) {
            paciente = null;
            logger.error("Error al consultar Paciente: {}", e.toString());
            logger.debug("Error al consultar Paciente: {}", e.toString(), e);
        }
        return paciente;
    }

    @Override
    public List<Categoria> consultarCategorias(Veterinaria veterinaria) {
        List<Categoria> categorias = new ArrayList<Categoria>();
        try {
            if (veterinaria != null) {
                categorias = servicioAtencion.consultarCategorias(veterinaria);
            }
        } catch (Exception e) {
            categorias = new ArrayList<Categoria>();
            logger.error("Consultar Categorias: {}", e.toString());
            logger.debug("Consultar Categorias: {}", e.toString(), e);
        }
        return categorias;
    }

    @Override
    public List<Diagnostico> consultarDiagnosticos() {
        List<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
        try {
            diagnosticos = servicioDiagnostico.consultarDiagnosticos();
        } catch (Exception e) {
            diagnosticos = new ArrayList<Diagnostico>();
            logger.error("Error consultando diagnosticos: {}", e.toString());
            logger.debug("Error consultando diagnosticos: {}", e.toString(), e);
        }
        return diagnosticos;
    }

    @Override
    public Ficha guardar(Ficha ficha) {
        Ficha resultado = null;
        try {
            if (ficha != null) {
                crearSession();
                resultado = servicioAtencion.guardar(ficha);

//                Session currentSession = sessionFactory.getCurrentSession();
//                resultado = (Ficha) currentSession.save(ficha);
            }
        } catch (Exception e) {
            resultado = null;
            logger.error("Error al guardar ficha: {}", e.toString());
            logger.debug("Error al guardar ficha: {}", e.toString(), e);
        }
        return resultado;
    }

    @Override
    public Atencion guardar(Atencion atencion) {
        Atencion resultado = null;
        try {
            if (atencion != null) {
                crearSession();
                resultado = servicioAtencion.guardar(atencion);

//                Session currentSession = sessionFactory.getCurrentSession();
//                resultado = (Atencion) currentSession.save(atencion);
            }
        } catch (Exception e) {
            resultado = null;
            logger.error("Error al guardar atencion: {}", e.toString());
            logger.debug("Error al guardar atencion: {}", e.toString(), e);
        }
        return resultado;
    }

}
