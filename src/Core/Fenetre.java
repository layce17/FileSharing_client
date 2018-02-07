package Core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import Component.FileTree;

@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	private JPanel container = new JPanel();
	private JScrollPane client_tree_view = new JScrollPane();
	private JScrollPane server_tree_view = new JScrollPane();
	private JPanel p_status;
	private JLabel lbl_status = new JLabel();
	private JPanel center_container = new JPanel();

	private FileTree client_tree;
	private ClientConnexion client;
	
	public Fenetre(int w, int h, String t) {
		this.setTitle(t);
		this.setSize(w, h);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		container.setLayout(new BorderLayout());
		Thread init_trees = new Thread(new Runnable() {
			public void run() {
				lbl_status.setText("Connexion au serveur...");
				reset();
				try {
					client = new ClientConnexion("127.0.0.1", 3456);
					lbl_status.setText("Connecté au serveur");
				} catch (Exception e) {
					lbl_status.setText("Impossible de se connecter au serveur");
				}
				reset();
				
				lbl_status.setText("Chargement des fichiers...");
				reset();
				initClientTree();
				lbl_status.setText("Fichiers chargés");
				reset();
				
				lbl_status.setText("Récupération de l'arborescance du serveur...");
				reset();
				try {
					server_tree_view = new JScrollPane(new JTree(client.getServerTree()));
					lbl_status.setText("Arborescance récupérée avec succes");
				} catch (Exception e) {
					lbl_status.setText("Impossible de récupérer l'aborescance du serveur");
				}
				reset();
			}
		});
		init_trees.start();
		
		p_status = new JPanel();
		p_status.setBorder(new BevelBorder(BevelBorder.LOWERED));
		container.add(p_status, BorderLayout.SOUTH);
		p_status.setPreferredSize(new Dimension(this.getWidth(), 16));
		p_status.setLayout(new BoxLayout(p_status, BoxLayout.X_AXIS));
		
		lbl_status.setHorizontalAlignment(SwingConstants.LEFT);
		p_status.add(lbl_status);
		
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				client.sendMsg("QUIT");
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		container.add(center_container, BorderLayout.CENTER);
		
		this.setContentPane(container);
		this.setVisible(true);
	}
	
	private void reset() {
		container.remove(center_container);
		center_container.removeAll();
		center_container.add(client_tree_view);
		center_container.add(server_tree_view);
		container.add(center_container, BorderLayout.CENTER);
		container.add(p_status, BorderLayout.SOUTH);

		setVisible(false);
		setVisible(true);
	}
	
	private void initMenu() {
		// TODO: Menu
	}
	
	private void initClientTree() {
		client_tree = new FileTree("C:/Users/Lucas/");

		client_tree_view = new JScrollPane(client_tree);
		center_container.add(client_tree_view);
	}
}
