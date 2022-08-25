package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.ItemOrcamentoDAO;
import br.com.wisintainer.dao.OrcamentoDAO;
import br.com.wisintainer.helper.SendEmail;
import br.com.wisintainer.helper.StringUtil;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.ItemOrcamento;
import br.com.wisintainer.model.Orcamento;

public class OrcamentoBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoDAO orcamentoDao;

	@Inject
	ItemOrcamentoDAO itemOrcamentoDao;

	@Inject
	private ResposataFornecedorBO respostaFornecedorBO;

	public void salvarSolicitacaoDeOrcamento(Orcamento orcamento, List<Fornecedor> fornecedores) throws Exception {

		try {
			// SALVANDO ORCAMENTO
			Integer idOrcamentoInserido = (Integer) orcamentoDao.saveReturningSaved(orcamento);

			// SALVANDO ITENS DO ORCAMENTO
			for (ItemOrcamento item : orcamento.getItensOrcamento()) {
				item.setOrcamento_id(idOrcamentoInserido);
				itemOrcamentoDao.save(item);
			}

			// ENVIANDO E-MAILS PARA FORNECEDORES
			enviarEmailsSolicitacoesOrcamento(orcamento, fornecedores);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer salvarPreRespostaItem(Orcamento orcamento, Fornecedor fornecedor) throws Exception {
		return respostaFornecedorBO.salvarPreResposta(orcamento, fornecedor);
	}

	public void enviarEmailsSolicitacoesOrcamento(Orcamento orcamento, List<Fornecedor> fornecedores) throws Exception {
		SendEmail sendEmail = new SendEmail();
		StringBuilder sb;

		Date hoje = new Date();

		for (Fornecedor fornecedor : fornecedores) {
			Integer idPreRespostaSalva = salvarPreRespostaItem(orcamento, fornecedor);
			StringUtil su = new StringUtil();
			String idPreRespostaSalvaCodificado = su.codificaTexto(idPreRespostaSalva.toString());
			String link = Application.getRequestBaseURL() + "public/resposta-orcamento.xhtml?identify="
					+ idPreRespostaSalvaCodificado;

			sb = new StringBuilder();

			sb.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<body>");

			sb.append("<div style=\"background-color: #1DA6FF;\">");
			sb.append("<h1 style=\"color: white;\"> ::LOGO:: </h1>");
			sb.append("</div>");
			sb.append("<div>");
			sb.append("<p> Prezado" + fornecedor.getNome() + ", </p>");
			sb.append("<p> Segue o link para realizar o orçamento das peças solicitadas pela A.D.A.S.C </p>");
			sb.append("<p> <a href=\" " + link + "\"> ACESSE O LINK </a> </p>");
			sb.append("<hr>");
			sb.append("<p> <b> DADOS DESTE ORÇAMENTO: </b> </p>");
			sb.append("<p> Placa:" + orcamento.getVeiculoPlaca() + "</p>");
			sb.append("<p> Data do envio do E-mail: " + hoje.getDate() + "/" + hoje.getMonth() + "/" + hoje.getYear()
					+ "</p>");
			sb.append("<p> Hora do envio do E-mail: " + hoje.getHours() + ":" + hoje.getMinutes() + "</p>");
			sb.append("<hr>");
			sb.append("<p> Atenciosamente </p>");
			sb.append("<p> <b> ASSOCIAÇÃO DE DESENVOLVIMENTO DOS AMIGOS DE SANTA CATERINA </b> </p>");
			sb.append("</div>");
			sb.append("<div style=\"background-color: #1DA6FF;\">");
			sb.append(".");
			sb.append("</div>");

			sb.append("</body>\r\n" + "</html>");

			sendEmail.sendEmail("A.D.A.S.C Orçamento", fornecedor.getEmail(), sb.toString(), true);
		}

	}

}
