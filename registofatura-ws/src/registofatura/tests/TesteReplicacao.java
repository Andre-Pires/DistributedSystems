package registofatura.tests;

import javax.xml.registry.JAXRException;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Mockit;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import pt.registofatura.ws.EmissorInexistente_Exception;
import pt.registofatura.ws.RegistoFaturaPortType;
import registofatura.ws.uddi.UDDINaming;


public class TesteReplicacao {

    @Mocked
    private UDDINaming uddi;

    static {
        // problema a iniciar o mock; classe nao corre devido a isto
        Mockit.setUpMocks(UDDINaming.class, RegistoFaturaPortType.class);
    }


    @Test
    private void reenviarPedidoPrimario(@Injectable final RegistoFaturaPortType primario,
                                        @Injectable final RegistoFaturaPortType secundario) throws JAXRException,
            EmissorInexistente_Exception {
        final String endpointPrim = "http://localhost:8080/RegistoFatura-ws/endpoint";
        final String endpointSec = "http://localhost:8082/RegistoFatura-ws/endpoint";

        new Expectations() {
            {
                uddi.lookup("RegistoFatura");
                returns(endpointPrim, endpointSec);
                primario.pedirSerie(anyInt);
                returns(anyInt, anyInt);
            }
        };

    }

    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(TesteReplicacao.class);
    }
}
