package montanhaRussa;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class imgVagon extends JPanel {
	
	public imgVagon() {
		setLayout(null);
		
		JLabel lblImgvagon = new JLabel();
		lblImgvagon.setBounds(0, 0, 128, 128);
		lblImgvagon.setIcon(new ImageIcon(System.getProperty("user.dir") + "/img/train.png"));
		add(lblImgvagon);
	}
}
