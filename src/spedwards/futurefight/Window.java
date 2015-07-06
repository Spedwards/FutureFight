package spedwards.futurefight;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import spedwards.futurefight.htmlreader.Reviews;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6383982798584674522L;
	
	private JPanel contentPane;
	private Window instance;
	
	public static final String __VERSION__ = "1.0.1";

	/**
	 * Launch the application.
	 * @throws IOException if all else fails!
	 */
	public static void main(String[] args) throws IOException {
		try {
			new Window();
		} catch (Exception e) {
			try {
				PrintStream ps = new PrintStream("error.log");
				e.printStackTrace(ps);
				ps.close();
			} catch (FileNotFoundException e1) {
				new File("error.log").createNewFile();
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public Window() {
		setTitle("MARVEL Future Fight");
		this.instance = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 500);
		
		(new Thread() {
			public void run() {
				try {
					Reviews.getReviews();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
			JEditorPane editorPane = new JEditorPane();
			editorPane.setContentType("text/html");
			editorPane.setBackground(UIManager.getColor("Panel.background"));
			editorPane.setEditable(false);
			
				JScrollPane scrollPane = new JScrollPane(editorPane);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());
				scrollPane.setBackground(UIManager.getColor("Panel.background"));
				scrollPane.setBounds(0, 11, 834, 430);
				contentPane.add(scrollPane);
		
		/*
		 * Menu Bar
		 */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
			JMenu mnFile = new JMenu("File");
			menuBar.add(mnFile);
			
				JMenuItem mntmExit = new JMenuItem("Exit");
				mnFile.add(mntmExit);
			
			JMenu mnPage = new JMenu("Page");
			menuBar.add(mnPage);
			
				ReviewMenu mnCharacterReviews = new ReviewMenu("Character Reviews", editorPane);
				mnPage.add(mnCharacterReviews);
				
					JMenuItem mntmBlackPanther = new JMenuItem("Black Panther");
					mnCharacterReviews.add(mntmBlackPanther);
					
					JMenuItem mntmCaptainAmerica = new JMenuItem("Captain America");
					mnCharacterReviews.add(mntmCaptainAmerica);
					
					JMenuItem mntmCaptainMarvel = new JMenuItem("Captain Marvel");
					mnCharacterReviews.add(mntmCaptainMarvel);
					
					JMenuItem mntmDaredevil = new JMenuItem("Daredevil");
					mnCharacterReviews.add(mntmDaredevil);
					
					JMenuItem mntmElektra = new JMenuItem("Elektra");
					mnCharacterReviews.add(mntmElektra);
					
					JMenuItem mntmFalcon = new JMenuItem("Falcon");
					mnCharacterReviews.add(mntmFalcon);
					
					JMenuItem mntmGroot = new JMenuItem("Groot");
					mnCharacterReviews.add(mntmGroot);
					
					JMenuItem mntmKingpin = new JMenuItem("Kingpin");
					mnCharacterReviews.add(mntmKingpin);
					
					JMenuItem mntmLoki = new JMenuItem("Loki");
					mnCharacterReviews.add(mntmLoki);
					
					JMenuItem mntmMockingbird = new JMenuItem("Mockingbird");
					mnCharacterReviews.add(mntmMockingbird);
					
					JMenuItem mntmRocketRaccoon = new JMenuItem("Rocket Raccoon");
					mnCharacterReviews.add(mntmRocketRaccoon);
					
					JMenuItem mntmSharonCarter = new JMenuItem("Sharon Carter");
					mnCharacterReviews.add(mntmSharonCarter);
					
					JMenuItem mntmUltron = new JMenuItem("Ultron");
					mnCharacterReviews.add(mntmUltron);
					
					JMenuItem mntmVision = new JMenuItem("Vision");
					mnCharacterReviews.add(mntmVision);
					
					JMenuItem mntmWarMachine = new JMenuItem("War Machine");
					mnCharacterReviews.add(mntmWarMachine);
					
					JMenuItem mntmWinterSoldier = new JMenuItem("Winter Soldier");
					mnCharacterReviews.add(mntmWinterSoldier);
					
			JMenu mnHelp = new JMenu("Help");
			menuBar.add(mnHelp);
				
				JMenuItem mntmCheckForUpdates = new JMenuItem("Check for Updates");
				mnHelp.add(mntmCheckForUpdates);
				
				JMenuItem mntmJSON = new JMenuItem("JSON");
				mnHelp.add(mntmJSON);
				
				JMenuItem mntmAbout = new JMenuItem("About");
				mnHelp.add(mntmAbout);
		
		/*
		 * Event Listeners
		 */
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		mntmJSON.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorPane.setText(Reviews.getJSON());
				editorPane.setCaretPosition(0);
			}
		});
		
		mntmCheckForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					URL url = new URL("https://raw.githubusercontent.com/Spedwards/FutureFight/master/VERSION");
					HttpURLConnection request = (HttpURLConnection) url.openConnection();
					request.connect();
					BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line+"\n");
					}
					br.close();
					
					String version = sb.toString().trim();
					if (__VERSION__.equals(version)) {
						JOptionPane.showMessageDialog(instance, "You are up to date.", "Update Check", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(instance, "Latest version is " + version, "Update Check", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException e1) {
					
				}
			}
		});
		
		setVisible(true);
	}
}
