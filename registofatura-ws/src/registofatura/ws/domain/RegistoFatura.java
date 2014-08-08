package registofatura.ws.domain;

public class RegistoFatura extends
        RegistoFatura_Base {

    public RegistoFatura() {
        super();
        super.setNextNumSerie(0);
    }

    public void addEmissor(String nome, int nif) {
        Emissor emissor = new Emissor();
        emissor.setNome(nome);
        emissor.setNif(nif);
        super.addEmissor(emissor);
    }

    public void addCliente(String nome, int nif) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setNif(nif);
        super.addCliente(cliente);
    }

    @Override
    public int getNextNumSerie() {
        int num = super.getNextNumSerie();
        super.setNextNumSerie(num + 1);
        return num;
    }

    /*
     * Não substitui o getSerie da FénixFramework.
     * 
     * @returns Serie A serie com o número de série correspondente ou nul se não existir.
     */
    public Serie getSerie(int numSerie) {

        for (Serie s : super.getSerie()) {
            if (s.getNumSerie() == numSerie) {
                return s;
            }
        }
        return null;
    }
}
