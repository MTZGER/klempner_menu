package menus;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.AdjustmentEvent;
import java.awt.print.PrinterException;
import java.io.*;
import javax.print.*;

import javax.print.attribute.*;
import javax.print.attribute.standard.*;
// fixed error: neu && fertig are under each other and on the left
// solution: Boxes createHorizontal... not createVertical...

public class klempner_menu
	extends JFrame implements ActionListener, AdjustmentListener
{
	private String Pfad = ".\\";
	private String file_name = "Diagnose.txt";
	private String SammelName = "PsychoseX.txt";
	
	private String[] Text = {"Klempner ist geschlossen", "Kein Zugang", "Geh nach Hause"};
	private ArrayList Diagnose = new ArrayList();
	private ArrayList Psychose = new ArrayList();
	private int Nr;
	
	private JPanel Platte;
	private JPanel Gruppe1, Gruppe2, Gruppe3;
	private JButton Knopf1, Knopf2;
	private Box Oben, Unten, Knoepfe;
	private JTextField input;
	private JLabel output;
	private JScrollBar bar;
	
	private JMenuBar Menuleiste;
	private JMenu Menu;
	private JMenuItem[] Entry = new JMenuItem[4];
	private JFileChooser file_searcher;
	

	public void createComponents() {
		Platte = new JPanel();
		Platte.setLayout(new FlowLayout());
		
		Knopf1 = new JButton("Neu");
		Knopf2 = new JButton("Fertig");
		input = new JTextField("", 28);
		output = new JLabel("");
		bar = new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, Diagnose.size());
		bar.addAdjustmentListener(this);
		
		Gruppe1 = new JPanel();
		Gruppe2 = new JPanel();
		Gruppe3 = new JPanel();
		Oben = Box.createHorizontalBox();
		Unten = Box.createHorizontalBox();
		Knoepfe = Box.createHorizontalBox();
		Knopf1.addActionListener(this);
		Knopf2.addActionListener(this);
	}
	public void setFormLayout() {
		Knopf1.setFont(new Font("Arial", 0, 20));
		Knopf2.setFont(new Font("Arial", 0, 20));
		input.setFont(new Font("Arial", 0, 18));
		output.setFont(new Font("Arial", 0, 18));
		bar.setPreferredSize(new Dimension(309, 25));
		
		Gruppe1.setPreferredSize(new Dimension(430, 70));
		Gruppe2.setPreferredSize(new Dimension(430, 70));
		Gruppe3.setPreferredSize(new Dimension(430, 70));
		
		Oben.setPreferredSize(new Dimension(430, 30));
		Unten.setPreferredSize(new Dimension(430, 30));
		
		Gruppe1.setBorder(BorderFactory.createTitledBorder("Das sagst du mir: "));
		Gruppe2.setBorder(BorderFactory.createTitledBorder("Das sage ich dir: "));
		Gruppe3.setBorder(BorderFactory.createTitledBorder("Diagnose-Manipulator: "));
	}
	public void createMenu() {
		Menuleiste = new JMenuBar();
		Menu = new JMenu("Datei");
		Entry[0] = new JMenuItem("Öffnen");
		Entry[1] = new JMenuItem("Speichern");
		Entry[2] = new JMenuItem("Drucken");
		Entry[3] = new JMenuItem("Ende");
		
		setJMenuBar(Menuleiste);
		Menuleiste.add(Menu);
		Menu.setFont(new Font("Arial", 1, 14));
		
		for (int i = 0; i < Entry.length; i++) {
			Menu.add(Entry[i]);
			Entry[i].addActionListener(this);
			Entry[i].setFont(new Font("Arial", 1, 14));
		}
		
		file_searcher = new JFileChooser();
		file_searcher.setCurrentDirectory(new File(Pfad));
		
	}
	
	
	public void readDiagnoseText(String DName) {
		try {
			BufferedReader read_file = new BufferedReader(new FileReader(DName));
			boolean file_end = false;
			Diagnose.clear();
			while (!file_end) {
				String file_line = read_file.readLine();
				if (file_line == null)
					file_end = true;
				else
					Diagnose.add(file_line);
//				JOptionPane.showMessageDialog(null, Diagnose.size() + Nr);
				if (Diagnose.size() == 0) {
					Diagnose = new ArrayList(Arrays.asList(Text));
				}
			}
			read_file.close();
		} catch (IOException x) {
			JOptionPane.showMessageDialog(null, "Kann Datei nicht laden");
			Diagnose = new ArrayList(Arrays.asList(Text));
		}
	}
	public void writeDiagnoseText(String DName) {
		try {
			BufferedWriter write_file = new BufferedWriter(new FileWriter(DName));
			for (int i = 0; i < Psychose.size(); i++) {
				write_file.write(Psychose.get(i).toString() + "\r\n");
			}
			write_file.close();
		} catch (Exception x) {
			JOptionPane.showMessageDialog(null, "Kann Datei nicht speichern");
		}
		System.out.println("close");
	}
	public void printDiagnoseText(String DName) {
		try {
			FileInputStream Datei = new FileInputStream(DName);
			DocFlavor Druckformat = DocFlavor.INPUT_STREAM.AUTOSENSE;
			
			Doc Dokument = new SimpleDoc(Datei, Druckformat, null);
			PrintService Drucker = PrintServiceLookup.lookupDefaultPrintService();
			
			PrintRequestAttributeSet  pras = new HashPrintRequestAttributeSet();
			pras.add(new Copies(1));
			
			DocPrintJob Druckauftrag = Drucker.createPrintJob();
			Druckauftrag.print(Dokument, pras);
			
			
			
			Datei.close();
		} catch (IOException x) {
			JOptionPane.showMessageDialog(null, "Kann Datei nicht laden!");
		} catch (PrintException x) {
			JOptionPane.showMessageDialog(null, "Kann Datei nicht drucken!");
		}
	}
	
	public void addComponents() {
		Knoepfe.add(Knopf1);
		Knoepfe.add(Knopf2);
		Gruppe1.add(input);
		Gruppe2.add(output);
		Gruppe3.add(bar);
		Platte.add(Oben);
		Platte.add(Gruppe1);
		Platte.add(Gruppe2);
		Platte.add(Gruppe3);
		Platte.add(Unten);
		Platte.add(Knoepfe);
	}
	
	
	public void actionPerformed(ActionEvent x) {
		Object quelle = x.getSource();
		if (quelle == Knopf2) {
			Nr = (int) (Math.random() * Diagnose.size());
			output.setText(Diagnose.get(Nr).toString());
			Psychose.add(input.getText());
			Psychose.add(output.getText());
		} else if (quelle == Knopf1) {
			output.setText("");
			input.setText("");
			input.requestFocus();
		} else if (quelle == Entry[0]) {
			int Wahl = file_searcher.showOpenDialog(this);
			if (Wahl == JFileChooser.APPROVE_OPTION) {
				file_name = file_searcher.getSelectedFile().getName();
				Diagnose.clear();
				readDiagnoseText(Pfad + file_name);
				bar.setMaximum(Diagnose.size());
			}
		} else if (quelle == Entry[1]) {
			int Wahl = file_searcher.showOpenDialog(this);
			if (Wahl == JFileChooser.APPROVE_OPTION) {
				SammelName = file_searcher.getSelectedFile().getName();
				writeDiagnoseText(Pfad + SammelName);
			}
		} else if (quelle == Entry[2]) {
			printDiagnoseText(Pfad + SammelName);
		}
	}
	
	public void adjustmentValueChanged(AdjustmentEvent e) {
		Nr = bar.getValue();
		output.setText(Diagnose.get(Nr).toString());
	}
	
	public WindowListener Fensterweachter =
		new WindowAdapter()
	{
		public void windowClosing(WindowEvent e) {
//			writeDiagnoseText(Pfad + SammelName);
			System.exit(0);
		}
	};
	

	
	public klempner_menu() {
		super("Seelenklempner");
		readDiagnoseText(Pfad + file_name);
		createComponents();
		setFormLayout();
		createMenu();
		addComponents();
		
		setContentPane(Platte);
		this.addWindowListener(Fensterweachter);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		klempner_menu Rahmen = new klempner_menu();
		Rahmen.setSize(600, 430);
	}

}