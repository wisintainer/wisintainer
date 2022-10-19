package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.OficinaDAO;
import br.com.wisintainer.model.Oficina;

public class OficinaBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private OficinaDAO oficinaDao;

	public List<Oficina> buscarOficinaPorNome(String nome) throws Exception {
		return oficinaDao.buscarOficinaPorNome(nome);
	}

	public void salvarOficina(Oficina oficina) throws Exception {
		oficinaDao.save(oficina);
	}

	public void editarOficina(Oficina oficina) throws Exception {
		oficinaDao.update(oficina);
	}

	public List<Oficina> buscar20Oficinas() throws Exception {
		return oficinaDao.buscar20Oficinas();
	}

}
