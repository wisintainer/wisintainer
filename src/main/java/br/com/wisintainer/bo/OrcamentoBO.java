package br.com.wisintainer.bo;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.ItemOrcamentoDAO;
import br.com.wisintainer.dao.OrcamentoDAO;
import br.com.wisintainer.helper.Router;
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
			String link = Application.getRequestBaseURL() + "public/resposta-orcamento.xhtml?identify=" + idPreRespostaSalvaCodificado;

			sb = new StringBuilder();
			sb.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<body>");
			sb.append("<div style=\"background-color: #00a24c;\">&nbsp;</div>");
			sb.append("<div><img style=\"height: 91px; width: 645px; float: left;\" src=\"https://lh3.googleusercontent.com/B_b0WWTOCPpKI0YkOeaG5mAjG9NHQ-arX6ZoWxckOyjcePTm-x11lAHg_nWwN1bKUfDZupEBUjgsebMrIzoMnr5AZLZeh8apeZxxZXHNMZVdpUqKk5MOhuk8fs1NqkLCYrxFHNs2uplGUmZQO29V3OkA56h5mSw4DiN0C8eU2btgz379-Zj-KbLPCNN1saU81I8EivHGUNfB8BYZ4DiRofX3BRBnGeMLCVIW-r5CI8AET3Fd0OLUIeFLX5Ya6DVdauUo1ec3lI5rLoGmTZi_W-nDhmwavXp59nb9esD_v4OUVsg2k7tQ6vq5NyRk4SJSq4mm-0R4uXXlzv8qV1mS0mi3N3RaCAK1IRHWz0BuNW27Oex7R-b4uqTE8y8D0P_FKR0r8Ntn7bxgVHuPXcftv-1EWS2SWeuIcyfqiewme-DBEP6W0ta1-GeRqAn1OK7ihjbiC8you4vK3mpCMCzewRcEq9HWSYvfL9v_QlffMTZEAPjB8TYBEvYm1EpAKsDC9xufhmy_3pdExb8xmNeR72zcW_3jh6PYC7mHzb1GYe8seQb5pL7r7f4fZ75e_IAaFZqu_WVSbiIkuyYIQybWrdYBcz3PNAHGfr2QuSmTaLPMPAZH4mgaDgPs7EgCpdH_h-cXBKrLDVs0uAO2YT-nH1jfB__1sLpBvuUGxYLRm0ga16nfoLUB-W7mApHJzzcgp2tjDo5HHrd3AIxSIZ3flBBhFkZ2M228t55Kzd6Uk2kTjpF8gx27TZ6fzQv39Xypp7-ckisc-hzRl78dz7cUXvIqpjwVQPVyU3W8K35OcM59VRuNadin8KMq5a3NZoMuj2BYERf8jrE11Azlghu2dxdubWP2N1p9Hs8pRhlVMq8ff5YsvLiu_n-OxELIeawiIq7mZT_c5M1iSLZKq7VfmfGOtzAXG1b416KeSOMLes2H1_BBRD4xrfD0Al5xsVh4dbB0JaLHWnb_LQ=w1920-h273-no?authuser=0\" alt=\"\" /></div>");

			sb.append("<p>&nbsp;</p>\r\n"
					+ "<p>&nbsp;</p>\r\n"
					+ "<p>&nbsp;</p>\r\n"
					+ "<p>&nbsp;</p>\r\n"
					+ "<p>&nbsp;</p>\r\n"
					+ "<p>&nbsp;</p>");
			sb.append("<p> Prezado " + fornecedor.getNome() + ", </p>");
			sb.append("<p>Segue o link para realizar o or&ccedil;amento das pe&ccedil;as solicitadas pela A.D.A.S.C</p>");
			sb.append("<p> <a href=\" " + link + "\"> ACESSE O LINK </a> </p>");
			sb.append("<hr />");
			sb.append("<p><strong>DADOS DESTE OR&Ccedil;AMENTO</strong></p>");			
			sb.append("<p> Placa:" + orcamento.getVeiculoPlaca() + "</p>");
			sb.append("<p> Chassi:" + orcamento.getVeiculoChassi() + "</p>");
			sb.append("<p> Marca:" + orcamento.getVeiculoFabricante() + "</p>");
			sb.append("<p> Modelo:" + orcamento.getVeiculoModelo() + "</p>");
			sb.append("<hr>");
			sb.append("<p> Atenciosamente </p>");
			sb.append("<p> <b> ASSOCIAÇÃO DE DESENVOLVIMENTO DOS AMIGOS DE SANTA CATERINA </b> </p>");
			sb.append("<div style=\"background-color: #00a24c;\"></div>");
			sb.append("</body>\r\n" + "</html>");
			
//			sb.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<body>");
//			sb.append("<div style=\"background-color: #1DA6FF;\">");
//			sb.append("<h1 style=\"color: white;\"> ::LOGO:: </h1>");
//			sb.append("</div>");
//			sb.append("<div>");
//			sb.append("<p> Prezado " + fornecedor.getNome() + ", </p>");
//			sb.append("<p> Segue o link para realizar o orçamento das peças solicitadas pela A.D.A.S.C </p>");
//			sb.append("<p> <a href=\" " + link + "\"> ACESSE O LINK </a> </p>");
//			sb.append("<hr>");
//			sb.append("<p> <b> DADOS DESTE ORÇAMENTO: </b> </p>");
//			sb.append("<p> Placa:" + orcamento.getVeiculoPlaca() + "</p>");
//			sb.append("<hr>");
//			sb.append("<p> Atenciosamente </p>");
//			sb.append("<p> <b> ASSOCIAÇÃO DE DESENVOLVIMENTO DOS AMIGOS DE SANTA CATERINA </b> </p>");
//			sb.append("</div>");
//			sb.append("<div style=\"background-color: #1DA6FF;\">");
//			sb.append(".");
//			sb.append("</div>");
//			sb.append("</body>\r\n" + "</html>");

			sendEmail.sendEmail("A.D.A.S.C Orçamento", fornecedor.getEmail(), sb.toString(), true);
		}

	}

	public Orcamento buscarOrcamentoPorId(Integer id) throws Exception {
		return orcamentoDao.buscarOrcamentoPorId(id);
	}

}
