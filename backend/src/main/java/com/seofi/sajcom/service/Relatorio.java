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
import com.seofi.sajcom.repository.SelicAcumuladaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class Relatorio {
    @Autowired
    private SelicAcumuladaRepository selicAcumuladaRepo;
    Map<Integer, List<BigDecimal>> dictSelicAcumulada;

    Relatorio() {
        this.dictSelicAcumulada = new HashMap<>();
    }

    public byte[] gerarPDF(LocalDate dataInicial, LocalDate dataFinal) throws IOException {
        Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Font font8Bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);

        ByteArrayOutputStream documentoBinario = new ByteArrayOutputStream();

        Document documento = new Document(PageSize.A4.rotate());
        float width = documento.getPageSize().getWidth();

        try {
            PdfWriter writer = PdfWriter.getInstance(documento, documentoBinario);
            writer.getInfo().put(PdfName.TITLE, new PdfString("Tabela de Atualização Monetária"));

            documento.open();

            Paragraph titulo = new Paragraph("TABELA DE ÍNDICES - TAXA SELIC (SIMPLES), DE ACORDO COM A RESOLUÇÃO CSJT Nº 137/2014.", font8Bold);
            Paragraph espaco = new Paragraph(" ");

            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(espaco);

            PdfPTable tabelaSelicAcumulada = new PdfPTable(13);
            gerarTabelaSelicAcumulada(tabelaSelicAcumulada, dataInicial, dataFinal, width);
            documento.add(tabelaSelicAcumulada);
            documento.add(new Paragraph("Obs: Para os créditos não tributários, a partir de dez/2021, a atualização monetária será feita com base na variação da SELIC (EC 113 de 08/12/2021, art. 3º), incidente sobre o valor total consolidado do crédito atualizado até dez/2021. Adoção do regime de capitalização simples da Taxa Selic deve ser aplicada no mês posterior ao de sua competência, inclusive para o mês de pagamento, sem, contudo, aplicar a taxa referente a 1% (um por cento) sobre o valor devido, em razão da ausência de previsão legal para tais pagamento sobre débitos não tributários da Fazenda Pública.\n", font8));

        } catch (RuntimeException ex) {

        }
        System.out.println("Abriu e fechou o PDF.");
        documento.close();

        return documentoBinario.toByteArray();
    }

    private void gerarTabelaSelicAcumulada(PdfPTable tabela, LocalDate dataInical, LocalDate dataFinal, float width) {

        extrairSelicAcumulada();

        Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Font font8Bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);

        tabela.getDefaultCell().setBorder(PdfPCell.BOX);
        tabela.setTotalWidth(width - 72);
        tabela.setLockedWidth(true);

        gerarCabecalhoTabelaSelicAcumulada(tabela, font8Bold);
        gerarLinhasTabelaSelicAcumulada(dataInical, dataFinal, tabela, font8, font8Bold);
    }

    private void gerarCabecalhoTabelaSelicAcumulada(PdfPTable tabela, Font font8Bold) {
        PdfPCell celulaVazia = new PdfPCell(new Phrase(" "));
        PdfPCell celulaJan = new PdfPCell(new Phrase("Janeiro", font8Bold));
        PdfPCell celulaFev = new PdfPCell(new Phrase("Fevereiro", font8Bold));
        PdfPCell celulaMar = new PdfPCell(new Phrase("Março", font8Bold));
        PdfPCell celulaAbr = new PdfPCell(new Phrase("Abril", font8Bold));
        PdfPCell celulaMai = new PdfPCell(new Phrase("Maio", font8Bold));
        PdfPCell celulaJun = new PdfPCell(new Phrase("Junho", font8Bold));
        PdfPCell celulaJul = new PdfPCell(new Phrase("Julho", font8Bold));
        PdfPCell celulaAgo = new PdfPCell(new Phrase("Agosto", font8Bold));
        PdfPCell celulaSet = new PdfPCell(new Phrase("Setembro", font8Bold));
        PdfPCell celulaOut = new PdfPCell(new Phrase("Outubro", font8Bold));
        PdfPCell celulaNov = new PdfPCell(new Phrase("Novembro", font8Bold));
        PdfPCell celulaDez = new PdfPCell(new Phrase("Dezembro", font8Bold));
        celulaJan.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaFev.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaMar.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaAbr.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaMai.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaJun.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaJul.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaAgo.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaSet.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaOut.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaNov.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
        celulaDez.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));

        celulaJan.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaFev.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaMai.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaSet.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaOut.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaDez.setHorizontalAlignment(Element.ALIGN_CENTER);

        celulaVazia.setBorder(PdfPCell.NO_BORDER);

        tabela.addCell(celulaVazia);
        tabela.addCell(celulaJan);
        tabela.addCell(celulaFev);
        tabela.addCell(celulaMar);
        tabela.addCell(celulaAbr);
        tabela.addCell(celulaMai);
        tabela.addCell(celulaJun);
        tabela.addCell(celulaJul);
        tabela.addCell(celulaAgo);
        tabela.addCell(celulaSet);
        tabela.addCell(celulaOut);
        tabela.addCell(celulaNov);
        tabela.addCell(celulaDez);
    }

    private void gerarLinhasTabelaSelicAcumulada(LocalDate dataInicial, LocalDate dataFinal, PdfPTable tabela, Font font8, Font font8Bold) {
        for (Map.Entry<Integer, List<BigDecimal>> entry : this.dictSelicAcumulada.entrySet()) {
            int ano = entry.getKey();
            List<BigDecimal> valores = entry.getValue();

            // Adicionar ano
            PdfPCell celulaAno = new PdfPCell(new Phrase(String.valueOf(ano), font8Bold));
            if (ano % 2 == 0) {
                celulaAno.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x33));
            }
            celulaAno.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabela.addCell(celulaAno);

            // Adicionar valores mensais
            for (int i = 0; i < valores.size(); i++) {
                BigDecimal valor = valores.get(i);
                PdfPCell celula = new PdfPCell(new Phrase(valor != null ? String.valueOf(valor) : "", font8));
                celula.setHorizontalAlignment(Element.ALIGN_CENTER);
                if (ano % 2 == 0) {
                    celula.setBackgroundColor(new Color(0x69, 0x69, 0x69, 0x0A));
                }
                //Colorir celulas que sao entre o intervalo da divida do solicitante
                if ((ano > dataInicial.getYear() || (ano == dataInicial.getYear() && i >= dataInicial.getMonthValue() - 1)) &&
                        (ano < dataFinal.getYear() || (ano == dataFinal.getYear() && i <= dataFinal.getMonthValue() - 1))) {
                    celula.setBackgroundColor(new Color(0xFF, 0xFF, 0x00, 0x33));
                }
                tabela.addCell(celula); // Adiciona valor ou vazio
            }
        }
    }

    public void extrairSelicAcumulada() {
        List<SelicAcumuladaDTO> indicesSelicAcumulada = this.selicAcumuladaRepo.buscarTudo();

        for (SelicAcumuladaDTO indice : indicesSelicAcumulada) {
            criarDictSelicAcumulada(indice);
        }
    }

    private void criarDictSelicAcumulada(SelicAcumuladaDTO indice) {
        Integer ano = indice.data().getYear();
        dictSelicAcumulada.putIfAbsent(ano, new ArrayList<BigDecimal>((Collections.nCopies(12, null))));
        List<BigDecimal> valores = dictSelicAcumulada.get(ano);
        int mes = indice.data().getMonthValue() - 1;
        valores.set(mes, indice.valor());
    }
}
