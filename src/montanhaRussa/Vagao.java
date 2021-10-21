package montanhaRussa;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

public class Vagao extends Thread {
	
    private void passeando() {
		int xVagao = 20;
		int b = 0;
		
		// Ida
		while (xVagao < 320) {
		    Main.vagonPanel.setLocation(xVagao, 70);
		    List<JPanel> imagensPassageiros = Main.getPassageirosEmbarcados().stream()
		    	.map(passageiroEmbarcado -> passageiroEmbarcado.getImagem())
		    	.collect(Collectors.toList());
		    
		    b=0;
		    
		    for (JPanel imagemPassageiro : imagensPassageiros) {
				imagemPassageiro.setLocation(xVagao + (b*20), 20);
				b++;
		    }
		    this.dormindo(Main.TempoViagem);
		    xVagao++;
		}
		
		b = 0;
		
		// Volta
		while (xVagao > 20) {
		    Main.vagonPanel.setLocation(xVagao, 70);
		    List<JPanel> imagensPassageiros = Main.getPassageirosEmbarcados().stream()
			    	.map(passageiroEmbarcado -> passageiroEmbarcado.getImagem())
			    	.collect(Collectors.toList());
		    
		    b = 0;
		    
		    for (JPanel imagemPassageiro : imagensPassageiros) {
	 			imagemPassageiro.setLocation(xVagao +(b*20) , 20);
				b++;
		    }
		    this.dormindo(Main.TempoViagem);
		    xVagao--;
		}
		
		Main.montaMensagem("Fim do Passeio, Vagão Chegou a estação!");
    }

    private void dormindo(long tempoTotalDoTrajeto) {
    	Long inicio = System.currentTimeMillis();
    	Long fim = inicio + ((tempoTotalDoTrajeto * 1000) / 320) / 2;

    	do {
		    if (System.currentTimeMillis() > fim) {
		    	break;
		    }
    	} while (true);
    }
    
    public void run() {
		while (true) {
		    // Vagão Down
		    try { Main.vagaoCheio.acquire();
		    } catch (InterruptedException e) {e.printStackTrace(); }
	
		    for (int i = 0; i < Main.nVagas; i++) {
				// aguardandoPasseio Up
				Main.aguardandoPasseio.release();
		    }
		    passeando();
	
		    for (int i = 0; i < Main.nVagas; i++) {
				// desembarcar Up
				Main.desembarcar.release();
		    }
		}
    } 
}
