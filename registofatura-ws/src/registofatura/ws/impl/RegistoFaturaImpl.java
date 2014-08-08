package registofatura.ws.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import jvstm.Atomic;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.registofatura.ws.ClienteInexistente;
import pt.registofatura.ws.ClienteInexistente_Exception;
import pt.registofatura.ws.EmissorInexistente;
import pt.registofatura.ws.EmissorInexistente_Exception;
import pt.registofatura.ws.Fatura;
import pt.registofatura.ws.FaturaInvalida;
import pt.registofatura.ws.FaturaInvalida_Exception;
import pt.registofatura.ws.ItemFatura;
import pt.registofatura.ws.RegistoFaturaPortType;
import pt.registofatura.ws.Serie;
import pt.registofatura.ws.TotaisIncoerentes;
import pt.registofatura.ws.TotaisIncoerentes_Exception;
import registofatura.ws.domain.Cliente;
import registofatura.ws.domain.Emissor;
import registofatura.ws.domain.RegistoFatura;
import registofatura.ws.domain.XMLCalendarToDate;

//@formatter:off
@WebService(
        endpointInterface = "pt.registofatura.ws.RegistoFaturaPortType", 
        wsdlLocation = "RegistoFatura.wsdl",
        name = "RegistoFaturaPortType", 
        portName = "RegistoFaturaPort", 
        targetNamespace = "urn:pt:registofatura:ws", 
        serviceName = "RegistoFaturaService")
