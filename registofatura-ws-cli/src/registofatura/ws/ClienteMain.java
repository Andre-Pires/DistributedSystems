package registofatura.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import pt.registofatura.ws.ClienteInexistente_Exception;
import pt.registofatura.ws.EmissorInexistente_Exception;
import pt.registofatura.ws.Fatura;
import pt.registofatura.ws.FaturaInvalida_Exception;
import pt.registofatura.ws.ItemFatura;
import pt.registofatura.ws.RegistoFaturaPortType;
import pt.registofatura.ws.Serie;
import pt.registofatura.ws.TotaisIncoerentes_Exception;
import registofatura.ws.uddi.UDDINaming;

public class ClienteMain {

    private static final int NIF_CLIENTE = 1001;
    private static final int MAX_VALIDADE = 10;
    private static final int NIF_MARIA = 2222;
    private RegistoFaturaPortType registoFatura;

    /**
     * Serve para propósitos de teste.
     */
    private static final DateTime dateTime = new DateTime(2000, 1, 1, 1, 1, 0, 0);

    /**
     * Stub para a invocação dos métodos remotos do serviço.
     */
    public static RegistoFaturaPortType port;
    private static UDDINaming uddiNaming;
    private static String name;

    /**
     * @return the registoFatura
     */
    public RegistoFaturaPortType getRegistoFatura() {
        return this.registoFatura;
    }

    /**
     * @param registoFatura the registoFatura to set
     */
    public void setRegistoFatura(RegistoFaturaPortType registoFatura) {
        this.registoFatura = registoFatura;
    }

    public ClienteMain() {
    }

    public static UDDINaming getUddiNaming() {
        return uddiNaming;
    }

    public static String getWebserviceName() {
        return name;
    }

