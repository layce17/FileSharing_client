package Core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
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
	private JPanel col_client;
	private JPanel col_server;
	
	/* MENU */
	private JMenuBar menu_bar;
	private JMenu menu_connexion;
	private JMenu menu_propos;
	private JMenuItem item_connexion;
	private JMenuItem item_deconnexion;
	private JMenuItem item_fermer;
	private JMenuItem item_infos;

	private FileTree client_tree;
	private ClientConnexion client = null;
	
	public Fenetre(int w, int h, String t) {
		this.setTitle(t);
		this.setSize(w, h);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.initMenu();
		
		col_client = new JPanel();
		col_client.setLayout(new BoxLayout(col_client, BoxLayout.Y_AXIS));
		col_client.add(new JLabel("Client :"));
		col_client.add(client_tree_view);
		col_server = new JPanel();
		col_server.setLayout(new BoxLayout(col_server, BoxLayout.Y_AXIS));
		col_client.add(new JLabel("Serveur :"));
		col_client.add(server_tree_view);
		
		center_container.setLayout(new BoxLayout(center_container, BoxLayout.X_AXIS));
		center_container.add(Box.createRigidArea(new Dimension(5, 0)));
		center_container.add(col_client);
		center_container.add(Box.createRigidArea(new Dimension(5, 0)));
		center_container.add(col_server);
		center_container.add(Box.createRigidArea(new Dimension(5, 0)));
		
		Thread client_tree_thread = new Thread(new LoadClientTree());
		client_tree_thread.start();
		
		container.setLayout(new BorderLayout());
		
		p_status = new JPanel();
		p_status.setBorder(new BevelBorder(BevelBorder.LOWERED));
		container.add(p_status, BorderLayout.SOUTH);
		p_status.setPreferredSize(new Dimension(this.getWidth(), 16));
		p_status.setLayout(new BoxLayout(p_status, BoxLayout.X_AXIS));
		
		lbl_status.setHorizontalAlignment(SwingConstants.LEFT);
		p_status.add(lbl_status);
		container.add(center_container, BorderLayout.CENTER);
		
		this.setContentPane(container);
		this.setVisible(true);
	}
	
	private void reset() {
		container.remove(center_container);
		
		col_client.removeAll();
		col_server.removeAll();
		col_client.add(new JLabel("Client :"));
		col_client.add(client_tree_view);
		col_server = new JPanel();
		col_client.add(new JLabel("Serveur :"));
		col_client.add(server_tree_view);
		
		center_container.removeAll();
		center_container.add(Box.createRigidArea(new Dimension(5, 0)));
		center_container.add(col_client);
		center_container.add(Box.createRigidArea(new Dimension(5, 0)));
		center_container.add(col_server);
		center_container.add(Box.createRigidArea(new Dimension(5, 0)));
		
		container.add(center_container, BorderLayout.CENTER);
		container.add(p_status, BorderLayout.SOUTH);

		setVisible(false);
		setVisible(true);
	}
	
	private void initMenu() {
		menu_bar = new JMenuBar();
		
		/* CONNEXION */
		menu_connexion = new JMenu("Connexion");
		item_connexion = new JMenuItem("Se connecter");
		item_connexion.addActionListener(new ConnexionListener());
		item_connexion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
		
		item_deconnexion = new JMenuItem("Se déconnecter");
		item_deconnexion.addActionListener(new DeconnexionListener());
		item_deconnexion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK));
		item_deconnexion.setEnabled(false);
		
		item_fermer = new JMenuItem("Fermer");
		item_fermer.addActionListener(new FermerListener());
		item_fermer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
		
		menu_connexion.add(item_connexion);
		menu_connexion.add(item_deconnexion);
		menu_connexion.addSeparator();
		menu_connexion.add(item_fermer);
		
		/* A PROPOS */
		menu_propos = new JMenu("A propos");
		item_infos = new JMenuItem("Informations");
		menu_propos.add(item_infos);
		
		menu_bar.add(menu_connexion);
		menu_bar.add(menu_propos);
		
		this.setJMenuBar(menu_bar);
	}
	
	private void initClientTree() {
		client_tree = new FileTree("C:/Users/lucas/Desktop");

		client_tree_view = new JScrollPane(client_tree);
		center_container.add(client_tree_view);
	}
	
	public void disconnect() {
		lbl_status.setText("Déconnexion au serveur...");
		reset();
		try {
			client.sendMsg("QUIT");
			lbl_status.setText("Déconnecté du serveur...");
			item_connexion.setEnabled(true);
			item_deconnexion.setEnabled(false);
			server_tree_view = new JScrollPane();
		} catch (Exception e) {
			e.printStackTrace();
			lbl_status.setText("Impossible de se déconnecter du serveur");
		}
		reset();
	}
	
	public void connect() {
		lbl_status.setText("Connexion au serveur...");
		reset();
		try {
			client = new ClientConnexion("127.0.0.1", 3456);
			lbl_status.setText("Connecté au serveur");
			item_connexion.setEnabled(false);
			item_deconnexion.setEnabled(true);
			
			Thread server_tree_thread = new Thread(new LoadServerTree());
			server_tree_thread.start();
		} catch (Exception e) {
			lbl_status.setText("Impossible de se connecter au serveur");
		}
		reset();
	}
	
	public void quit() {
		System.exit(0);
	}
	
	private class DeconnexionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (client != null)
				if (!client.getClient().isClosed())
					disconnect();
			else
				JOptionPane.showMessageDialog(container, "Vous devez être connecté pour vous déconnecter", "Déconnexion", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class ConnexionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (client == null)
				connect();
			else if (client.getClient().isClosed())
				connect();
		}
	}
	
	private class FermerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (client != null)
				if (!client.getClient().isClosed())
					disconnect();
			quit();
		}
	}
	
	private class LoadServerTree implements Runnable{
		public void run() {
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
	}
	
	private class LoadClientTree implements Runnable{
		public void run() {		
			lbl_status.setText("Chargement des fichiers...");
			reset();
			initClientTree();
			lbl_status.setText("Fichiers chargés");
			reset();
			System.out.println("Files load");
		}
	}
}
