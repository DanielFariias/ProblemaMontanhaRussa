package montanhaRussa;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Main extends Thread {

    // Variaveis
    public static int nPassageiros = 0;
    public static int nVagas = 1;
    public static int TempoViagem = 0;
    public static int passangerPosition = 0;
    static int id = 0;
    
    public static String nTempoEmbarque;
    public static String nTempoDesembarque;
    
    
    private static List<Passageiro> passageirosEmbarcados = new ArrayList<>();
    static Main window = null;
    
    // Semaphores
    public static Semaphore aguardandoPasseio = new Semaphore(0, true);
    public static Semaphore desembarcar = new Semaphore(0, true);
    public static Semaphore vagaoCheio = new Semaphore(0);
    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore nVagasLivres;

    // JFrame
  	static JFrame frame;
  	private static JTextField tempoEmbarque;
  	private static JTextField tempoDesembarque;
  	
    public static JTextArea consoler = new JTextArea();
    public static imgVagon vagonPanel = new imgVagon();
    
    // Envia Mensagem para o Console do App
    public static void montaMensagem(String mensagem) {
		if (Main.consoler != null) {
	
		    mensagem = consoler.getText() + mensagem + "\n";
		    consoler.setText(mensagem);
		}
		System.out.println(mensagem);
    }

    // Obter lista de Passageiros
    public static List<Passageiro> getPassageirosEmbarcados() {
    	return passageirosEmbarcados;
    }

    // Função Para renderizar o Frame
    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		    public void run() {
				try {
				    window = new Main();
				    window.frame.setVisible(true);
				} catch (Exception e) {
				    e.printStackTrace();
				}
		    }
		});
    }

    // Função Main
    public Main() {
    	initialize();
    }

    // Renderização de todo App
    private void initialize() {
    	//Get Numero de Vagas e Tempo de viagens antes do App Executar
		nVagas = Integer.parseInt(JOptionPane.showInputDialog("Digite o número de vagas no Vagão: "));
		TempoViagem = Integer.parseInt(JOptionPane.showInputDialog("Digite o Tempo de Viagem: "));
		
		nVagasLivres = new Semaphore(nVagas, true);
	
		// Monta o Frame na tela
		frame = new JFrame("Passeio de Montanha-Russa");
		frame.setBounds(600, 600, 480, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	
		// Criar Vagão
		Vagao vagao = new Vagao();
		vagao.start();
	
		// Criar Carrinho
		vagonPanel.setBounds(20, 70, 128, 128);
		frame.getContentPane().add(vagonPanel);
		
		//Label Novo Passageiro
		JLabel LabelNewPass = new JLabel("Novo Passageiro");
		LabelNewPass.setBounds(20, 300, 100, 14);
		frame.getContentPane().add(LabelNewPass);
	
		// Input e Label Tempo de Embarque
		JLabel LabelTE = new JLabel("Tempo de Embarque:");
		LabelTE.setBounds(20, 330, 120, 14);
		frame.getContentPane().add(LabelTE);
	
		tempoEmbarque = new JTextField();
		tempoEmbarque.setBounds(150, 330, 120, 20);
		frame.getContentPane().add(tempoEmbarque);
		tempoEmbarque.setColumns(10);
		nTempoEmbarque = tempoEmbarque.getText();
	
		// Input e Label Tempo de Desembarque
		JLabel LabelDE = new JLabel("Tempo de Desembarque:");
		LabelDE.setBounds(20, 360, 120, 14);
		frame.getContentPane().add(LabelDE);
	
		tempoDesembarque = new JTextField();
		tempoDesembarque.setBounds(150, 360, 120, 20);
		frame.getContentPane().add(tempoDesembarque);
		tempoDesembarque.setColumns(10);
		nTempoDesembarque = tempoDesembarque.getText();
		
		// Criar passageiro
		JButton btnCreatePassageiro = new JButton("Criar Passageiro");
		btnCreatePassageiro.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
			id++;
			nTempoEmbarque = tempoEmbarque.getText();
			nTempoDesembarque = tempoDesembarque.getText();
			System.out.println(tempoEmbarque.getText());
			
			ImgPassanger passangerPanel = new ImgPassanger(Integer.toString(id));
			passangerPanel.setBounds(passangerPosition + 20, 200, 20, 56);
			passangerPosition = passangerPosition + 20;
			frame.getContentPane().add(passangerPanel);
			Passageiro passageiro = new Passageiro(Integer.toString(id), Integer.parseInt(nTempoEmbarque), 
					Integer.parseInt(nTempoDesembarque), passangerPanel);
			
			passageiro.start();
			
			montaMensagem("Um novo passageiro foi criado!");
		    }
		});
		btnCreatePassageiro.setBounds(280, 330, 150, 50);
		frame.getContentPane().add(btnCreatePassageiro);
		
		
		//Scroll Textarea View
		JScrollPane scroll = new JScrollPane(consoler, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
		    public void adjustmentValueChanged(AdjustmentEvent e) {  
		        e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
		    }
		});
		
		scroll.setBounds(20, 400, 420, 150);
		scroll.setVisible(true);
		consoler.setEditable(false);
	
		consoler.setFont(new Font("Roboto", 1, 12));
		consoler.setLineWrap(true);
		consoler.setWrapStyleWord(true);
	
		frame.add(scroll);
		scroll.setAutoscrolls(true);
    } 
}
