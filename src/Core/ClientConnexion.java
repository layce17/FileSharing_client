package Core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.tree.TreeModel;

public class ClientConnexion {
	private Socket con = null;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;
	private ObjectInputStream obj_reader = null;
	
	public ClientConnexion(String host, int port) {
		try {
			con = new Socket(host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TreeModel getServerTree() {
		try {
			System.out.println("Debut de la demande");
			writer = new PrintWriter(con.getOutputStream(), true);
			obj_reader = new ObjectInputStream(con.getInputStream());
			
			String cmd = "TREE_FILES";
			writer.write(cmd);
			writer.flush();
			
			TreeModel ft = (TreeModel) obj_reader.readObject();
			
			return ft;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}
	
	public void sendMsg(String msg) {
		try {
			writer = new PrintWriter(con.getOutputStream(), true);
			reader = new BufferedInputStream(con.getInputStream());
			
			String cmd = msg;
			writer.write(cmd);
			writer.flush();
			
			System.out.println("Commande "+ cmd +" envoyé au serveur");
			
			String response = read();
			System.out.println("Réponse reçue: "+ response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (msg.equals("QUIT"))
			writer.close();
	}
	
	private String read() throws IOException{
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		
		return response;
	}
}
