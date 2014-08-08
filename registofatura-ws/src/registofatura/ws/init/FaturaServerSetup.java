package registofatura.ws.init;

import jvstm.Atomic;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import registofatura.ws.domain.RegistoFatura;

public class FaturaServerSetup {

    public static void main(String[] args) {

        StringBuilder db = new StringBuilder("//localhost:3306/");
        if (args.length > 0) {
            db.append(args[0]);
        }
        final String finaldb = db.toString();
        FenixFramework.initialize(new Config() {
            {
                dbUsername = "rest";
                dbPassword = "r3st";
                dbAlias = finaldb;
                domainModelPath = "src/registofatura/dml/domain.dml";
                rootClass = RegistoFatura.class;
            }
        });

        populateDomain();
    }

    @Atomic
    private static void populateDomain() {
        RegistoFatura registo = FenixFramework.getRoot();

        registaUtilizadores(registo);
    }

    private static void registaUtilizadores(RegistoFatura registo) {
        registo.addCliente("alice", 1001);
        registo.addCliente("bruno", 1002);
        registo.addCliente("carlos", 1003);
        registo.addCliente("xpto", 5001);
        registo.addCliente("yez", 5002);
        registo.addCliente("zleep", 5003);
        registo.addCliente("zeze", 1111);
        registo.addCliente("mariazinha", 2222);
        registo.addCliente("mng", 3333);
        registo.addCliente("pp", 4444);
        registo.addCliente("RestPortal", 1212);
        registo.addCliente("bc", 5111);
        registo.addCliente("bf", 5222);

        registo.addEmissor("xpto", 5001);
        registo.addEmissor("yez", 5002);
        registo.addEmissor("zleep", 5003);
        registo.addEmissor("zeze", 1111);
        registo.addEmissor("mariazinha", 2222);
        registo.addEmissor("mng", 3333);
        registo.addEmissor("pp", 4444);
        registo.addEmissor("RestPortal", 1212);
        registo.addEmissor("bc", 5111);
        registo.addEmissor("bf", 5222);
    }
}
