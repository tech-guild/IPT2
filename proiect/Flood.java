package proiect;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import static proiect.BaseInterface.imageToProcess;
import static proiect.Main.imagine;
import static proiect.Main.interfata;
import proiect.ProcessedImage;

@SuppressWarnings("serial")
public class Flood extends JFrame {

	private JPanel contentPane;
	BufferedImage img;
	
	// private String sPath = "C:\\Users\\user\\Desktop\\Proiect PI";
	private String sPath = "C:\\Users\\Leo Mada\\Desktop\\Practica\\Practica\\UVT\\[2019]\\Img";
	// private String sFileName = "fall.jpg";
	private String sFileName = "Brugia_40x - 2 worms.jpg";
	
	static int toleranta=10;
	static int nrVecini=4;
	static int fAFactor = 30;
	static int iTol = 15;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Flood frame = new Flood();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Flood() {
		super("Filters");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ProcessedImage panel = new ProcessedImage();
		panel.setBounds(450, 171, 420, 469);
		contentPane.add(panel);
		
		//OriginalPanel panel_1 = new OriginalPanel();
		//panel_1.setBounds(12, 171, 420, 469);
		//contentPane.add(panel_1);
		
		
		
		JLabel lblCeFiltruDoriti = new JLabel("Ce filtru doriti sa aplicati pe imagine?");
		lblCeFiltruDoriti.setFont(new Font("Gill Sans MT", Font.PLAIN, 20));
		lblCeFiltruDoriti.setBounds(281, 25, 317, 27);
		contentPane.add(lblCeFiltruDoriti);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.BackToImage(sPath+"\\"+sFileName);
			}
		});
		btnBack.setBounds(12, 16, 97, 25);
		contentPane.add(btnBack);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OpenFile();
		//		panel_1.LoadImage(sPath+"\\"+sFileName);
		//		panel_1.repaint();
				panel.LoadImage(sPath+"\\"+sFileName);
				panel.repaint();
			}
		});
		btnOpen.setBounds(762, 16, 97, 25);
		contentPane.add(btnOpen);
		
		JButton btnFloodfill = new JButton("Setare FloodFill");
		btnFloodfill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				
				JFrame frame1 = new JFrame("Setari FloodFill");
				frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				frame1.setBounds(100, 100, 550, 350);
				JPanel contentPane1 = new JPanel();
				contentPane1.setBorder(new EmptyBorder(5, 5, 5, 5));
				contentPane1.setLayout(null);
				
				JSpinner spinner = new JSpinner();
				spinner.setModel(new SpinnerNumberModel(10, 0, 100, 1));
				spinner.setBounds(351, 59, 63, 34);
				contentPane1.add(spinner);
				
				JLabel lblCeTolerantaDoriti = new JLabel("Ce toleranta doriti?");
				lblCeTolerantaDoriti.setFont(new Font("Gill Sans MT", Font.PLAIN, 26));
				lblCeTolerantaDoriti.setBounds(60, 62, 211, 31);
				contentPane1.add(lblCeTolerantaDoriti);
				
				JSpinner spinner_1 = new JSpinner();
				spinner_1.setModel(new SpinnerNumberModel(4, 4, 8, 4));
				spinner_1.setBounds(351, 122, 63, 34);
				contentPane1.add(spinner_1);
				
				JLabel lblCatiPixeliVecini = new JLabel("Cati pixeli vecini doriti?");
				lblCatiPixeliVecini.setFont(new Font("Gill Sans MT", Font.PLAIN, 25));
				lblCatiPixeliVecini.setBounds(35, 116, 236, 39);
				contentPane1.add(lblCatiPixeliVecini);
				
				JSpinner spinner_2 = new JSpinner();
				spinner_2.setModel(new SpinnerNumberModel(10, 0, 100, 1));
				spinner_2.setBounds(351, 195, 63, 34);
				contentPane1.add(spinner_2);
				
				JLabel lblCeFactorDoriti = new JLabel("Ce factor doriti?");
				lblCeFactorDoriti.setFont(new Font("Gill Sans MT", Font.PLAIN, 25));
				lblCeFactorDoriti.setBounds(31, 188, 260, 47);
				contentPane1.add(lblCeFactorDoriti);
				
				JButton btnOk = new JButton("Ok");
				btnOk.setBounds(137, 260, 97, 25);
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
						    spinner.commitEdit();
						} catch ( java.text.ParseException e ) { e.printStackTrace(); }
						toleranta = (Integer) spinner.getValue();
						
						try {
						    spinner_1.commitEdit();
						} catch ( java.text.ParseException e ) { e.printStackTrace(); }
						nrVecini = (Integer) spinner_1.getValue();
						try {
						    spinner_1.commitEdit();
						} catch ( java.text.ParseException e ) { e.printStackTrace(); }
						fAFactor = (Integer) spinner_2.getValue();
						frame1.dispose();
					}
				});
				contentPane1.add(btnOk);
				
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setBounds(291, 260, 97, 25);
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						frame1.dispose();
					}
				});
				contentPane1.add(btnCancel);
				
				frame1.getContentPane().add(contentPane1);
				frame1.setVisible(true);
			}
		});
		btnFloodfill.setBounds(657, 98, 121, 25);
		contentPane.add(btnFloodfill);
		
		JButton btnClearNoise = new JButton("Clear Noise");
		btnClearNoise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.NoImageNoise(iTol);
			}
		});
		btnClearNoise.setBounds(526, 98, 121, 25);
		contentPane.add(btnClearNoise);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("Connected Components");
		comboBox.addItem("Flood Fill");
		comboBox.setSelectedItem(null);
		comboBox.setToolTipText("");
		comboBox.setBounds(376, 98, 140, 25);
		contentPane.add(comboBox);
		
		JButton btnAdaptiveBlur_1 = new JButton("Adaptive Blur");
		btnAdaptiveBlur_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                 
         panel.Process();
         imagine.img=panel.img;
         interfata.DisplayImage(imagine.img);
                            
                            //panel.Process();
                            
                        }
		});
		btnAdaptiveBlur_1.setBounds(245, 98, 121, 25);
		contentPane.add(btnAdaptiveBlur_1);
		
		panel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x=e.getX();
				int y=e.getY();
				if(comboBox.getSelectedIndex() == 1) 
					panel.FloodFillImage(x, y, toleranta, nrVecini, fAFactor);
				else
				if(comboBox.getSelectedIndex() == 0) 
					panel.ConnectedComponentsImage(x, y, toleranta, nrVecini, fAFactor);
			}
		});
		
		//panel_1.LoadImage(sPath+"\\"+sFileName);
		//panel_1.repaint();
		panel.LoadImage(sPath+"\\"+sFileName);
		panel.repaint();
	}
	
	public void OpenFile() {
		final JFileChooser chooser = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File(sPath));
    	System.out.println(chooser.getCurrentDirectory());

        final int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
            sPath = chooser.getSelectedFile().getAbsoluteFile().getParent();
            sFileName = chooser.getSelectedFile().getName();
        }
	}
}