    //public ClienteMain(RegistoFaturaPortType registoFatura) {
    //    this.registoFatura = registoFatura;
    //}

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", ClienteMain.class.getName());
            return;
        }

        String uddiURL = args[0];
        name = args[1];

        System.out.printf("Contacting UDDI at %s%n", uddiURL);
        uddiNaming = new UDDINaming(uddiURL);

        System.out.printf("Looking for '%s'%n", name);
        String endpointAddress = uddiNaming.lookup(name);

        if (endpointAddress == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }


        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage message = mf.createMessage();
        message.getSOAPHeader().setAttribute("CheckAvailable", "Hello");

        SOAPConnectionFactory cf = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = cf.createConnection();

        while (true) {
            try {
                soapConnection.call(message, endpointAddress);
                break;
            } catch (SOAPException se) {
                //Not found; retry
                endpointAddress = uddiNaming.lookup(name);
            }
        }


        System.out.println("Creating stub ...");
        HandlerService service = new HandlerService();
        port = service.getRegistoFaturaPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);


        //@formatter:off
        /************************************************************************************************************
         * Testa as várias funcionalidades oferecidas pelo web-service. Analisa individualmente os
         * casos de erro para cada uma das funções fornecidas pelo web-service. Verifica as
         * situações onde o serviço deve detectar comportamento inválido.
         *  São testadas as seguintes situações: 
         * 
         * - Comunicar fatura com uma data de validade ultrapassada;
         * - Comunicar fatura com um numero de série inexistente;
         * - Comunicar fatura com uma data anterior à data das faturas da sua série;
         * - Comunicar fatura com um NIF de um emissor inexistente;
         * - Comunicar fatura com totais incoerentes
         ****
         * - Pedir uma série para um emissor inexistente
         * - Pedir uma série para um emissor registado no serviço
         ****
         * - Verificar o iva total das faturas com o iva retornado pela função consultarIVADevido();
         * 
         * Assume que a base de dados se encontra populada com os exemplos de teste fornecidos pelo corpo docente. É
         * necessário ter as bibliotecas de suporte ao Junit 4.11
         * 
         * @see <a href="http://bit.ly/My9IXz">junit.jar</a>
         * @see <a href="http://bit.ly/1gbl25b">hamcrest-core.jar</a>
         */
        //@formatter:on

        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(ClienteMain.class);
    }

    @Test
    public void pedirSerieEmissorCorrecto() {
        // Act
        try {
            port.pedirSerie(NIF_MARIA);
        } catch (EmissorInexistente_Exception e) {
            fail("O nif do Emissor existe; NÃO deveria ter lançado EmissorInexistente().");
        }
        System.out.println("pedir serie emissor correcto - OK");
    }

    @Test
    public void pedirSerieEmissorInexistente() {
        // Arrange
        final int numEmissorErrado = 123456;

        // Act
        try {
            port.pedirSerie(numEmissorErrado);
        } catch (EmissorInexistente_Exception e) {
            System.out.println("pedir série emissor inexistente: - OK");
            return;
        }

        // Assert
        fail("O nif do Emissor não existe; deveria ter lançado EmissorInexistente().");
    }

    // **
    // * Comunica uma fatura sem items e valores correctos. Não deve acusar nenhuma
    // * excepção.
    // *
    @Test
    public void comunicarFaturaSemItemsCorrecta() {
        // Arrange
        Serie s = null;
        try {
            s = port.pedirSerie(NIF_MARIA);
            assertNotNull("Não conseguiu pedir a serie para correr o teste", s);
        } catch (EmissorInexistente_Exception e) {
            fail("comunicarFaturaSemItems(): Não foi possível pedir a serie (Arrange).");
        }
        final int numSerie = s.getNumSerie();
        final XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(dateTime
                .toDate());

        // Act
        try {
            port.comunicarFatura(createFatura(gregDate, numSerie, 1, "mariazinha", NIF_MARIA,
                    NIF_CLIENTE, 0, 0));
        } catch (TotaisIncoerentes_Exception e) {
            System.err.println(e.getMessage());
            fail("Comunicar fatura enviou uma fatura correcta. Não deveria ter lançado uma excepção.");
        } catch (ClienteInexistente_Exception e) {
            fail("Comunicar fatura enviou uma fatura correcta. Não deveria ter lançado uma excepção (ClienteInexistente).");
        } catch (EmissorInexistente_Exception e) {
            fail("Comunicar fatura enviou uma fatura correcta. Não deveria ter lançado uma excepção (EmissorInexistente).");
        } catch (FaturaInvalida_Exception e) {
            fail("Comunicar fatura enviou uma fatura correcta. Não deveria ter lançado uma excepção (FaturaInvalida).");
        }

        // Assert
        System.out.println("comunicar fatura sem items correcta - OK");
    }

    /**
     * Comunica uma fatura com items e valores correctos. Não deve acusar nenhuma
     * excepção.
     */
    @Test
    public void comunicarFaturaComItemsCorrecta() {
        // Arrange
        Serie s = null;
        try {
            s = port.pedirSerie(NIF_MARIA);
            assertNotNull(s);
        } catch (EmissorInexistente_Exception e) {
            fail("comunicarFaturaSemItems(): Não conseguiu pedir a serie.");
        }
        final int numSerie = s.getNumSerie();
        final XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(dateTime
                .toDate());
        final Fatura fatura = createFaturaComItems(gregDate, numSerie, 1, "mariazinha", NIF_MARIA,
                NIF_CLIENTE);

        try {
            port.comunicarFatura(fatura);
        } catch (TotaisIncoerentes_Exception e) {
            System.err.println(e.getMessage());
            fail("\nAtirou TotaisIncoerentes quando nao devia.");
        } catch (ClienteInexistente_Exception e) {
            fail("Excepção Errada (ClienteInexistente).");
        } catch (EmissorInexistente_Exception e) {
            fail("Excepção Errada (EmissorInexistente).");
        } catch (FaturaInvalida_Exception e) {
            fail("Excepção Errada (ClienteInexistente).");
        }

        System.out.println("comunicar fatura com items - OK");
    }

    /**
     * Comunica uma fatura com itens, mas comunica com o total da fatura diferente do
     * total dos items. Deve acusar uma {@link TotaisIncoerentes_Exception}.
     */
    public void comunicarFaturaComItemsTotalErrado() {
        // ARRANGE
        Serie s = null;
        try {
            s = port.pedirSerie(NIF_MARIA);
            assertNotNull("Não conseguiu pedir a serie para correr o teste", s);
        } catch (EmissorInexistente_Exception e) {
            fail("comunicarFaturaSemItems(): Não foi possível pedir a serie (Arrange).");
        }
        final int numSerie = s.getNumSerie();
        final XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(dateTime
                .toDate());
        final int totalErrado = 12345;
        final Fatura fatura = createFaturaComItems(gregDate, numSerie, 1, "mariazinha", NIF_MARIA,
                NIF_CLIENTE);
        fatura.setTotal(totalErrado);


        try {
            port.comunicarFatura(fatura);
        } catch (TotaisIncoerentes_Exception e) {
            System.out.println("comunicar fatura com items total errado - OK");
            return;
        } catch (ClienteInexistente_Exception e) {
            fail("Excepção Errada (ClienteInexistente).");
        } catch (EmissorInexistente_Exception e) {
            fail("Excepção Errada (EmissorInexistente).");
        } catch (FaturaInvalida_Exception e) {
            fail("Excepção Errada (FaturaInvalida).");
        }

        fail("Comunicou uma fatura com total != total dos items.");
    }

    @Test
    public void comunicarFaturaSerieInexistente() {
        //Arrange
        final ClienteMain g = new ClienteMain();
        // numero inexistente
        final int numSerie = 123456;
        final XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(dateTime
                .toDate());

        //Act
        try {
            final Fatura fatura = g.createFaturaComItems(gregDate, numSerie, 1, "mariazinha",
                    NIF_MARIA, NIF_CLIENTE);
            port.comunicarFatura(fatura);
        } catch (FaturaInvalida_Exception fe) {
            System.out.println("comunicar fatura serie inexistente - OK");
            return;
        } catch (TotaisIncoerentes_Exception e) {
            fail("Excepção errada (TotaisIncoerentes)");
        } catch (EmissorInexistente_Exception e) {
            fail("Excepção errada (EmissorInexistente)");
        } catch (ClienteInexistente_Exception e) {
            fail("Excepção errada (ClienteInexistente)");
        }

        // Assert
        fail("Comunicar fatura enviou uma fatura com um número de série inexistente. Não lançou uma excepção como deveria.");
    }

    @Test
    public void comunicarFaturaValidadeExpirada() throws EmissorInexistente_Exception {
        // Arrange
        final int numSerie = port.pedirSerie(NIF_MARIA).getNumSerie();
        DateTime dataExpirada = new DateTime().plusDays(MAX_VALIDADE + 1);
        final XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(dataExpirada
                .toDate());

        // Act
        try {
            final Fatura fatura = createFaturaComItems(gregDate, numSerie, 1, "mariazinha",
                    NIF_MARIA, NIF_CLIENTE);
            port.comunicarFatura(fatura);
        } catch (FaturaInvalida_Exception fe) {
            System.out.println("comunicar fatura validade expirada - OK");
            return;
        } catch (TotaisIncoerentes_Exception e) {
            fail("Excepção errada (TotaisIncoerentes)");
        } catch (EmissorInexistente_Exception e) {
            fail("Excepção errada (EmissorInexistente)");
        } catch (ClienteInexistente_Exception e) {
            fail("Excepção errada (ClienteInexistente)");
        }

        // Assert
        fail("Comunicar fatura recebeu uma fatura com a validade expirada e não lançou uma excepção como deveria.");
    }

    @Test
    public void comunicarFaturaDataAnteriorOutras() {
        // Arrange
        Serie s = null;
        DateTime dateTimeAnterior = new DateTime(1999, 1, 1, 1, 1, 0, 0);
        try {
            s = port.pedirSerie(NIF_MARIA);
            assertNotNull("Não conseguiu pedir a série para correr o teste", s);
        } catch (EmissorInexistente_Exception e) {
            fail("comunicarFaturaSemItems(): Não foi possível pedir a serie (emissor inexistente).");
        }

        // Comunica uma primeira fatura
        final int numSerie = s.getNumSerie();
        final XMLGregorianCalendar gregDateAnterior = XMLCalendarToDate
                .toXMLGregorianCalendar(dateTimeAnterior.toDate());
        final XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(dateTime
                .toDate());
        try {
            port.comunicarFatura(createFatura(gregDate, numSerie, 10, "mariazinha", NIF_MARIA,
                    NIF_CLIENTE, 0, 0));
        } catch (TotaisIncoerentes_Exception e) {
            System.out.println(e.getMessage());
            fail("Excepção no arrange.");
        } catch (ClienteInexistente_Exception e) {
            //////            e.printStackTrace();
            fail("Excepção no arrange.");
        } catch (EmissorInexistente_Exception e) {
            //////            e.printStackTrace();
            fail("Excepção no arrange.");
        } catch (FaturaInvalida_Exception e) {
            //////            e.printStackTrace();
            fail("Excepção no arrange.");
        }

        // Act
        try {
            port.comunicarFatura(createFatura(gregDateAnterior, numSerie, 20, "mariazinha",
                    NIF_MARIA, NIF_CLIENTE, 0, 0));
        } catch (FaturaInvalida_Exception e) {
            System.out.println("comunicar fatura com data anterior às outras - OK");
            return;
        } catch (ClienteInexistente_Exception e) {
            ////            e.printStackTrace();
            fail("Excepção errada.");
        } catch (EmissorInexistente_Exception e) {
            ////            e.printStackTrace();
            fail("Excepção errada.");
        } catch (TotaisIncoerentes_Exception e) {
            ////            e.printStackTrace();
            fail("Excepção errada.");
        }

        // Assert
        fail("Comunicou uma fatura com uma data inválida. Não lançou uma excepção como deveria.\n");
    }

    /**
     * Comunica uma fatura com um emissor que nao corresponde a quem pediu a serie.
     * Verifica se lança a excepção {@link FaturaInvalida_Exception} que identifica que o
     * emissor está incorrecto.
     */
    public void comunicarFaturaEmissorErrado() {
        // Arrange 
        Serie s = null;
        try {
            s = port.pedirSerie(NIF_MARIA);
            assertNotNull(s);
        } catch (EmissorInexistente_Exception e) {
            fail("comunicarFaturaSemItems(): Não foi possível pedir a serie (Arrange).");
        }
        final int numSerie = s.getNumSerie();
        final XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(dateTime
                .toDate());

        // Act 
        try {
            int nifErrado = 1234;
            port.comunicarFatura(createFatura(gregDate, numSerie, 1, "mariazinha", nifErrado,
                    NIF_CLIENTE, 321, 100));
        } catch (FaturaInvalida_Exception e) {
            System.out.println("Comunicar fatura emissor DIFERENTE emissor fatura - OK");
        } catch (ClienteInexistente_Exception e) {
            ////            e.printStackTrace();
            fail("Excepção errada (ClienteInexistente).\n");
        } catch (EmissorInexistente_Exception e) {
            ////            e.printStackTrace();
            fail("Excepção errada (EmissorInexistente).\n");
        } catch (TotaisIncoerentes_Exception e) {
            ////            e.printStackTrace();
            fail("Excepção errada (TotaisIncoerentes).\n");
        }
        // Assert
        fail("Comunicou uma fatura com o NIF do emissor da serie != do NIF do emissor da fatura.");
    }


    //Procedimento: Cria série, depois comunica fatura com total incoerente (total=10000,
    //total efectivo=123)
    //Verificação: deve lançar excepção TotaisIncoerentes_Exception
    @Test(expected = TotaisIncoerentes_Exception.class)
    public void comunicarTotalIncoerente() throws Exception {
        Serie s = port.pedirSerie(1111);
        assertNotNull(s);

        int numSerie = s.getNumSerie();
        XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(new Date());

        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "zeze", 1111, 2222);
        f.setTotal(10000);

        port.comunicarFatura(f);
    }

    //Procedimento: Cria série, depois comunica fatura com total de iva incoerente
    //(totalIva=1000, total de iva efectivo =23)
    //Verificação: deve lançar excepção TotaisIncoerentes_Exception
    @Test(expected = TotaisIncoerentes_Exception.class)
    public void comunicarTotalIVAIncoerente() throws Exception {
        Serie s = port.pedirSerie(1111);
        assertNotNull(s);

        int numSerie = s.getNumSerie();
        XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(new Date());

        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "zeze", 1111, 2222);
        f.setIva(1000);

        port.comunicarFatura(f);
    }


    //Procedimento: Comunica fatura sobre série 9999, que nunca foi gerada
    //Verificação: deve lançar excepção FaturaInvalida_Exception
    @Test(expected = FaturaInvalida_Exception.class)
    public void comunicarFaturaSerieInexistente1() throws Exception {

        XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(new Date());

        Fatura f = createFaturaExemplo(gregDate, 9999, 1, "zeze", 1111, 2222);
        port.comunicarFatura(f);
    }


    //Procedimento: Pede nova série do emissor 1111, depois comunica fatura com um emissor diferente
    //Verificação: deve lançar excepção FaturaInvalida_Exception
    @Test(expected = FaturaInvalida_Exception.class)
    public void comunicarEmissorErrado() throws Exception {
        Serie s = port.pedirSerie(1111);
        assertNotNull(s);

        XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(new Date());

        Fatura f = createFaturaExemplo(gregDate, 9999, 1, "mng", 3333, 2222);

        port.comunicarFatura(f);
    }

    //Procedimento: Pede nova série do emissor, depois comunica fatura com data de 100 dias após
    //Verificação: deve lançar excepção FaturaInvalida_Exception
    @Test(expected = FaturaInvalida_Exception.class)
    public void comunicarFaturaForaDeValidade() throws Exception {
        Serie s = port.pedirSerie(1111);
        assertNotNull(s);

        int numSerie = s.getNumSerie();

        //define data para alem da validade
        GregorianCalendar d = new GregorianCalendar();
        d.add(GregorianCalendar.DATE, 100);
        XMLGregorianCalendar gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);

        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "zeze", 1111, 2222);

        port.comunicarFatura(f);
    }

    //Procedimento: Pede nova série do emissor, comunica fatura com data+2 dias
    //após, comunica outra fatura com data original da série (i.e., fora de ordem)
    //Verificação: deve lançar excepção FaturaInvalida_Exception
    @Test(expected = FaturaInvalida_Exception.class)
    public void comunicarFaturaForaDeOrdem() throws Exception {
        Serie s = port.pedirSerie(1111);
        assertNotNull(s);

        int numSerie = s.getNumSerie();

        //emite primeira fatura
        GregorianCalendar d = new GregorianCalendar();
        d.add(GregorianCalendar.DATE, 2);
        XMLGregorianCalendar gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);

        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "zeze", 1111, 2222);
        port.comunicarFatura(f);

        //emite segunda fatura, com data anterior
        d = new GregorianCalendar();
        gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);

        f = createFaturaExemplo(gregDate, numSerie, 1, "zeze", 1111, 2222);

        port.comunicarFatura(f);
    }


    //Procedimento: Pede nova série, comunica fatura com emissor inexistente
    //Verificação: deve lançar excepção EmissorInexistente_Exception
    @Test(expected = EmissorInexistente_Exception.class)
    public void comunicarFaturaEmissorInexistente() throws Exception {
        Serie s = port.pedirSerie(1111);
        assertNotNull(s);

        int numSerie = s.getNumSerie();

        //define data para alem da validade
        GregorianCalendar d = new GregorianCalendar();
        XMLGregorianCalendar gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);

        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "xpto", 123456, 2222);

        port.comunicarFatura(f);
    }


    //Procedimento: Pede nova série, comunica lote de 5 faturas, cada uma com 1 dia de diferença
    //Verificação: deve lançar excepção FaturaInvalida_Exception
    @Test(expected = FaturaInvalida_Exception.class)
    public void comunicarDemasiadasFaturas() throws Exception {
        Serie s = port.pedirSerie(1111);
        assertNotNull(s);

        int numSerie = s.getNumSerie();

        //emite primeira fatura
        GregorianCalendar d = new GregorianCalendar();
        d.add(GregorianCalendar.DATE, 1);
        XMLGregorianCalendar gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "zeze", 1111, 2222);
        port.comunicarFatura(f);

        d.add(GregorianCalendar.DATE, 1);
        gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        f = createFaturaExemplo(gregDate, numSerie, 2, "zeze", 1111, 2222);
        port.comunicarFatura(f);

        d.add(GregorianCalendar.DATE, 1);
        gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        f = createFaturaExemplo(gregDate, numSerie, 3, "zeze", 1111, 2222);
        port.comunicarFatura(f);

        d.add(GregorianCalendar.DATE, 1);
        gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        f = createFaturaExemplo(gregDate, numSerie, 4, "zeze", 1111, 2222);
        port.comunicarFatura(f);

        d.add(GregorianCalendar.DATE, 1);
        gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        f = createFaturaExemplo(gregDate, numSerie, 5, "zeze", 1111, 2222);
        port.comunicarFatura(f);

    }

    //Procedimento: Pede nova série, comunica fatura, depois lista-a
    //Verificação: verifica se a listagem tem a fatura
    //Nota: Assume-se que este é o único teste que emite fatura em nome de 5001
    @Test
    public void comunicaEListarUmaFatura() throws Exception {
        Serie s = port.pedirSerie(5001);
        assertNotNull(s);

        int numSerie = s.getNumSerie();
        XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(new Date());

        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "xpto", 5001, 2222);

        port.comunicarFatura(f);

        //lista esta fatura
        List<Fatura> l = port.listarFacturas(5001, 2222);
        assertNotNull(l);
        // expected, actual
        assertEquals(1, l.size());
        assertEquals(f.getTotal(), l.get(0).getTotal());
    }

    //Procedimento: Pede nova série, comunica lote de 3 faturas, cada uma com 1 dia de diferença,
    //depois lista-as
    //Verificação: verifica se a listagem tem as 3 faturas
    //Nota: Assume-se que este é o único teste que emite fatura em nome de 5001
    @Test
    public void comunicarEListarLoteDeFaturas() throws Exception {
        Serie s = port.pedirSerie(5003);
        assertNotNull(s);

        int numSerie = s.getNumSerie();

        //emite primeira fatura
        GregorianCalendar d = new GregorianCalendar();
        d.add(GregorianCalendar.DATE, 1);
        XMLGregorianCalendar gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "zleep", 5003, 2222);
        port.comunicarFatura(f);

        d.add(GregorianCalendar.DATE, 1);
        gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        f = createFaturaExemplo(gregDate, numSerie, 2, "zleep", 5003, 2222);
        port.comunicarFatura(f);

        d.add(GregorianCalendar.DATE, 1);
        gregDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        f = createFaturaExemplo(gregDate, numSerie, 3, "zleep", 5003, 2222);
        port.comunicarFatura(f);

        //lista esta fatura
        List<Fatura> l = port.listarFacturas(5003, 2222);
        assertNotNull(l);
        assertEquals(3, l.size());
        assertEquals(f.getTotal(), l.get(0).getTotal());
        assertEquals(f.getTotal(), l.get(1).getTotal());
        assertEquals(f.getTotal(), l.get(2).getTotal());
    }

    //Procedimento: Pede nova série, comunica fatura, depois consulta o IVA do emissor
    //Verificação: verifica se o iva é o correcto
    //Nota: Assume-se que este é o único teste que emite fatura em nome de 5002
    @Test
    public void comunicaEConsultarIVAOK() throws Exception {
        Serie s = port.pedirSerie(5002);
        assertNotNull(s);

        int numSerie = s.getNumSerie();
        XMLGregorianCalendar gregDate = XMLCalendarToDate.toXMLGregorianCalendar(new Date());

        Fatura f = createFaturaExemplo(gregDate, numSerie, 1, "yez", 5002, 2222);

        port.comunicarFatura(f);

        //lista esta fatura
        assertEquals(f.getIva(), port.consultarIVADevido(5002, gregDate));
    }

    //Procedimento: Pede nova série
    //Verificação: se data de validade da nova série é a esperada
    @Test
    public void pedirSerieEmissorCorrecto1() throws Exception {

        Serie s = port.pedirSerie(NIF_MARIA);

        GregorianCalendar d = new GregorianCalendar();
        d.add(GregorianCalendar.DATE, 10);
        XMLGregorianCalendar c = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
        assertEquals(s.getValidoAte().getDay(), c.getDay());
        assertEquals(s.getValidoAte().getMonth(), c.getMonth());
        assertEquals(s.getValidoAte().getYear(), c.getYear());
    }

    //Procedimento: Pede nova série para emissor inexistente
    //Verificação: deve lançar excepção emissorInexistente
    @Test(expected = EmissorInexistente_Exception.class)
    public void pedirSerieEmissorInexistente1() throws Exception {
        port.pedirSerie(999999);
    }

    private Fatura createFaturaExemplo(XMLGregorianCalendar date,
                                       int numSerie,
                                       int numSeqFatura,
                                       String nomeEmissor,
                                       int nifEmissor,
                                       int nifCliente) {
        Fatura f = new Fatura();

        f.setData(date);
        f.setNumSeqFatura(numSeqFatura);
        f.setNumSerie(numSerie);
        f.setNifEmissor(nifEmissor);
        f.setNifCliente(nifCliente);

        ItemFatura item = new ItemFatura();
        item.setDescricao("produto1");
        item.setPreco(123);
        item.setQuantidade(1);
        f.getItens().add(item);

        f.setIva(23);
        f.setTotal(123);
        f.setNomeEmissor(nomeEmissor);

        return f;
    }

    private Fatura createFatura(XMLGregorianCalendar date,
                                int numSerie,
                                int numSeqFatura,
                                String nomeEmissor,
                                int nifEmissor,
                                int nifCliente,
                                int iva,
                                int total) {
        final Fatura f = new Fatura();
        f.setData(date);
        f.setNumSeqFatura(numSeqFatura);
        f.setNumSerie(numSerie);
        f.setNifEmissor(nifEmissor);
        f.setNifCliente(new Integer(nifCliente));
        f.setIva(iva);
        f.setNomeEmissor(nomeEmissor);
        f.setTotal(total);
        return f;
    }

    private ItemFatura createItem(String desc, int precoUnitario, int quant) {
        ItemFatura item = new ItemFatura();
        item.setDescricao(desc);
        item.setQuantidade(quant);
        item.setPreco(precoUnitario * quant);
        return item;
    }

    private Fatura createFaturaComItems(XMLGregorianCalendar date,
                                        int numSerie,
                                        int numSeqFatura,
                                        String nomeEmissor,
                                        int nifEmissor,
                                        int nifCliente) {



        final int precoBitoque = 5;
        final int quantBitoque = 2;
        ItemFatura bitoque = createItem("Cantinho do Zétó - Bitoque", precoBitoque, quantBitoque);
        final int precoPizza = 10;
        final int quantPizza = 2;
        ItemFatura pizza = createItem("Os Gordos - pizza 4Estações", precoPizza, quantPizza);
        final int total = precoBitoque * quantBitoque + precoPizza * quantPizza;
        // P = P *
        BigDecimal iva = BigDecimal.valueOf(total).multiply(
                new BigDecimal("0.23").divide(new BigDecimal("1.23"), 2, RoundingMode.HALF_UP));
        iva = iva.setScale(0, RoundingMode.HALF_UP);

        final Fatura f = createFatura(date, numSerie, numSeqFatura, nomeEmissor, nifEmissor,
                nifCliente, iva.intValueExact(), total);
        f.getItens().add(bitoque);
        f.getItens().add(pizza);

        return f;
    }
}
