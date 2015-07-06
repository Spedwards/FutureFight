package spedwards.futurefight;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.text.JTextComponent;

import spedwards.futurefight.htmlreader.Reviews;

public class ReviewMenu extends JMenu {
	
	private ActionListener listener;
	
	public ReviewMenu(String name, JTextComponent pane) {
		super(name);
		this.listener = (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pane.setText(Reviews.getReview(((JMenuItem) e.getSource()).getText()));
				pane.setCaretPosition(0);
			}
		});
	}
	
	@Override
	public JMenuItem add(JMenuItem item) {
		item.addActionListener(listener);
		return super.add(item);
	}
	
}
