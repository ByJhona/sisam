package com.seofi.sajcom.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import com.seofi.sajcom.domain.SelicAcumuladaDTO;
import com.seofi.sajcom.repository.IndiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class Relatorio {
    @Autowired
    private IndiceRepository indiceRepo;
    private static final Color COR_CINZA = new Color(0x69, 0x69, 0x69, 0x33);
    private static final Color COR_AMARELO = new Color(0xFF, 0xFF, 0x00, 0x33);
    private static final Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8);
    private static final Font font8Bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);



    public byte[] gerarPDF(LocalDate dataInicial, LocalDate dataFinal) throws IOException {


        ByteArrayOutputStream documentoBinario = new ByteArrayOutputStream();

        Document documento = new Document(PageSize.A4.rotate());
        float width = documento.getPageSize().getWidth();

        try {
            PdfWriter writer = PdfWriter.getInstance(documento, documentoBinario);
            writer.getInfo().put(PdfName.TITLE, new PdfString("Tabela de Atualização Monetária"));

            documento.open();

            Paragraph titulo = inserirTitulo( "TABELA DE ÍNDICES - TAXA SELIC (SIMPLES), DE ACORDO COM A RESOLUÇÃO CSJT Nº 137/2014.");
            documento.add(titulo);

            PdfPTable tabelaSelicAcumulada = new PdfPTable(13);
            gerarTabelaSelicAcumulada(tabelaSelicAcumulada, dataInicial, dataFinal, width);
            documento.add(tabelaSelicAcumulada);

            documento.add(new Paragraph("Obs: Para os créditos não tributários, a partir de dez/2021, a atualização monetária será feita com base na variação da SELIC (EC 113 de 08/12/2021, art. 3º), incidente sobre o valor total consolidado do crédito atualizado até dez/2021. Adoção do regime de capitalização simples da Taxa Selic deve ser aplicada no mês posterior ao de sua competência, inclusive para o mês de pagamento, sem, contudo, aplicar a taxa referente a 1% (um por cento) sobre o valor devido, em razão da ausência de previsão legal para tais pagamento sobre débitos não tributários da Fazenda Pública.\n", font8));

        } catch (RuntimeException ex) {

        }
        documento.close();

        return documentoBinario.toByteArray();
    }

    private Paragraph inserirTitulo(String tituloString){
        Paragraph titulo = new Paragraph(tituloString, font8Bold);
        Paragraph espaco = new Paragraph(" \n");
        titulo.add(espaco);

        titulo.setAlignment(Element.ALIGN_CENTER);
       return titulo;
    }

    private void gerarTabelaSelicAcumulada(PdfPTable tabela, LocalDate dataInical, LocalDate dataFinal, float width) {
        extrairSelicAcumulada();
        Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Font font8Bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        tabela.getDefaultCell().setBorder(PdfPCell.BOX);
        tabela.setTotalWidth(width - 72);
        tabela.setLockedWidth(true);
        gerarCabecalhoTabelaSelicAcumulada(tabela, font8Bold);
        List<PdfPCell> celulasLinhas = gerarLinhasTabelaSelicAcumulada(dataInical, dataFinal, font8, font8Bold);
        for(PdfPCell celulaLinha: celulasLinhas){
            tabela.addCell(celulaLinha);
        }
    }

    private void gerarCabecalhoTabelaSelicAcumulada(PdfPTable tabela, Font font8Bold) {
        PdfPCell celulaVazia = criarCelulaVazia();
        tabela.addCell(celulaVazia);

        List<PdfPCell> celulasMeses = criarCelulasCabecalho(font8Bold);

        for(PdfPCell celulaMes: celulasMeses){
            tabela.addCell(celulaMes);
        }
    }

    private PdfPCell criarCelulaVazia(){
        PdfPCell celulaVazia = new PdfPCell(new Phrase(" "));
        celulaVazia.setBorder(PdfPCell.NO_BORDER);
        return celulaVazia;
    }

    private List<PdfPCell> criarCelulasCabecalho(Font font8Bold ){
        List<String> meses = Arrays.asList("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");
        List<PdfPCell> celulas = new ArrayList<>();

        for (String mes : meses) {
            PdfPCell celula = new PdfPCell(new Phrase(mes, font8Bold));
            celula.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
            celula.setHorizontalAlignment(Element.ALIGN_CENTER);
            celulas.add(celula);
        }

        return celulas;
    }

    private List<PdfPCell> gerarLinhasTabelaSelicAcumulada(LocalDate dataInicial, LocalDate dataFinal, Font font8, Font font8Bold) {
        List<PdfPCell> celulas = new ArrayList<>();
        Map<Integer, List<BigDecimal>> dictSelicAcumulada = extrairSelicAcumulada();

        for (Map.Entry<Integer, List<BigDecimal>> entry : dictSelicAcumulada.entrySet()) {
            Integer ano = entry.getKey();
            List<BigDecimal> valores = entry.getValue();

            PdfPCell celulaAno = criarCelulaAno(ano, font8Bold);
            celulas.add(celulaAno);
            // Adicionar valores mensais
            for (int mes = 0; mes < valores.size(); mes++) {
                BigDecimal valor = valores.get(mes);
                PdfPCell celula = criarCelulaValor(valor, font8, ano, mes, dataInicial, dataFinal);
                celulas.add(celula);
            }
        }
        return celulas;
    }

    private boolean verificarIntervaloDatas(Integer ano, Integer mes, LocalDate dataInicial, LocalDate dataFinal){
        if ((ano > dataInicial.getYear() || (ano == dataInicial.getYear() && mes >= dataInicial.getMonthValue() - 1)) &&
                (ano < dataFinal.getYear() || (ano == dataFinal.getYear() && mes <= dataFinal.getMonthValue() - 1))) {
            return true;
        }
        return false;
    }

    private PdfPCell criarCelulaAno(Integer ano, Font font8Bold){
        PdfPCell celulaAno = new PdfPCell(new Phrase(String.valueOf(ano), font8Bold));
        if (ano % 2 == 0) {
            celulaAno.setBackgroundColor(COR_CINZA);
        }
        celulaAno.setHorizontalAlignment(Element.ALIGN_CENTER);
        return celulaAno;
    }

    private PdfPCell criarCelulaValor(BigDecimal valor, Font font8, Integer ano, Integer mes, LocalDate dataInicial, LocalDate dataFinal){
        PdfPCell celula = new PdfPCell(new Phrase(valor != null ? String.valueOf(valor) : "", font8));
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (ano % 2 == 0) {
            celula.setBackgroundColor(COR_CINZA);
        }
        if(verificarIntervaloDatas(ano, mes, dataInicial, dataFinal)){
            celula.setBackgroundColor(COR_AMARELO);
        }
        return celula;
    }

    private Map<Integer, List<BigDecimal>> extrairSelicAcumulada() {
        Map<Integer, List<BigDecimal>> dictSelicAcumulada = new HashMap<>();
        List<SelicAcumuladaDTO> indicesSelicAcumulada = new ArrayList<>();

        for (SelicAcumuladaDTO indice : indicesSelicAcumulada) {
            criarDictSelicAcumulada(dictSelicAcumulada, indice);
        }
        return dictSelicAcumulada;
    }

    private void criarDictSelicAcumulada(Map<Integer, List<BigDecimal>> dictSelicAcumulada, SelicAcumuladaDTO indice) {

        Integer ano = indice.data().getYear();
        dictSelicAcumulada.putIfAbsent(ano, new ArrayList<BigDecimal>((Collections.nCopies(12, null))));
        List<BigDecimal> valores = dictSelicAcumulada.get(ano);
        int mes = indice.data().getMonthValue() - 1;
        valores.set(mes, indice.valor());
    }
}
