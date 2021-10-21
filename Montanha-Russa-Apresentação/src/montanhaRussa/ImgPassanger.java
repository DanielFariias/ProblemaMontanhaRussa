package montanhaRussa;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImgPassanger extends JPanel {
	String nome;
    
    public ImgPassanger(String nome) {
    this.nome = nome;
	setLayout(null);

	JLabel lblImgpassanger = new JLabel(nome);
	lblImgpassanger.setBounds(0, 0, 20, 56);
	lblImgpassanger.setFont(new Font("Roboto",1, 20));
	add(lblImgpassanger);
    }

}
