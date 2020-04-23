/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proiect;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
 

/**
 *
 * @author Popandau
 */
public class DynamicKernel extends javax.swing.JFrame {
	public static int initial_i=3; //starting width
	public static int initial_j=3; //starting length
	int iterator=1;
    private JFrame mainFrame ;
    private TextField lin;
    private Label linie,coli,mesaj;
	private TextField col;
    private JButton addButton,load,save;
    private String[] columnNamesArr;
    private ArrayList<String> columnNamesList;
    private JScrollPane scrollPane;
    private DefaultTableModel defaultTableModel;
    private JTable table;
    private TableColumnModel tableColumnModel;
	private JPanel panel;
    private JPanel bottompanel;
    private String[][] data;
   static private int[][] datele;
    public DynamicKernel() {
    	mainFrame= new JFrame("Tabel");
	    lin=new TextField();
	    col=new TextField();
        
        columnNamesList = new ArrayList<String>();
		 
		columnNamesList.add(Integer.toString(iterator));iterator++;
		columnNamesList.add(Integer.toString(iterator));iterator++;
		columnNamesList.add(Integer.toString(iterator));
                
        data = new String[columnNamesList.size()][columnNamesList.size()];

        columnNamesArr = new String[columnNamesList.size()];
     
		for(int i=0;i<columnNamesList.size();i++){   
			
			for(int j=0;j<columnNamesList.size();j++){
				columnNamesArr[i] = columnNamesList.get(i);
				data[i][j]="0";
            }
		}
        defaultTableModel = new DefaultTableModel(data, columnNamesArr);
                
        table = new JTable(defaultTableModel);
		tableColumnModel = table.getColumnModel();
                
        for(int i=0;i<columnNamesList.size();i++){
			tableColumnModel.getColumn(i).setPreferredWidth(columnNamesList.get(i).length());
		}
        
        
        
		table.setPreferredScrollableViewportSize(table.getPreferredSize()); 
		scrollPane = new JScrollPane(table);
        addButton = new JButton("OK");
        Label mesaj=new Label(" ");
        addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) 
			{
				int l=Integer.parseInt(lin.getText());
	            int c=Integer.parseInt(col.getText());
	                            
	            
	            
	            data = new String[l][c];
                columnNamesArr = new String[c];
                for(int j=0;j<c;j++) {
    				columnNamesArr[j] = Integer.toString(j);
    				
    			}
                for(int i=0;i<l;i++){
        			
        			for(int j=0;j<c;j++) {
        				
        				data[i][j]="0";
        			}
        			
        		}
                defaultTableModel = new DefaultTableModel(data, columnNamesArr);
                
                System.out.println(defaultTableModel.getColumnCount());
	            table.setModel(defaultTableModel);
	            
	            table.validate();
	                         
			}
        });
                  
     	
        load = new JButton("Apply");
                
        load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) 
			{  
				int k=1;
                datele=new int[table.getRowCount()][table.getColumnCount()];
                for(int u=0;u<table.getRowCount();u++){
                   
                        for(int w=0;w<table.getColumnCount();w++){
                        	if(table.getModel().getValueAt(u,w).toString()=="")
                            {
                            	k=0;
                            	
                                System.out.println("nu ai pus toate valorile ");
                                break;      
                                        
                            }
                        }
                        if(k == 0) {
                        	
                        	break;
                        }
                         
                }
                if(k==0)
                	System.out.println("Adauga toate datele");
                else
                if(k==1)
                {
                	for(int u=0;u<table.getRowCount();u++){
                        for(int w=0;w<table.getColumnCount();w++){
                                datele[u][w]= Integer.parseInt((table.getModel().getValueAt(u,w).toString()));
                        }
                	}
	                for(int r=0;r<table.getRowCount();r++){
	                	for(int g=0;g<table.getColumnCount();g++){
	                		System.out.print(datele[r][g]);
	                		System.out.print(" ");
	                	}
	                	System.out.println();
	                }
                }
                
                      Sobel s=new Sobel();
            
                            try {
                                s.Operator(datele);
                            } catch (IOException ex) {
                                Logger.getLogger(DynamicKernel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                
                      
               
                
            }
                        
                     
                    
              
                        
		});
                    
        save=new JButton("Apply");
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
 
                
            }
            
        });
        Label linie=new Label("Lini");
        Label coli=new Label("Coloane");
        panel = new JPanel();
        panel.add(linie);
		panel.add(lin);
        panel.add(coli);
		panel.add(col);
        panel.add(addButton);
        bottompanel=new JPanel();
        bottompanel.add(load);
        
		//mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);   
		mainFrame.add(scrollPane, BorderLayout.CENTER);   
		mainFrame.add(panel, BorderLayout.NORTH); 
        mainFrame.add(bottompanel, BorderLayout.SOUTH);
		mainFrame.pack();   
		mainFrame.setLocation(200,200);   
		mainFrame.setVisible(true);   
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textField1 = new java.awt.TextField();
        textField2 = new java.awt.TextField();
        label1 = new java.awt.Label();
        label2 = new java.awt.Label();
        button1 = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 500));

        textField1.setText("textField1");

        textField2.setText("textField2");

        label1.setText("Coloane");

        label2.setText("Linii");

        button1.setLabel("button1");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(textField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    

    
    public static void main(String[] args) {   
        EventQueue.invokeLater(new Runnable() {   
  
            public void run() {   
                DynamicKernel jts = new DynamicKernel();   
            }   
        });   
    } 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.TextField textField1;
    private java.awt.TextField textField2;
    // End of variables declaration//GEN-END:variables
}
