package com.lcwdz.serial;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import java.awt.Window.Type;




public class View extends JFrame implements ActionListener , MouseWheelListener {
	public View() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		
	}
	
	
	private static final long serialVersionUID = 1L;
	
	private Control control;
	
	
	
	private JComboBox<String> serialName, baud, checkBit, dataBit, stopBit;
	private JButton connect;
	private boolean verifyDataOnOff = false;
	private boolean standardTemplateOnOff = true;
	Boolean initCloseListenerEvent = false;   // ���� �ı�����¼� ����ʼ�� ʱ ��ֹ ���ڲ���ִ��
	private JTextArea serialReceiveTextHex;
	private JTextArea serialSendText;
	private JButton sendButton;
	private JComboBox<String> verifyDataSelect;
	private JTable  verifyDataJtable;
	private JButton addRow;
	private JButton removeRow;
	private DefaultTableModel tableModel;
	Boolean verifyDataMode = false;
	int verifyDataIndex;
	private JButton loadVerifyData;
	private JButton saveVerifyData;
	private JPanel centerJPanel;
	private CardLayout gl;
	private JLabel serialState;
	private JCheckBox autoLine;
	private JTextField autoLineCount;
	 
	
	private JPanel sendJp;
	private JCheckBox sendHex;
	private JCheckBox sendBin;
	//private Boolean sendDisplayState = false;  // true=Hex false=char 
	private JLabel sendFormatData;
	
	JCheckBox hexDisplay;
	JCheckBox binDisplay;

	private CustomJLable clerRevContent;

	private JTextArea serialReceiveText;

	private JLabel revData;

	private JLabel sendData;

	JScrollPane jsp1;

	private JPanel setDisply;

	private CustomJLable sendEmpty;

	//private JCheckBox hexVerify;

	private JCheckBox stopRev;

	private int wheelIndex;

	private JToggleButton hexVerify;
	
	
	public void setControl(Control control){
		this.control = control;
		
			
	}
	
	public void initialize(){
		
		setTitle("����������΢����");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout(10, 10));
		
		// ���� �������ֵ� ����
		getContentPane().add(northContent(), BorderLayout.NORTH);
		getContentPane().add(southContent(), BorderLayout.SOUTH);
		getContentPane().add(westContent(), BorderLayout.WEST);
		getContentPane().add(new JLabel(), BorderLayout.EAST);
		getContentPane().add(centerContent(), BorderLayout.CENTER);
	}
	
	// �в�
	private Component centerContent() {
		// TODO �в� ����
		
		centerJPanel = new JPanel();
		gl = new CardLayout();
		centerJPanel.setLayout(gl);
		centerJPanel.add(standardTemplate(), "standardTemplate");
		centerJPanel.add(verifyDataTemplate(),"verifyDataTemplate");
		
		
		
		return centerJPanel;
	}
	
	private JPanel standardTemplate(){
		// ��׼���� ���
		
		JPanel st = new JPanel(new BorderLayout(10,30));
		
		JPanel center = new JPanel(new BorderLayout());
		
		/******/
		JPanel hexJp = new JPanel(new BorderLayout());
		JPanel strJp = new JPanel(new BorderLayout());
		JPanel hexAndStrJP = new JPanel(new GridLayout(2,1));
		
		
		serialReceiveTextHex = new JTextArea();
		serialReceiveTextHex.setWrapStyleWord(true);  // ��ȡ���з�ʽ������ı���Ҫ���У����������Ϊ true�����еĳ��ȴ���������Ŀ��ʱ�����ڵ��ʱ߽磨���հף�������
		serialReceiveTextHex.setColumns(2);
		serialReceiveTextHex.invalidate();
serialReceiveText = new JTextArea();
		//serialReceiveTextHex.setEditable(false);
		//serialReceiveText.setEnabled(false);
		serialReceiveTextHex.setLineWrap(true);
		serialReceiveText.setLineWrap(true);
		jsp1 = new JScrollPane(serialReceiveTextHex);
		JScrollPane jsp2 = new JScrollPane(serialReceiveText);
		
		hexJp.add(jsp1);
		strJp.add(jsp2);
		hexAndStrJP.add(jsp1);
		hexAndStrJP.add(jsp2);
		
		
		//centerCenter.add(strJp, "strJp");
		//centerCenter.add(hexAndStrJP, "hexAndStrJP");
		
		
		setDisply = new JPanel(new FlowLayout(FlowLayout.LEFT,0,3));
		hexDisplay = new JCheckBox("Hex ��ʾ");
		hexDisplay.setToolTipText(Util.hexDisplay);
		hexDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(binDisplay.isSelected()){
					binDisplay.setSelected(false);
				}
				//control.clickHexDisply();
			}
		});
		
		binDisplay = new JCheckBox("Bin ��ʾ");
		binDisplay.setToolTipText(Util.binDisplay);
		binDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(ishexDisply()){
					hexDisplay.setSelected(false);
				}
				//control.clickBin(((JCheckBox)arg0.getSource()).isSelected());
			}
		});
		
		clerRevContent = new CustomJLable("�������"){
			public void mouseClicked(MouseEvent e) {
				control.clerContent();
			}
		};
		clerRevContent.setToolTipText(Util.clerRevContent);
		clerRevContent.addMouseListener(clerRevContent);		
		
		stopRev = new JCheckBox("��ͣ����");
		stopRev.setToolTipText(Util.stopRev);
		stopRev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!stopRev.isSelected()){
					standardTemplateOnOff = true;
				}
				else
					standardTemplateOnOff = false;
			}
		});
		
		autoLineCount = new JTextField(3);
		autoLine = new JCheckBox("�Զ�����");
		autoLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				control.clickAutoLine(setDisply,autoLine.isSelected(),autoLineCount);
			}
		});
		autoLine.setToolTipText(Util.autoLine);
		autoLine.addMouseWheelListener(this);
			
		setDisply.add(binDisplay);
		setDisply.add(hexDisplay);
		setDisply.add(stopRev);
		setDisply.add(autoLine);
		setDisply.add(clerRevContent);
		//setDisply.add(autoLineCount); // δ�õ�
		
		center.add(jsp1,BorderLayout.CENTER);
		center.add(setDisply,BorderLayout.SOUTH);
		
		
		st.add(center, BorderLayout.CENTER);
		st.add(new JLabel(),BorderLayout.EAST);
		st.add(sendJp(), BorderLayout.SOUTH);
		
		
		
		return st;
	}
	
	private Component sendJp() {
		// TODO �������
		sendJp = new JPanel(new BorderLayout());
		
		JPanel sendArea = new JPanel(new BorderLayout(5,0));
		serialSendText = new JTextArea(6,30);
		serialSendText.enableInputMethods(false);	//ֻ������ �ַ�
		sendButton = new JButton("��  ��"); 
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO �������� 
				control.sendData(serialSendText, serialSendText.getText());
			}
		});
		
		JScrollPane jspSend = new JScrollPane(serialSendText); 
		sendArea.add(jspSend, BorderLayout.CENTER);
		sendArea.add(sendButton, BorderLayout.EAST);
		
		sendFormatData = new JLabel("sendData��");
		
		sendJp.add(sendFormatData, BorderLayout.NORTH);
		sendJp.add(sendArea, BorderLayout.CENTER);
		sendJp.add(sendSet(), BorderLayout.SOUTH);
		
		
		return sendJp;
	}

	private Component sendSet() {
		// TODO ��������
		JPanel sendSetJp = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
		
		sendBin = new JCheckBox("Bin ����");
		sendHex = new JCheckBox("Hex ����");
		sendBin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(sendBin.isSelected()){
					sendHex.setSelected(false);
					//sendDisplayState = false;
				}
			//	control.clickSendDataDisplay(sendBin.isSelected(),sendHex.isSelected(),sendDisplayState,serialSendText);
			}
		});
		sendHex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sendHex.isSelected()){
					sendBin.setSelected(false);
					//sendDisplayState = true;
				}
				//control.clickSendDataDisplay(sendBin.isSelected(),sendHex.isSelected(),sendDisplayState,serialSendText);
			}
		});
		
		sendEmpty = new CustomJLable("�������"){
			public void mouseClicked(MouseEvent e) {
				control.clickSendContent(serialSendText);
			}
		};
		sendEmpty.addMouseListener(sendEmpty);
		
		
		
		//sendSetJp.add(sendBin);	 ������ ���� δʵ��
		sendSetJp.add(sendHex);
		
		sendSetJp.add(sendEmpty);
		
		return sendSetJp;
	}
	
	private JPanel verifyDataTemplate(){
		// ��֤���� ���
		
		
		JPanel vdt = new JPanel(new BorderLayout());
		
		tableModel = new DefaultTableModel(new String[]{"��������","��������","��֤���"},10){  
            @Override  
            public boolean isCellEditable(int row,int column){  
                if(column == 2 || column == 1)
                	return false;
                else
                	return true;
            }  
        };
        
       
       
        
        verifyDataJtable = new JTable(tableModel){
        	public String paramString(){
        		
        		return "OK";
        	}
        	@Override
        	public String toString() {
        		// TODO ���� ��������ֵ
        		StringBuffer value = new StringBuffer() ;
        		int tableRow = verifyDataJtable.getRowCount();
        		if(tableRow != -1){
        			
        			for(int i=0; i<tableRow; i++){
        				Object o = verifyDataJtable.getValueAt(i, 0);
        				if(o!= null){
        					if(!(o.toString().equals("")))
        						value.append(","+o.toString());
        				}
            		}
        		
        		}
        			
        		
        		return value.toString();
        	}
        }; 
        
        verifyDataJtable.setRowHeight(20);
        verifyDataJtable.getTableHeader().setReorderingAllowed(false); // ���������϶�
		
		JScrollPane jsp = new JScrollPane(verifyDataJtable);
		vdt.add(jsp);
		
		// ��֤���� ���  �ϲ�����
		JPanel southJp = new JPanel(new FlowLayout(FlowLayout.LEFT,5,1));
		
		verifyDataSelect = new JComboBox<String>(new String[]{});
		verifyDataSelect.addItem("�ֶ���֤");
		verifyDataSelect.addItem("�Զ���֤");
		verifyDataSelect.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO ��� ��֤��ť  ��ʼ ���մ������� ��֤
				String selectValue = (String)verifyDataSelect.getSelectedItem();
				if(selectValue.equals("�ֶ���֤")){
					verifyDataMode = false;
				}
				else{

					verifyDataMode = true;
					verifyDataIndex = 0;
				}
		}});
		
		hexVerify = new JToggleButton("Hex ��ʾ");
		hexVerify.setSelected(true);
		hexVerify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//control.clickHexVerify(((JToggleButton)e.getSource()).isSelected(), verifyDataJtable);
			}
		});
		
		
		addRow = new JButton(" �� �� �� ");
		addRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = verifyDataJtable.getSelectedRow();
				if(selectedRow != -1){
					tableModel.insertRow(selectedRow+1, new String[]{});
				}
				else
					tableModel.addRow(new String[]{});
			}
		});
		removeRow = new JButton(" �� �� �� ");
		removeRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 int selectedRow = verifyDataJtable.getSelectedRow();//���ѡ���е�����  
	             if(selectedRow!=-1)  //����ѡ����
	            	 {  
	            	 	if(verifyDataJtable.getValueAt(selectedRow, 0)==null||verifyDataJtable.getValueAt(selectedRow, 0).equals("")){
	            	 		tableModel.removeRow(selectedRow);  //ɾ���� 
	            	 		if(verifyDataJtable.getRowCount()>0)
	            	 			verifyDataIndex --;
//	     System.out.println("��������Ϊ�գ�");
	            	 	}
	            	 	else {
	            	 		int selectValue = JOptionPane.showConfirmDialog(verifyDataJtable, "ȷ��ɾ������");
//		  System.out.println("ѡ������"+selectValue);
	            	 		if(selectValue == 0)
		            	 		tableModel.removeRow(selectedRow);  //ɾ���� 
	            	 		if(verifyDataJtable.getRowCount()>0)
	            	 			verifyDataIndex --;
	            	 	}
	            	 	
	            	 	
	            	 } 
	             else{
	            	 showErrorMessage("���� ѡ����Ҫ ɾ�����У�");
	             }
			}
		});
		
		JButton empytData = new JButton("�������");
		empytData.addActionListener(new ActionListener() {	// �������
			public void actionPerformed(ActionEvent arg0) {
				int dataRow = tableModel.getRowCount();
				for(int i=0; i<dataRow; i++){
					tableModel.removeRow(0);
					tableModel.addRow(new String[]{});
					verifyDataIndex = 0;
//System.out.println(i);
					
				}
			}
		});
		
		loadVerifyData = new JButton("��������");
		loadVerifyData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				
				JFileChooser jfc = new JFileChooser();
				
				//jfc.setAcceptAllFileFilterUsed(true);
				jfc.addChoosableFileFilter(new FileFilter(){

					public boolean accept(File f) {
						// TODO Auto-generated method stub
						String nameString = f.getName();
						return nameString.toLowerCase().endsWith(".txt");
						}
						@Override
						public String getDescription() {
						// TODO Auto-generated method stub
						return "*.txt(�ı��ļ�)";
						}});
				
				if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
					String fileName = jfc.getSelectedFile().toString();
					control.loadVerifyData(fileName);
				}
			}
		});
		saveVerifyData = new JButton("��������");
		saveVerifyData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
			
				if(jfc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
					String fileName = jfc.getSelectedFile().toString();
					
			
					control.saveVerifyData(verifyDataJtable.toString(),fileName+".txt");
				
				}
				
				
				
			}
		});;
		
		
		southJp.add(verifyDataSelect);
		southJp.add(hexVerify);
		southJp.add(empytData);
		southJp.add(addRow);
		southJp.add(removeRow);
		southJp.add(loadVerifyData);
		southJp.add(saveVerifyData);
		
		
		vdt.add(southJp, BorderLayout.SOUTH);
		
		
		return vdt;
	}


	private Component westContent() {
		// TODO ��
		
		JPanel wcj = new JPanel();
		wcj.setForeground(Color.WHITE);
		wcj.setLayout(new GridLayout(2, 1, 0, 5));
		
		wcj.add(setSerialJp());
		wcj.add(extendJp());
		
		return wcj;
	}
	
	private Component extendJp() {
		// TODO ��չ���
		
		JPanel ej = new JPanel(new GridLayout(8, 2, 0, 2));
		
		ej.add(new JLabel("   ��  ��  ����"));
		
		JButton verifyDataButton = new JButton("�� ֤ �� ��");
		verifyDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setStandardTemplateOnOff(false);
				setVerifyDataOnOff(true);
				gl.show(centerJPanel, "verifyDataTemplate");
			}
		});
		
		JButton standardTemplateButton = new JButton("�� ׼ �� ��");
		standardTemplateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVerifyDataOnOff(false);
				setStandardTemplateOnOff(true);
				gl.show(centerJPanel, "standardTemplate");
			}
		});
		
		JButton calcButton = new JButton("��    ��    ��");
		calcButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO ����ϵͳ������
				try{ 
					Runtime run = Runtime.getRuntime(); 
					run.exec("cmd.exe /c start C:\\WINDOWS\\system32\\calc.exe "); 
					}catch(Exception e){ 
						showErrorMessage("�򿪼���������\r\n"+e.getMessage());
					} 
			}
		});
		
		JButton  ascii = new JButton("ASCII �� ��");
		ascii.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame("ASCCII");
				f.setLocation(200, 50);
				f.setType(Type.UTILITY);
				ImageIcon bg = new ImageIcon(View.class.getResource("/com/lcwdz/serial/ASCII.png"));
				JLabel jl = new JLabel(bg);
				f.setLayout(new FlowLayout());
				f.getContentPane().add(jl);
				f.pack();
				f.setVisible(true);
				
			}
		});
		
		ej.add(standardTemplateButton);
		ej.add(verifyDataButton);
		ej.add(calcButton);
		ej.add(ascii);
		
		return ej;
	}

	private Component setSerialJp() {
		// TODO �����������  serialName, baud, checkBit, dataBit, stopBit;
		
		JPanel ssj = new JPanel();
		
		JLabel lblNewLabel = new JLabel("��������");
		
		JLabel lblNewLabel_1 = new JLabel("���ں�");
		serialName = new JComboBox<String>();
		serialName.setToolTipText("����Ҽ� ���� ���ô��ں�");
		serialName.setName("serialName");
		serialName.addActionListener(this);
		serialName.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getModifiers()==InputEvent.BUTTON3_MASK){
					String com = JOptionPane.showInputDialog("�����ó��ô��ڣ�");
					if(com==null||com.equals(""))
					{
						com = "COM1";
					}
					Util.write("config.properties", "COM", com.toUpperCase());
				}
			}
		
		});
		
		JLabel lblNewLabel_2 = new JLabel("������");
		baud = new JComboBox<String>();
		baud.setName("baud");
		baud.addActionListener(this);
		
		JLabel lblNewLabel_3 = new JLabel("У��λ");
		checkBit = new JComboBox<String>();
		checkBit.setName("checkBit");
		checkBit.addActionListener(this);
		
		JLabel lblNewLabel_4 = new JLabel("����λ");
		dataBit = new JComboBox<String>();
		dataBit.setName("dataBit");
		dataBit.addActionListener(this);
		
		JLabel lblNewLabel_5 = new JLabel("ֹͣλ");
		stopBit = new JComboBox<String>();
		stopBit.setName("stopBit");
		stopBit.addActionListener(this);
		
		connect = new JButton();
		setConnectState(false);
		
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO ��� ���� ��ť ����
			control.clickConnect();
		}});
		
		GroupLayout gl_ssj = new GroupLayout(ssj);
		gl_ssj.setHorizontalGroup(
			gl_ssj.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_ssj.createSequentialGroup()
					.addGroup(gl_ssj.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_ssj.createSequentialGroup()
							.addGap(18)
							.addComponent(connect, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
						.addGroup(gl_ssj.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_ssj.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addGroup(gl_ssj.createSequentialGroup()
									.addComponent(lblNewLabel_1)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(serialName, 0, 61, Short.MAX_VALUE))
								.addGroup(gl_ssj.createSequentialGroup()
									.addComponent(lblNewLabel_2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(baud, 0, 61, Short.MAX_VALUE))
								.addGroup(gl_ssj.createSequentialGroup()
									.addComponent(lblNewLabel_3)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(checkBit, 0, 61, Short.MAX_VALUE))
								.addGroup(gl_ssj.createSequentialGroup()
									.addComponent(lblNewLabel_4)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dataBit, 0, 61, Short.MAX_VALUE))
								.addGroup(gl_ssj.createSequentialGroup()
									.addComponent(lblNewLabel_5)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(stopBit, 0, 61, Short.MAX_VALUE)))))
					.addContainerGap())
		);
		gl_ssj.setVerticalGroup(
			gl_ssj.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ssj.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ssj.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(serialName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ssj.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(baud, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ssj.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(checkBit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ssj.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(dataBit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ssj.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_5)
						.addComponent(stopBit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(connect)
					.addContainerGap(29, Short.MAX_VALUE))
		);
		ssj.setLayout(gl_ssj);
		
		return ssj;
	}

	private Component southContent() {
		// TODO �ϲ�
		
		JPanel scj = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
		serialState = new JLabel("���ں�:"+ " " +
				"     ������:"+ " " +
				"     ����λ:"+ " " +
				"     ֹͣλ:"+ " " +
				"     У��λ:"+ " " +
				"     ��״̬:"+ " ");
		serialState.setForeground(new Color(30, 144, 255));
		
		revData  = new JLabel("RX��"+0);
		revData.setForeground(new Color(30, 144, 255));
		sendData = new JLabel("TX��"+0);
		sendData.setForeground(new Color(30, 144, 255));
	
		scj.add(serialState);
		scj.add(revData);
		scj.add(sendData);
		
		
		
		return scj;
	}

	private Component northContent() {
		// TODO ���� ��� 
		
		JPanel ncj = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ncj.add(new JLabel("serial"));
		
		return ncj;
	}
	
	
	
	public JComboBox<String> getSerialName() {
		return serialName;
	}

	public JComboBox<String> getBaud() {
		return baud;
	}

	public JComboBox<String> getCheckBit() {
		return checkBit;
	}

	public JComboBox<String> getDataBit() {
		return dataBit;
	}

	public JComboBox<String> getStopBit() {
		return stopBit;
	}

	public JButton getConnect(){
		return connect;
	}
	
	public JButton getSendButton() {
		return sendButton;
	}

	public void setSendButton(JButton sendButton) {
		this.sendButton = sendButton;
	}

	public JTextArea getSerialReceiveText() {
		return serialReceiveTextHex;
	}

	public JTextArea getSerialSendText() {
		return serialSendText;
	}

	public JComboBox<String> getVerifyDataSelect() {
		return verifyDataSelect;
	}
	
	public boolean getVerifyDataOnOff(){
		return verifyDataOnOff;
	}
	
	
	public JTable getVerifyDataJtable() {
		return verifyDataJtable;
	}

	public void setVerifyDataOnOff(boolean verifyDataOnOff){
		this.verifyDataOnOff = verifyDataOnOff;
	}
	
	public boolean getStandardTemplateOnOff() {
		return standardTemplateOnOff;
	}

	public void setStandardTemplateOnOff(boolean standardTemplateOnOff) {
		this.standardTemplateOnOff = standardTemplateOnOff;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
	
	public void setConnectState(boolean connectState){
		if(connectState){
			connect.setText("�� �� �� ��");
			connect.setBackground(new Color(30, 144, 255));
		}
		else{
			connect.setText("�� �� �� ��");
			connect.setBackground(Color.LIGHT_GRAY);
		}
	}
	
	public void setSerialStateContent(String serialState) {
		this.serialState.setText(serialState);
	}
	
	public void setRevDataCount(long count) {
		this.revData.setText("RX��"+count);
	}

	public void setSendDataCount(long count) {
		this.sendData.setText("TX��"+count);
	}

	public boolean isHexVerify(){
		return hexVerify.isSelected();
	}
	

	public void showErrorMessage(String error){
		JOptionPane.showMessageDialog(this, error);	// �����Ի��� ��ʾ������Ϣ��
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �������� ����¼�����
		
		if(initCloseListenerEvent)
			control.serialSetChange(e);
	}
	
	class TxtFileFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
		// TODO Auto-generated method stub
		String nameString = f.getName();
		return nameString.toLowerCase().endsWith(".txt");
		}
		@Override
		public String getDescription() {
		// TODO Auto-generated method stub
		return "*.txt(�ı��ļ�)";
		}
	}

	public JTextArea getSerialReceiveTextHex() {
		// TODO Auto-generated method stub
		return serialReceiveTextHex;
	}

	public boolean isSendBin() {
		// TODO Auto-generated method stub
		return sendBin.isSelected();
	}

	public boolean isSendHex() {
		// TODO Auto-generated method stub
		return sendHex.isSelected();
	}

	public void setFormatData(String formatData) {
		// TODO Auto-generated method stub
		sendFormatData.setText("sendData��"+formatData);
	}
	
	public void setWheelIndex(int i) {
		// TODO Auto-generated method stub
		wheelIndex = i;
	}
	
	class CustomJLable extends JLabel implements MouseListener{
	
		public CustomJLable() {
			// TODO Auto-generated constructor stub
			super();
		}
		
		public CustomJLable(String text) {
			// TODO Auto-generated constructor stub
			super(text);
			
			init();
		}
		
		private void init(){
			
			setCursor(new Cursor(Cursor.HAND_CURSOR));  // �������
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO ���ʱ
			//control.clickSendContent(serialSendText);
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO �������
			setForeground(new Color(30, 144, 255));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO �뿪���
			setForeground(new Color(51, 51, 51));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO ��� �����¼�
			
		if(serialReceiveTextHex.equals("")){
			return ;
		}
		
		if(e.getWheelRotation()==1 && wheelIndex<=50){	// ���Ϲ�
			wheelIndex ++;
		}
		if(e.getWheelRotation()==-1 && wheelIndex>=0){	// ���¹�
			wheelIndex --;
		}
//System.out.println(wheelIndex+"-");		
		control.mouseWheel(serialReceiveTextHex, wheelIndex, autoLine.isSelected());
		
			
		
	}

	public boolean ishexDisply() {
		// TODO Auto-generated method stub
		return hexDisplay.isSelected();
	}



}

