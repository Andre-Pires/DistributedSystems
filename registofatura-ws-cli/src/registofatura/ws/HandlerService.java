package registofatura.ws;

import javax.jws.HandlerChain;

import pt.registofatura.ws.RegistoFaturaService;

@HandlerChain(file = "/handler-chain.xml")
public class HandlerService extends
        RegistoFaturaService {
    //Injecta o handler chain para ser usado pelo cliente
}