//@formatter:on
@HandlerChain(file = "/handler-chain.xml")
public class RegistoFaturaImpl
        implements RegistoFaturaPortType {

    private static final int MAX_FATURAS_SERIE = 4;

    private final boolean debug = true;

    public RegistoFaturaImpl() {
    }

    @Override
    @Atomic
    public Serie pedirSerie(int nifEmissor) throws EmissorInexistente_Exception {
        RegistoFatura registo = FenixFramework.getRoot();
        getEmissor(nifEmissor);

        final DateTime date = new DateTime().plusDays(10);
        final int numeroDeSerie = registo.getNextNumSerie();
        final registofatura.ws.domain.Serie serieLocal = new registofatura.ws.domain.Serie(
                nifEmissor, numeroDeSerie, date);
        registo.addSerie(serieLocal);

        final Serie serieRemota = serieLocal.getRemoteSerie();
        return serieRemota;
    }

    /**
     * Retorna o emissor que possui o NIF indicado.
     * 
     * @param nif NIF do emissor
     * @return O Emissor correspondente.
     * @throws EmissorInexistente_Exception Caso o emissor não exista (não exista com
     *             aquele NIF).
     */
    private Emissor getEmissor(int nif) throws EmissorInexistente_Exception {
        RegistoFatura registo = FenixFramework.getRoot();
        for (Emissor e : registo.getEmissor()) {
            if (e.getNif() == nif) {
                return e;
            }
        }
        EmissorInexistente emissorErrado = new EmissorInexistente();
        String mensagem = "O emissor com o nif " + nif + "não existe.";
        emissorErrado.setMensagem(mensagem);
        throw new EmissorInexistente_Exception(mensagem, emissorErrado);
    }

    /**
     * Retorna o cliente que possui o NIF indicado.
     * 
     * @param nif NIF do cliente
     * @return O Cliente correspondente.
     * @throws ClienteInexistente_Exception Caso o cliente não exista (não exista com
     *             aquele NIF).
     */
    private Cliente getCliente(int nif) throws ClienteInexistente_Exception {
        RegistoFatura registo = FenixFramework.getRoot();
        for (Cliente c : registo.getCliente()) {
            if (c.getNif() == nif) {
                return c;
            }
        }
        ClienteInexistente clienteErrado = new ClienteInexistente();
        String mensagem = "O cliente com o nif " + nif + "não existe.";
        clienteErrado.setMensagem(mensagem);
        throw new ClienteInexistente_Exception(mensagem, clienteErrado);
    }

    @Override
    @Atomic
    /**
     * Comunica a factura recebida, caso esta verifique as condições indicadas.
     * 
     * @throws ClienteInexistente_Exception 
     *          Caso o cliente não esteja registado.
     * @throws EmissorInexistente_Exception 
     *          Caso o emissor não esteja registado.
     * @throws TotaisIncoerentes_Exception 
     *          Caso o total e o iva indicado na fatura nao corresponda ao total dos itens
     *          da fatura e ao total dos respectivos items.
     * @throws FaturaInvalida_Exception 
     *          Lança a excepção se uma das seguintes situações se verificar:
     *            - caso a fatura já tenha sido comunicada; 
     *            - caso a fatura tenha uma data de validade superior à permitida pela série;
     *            - caso o nif do emissor não corresponda ao nif da série;
     *            - caso o número de sequência seja inferior aos comunicados (não seja sequencial);
     *            - caso já tenham sido comunicadas 4 faturas para o número de série indicado. 
     */
    public void comunicarFatura(Fatura fatura) throws ClienteInexistente_Exception,
            EmissorInexistente_Exception,
            FaturaInvalida_Exception,
            TotaisIncoerentes_Exception {

        System.out.println("comunicou fatura. numSeq = " + fatura.getNumSeqFatura() + " numSerie "
                + fatura.getNumSerie());
        getEmissor(fatura.getNifEmissor());
        getCliente(fatura.getNifCliente());
        System.out.println("check");
        checkFaturaValida(fatura);
        System.out.println("pos check");


        // Verifica os totais
        final int totalFatura = fatura.getTotal();
        final int ivaCobrado = fatura.getIva();
        final BigDecimal taxaIva = new BigDecimal("0.23");

        int totalItems = 0;
        BigDecimal ivaTotalItems = BigDecimal.valueOf(0);
        for (ItemFatura item : fatura.getItens()) {
            int preco = item.getPreco();
            totalItems += preco;

            // iva = P * taxaIva / (1 + taxaIva)
            // Define a precisão da divisão nas 2 casas decimais e arredonda para cima no 5
            BigDecimal ivaItem = BigDecimal.valueOf(preco).multiply(taxaIva)
                    .divide(taxaIva.add(BigDecimal.valueOf(1)), 2, RoundingMode.HALF_UP);
            ivaTotalItems = ivaTotalItems.add(ivaItem);
        }


        // arredonda o valor obtido para inteiro (antes da virgula)
        ivaTotalItems = ivaTotalItems.setScale(0, RoundingMode.HALF_UP);
        if (totalItems != totalFatura || !ivaTotalItems.equals(BigDecimal.valueOf(ivaCobrado))) {
            //@formatter:off
            StringBuilder message = new StringBuilder("O total da fatura não equivale ao iva cobrado mais a quantidade de itens comprada.\n");
            if (debug) {        
                message.append(" total factura: ").append(totalFatura) 
                       .append("\n total items: ").append(totalItems)
                       .append("\n iva total: "  ).append(ivaTotalItems.toString()) 
                       .append("\n iva cobrado: ").append(ivaCobrado);
                System.out.println(message.toString());
            }
          //@formatter:on
            throw new TotaisIncoerentes_Exception(message.toString(), new TotaisIncoerentes());
        }


        RegistoFatura registo = FenixFramework.getRoot();
        registofatura.ws.domain.Serie s = registo.getSerie(fatura.getNumSerie());
        DateTime data = new DateTime(XMLCalendarToDate.toDate(fatura.getData()));

        //@formatter:off
        registofatura.ws.domain.Fatura faturaLocal = 
                new registofatura.ws.domain.Fatura(
                    data,
                    fatura.getNumSeqFatura(), 
                    fatura.getNumSerie(), 
                    fatura.getNifEmissor(),
                    fatura.getNomeEmissor(),
                    fatura.getNifCliente(),
                    fatura.getIva(),
                    fatura.getTotal()
               );
        //@formatter:on

        s.addFatura(faturaLocal);
        System.out.println("comunicou fatura");

    }

    /*
     * Verifica se a serie com o numero de série passado existe.
     * @param numSerie Número de série a verificar
     */
    private boolean existsNumSerie(int numSerie) {
        RegistoFatura registo = FenixFramework.getRoot();
        int numSeriesEmitidas = registo.getSerieCount();
        System.out.println("numSerie: " + numSerie + "numSeriesEmitidas: " + numSeriesEmitidas);
        if (numSerie <= numSeriesEmitidas) {
            return true;
        }

        return false;
    }

    /**
     * Verifica se uma fatura é válida e lança {@link FaturaInvalida_Exception} se a
     * fatura já tinha sido antes comunicada, a série não existir, a data da fatura
     * estiver inválida (fora da validade da série de faturas a que a fatura pertence ou
     * violando a ordem de datas de outras faturas já comunicadas para a mesma série) ou
     * se já tiver sido emitido o número máximo de faturas para o seu número de série.
     * 
     * @param fatura A fatura a ser verificada.
     */
    private void checkFaturaValida(Fatura fatura) throws FaturaInvalida_Exception {

        RegistoFatura registo = FenixFramework.getRoot();

        final int numSerie = fatura.getNumSerie();
        final registofatura.ws.domain.Serie serie = registo.getSerie(numSerie);

        /**
         * Verifica se o numero de série é válido e se o nif do emissor corresponde ao nif
         * do emissor da série.
         */
        if (!existsNumSerie(numSerie)) {
            throw new FaturaInvalida_Exception("Numero de serie invalido", new FaturaInvalida());
        }

        if (serie.getNifEmissor() != fatura.getNifEmissor()) {
            throw new FaturaInvalida_Exception("O emissor da série difere do emissor da fatura",
                    new FaturaInvalida());
        }

        /**
         * Garante que o registo da fatura não ultrapassa o limite máximo de faturas por
         * série (4).
         */
        if (serie.getFaturaCount() >= MAX_FATURAS_SERIE) {
            throw new FaturaInvalida_Exception("Numero maximo de faturas da serie excedido",
                    new FaturaInvalida());
        }

        for (registofatura.ws.domain.Fatura f : serie.getFatura()) {

            if (fatura.getNumSeqFatura() <= f.getNumSeqFatura()) {
                throw new FaturaInvalida_Exception("Numero de sequencia invalido",
                        new FaturaInvalida());
            }
        }

        /**
         * Garante que a fatura não excede a data de validade da série.
         */
        final Date faturaDate = XMLCalendarToDate.toDate(fatura.getData());
        final Date dataValidade = serie.getValidoAte().toDate();
        if (faturaDate.after(dataValidade)) {
            throw new FaturaInvalida_Exception("Data de validade da serie excedida",
                    new FaturaInvalida());
        }

        /**
         * Verifica se a data de validade da fatura é inferior à data de validade da
         * última fatura registada.
         */
        if (serie.hasAnyFatura()) {
            final int length = serie.getFaturaCount();
            final registofatura.ws.domain.Fatura lastFatura = serie.getFatura().get(length - 1);
            if (faturaDate.before(lastFatura.getData().toDate())) {
                throw new FaturaInvalida_Exception(
                        "Fatura com data inferior a uma fatura ja emitida", new FaturaInvalida());
            }
        }
    }

    @Override
    @Atomic
    public List<Fatura> listarFacturas(Integer nifEmissor, Integer nifCliente) throws ClienteInexistente_Exception,
            EmissorInexistente_Exception {
        RegistoFatura registo = FenixFramework.getRoot();

        /*
         * Lançam uma excepção caso o cliente ou emissor não existam. 
         */
        getEmissor(nifEmissor);
        getCliente(nifCliente);

        List<Fatura> faturas = new ArrayList<Fatura>();
        for (registofatura.ws.domain.Serie s : registo.getSerie()) {
            for (registofatura.ws.domain.Fatura f : s.getFatura()) {
                if (nifEmissor.equals(f.getNifEmissor()) && nifCliente.equals(f.getNifCliente())) {
                    faturas.add(f.getRemoteFatura());
                }
            }
        }
        return faturas;
    }

    @Override
    @Atomic
    public int consultarIVADevido(int nifEmissor, XMLGregorianCalendar ano) throws EmissorInexistente_Exception {
        RegistoFatura registo = FenixFramework.getRoot();
        getEmissor(nifEmissor);
        int IVADevido = 0;
        for (registofatura.ws.domain.Serie s : registo.getSerie()) {
            for (registofatura.ws.domain.Fatura f : s.getFatura()) {
                int anoFatura = f.getGregorianData().getYear();
                int anoRecebido = ano.getYear();
                if (nifEmissor == f.getNifEmissor() && anoFatura == anoRecebido) {
                    IVADevido += f.getIva();
                }
            }
        }
        return IVADevido;
    }

}
