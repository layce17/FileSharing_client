package Core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeCellRenderer;

import Component.FileTree;

@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	private JPanel container = new JPanel();
	private FileTree client_tree;
	private JScrollPane client_tree_view;
	private JButton btn_test;
	
	public Fenetre(int w, int h, String t) {
		this.setTitle(t);
		this.setSize(w, h);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		this.initClientTree();
		btn_test = new JButton("Envoy� bonjour");
		btn_test.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		this.setContentPane(container);
		this.setVisible(true);
	}
	
	private void initClientTree() {
		client_tree = new FileTree("C:/users/lucas/Desktop/");
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) client_tree.getCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);

		client_tree_view = new JScrollPane(client_tree);
		container.add(client_tree_view);
	}
}
