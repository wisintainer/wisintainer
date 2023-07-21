package br.com.wisintainer.controller.orcamento;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import br.com.wisintainer.bo.AprovacaoBO;
import br.com.wisintainer.bo.FornecedorBO;
import br.com.wisintainer.bo.OrcamentoBO;
import br.com.wisintainer.bo.OrdemDeCompraBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.helper.Router;
import br.com.wisintainer.helper.SendEmail;
import br.com.wisintainer.helper.StringUtil;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Aprovacao;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.InputStreamPojo;
import br.com.wisintainer.model.JasperRootAnchor;
import br.com.wisintainer.model.Messager;
import br.com.wisintainer.model.Orcamento;
import br.com.wisintainer.model.OrdemDeCompra;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;

@ViewScoped
@Named("visualizacaoOrcamentoMB")
public class VisualizacaoOrcamentoMB extends AbstractMB {

	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoCriarorcamento()) {
			return true;
		} else {
			return false;
		}
	}

	@Inject
	private OrcamentoBO orcamentoBO;

	@Inject
	private AprovacaoBO aprovacaoBO;

	@Inject
	private FornecedorBO fornecedorBO;

	@Inject
	private OrdemDeCompraBO ordemDeCompraBO;

	private Orcamento orcamento;

	private List<Aprovacao> aprovaoesDoOrcamento;

	private Aprovacao aprovacaoParaRemover;

	@PostConstruct
	public void inicializar() throws NumberFormatException, Exception {
		carregarOrcamento();
		this.aprovaoesDoOrcamento = new ArrayList<Aprovacao>();
		carregarItensAprovadosDoOrcamento();
		this.aprovacaoParaRemover = new Aprovacao();
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public List<Aprovacao> getAprovaoesDoOrcamento() {
		return aprovaoesDoOrcamento;
	}

	public void setAprovaoesDoOrcamento(List<Aprovacao> aprovaoesDoOrcamento) {
		this.aprovaoesDoOrcamento = aprovaoesDoOrcamento;
	}

	public Aprovacao getAprovacaoParaRemover() {
		return aprovacaoParaRemover;
	}

	public void setAprovacaoParaRemover(Aprovacao aprovacaoParaRemover) {
		this.aprovacaoParaRemover = aprovacaoParaRemover;
	}

	public void carregarOrcamento() throws NumberFormatException, Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String idOrcamento = request.getParameter("orcamento");

		this.orcamento = orcamentoBO.buscarOrcamentoPorId(Integer.parseInt(idOrcamento));
	}

	public void carregarItensAprovadosDoOrcamento() throws Exception {
		this.aprovaoesDoOrcamento = aprovacaoBO.buscarAprovacoesPorOrcamento(this.orcamento.getId());
	}

	public String valorTotalAprovadoOrcamento() {
		Double total = 0.0;

		for (Aprovacao itens : aprovaoesDoOrcamento) {
			total = total + (itens.getItem_valor() * itens.getItem_quantidade());
		}

		DecimalFormat decimal = new DecimalFormat("###,###,###,##0.00");
		return decimal.format(total);
	}

	public void removerAprovacaoDoOrcamento() throws Exception {
		if (this.aprovacaoParaRemover != null) {
			try {
				aprovacaoBO.removerDaAprovacao(aprovacaoParaRemover);
				carregarItensAprovadosDoOrcamento();
				FacesMessage msg = new FacesMessage("Item removido do Orçamento!");
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch (Exception e) {
				e.printStackTrace();
				FacesMessage msg = new FacesMessage("Erro ao remover item do Orçamento!");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} else {
			System.out.println("AprovacaoParaRemover está null");
		}

	}

	private File getFileFromResource(String fileName) throws URISyntaxException {

		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {

			// failed if files have whitespaces or special characters
			// return new File(resource.getFile());

			return new File(resource.toURI());
		}

	}

	public void gerarOrdemDeCompra() throws Exception {
		List<Integer> todosIdsFornecedoresDaAprovacao = aprovacaoBO.buscarTodosFornecedoresDaAprovacoesPorOrcamento(orcamento.getId());

		for (Integer fornecedor : todosIdsFornecedoresDaAprovacao) {
			Fornecedor flinha = fornecedorBO.buscarFornecedorPorId(fornecedor);

			// Itens aprovados daquele fornecedor naquele orçamento
			List<Aprovacao> aprovacoesDoFornecedor = aprovacaoBO.buscarAprovacoesPorOrcamentoEFornecedor(orcamento.getId(), fornecedor);

			// Orcamento referente ás aprovações
			Orcamento olinha = orcamentoBO.buscarOrcamentoPorId(aprovacoesDoFornecedor.get(0).getOrcamento_id());

			JRBeanCollectionDataSource itensJrBean = new JRBeanCollectionDataSource(aprovacoesDoFornecedor);

			HashMap<String, Object> params = new HashMap<String, Object>();

			// FORNECEDOR
			params.put("NOME_FORNECEDOR", flinha.getNome());
			params.put("ENDERECO_FORNECEDOR", flinha.getEndereco());
			params.put("CNPJ_FORNECEDOR", flinha.getCnpj());
			params.put("EMAIL_FORNECEDOR", flinha.getEmail());
			params.put("TELEFONE_FORNECEDOR", flinha.getTelefone());

			// PEDIDO
			params.put("PEDIDO_NUMERO", ordemDeCompraBO.retornaIdProx());
			Date hoje = new Date();
			params.put("PEDIDO_DATA", hoje);
			params.put("PEDIDO_REQUISITANTE", olinha.getEntrega_requisitante());
			params.put("PEDIDO_EMPRESA", olinha.getEntrega_empresa());
			params.put("PEDIDO_RESPONSAVEL", olinha.getEntrega_responsavel());
			params.put("PEDIDO_ENDERECO", olinha.getEntrega_endereco());
			params.put("PEDIDO_TELEFONE", olinha.getEntrega_telefone());
			params.put("PEDIDO_EMAIL", olinha.getEntrega_email());
			params.put("PEDIDO_CEP", olinha.getEntrega_cep());
			params.put("PEDIDO_CIDADE", olinha.getEntrega_cidade());
			params.put("PEDIDO_ESTADO", olinha.getEntrega_estado());
			params.put("PEDIDO_ENDERECO_NUMERO", olinha.getEntrega_numero());
			params.put("PEDIDO_BAIRRO", olinha.getEntrega_bairro());
			params.put("PEDIDO_CNPJ", olinha.getEntrega_cnpj());

			Double valorSomado = 0.0;
			for (Aprovacao ap : aprovacoesDoFornecedor) {
				valorSomado = valorSomado + (ap.getItem_valor() * ap.getItem_quantidade());
			}
			params.put("VALOR_TOTAL_ORDEM_DE_COMPRA", valorSomado);

			// VEICULO
			params.put("VEICULO_MODELO", olinha.getVeiculoModelo());
			params.put("VEICULO_MARCA", olinha.getVeiculoFabricante());
			params.put("VEICULO_ANO_FABRICACAO", olinha.getVeiculoAnoFabricao().toString());
			params.put("VEICULO_ANO_MODELO", olinha.getVeiculoAnoModelo().toString());
			params.put("VEICULO_PLACA", olinha.getVeiculoPlaca());
			params.put("VEICULO_CHASSI", olinha.getVeiculoChassi());

			params.put("CollectionBeanParam", itensJrBean);

			File logo = new File(Router.getRoot(), "resources/images/logo-selos.png");
			Image image = ImageIO.read(logo);

			params.put("LOGO", image);

			JasperReport jr = null;
			InputStreamPojo inputSPojo = new InputStreamPojo();

			URL url = JasperRootAnchor.class.getResource("");
			File root = null;
			root = new File(URLDecoder.decode(url.getFile(), "UTF-8"));

			File f = new File(root + ".jrxml");
			OutputStream os = new FileOutputStream(f);
			os.write(obterModeloOrdemDeCompra().getBytes("UTF-8"));
			os.close();
			FileInputStream reportStream = new FileInputStream(f);
			f.delete();

			inputSPojo.setInputStream(reportStream);

			jr = JasperCompileManager.compileReport(inputSPojo.getInputStream());
			jr.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
			ArrayList<Object> lstForcaDetail = new ArrayList<Object>();
			lstForcaDetail.add("infoinfo");
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(lstForcaDetail);
			JasperPrint print = JasperFillManager.fillReport(jr, params, datasource);

			byte[] reportData = null;
			reportData = JasperExportManager.exportReportToPdf(print);

			String marca = new Date().getTime() + "";
			File outputFile = new File(root + marca + ".pdf");

			FileUtils.writeByteArrayToFile(outputFile, reportData);

			if (reportData == null || reportData.length <= 0) {
				Messager.addError("Erro ao gerar Credenciamento.");
				return;
			} else {
				// Enviando e-mail para o fornecedor
				enviarEmailsOrdensDeCompra(outputFile, flinha);

				// Salvando ordem de compra na base
				OrdemDeCompra ordem = new OrdemDeCompra();
				ordem.setData_envio(new Date());
				ordem.setOrdemdecompra(reportData);

				ordemDeCompraBO.salvarOrdemDeCompra(ordem, aprovacoesDoFornecedor);
				
				FacesMessage msg = new FacesMessage("Informação: ");
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
				msg.setDetail("Ordem de compra gerada e enviada aos fornecedores!");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				// Descomentar para exibir arquivo gerado na tela
//				FacesContext facesContext = FacesContext.getCurrentInstance();
//				HttpServletResponse response = (HttpServletResponse) (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
//						.getResponse();
//
//				response.setContentType("application/pdf");
//				response.setHeader("Content-Disposition", "fileName=hab" + "oc" + ".pdf");
//				response.setContentLength(reportData.length);
//				facesContext.responseComplete();
//				ServletOutputStream os2 = response.getOutputStream();
//				os2.write(reportData, 0, reportData.length);
//				os2.flush();
//				os2.close();
			}
		}

	}

	public void enviarEmailsOrdensDeCompra(File ordemdecompra, Fornecedor fornecedor) throws Exception {
		SendEmail sendEmail = new SendEmail();
		StringBuilder sb;

		Date hoje = new Date();

		sb = new StringBuilder();

		sb.append("Segue ordem de compra.");

		sendEmail.sendEmailComAnexo(ordemdecompra, "A.D.A.S.C Ordem de Compra", fornecedor.getEmail(), sb.toString(), true);

	}

	public String obterModeloOrdemDeCompra() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<!-- Created with Jaspersoft Studio version 6.20.0.final using JasperReports Library version 6.20.0-2bc7ab61c56f459e8176eb05c7705e145cd400ad  -->\r\n"
				+ "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"ordemdecompra\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"535\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"4eedbb89-b4f6-4469-9ab6-f642a1688cf7\">\r\n"
				+ "	<property name=\"com.jaspersoft.studio.data.defaultdataadapter\" value=\"One Empty Record\"/>\r\n"
				+ "	<style name=\"Title\" forecolor=\"#FFFFFF\" fontName=\"Times New Roman\" fontSize=\"50\" isBold=\"false\" pdfFontName=\"Times-Bold\"/>\r\n"
				+ "	<style name=\"SubTitle\" forecolor=\"#CCCCCC\" fontName=\"Times New Roman\" fontSize=\"18\" isBold=\"false\" pdfFontName=\"Times-Roman\"/>\r\n"
				+ "	<style name=\"Column header\" forecolor=\"#666666\" fontName=\"Times New Roman\" fontSize=\"14\" isBold=\"true\"/>\r\n"
				+ "	<style name=\"Detail\" mode=\"Transparent\" fontName=\"Times New Roman\"/>\r\n"
				+ "	<style name=\"Row\" mode=\"Transparent\" fontName=\"Times New Roman\" pdfFontName=\"Times-Roman\">\r\n"
				+ "		<conditionalStyle>\r\n"
				+ "			<conditionExpression><![CDATA[$V{REPORT_COUNT}%2 == 0]]></conditionExpression>\r\n"
				+ "			<style mode=\"Opaque\" backcolor=\"#EEEFF0\"/>\r\n"
				+ "		</conditionalStyle>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"1.0\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"1.0\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"1.0\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"1.0\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"1.0\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table_TH\" mode=\"Opaque\" backcolor=\"#FFFFFF\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table_CH\" mode=\"Opaque\" backcolor=\"#CACED0\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table_TD\" mode=\"Opaque\" backcolor=\"#FFFFFF\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "		<conditionalStyle>\r\n"
				+ "			<conditionExpression><![CDATA[$V{REPORT_COUNT}%2 == 0]]></conditionExpression>\r\n"
				+ "			<style backcolor=\"#D8D8D8\"/>\r\n"
				+ "		</conditionalStyle>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table 1_TH\" mode=\"Opaque\" backcolor=\"#F0F8FF\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table 1_CH\" mode=\"Opaque\" backcolor=\"#00A24C\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table 1_TD\" mode=\"Opaque\" backcolor=\"#FFFFFF\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<subDataset name=\"tableDataset\" uuid=\"f13e6d36-5148-4ecc-bbe3-3035def80980\">\r\n"
				+ "		<queryString>\r\n"
				+ "			<![CDATA[]]>\r\n"
				+ "		</queryString>\r\n"
				+ "	</subDataset>\r\n"
				+ "	<subDataset name=\"Colecao\" uuid=\"da0cb906-5021-4cdd-b76b-320c70f719b7\">\r\n"
				+ "		<queryString>\r\n"
				+ "			<![CDATA[]]>\r\n"
				+ "		</queryString>\r\n"
				+ "		<field name=\"item_nome\" class=\"java.lang.String\"/>\r\n"
				+ "		<field name=\"item_quantidade\" class=\"java.lang.Integer\"/>\r\n"
				+ "		<field name=\"item_valor\" class=\"java.lang.Double\"/>\r\n"
				+ "	</subDataset>\r\n"
				+ "	<parameter name=\"NOME_FORNECEDOR\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"ITENS\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"CollectionBeanParam\" class=\"net.sf.jasperreports.engine.data.JRBeanCollectionDataSource\"/>\r\n"
				+ "	<parameter name=\"LOGO\" class=\"java.awt.Image\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_NUMERO\" class=\"java.lang.Integer\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_DATA\" class=\"java.util.Date\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_REQUISITANTE\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"VEICULO_MODELO\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"VEICULO_MARCA\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"VEICULO_ANO_FABRICACAO\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"VEICULO_ANO_MODELO\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"VEICULO_PLACA\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"VEICULO_CHASSI\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"CNPJ_FORNECEDOR\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"ENDERECO_FORNECEDOR\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"EMAIL_FORNECEDOR\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"TELEFONE_FORNECEDOR\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_EMPRESA\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_RESPONSAVEL\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_ENDERECO\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_BAIRRO\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_CIDADE\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_CEP\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_TELEFONE\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_EMAIL\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_ENDERECO_NUMERO\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"VALOR_TOTAL_ORDEM_DE_COMPRA\" class=\"java.lang.Double\"/>\r\n"
				+ "	<parameter name=\"PEDIDO_CNPJ\" class=\"java.lang.String\"/>\r\n"
				+ "	<queryString>\r\n"
				+ "		<![CDATA[]]>\r\n"
				+ "	</queryString>\r\n"
				+ "	<title>\r\n"
				+ "		<band height=\"384\" splitType=\"Stretch\">\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"283\" y=\"19\" width=\"275\" height=\"20\" uuid=\"0f86baff-6386-4f3f-b3fe-2388707babe8\"/>\r\n"
				+ "				<box rightPadding=\"4\"/>\r\n"
				+ "				<textElement textAlignment=\"Center\">\r\n"
				+ "					<font size=\"12\" isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[PEDIDO DE COMPRA POR E-MAIL]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"82\" width=\"107\" height=\"15\" uuid=\"c226e44d-3bb5-4145-b0b3-903bf1d79fde\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[NÚMERO DO PEDIDO:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"97\" width=\"107\" height=\"15\" uuid=\"ee07dc38-9745-4a2a-a04d-35a3357cd144\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[DATA DO PEDIDO:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"112\" width=\"80\" height=\"15\" uuid=\"2a411603-c0c7-4827-91b9-b3c2d6d4a79f\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[REQUISITANTE:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"138\" width=\"555\" height=\"24\" uuid=\"a7f798a6-c622-4c30-bde3-737e7c7013e0\"/>\r\n"
				+ "				<textElement textAlignment=\"Center\">\r\n"
				+ "					<font size=\"12\" isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[DADOS DO VEÍCULO]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"156\" width=\"107\" height=\"15\" uuid=\"49836246-3918-492d-a0f3-2f3cdbe71f4b\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[MODELO:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"186\" width=\"107\" height=\"15\" uuid=\"e04e5a42-9252-4436-9e1e-45c78908e729\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[ANO FABRICAÇÃO:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"201\" width=\"107\" height=\"15\" uuid=\"7bf31fec-9dc6-4b8d-b9ff-e7d14e3f802a\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[ANO MODELO:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"216\" width=\"107\" height=\"15\" uuid=\"1300007a-0fae-418c-90cd-f426ac13914e\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[PLACA:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"230\" width=\"107\" height=\"15\" uuid=\"324f3422-b5f9-40e1-b2ac-5fcb7aa45f2c\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[CHASSI:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"-14\" y=\"64\" width=\"570\" height=\"20\" uuid=\"97a78476-0253-4ec0-acb5-2a09a3c3179c\"/>\r\n"
				+ "				<box rightPadding=\"4\"/>\r\n"
				+ "				<textElement textAlignment=\"Right\"/>\r\n"
				+ "				<text><![CDATA[ASSOCIAÇÃO DE DESENVOLVIMENTO DOS AMIGOS DE SANTA CATARINA]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"317\" y=\"76\" width=\"239\" height=\"20\" uuid=\"e9e16473-c675-486d-9c95-50296befb446\"/>\r\n"
				+ "				<box rightPadding=\"4\"/>\r\n"
				+ "				<textElement textAlignment=\"Right\"/>\r\n"
				+ "				<text><![CDATA[CNPJ: 23.696.592/0001-23]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"316\" y=\"88\" width=\"239\" height=\"55\" uuid=\"94549bb8-9c54-4107-bda9-cb0d09d27010\"/>\r\n"
				+ "				<box rightPadding=\"4\"/>\r\n"
				+ "				<textElement textAlignment=\"Right\"/>\r\n"
				+ "				<text><![CDATA[AVENIDA ATILIO PEDRO PAGANI 1106\r\n"
				+ "PASSA VINTE PALHOÇA / CEP: 88132-149\r\n"
				+ "Tel: 48 3286-9552 / E-mail: boleto@adasc.net.br]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<rectangle>\r\n"
				+ "				<reportElement x=\"-20\" y=\"-20\" width=\"595\" height=\"20\" backcolor=\"#00A24C\" uuid=\"5aff1e46-663e-497d-9cde-f11bb91f8825\"/>\r\n"
				+ "			</rectangle>\r\n"
				+ "			<rectangle>\r\n"
				+ "				<reportElement x=\"0\" y=\"257\" width=\"279\" height=\"113\" uuid=\"b4547b05-6283-4c7f-b62a-975dfbb1e18c\"/>\r\n"
				+ "			</rectangle>\r\n"
				+ "			<rectangle>\r\n"
				+ "				<reportElement x=\"279\" y=\"257\" width=\"279\" height=\"113\" uuid=\"b9457075-a3f1-4a9d-999a-e040552cc162\"/>\r\n"
				+ "			</rectangle>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"256\" width=\"278\" height=\"19\" uuid=\"8a5ebed1-d591-4a5b-b6a7-239831a182f9\"/>\r\n"
				+ "				<textElement textAlignment=\"Center\">\r\n"
				+ "					<font size=\"12\" isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[FORNECEDOR]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"3\" y=\"269\" width=\"279\" height=\"18\" uuid=\"f5e94461-3d04-491d-8c8e-8155fefda669\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{NOME_FORNECEDOR}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField pattern=\"d/M/yyyy\">\r\n"
				+ "				<reportElement x=\"91\" y=\"97\" width=\"100\" height=\"16\" uuid=\"2012f7d4-65fe-4c3d-898f-b12052568c4d\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_DATA}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"81\" y=\"112\" width=\"226\" height=\"14\" uuid=\"d87aa7ef-2336-482e-ab4b-0397a8cc64da\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_REQUISITANTE}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"49\" y=\"156\" width=\"279\" height=\"22\" uuid=\"e11e0eb5-bf09-4015-b770-c00aa2063c82\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{VEICULO_MODELO}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"0\" y=\"171\" width=\"107\" height=\"15\" uuid=\"7f6d1bca-d03d-4f92-a956-efec35df1b94\"/>\r\n"
				+ "				<textElement>\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[MARCA:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"42\" y=\"171\" width=\"196\" height=\"20\" uuid=\"480e2d36-de05-4c36-910c-cafbbd18bccf\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{VEICULO_MARCA}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"97\" y=\"186\" width=\"199\" height=\"18\" uuid=\"2434e52b-39b3-4820-8d3b-ee22b5ff322a\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{VEICULO_ANO_FABRICACAO}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"74\" y=\"201\" width=\"236\" height=\"19\" uuid=\"1e34e9e4-c2dd-43cd-872b-b5f0b96a154b\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{VEICULO_ANO_MODELO}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"39\" y=\"216\" width=\"174\" height=\"20\" uuid=\"e454c7e6-6e97-462c-b928-911c03aee23e\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{VEICULO_PLACA}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"43\" y=\"230\" width=\"170\" height=\"14\" uuid=\"51be45ce-8699-47d6-a0df-72ba5dc3bd4f\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{VEICULO_CHASSI}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"3\" y=\"282\" width=\"40\" height=\"19\" uuid=\"b07f64cc-87c1-4a7f-832f-29dc15c8b82c\"/>\r\n"
				+ "				<text><![CDATA[CNPJ:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"33\" y=\"282\" width=\"136\" height=\"14\" uuid=\"4afd38fd-ea2c-4b41-9c2e-0c5d75cdb3c5\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{CNPJ_FORNECEDOR}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"3\" y=\"296\" width=\"275\" height=\"40\" uuid=\"ec834aa0-bb62-4294-bc5c-b5c761b45a94\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{ENDERECO_FORNECEDOR}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"3\" y=\"335\" width=\"47\" height=\"19\" uuid=\"a016d3df-0a40-46f0-9038-57a205acedf4\"/>\r\n"
				+ "				<text><![CDATA[E-MAIL:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"42\" y=\"335\" width=\"188\" height=\"18\" uuid=\"0db5c3dd-13a2-437a-b666-4d4541b25473\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{EMAIL_FORNECEDOR}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"2\" y=\"347\" width=\"58\" height=\"19\" uuid=\"49971c7c-5965-4694-898e-9b7d234d8e59\"/>\r\n"
				+ "				<text><![CDATA[TELEFONE:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"60\" y=\"347\" width=\"150\" height=\"24\" uuid=\"e69ca203-dbbd-4faf-83f8-3af67e455853\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{TELEFONE_FORNECEDOR}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"281\" y=\"259\" width=\"276\" height=\"19\" uuid=\"e3d2f075-eb87-4e7a-b9df-b74eaa3c3faf\"/>\r\n"
				+ "				<textElement textAlignment=\"Center\">\r\n"
				+ "					<font size=\"12\" isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[ENVIAR PEÇA PARA]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"281\" y=\"271\" width=\"175\" height=\"18\" uuid=\"760b5b3d-62bd-42e9-8e0c-60ab8dd153c6\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_EMPRESA}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"281\" y=\"283\" width=\"79\" height=\"19\" uuid=\"834e7106-e3da-4e4d-88d8-06404cba1180\"/>\r\n"
				+ "				<text><![CDATA[RESPONSÁVEL:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"361\" y=\"283\" width=\"195\" height=\"19\" uuid=\"5dcba926-3d59-4128-8369-673d61dfa0c5\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_RESPONSAVEL}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"281\" y=\"295\" width=\"239\" height=\"20\" uuid=\"9acbbae2-a492-4bb5-9b0a-5b12cac34635\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_ENDERECO}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"281\" y=\"308\" width=\"130\" height=\"16\" uuid=\"8a29c619-8686-453d-ae2b-7bdd2adc9450\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_BAIRRO}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"410\" y=\"308\" width=\"146\" height=\"15\" uuid=\"789e599f-5284-49a9-9162-a4a046fd1af4\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_CIDADE}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"281\" y=\"321\" width=\"36\" height=\"19\" uuid=\"c3077243-962b-4f8a-801a-df42c95233aa\"/>\r\n"
				+ "				<text><![CDATA[CEP:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"309\" y=\"321\" width=\"100\" height=\"19\" uuid=\"91a32cf7-ec94-4a90-b30f-866996268092\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_CEP}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"281\" y=\"333\" width=\"36\" height=\"19\" uuid=\"54a3b69e-f121-4ed8-949a-7f3d08f910f2\"/>\r\n"
				+ "				<text><![CDATA[TEL:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"304\" y=\"333\" width=\"131\" height=\"18\" uuid=\"61b4147a-7bbf-4122-98ba-ae9254ad9ea3\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_TELEFONE}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"281\" y=\"344\" width=\"48\" height=\"19\" uuid=\"882bb4a8-de8e-4acf-a499-f11555fa8795\"/>\r\n"
				+ "				<text><![CDATA[E-MAIL:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"319\" y=\"344\" width=\"244\" height=\"18\" uuid=\"16bd6ed2-50ec-4b57-a14f-8e7489d29888\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_EMAIL}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"108\" y=\"82\" width=\"154\" height=\"20\" uuid=\"acfc899a-0c4c-4ef6-b611-3332de679ebc\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_NUMERO}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<rectangle>\r\n"
				+ "				<reportElement x=\"-1\" y=\"131\" width=\"560\" height=\"1\" uuid=\"89578c01-315b-47e6-96b0-9a1260b1522a\">\r\n"
				+ "					<property name=\"com.jaspersoft.studio.unit.height\" value=\"px\"/>\r\n"
				+ "				</reportElement>\r\n"
				+ "			</rectangle>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"502\" y=\"295\" width=\"54\" height=\"20\" uuid=\"c2ac0afb-ef2b-488a-a469-fe4c43cb1d3a\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[\"N-\" + $P{PEDIDO_ENDERECO_NUMERO}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<image hAlign=\"Center\" vAlign=\"Top\">\r\n"
				+ "				<reportElement x=\"0\" y=\"19\" width=\"281\" height=\"139\" uuid=\"38c0f68b-bd06-4da6-a83c-10a99ba1c321\"/>\r\n"
				+ "				<imageExpression><![CDATA[$P{LOGO}]]></imageExpression>\r\n"
				+ "			</image>\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement x=\"313\" y=\"356\" width=\"241\" height=\"15\" uuid=\"d867fb7a-31ef-4e70-98d1-f3b2da77a510\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{PEDIDO_CNPJ}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"281\" y=\"356\" width=\"34\" height=\"19\" uuid=\"b2a48f9f-f1ea-4749-aeaf-126df6253cc3\"/>\r\n"
				+ "				<text><![CDATA[CNPJ:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "		</band>\r\n"
				+ "	</title>\r\n"
				+ "	<detail>\r\n"
				+ "		<band height=\"109\">\r\n"
				+ "			<componentElement>\r\n"
				+ "				<reportElement x=\"0\" y=\"0\" width=\"557\" height=\"109\" isRemoveLineWhenBlank=\"true\" backcolor=\"#FF1612\" uuid=\"ee60be5d-95c6-4a4f-92ab-642aa9562c16\">\r\n"
				+ "					<property name=\"com.jaspersoft.studio.layout\" value=\"com.jaspersoft.studio.editor.layout.VerticalRowLayout\"/>\r\n"
				+ "					<property name=\"com.jaspersoft.studio.table.style.table_header\" value=\"Table 1_TH\"/>\r\n"
				+ "					<property name=\"com.jaspersoft.studio.table.style.column_header\" value=\"Table 1_CH\"/>\r\n"
				+ "					<property name=\"com.jaspersoft.studio.table.style.detail\" value=\"Table 1_TD\"/>\r\n"
				+ "					<property name=\"net.sf.jasperreports.style.backcolor\" value=\"#1F5E0B\"/>\r\n"
				+ "				</reportElement>\r\n"
				+ "				<jr:table xmlns:jr=\"http://jasperreports.sourceforge.net/jasperreports/components\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports/components http://jasperreports.sourceforge.net/xsd/components.xsd\">\r\n"
				+ "					<datasetRun subDataset=\"Colecao\" uuid=\"06f00114-30da-4158-9381-e2269341e5de\">\r\n"
				+ "						<dataSourceExpression><![CDATA[$P{CollectionBeanParam}]]></dataSourceExpression>\r\n"
				+ "					</datasetRun>\r\n"
				+ "					<jr:column width=\"310\" uuid=\"061169d6-233a-4973-9dfd-349b70ec30a8\">\r\n"
				+ "						<property name=\"com.jaspersoft.studio.components.table.model.column.name\" value=\"Column1\"/>\r\n"
				+ "						<jr:columnHeader style=\"Table 1_CH\" height=\"30\" rowSpan=\"1\">\r\n"
				+ "							<staticText>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"310\" height=\"30\" forecolor=\"#FFFFFF\" uuid=\"72127b12-7996-4f26-ae66-de529f8ece4a\"/>\r\n"
				+ "								<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
				+ "									<font isBold=\"true\"/>\r\n"
				+ "								</textElement>\r\n"
				+ "								<text><![CDATA[PRODUTO/SERVIÇO]]></text>\r\n"
				+ "							</staticText>\r\n"
				+ "						</jr:columnHeader>\r\n"
				+ "						<jr:detailCell style=\"Table 1_TD\" height=\"30\">\r\n"
				+ "							<textField>\r\n"
				+ "								<reportElement stretchType=\"RelativeToTallestObject\" x=\"0\" y=\"0\" width=\"310\" height=\"30\" uuid=\"ee96d42f-f467-4576-959c-88a44234815c\"/>\r\n"
				+ "								<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$F{item_nome}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:detailCell>\r\n"
				+ "					</jr:column>\r\n"
				+ "					<jr:column width=\"70\" uuid=\"4271db93-64f0-43af-a4d2-6ea750f50e52\">\r\n"
				+ "						<property name=\"com.jaspersoft.studio.components.table.model.column.name\" value=\"Column2\"/>\r\n"
				+ "						<jr:columnHeader style=\"Table 1_CH\" height=\"30\" rowSpan=\"1\">\r\n"
				+ "							<staticText>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"70\" height=\"30\" forecolor=\"#FFFFFF\" uuid=\"836f2bd9-0f9e-4d50-9501-b4e7d150dee4\"/>\r\n"
				+ "								<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
				+ "									<font isBold=\"true\"/>\r\n"
				+ "								</textElement>\r\n"
				+ "								<text><![CDATA[QUANTIDADE]]></text>\r\n"
				+ "							</staticText>\r\n"
				+ "						</jr:columnHeader>\r\n"
				+ "						<jr:detailCell style=\"Table 1_TD\" height=\"30\">\r\n"
				+ "							<textField>\r\n"
				+ "								<reportElement stretchType=\"RelativeToTallestObject\" x=\"0\" y=\"0\" width=\"70\" height=\"30\" uuid=\"46cebbe2-ab8c-40d6-97e9-1514b737adfc\"/>\r\n"
				+ "								<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$F{item_quantidade}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:detailCell>\r\n"
				+ "					</jr:column>\r\n"
				+ "					<jr:column width=\"100\" uuid=\"f8c7bd1b-af09-4270-90ff-4fdb295e0df5\">\r\n"
				+ "						<property name=\"com.jaspersoft.studio.components.table.model.column.name\" value=\"Column3\"/>\r\n"
				+ "						<jr:columnHeader style=\"Table 1_CH\" height=\"30\" rowSpan=\"1\">\r\n"
				+ "							<staticText>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"100\" height=\"30\" forecolor=\"#FFFFFF\" backcolor=\"#00A24C\" uuid=\"93b28599-809c-459c-8b3f-67aac7818b09\"/>\r\n"
				+ "								<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
				+ "									<font isBold=\"true\"/>\r\n"
				+ "								</textElement>\r\n"
				+ "								<text><![CDATA[VALOR UNITÁRIO]]></text>\r\n"
				+ "							</staticText>\r\n"
				+ "						</jr:columnHeader>\r\n"
				+ "						<jr:detailCell style=\"Table 1_TD\" height=\"30\">\r\n"
				+ "							<textField pattern=\"¤#,##0.00;¤-#,##0.00\">\r\n"
				+ "								<reportElement stretchType=\"RelativeToTallestObject\" x=\"0\" y=\"0\" width=\"100\" height=\"30\" uuid=\"aa7313d4-e83f-4044-aead-57e6738a0444\"/>\r\n"
				+ "								<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$F{item_valor}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:detailCell>\r\n"
				+ "					</jr:column>\r\n"
				+ "					<jr:column width=\"80\" uuid=\"cffc9a47-5669-43c3-89c9-d643b3095034\">\r\n"
				+ "						<property name=\"com.jaspersoft.studio.components.table.model.column.name\" value=\"Column4\"/>\r\n"
				+ "						<jr:columnHeader style=\"Table 1_CH\" height=\"30\" rowSpan=\"1\">\r\n"
				+ "							<staticText>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"80\" height=\"30\" forecolor=\"#FFFFFF\" uuid=\"8f70f555-a8c8-4a4f-bea6-65c9363fd4ae\"/>\r\n"
				+ "								<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
				+ "									<font isBold=\"true\"/>\r\n"
				+ "								</textElement>\r\n"
				+ "								<text><![CDATA[VALOR TOTAL]]></text>\r\n"
				+ "							</staticText>\r\n"
				+ "						</jr:columnHeader>\r\n"
				+ "						<jr:detailCell style=\"Table 1_TD\" height=\"30\">\r\n"
				+ "							<textField pattern=\"¤#,##0.00;¤-#,##0.00\">\r\n"
				+ "								<reportElement stretchType=\"RelativeToTallestObject\" x=\"0\" y=\"0\" width=\"80\" height=\"30\" uuid=\"d793f678-6897-44dd-a6bf-f16b81d1fedc\"/>\r\n"
				+ "								<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$F{item_quantidade} * $F{item_valor}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:detailCell>\r\n"
				+ "					</jr:column>\r\n"
				+ "				</jr:table>\r\n"
				+ "			</componentElement>\r\n"
				+ "		</band>\r\n"
				+ "		<band height=\"187\">\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"-20\" y=\"81\" width=\"594\" height=\"106\" uuid=\"90fb6ec1-7cc5-46e8-90b7-35d4e417d235\"/>\r\n"
				+ "				<textElement textAlignment=\"Center\"/>\r\n"
				+ "				<text><![CDATA[OBSERVAÇÕES\r\n"
				+ "CASO ALGUM ITEM DESSE PEDIDO SOFRA ALTERAÇÃO, INFORME PREVIAMENTE AO SETOR DE COMPRAS ATRAVÉS DO E-MAIL \r\n"
				+ "pecas@adasc.net.br. \r\n"
				+ "PARA ANALISARMOS A VIABILIDADE DA ALTERAÇÃO E POSSAMOS APROVAR NOVAMENTE. \r\n"
				+ "Atenciosamente,\r\n"
				+ "ASSOCIAÇÃO DE DESENVOLVIMENTO DOS AMIGOS DE SANTA CATARINA]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<staticText>\r\n"
				+ "				<reportElement x=\"296\" y=\"-1\" width=\"191\" height=\"14\" uuid=\"cbeb640b-155d-4bf0-b171-f9bb1dac008a\"/>\r\n"
				+ "				<textElement textAlignment=\"Right\">\r\n"
				+ "					<font isBold=\"true\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<text><![CDATA[VALOR TOTAL DA NOTA DE COMPRA:]]></text>\r\n"
				+ "			</staticText>\r\n"
				+ "			<textField pattern=\"¤#,##0.00;¤-#,##0.00\">\r\n"
				+ "				<reportElement x=\"482\" y=\"-1\" width=\"72\" height=\"20\" uuid=\"16cbd4dc-c4f5-4617-8c60-fcfcbcae49a6\"/>\r\n"
				+ "				<textElement textAlignment=\"Right\"/>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{VALOR_TOTAL_ORDEM_DE_COMPRA}]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "		</band>\r\n"
				+ "	</detail>\r\n"
				+ "	<lastPageFooter>\r\n"
				+ "		<band height=\"20\">\r\n"
				+ "			<rectangle>\r\n"
				+ "				<reportElement x=\"-20\" y=\"0\" width=\"595\" height=\"20\" backcolor=\"#00A24C\" uuid=\"ca487138-7d1f-4c28-9716-b246b0c10726\"/>\r\n"
				+ "			</rectangle>\r\n"
				+ "		</band>\r\n"
				+ "	</lastPageFooter>\r\n"
				+ "</jasperReport>\r\n"
				+ "";
	}

}
