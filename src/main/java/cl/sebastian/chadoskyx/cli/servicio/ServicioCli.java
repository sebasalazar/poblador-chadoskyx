/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.sebastian.chadoskyx.cli.servicio;

import cl.chadoskyx.titulo.modelo.Atencion;
import cl.chadoskyx.titulo.modelo.Categoria;
import cl.chadoskyx.titulo.modelo.Diagnostico;
import cl.chadoskyx.titulo.modelo.Ficha;
import cl.chadoskyx.titulo.modelo.Medico;
import cl.chadoskyx.titulo.modelo.Paciente;
import cl.chadoskyx.titulo.modelo.Sucursal;
import cl.chadoskyx.titulo.modelo.Veterinaria;
import java.util.List;

/**
 *
 * @author Sebastián Salazar Molina <ssalazarm.externo@bancofalabella.cl>
 */
public interface ServicioCli {

    public Sucursal consultarSucursal(Integer id);

    public Medico consultarMedico(Integer id);

    public Paciente consultarPaciente(Long id);

    public List<Categoria> consultarCategorias(Veterinaria veterinaria);

    public List<Diagnostico> consultarDiagnosticos();

    public Ficha guardar(Ficha ficha);

    public Atencion guardar(Atencion atencion);
}
