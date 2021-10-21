package montanhaRussa;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

public class Passageiro extends Thread {
	
    // Variaveis
    String nome;
    int tempoDesembarque;
    int tempoEmbarque;
    private JPanel imagem;
    
    int bb = 0;

    public Passageiro(String nome, int embarque, int desembarque, JPanel imagem) {
	this.nome = nome;
	this.tempoEmbarque = embarque;
	this.tempoDesembarque = desembarque;
	this.imagem = imagem;
    }

    private void dormindo(long tempo) {
	Long inicio = System.currentTimeMillis();
	Long fim = inicio + 1000 * tempo;

	do {
	    if (System.currentTimeMillis() > fim) {
		break;
	    }
	} while (true);
    }

    public void embarcar() {

		Main.montaMensagem("Passageiro #" + this.nome + " esta Subindo		" + LocalTime.now());
	
		dormindo(this.tempoEmbarque);
	
		Main.montaMensagem("Passageiro #" + this.nome + " embarcou		" + LocalTime.now());
		
		this.imagem.setBounds(bb + 20, 20, 20, 56);
	    bb = bb + 20;
		Main.frame.getContentPane().add(this.imagem);
		
		Main.getPassageirosEmbarcados().add(this);
	
		List<JPanel> imagensPassageiros = Main.getPassageirosEmbarcados().stream()
		    	.map(passageiroEmbarcado -> passageiroEmbarcado.getImagem())
		    	.collect(Collectors.toList());
		    
	    bb=0;
	    for (JPanel imagemPassageiro : imagensPassageiros) {
	    	imagemPassageiro.setLocation(20 + (bb*20), 20);
	    	bb++;
	    }
	
    }

    public void passeando() {

		Long fim = System.currentTimeMillis() + ((Main.TempoViagem * 1000));
	
		while (true) {
		    if (fim > System.currentTimeMillis()) {
				Main.montaMensagem("Passageiro #" + this.nome + " esta Admirando a Paisagem");
				dormindo(1);
		    } else {
		    	break;
		    }
		}

    }

    public void desembarque() {
		Main.montaMensagem("Passageiro #" + this.nome + " esta Descendo		" + LocalTime.now());
		dormindo(this.tempoDesembarque);
		Main.montaMensagem("Passageiro #" + this.nome + " desembarcou		" + LocalTime.now());
		Main.getPassageirosEmbarcados().remove(this);
    }

    public void run() {
		while (true) {
			
			// nVagasLivres Down
		    try { Main.nVagasLivres.acquire();
		    } catch (InterruptedException e) { e.printStackTrace(); }
	
		    // mutex down
		    try { Main.mutex.acquire(); 
		    } catch (InterruptedException e) { e.printStackTrace(); }
	
			    Main.nPassageiros++;
		
			    embarcar();
			    Main.montaMensagem("Passageiro #" + this.nome + " esta agurdando Passeio");
		
			    if (Main.nPassageiros == Main.nVagas) { // vagão Cheio
			    	Main.vagaoCheio.release();
			    }
	
		    // mutex Up
		    Main.mutex.release();
	
		    // aguardandoPasseio down
		    try { Main.aguardandoPasseio.acquire();
		    } catch (InterruptedException e) {e.printStackTrace(); }
	
		    passeando();
	
		    // desembarcar down
		    try {
			Main.desembarcar.acquire();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
	
		    // mutex down
		    try {
			Main.mutex.acquire();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
	
		    Main.nPassageiros--;
		    desembarque();
	
		    if (Main.nPassageiros == 0) {
			for (int i = 0; i < Main.nVagas; i++) {
			    // nVagasLivres Up
			    Main.nVagasLivres.release();
			}
			Main.montaMensagem("Vagão esta Vazio");
		    }
		    
		    this.imagem.setBounds(Main.passangerPosition + 20, 200, 20, 56);
		    Main.passangerPosition = Main.passangerPosition + 20;
			Main.frame.getContentPane().add(this.imagem);
		    if(Main.passangerPosition > 380) {
		    	Main.passangerPosition = 20;
		    }
		    // mutex Up
		    Main.mutex.release();
		}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((nome == null) ? 0 : nome.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
		if (this == obj)
		    return true;
		if (obj == null)
		    return false;
		if (getClass() != obj.getClass())
		    return false;
		Passageiro other = (Passageiro) obj;
		if (nome == null) {
		    if (other.nome != null)
			return false;
		} else if (!nome.equals(other.nome))
		    return false;
		return true;
    }
    
    public JPanel getImagem() {
    	return imagem;
    }
    
}
