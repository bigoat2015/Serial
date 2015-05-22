package com.lcwdz.serial;


import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;




public class Control {

	Serial serial;
	View view;
	
	private String emptyCount = "  ";
	
	
	public void setViewAndContorl(Serial serial, View view){
		this.serial = serial;
		this.view = view;
		
	}
	

	private void loadDataToView(){
		// ��ʼ��  ��ͼ
		
	//  serialName, baud, checkBit, dataBit, stopBit;
		
			Iterator<String> serialName = serial.getSerialNameList().iterator();
			while(serialName.hasNext()){
				view.getSerialName().addItem(serialName.next());
			}
			
			Iterator<Integer> baud = serial.getBaudList().iterator();
			while(baud.hasNext()){
				view.getBaud().addItem(baud.next()+"");
			}
			
			Iterator<String> checkBit = serial.getCheckBitList().iterator();
			while(checkBit.hasNext()){
				view.getCheckBit().addItem(checkBit.next()+"");
			}
			
			Iterator<Integer> dataBit = serial.getDataBitList().iterator();
			while(dataBit.hasNext()){
				view.getDataBit().addItem(dataBit.next()+"");
			}
			
			Iterator<String> stopBit = serial.getStopBitLit().iterator();
			while(stopBit.hasNext()){
				view.getStopBit().addItem(stopBit.next()+"");
			}
			
			view.getSerialName().setSelectedItem(serial.getSerialName());
			view.getBaud().setSelectedItem(serial.getBaud()+"");
			view.getCheckBit().setSelectedItem(serial.getCheckBit()+"");
			view.getDataBit().setSelectedItem(serial.getDataBit()+"");
			view.getStopBit().setSelectedItem(serial.getStopBit()+"");
			
			view.initCloseListenerEvent = true;
			
			try {
				serial.open();
			} catch (SerialConnectionException e) {
				serial.setConnectState(false);
				view.setConnectState(false);
				view.showErrorMessage(e.getMessage());
				return ;
			} 
			
			serial.setConnectState(true);
			view.setConnectState(true);
	}


	public void clickConnect() {
		// TODO ��� ���� ��ť ����
		
		serial.revDataCount = 0;
		serial.sendDataCount = 0;
		
		Boolean connectState = serial.getConnectState();

		if(connectState){
			try {
				serial.close();
			} catch (IOException e) {
				serial.setConnectState(true);
				view.setConnectState(true);
				view.showErrorMessage("�Ͽ����� ʧ�� : "+"\r\n"+e.getMessage());
				return ;
			} 
			serial.setConnectState(false);
			view.setConnectState(false);
				
			
		}
		else {
			try {
				serial.open();
			} catch (SerialConnectionException e) {
				serial.setConnectState(false);
				view.setConnectState(false);
				view.showErrorMessage(e.getMessage());
				return ;
			}
			
			serial.setConnectState(true);
			view.setConnectState(true);
			
		}
	}


	public void serialSetChange(ActionEvent source) {
		// TODO �������øı�		name ==> serialName, baud, checkBit, dataBit, stopBit;
		
		@SuppressWarnings("unchecked")
		JComboBox<String> jcb = (JComboBox<String>)source.getSource();
		String selectName = jcb.getName();
		String setvalue = (String)jcb.getSelectedItem();
		
		switch(selectName){	// ���ô��ڲ���
		
		case "serialName" : serial.setSerialName(setvalue); break;
		case "baud" : serial.setBaud(Integer.parseInt(setvalue)); break;
		case "checkBit" : serial.setCheckBit(setvalue); break;
		case "dataBit" : serial.setDataBit(Integer.parseInt(setvalue)); break;
		case "stopBit" : serial.setStopBit(setvalue); break;
		
		}
		
		if(serial.getConnectState()){
			try {
				serial.close();
				serial.open();
			} catch (IOException e) {
				serial.setConnectState(false);
				view.setConnectState(false);
				view.showErrorMessage("�򿪶˿�ʧ�ܣ�\r\n"+e.getMessage());
				return ;
			} catch (SerialConnectionException e) {
				serial.setConnectState(false);
				view.setConnectState(false);
				view.showErrorMessage(e.getMessage());
				return ;
			}
			
			serial.setConnectState(true);
			view.setConnectState(true);
		}
		
		
	}

	public void init() {
		// TODO serial control ��ʼ ֮�� ִ�иú��� ��ʼ��
		view.initialize();
		loadDataToView();
		
		
		view.setVisible(true);
		
		
	}


	public void receiveToData(byte[] receiveBuf, int max) {
		// TODO �ɴ����¼�ֱ�ӵ���  ���յ�����
		
		JTextArea revContent = view.getSerialReceiveText();
		view.setRevDataCount(serial.revDataCount+max);
		
		
		if(view.getStandardTemplateOnOff()){
			
			
			if(view.ishexDisply()){
				revContent.append(Util.bytes2Hex(receiveBuf, max, emptyCount)); 
			}
			
			if(view.binDisplay.isSelected()){
				revContent.append(Util.bytes2bin(receiveBuf, max, emptyCount));
			}
			
			if(!(view.ishexDisply()||view.binDisplay.isSelected())){	
				revContent.append(new String(receiveBuf, 0, max)+emptyCount);
			}
			
			revContent.selectAll();
		
		}
		
		if(view.getVerifyDataOnOff()){
			// �ж� ��֤���� �ӿ� �Ƿ��
			verifyData(receiveBuf, max);
		}
		
	}
	
	
	private void verifyData(byte[] receiveBuf, int max) {
		// TODO ��֤���ݴ���
		
		JTable jt = view.getVerifyDataJtable();
//System.out.println(view.verifyDataMode);	
		if(view.verifyDataMode){	// �ж����Զ���֤ �����ֶ���֤
	
			int seleceRow = jt.getSelectedRow();
			int rowCount = jt.getRowCount();
			int rowIndex = seleceRow;
			
			if(rowCount==0){	// ���� Ϊ��  return
				return ;
			}
			if(seleceRow == -1){
				rowIndex = 0;
			}
			
			for( ; rowIndex<rowCount; rowIndex++){
				
				jt.setRowSelectionInterval(rowIndex, rowIndex);
			
				if(view.isHexVerify()){
					
					String revText = Util.bytes2Hex(receiveBuf, max, " ");
					jt.setValueAt(revText, rowIndex, 1);	
					String setText = ((String)jt.getValueAt(rowIndex, 0));

					if(setText != null){
						setText = setText.replaceAll(" ", "").replaceAll("0x", "").trim().toUpperCase();
						revText = revText.replaceAll(" ", "");
									
						if(setText.equals(revText)){
							jt.setValueAt("PASS", rowIndex, 2);
						}
						else {
							jt.setValueAt("## ERROR ##", rowIndex, 2);
						}
						
					}
					else {
						jt.setValueAt("## ERROR ##", rowIndex, 2);
					}
						
				}
				
				else {
					
					String revText = new String(receiveBuf, 0, max);
					jt.setValueAt(revText, rowIndex, 1);
					
					String setText = ((String)jt.getValueAt(rowIndex, 0));
					
					if((setText !=null) && revText.trim().equals(setText.trim())) {
						jt.setValueAt("PASS", rowIndex, 2);
					}
					else {
						jt.setValueAt("## ERROR ##", rowIndex, 2);
					}
				}
			}	
				
			}
			else {
			int selectRow = jt.getSelectedRow();
			
			if(selectRow == -1){
				return ;
			}
			
			if(view.isHexVerify()){
				
				String revText = Util.bytes2Hex(receiveBuf, max, " ");
				jt.setValueAt(revText, selectRow, 1);	
				String setText = ((String)jt.getValueAt(selectRow, 0));

				if(setText != null){
					setText = setText.replaceAll(" ", "").replaceAll("0x", "").trim().toUpperCase();
					revText = revText.replaceAll(" ", "");
								
					if(setText.equals(revText)){
						jt.setValueAt("PASS", selectRow, 2);
					}
					else {
						jt.setValueAt("## ERROR ##", selectRow, 2);
					}
					
				}
				else {
					jt.setValueAt("## ERROR ##", selectRow, 2);
				}
					
			}
			
			else {
				
				String revText = new String(receiveBuf, 0, max);
				jt.setValueAt(revText, selectRow, 1);
				
				String setText = ((String)jt.getValueAt(selectRow, 0));
				
				if((setText !=null) && revText.trim().equals(setText.trim())) {
					jt.setValueAt("PASS", selectRow, 2);
				}
				else {
					jt.setValueAt("## ERROR ##", selectRow, 2);
				}
			}	
		}
	}


	public void sendData(JTextArea sendArea,String sendData) {
		// TODO ������� ���� ʵ��
		
		if(!serial.getConnectState()){
			view.showErrorMessage("�ף� �㻹û�д򿪴��ڣ�\r\n ���� �򿪴��� ���ڽ��з��͡� ");
			return ;
		}

		String formatData = "";
		
		if(view.isSendBin()){
			
			formatData = Util.char2Bin(sendData);
			view.setFormatData(formatData);

		}
		
		if(view.isSendHex()){
			
			formatData = Util.char2Hex(sendData);
			view.setFormatData(formatData);
			formatData = formatData.replace(" ", "");
			
		}
		
		if( (!(view.isSendBin()||view.isSendHex()))){
			
			formatData = sendData.trim();
			view.setFormatData(formatData);
			
		}
		
		try {
			serial.write(formatData);
		} catch (IOException e) {
			view.showErrorMessage("send ����");
			return ;
		}
		
		view.setSendDataCount(serial.sendDataCount += formatData.trim().length());
		
	}


	public void loadVerifyData(String fileName) {
		// TODO ��������ʵ��
		
		if(fileName.toLowerCase().endsWith(".txt")){
			
			try {
					
				byte idBuf[] = new  byte[14];
				FileInputStream fr = new FileInputStream(new File(fileName));
				fr.read(idBuf);
				
				if(new String(idBuf).equals("LCW_VerifyData")){
					
					StringBuffer verifyDataValues = new StringBuffer();
					byte[] tempbuf = new byte[20];
					
					while(fr.read(tempbuf)!=-1){
						verifyDataValues.append(new String(tempbuf));
						
					}
					
					String[] verifyValues = verifyDataValues.toString().split(",");
					JTable jt = view.getVerifyDataJtable();
						int a = jt.getRowCount();
						int b = verifyValues.length;
						int insertRow =  a>b ? -1 : b-a ;
						for(int j=0 ;j<insertRow; j++){
							view.getTableModel().addRow(new String[]{});
						}
					
					for(int i=1; i<verifyValues.length; i++){
						
						String value = verifyValues[i];
						
						jt.setValueAt(value, i-1, 0);
					}
					
				}else {
					view.showErrorMessage("���ݴ���: ������Ч�����ݣ�");
				}
				
				} catch (FileNotFoundException e) {
					view.showErrorMessage("�ף��ļ�û���ҵ���");
				}catch (IOException e) {
					view.showErrorMessage("�ף�I/O����  ��");
				}
			
		}else {
			view.showErrorMessage("ѡ����ļ�����ȷ,��ѡ���� .txt ��β���ı��ļ�");
		}
		
		
		
		
	}


	public void saveVerifyData(String verifyData, String FileName) {
		// TODO ��������ʵ��
		
		String idString = "LCW_VerifyData"; //"\r\n"  ����
		
		File f = new File(FileName);
		
	
		try {
			
			if(f.createNewFile()){
				FileWriter fos = new FileWriter(f);
				fos.write(idString+verifyData);
				
				fos.flush();
				fos.close();
			}
			else{
				view.showErrorMessage("��������ʧ�ܣ����ļ��Ѿ����ڣ�");
			}		
			
		} catch (IOException e) {
			view.showErrorMessage("IO�������");
		}
		
		
	}

	public void clerContent() {
		// TODO ��� ����
		view.getSerialReceiveTextHex().setText("");
		serial.revDataCount = 0;
		view.setRevDataCount(serial.revDataCount);
		
	}


	public void clickAutoLine(JPanel setDisply, boolean autoLine, JTextField autoLineCount) {
		// TODO ����Զ� ���� ����
		
		if(autoLine){
			emptyCount = "\r\n";
			view.setWheelIndex(1);
		}
		else {
			emptyCount = "  ";
		}
		
	}


	public void clickSendContent(JTextArea serialSendText) {
		// TODO ���� ���� ���
		
		serialSendText.setText("");
		view.setFormatData("");
		serial.sendDataCount = 0;
		view.setSendDataCount(serial.sendDataCount);
		
	}

	public void clickHexVerify(boolean selected, JTable verifyDataJtable) {
		// TODO ��� hex ��֤ʵ��            >>  ��Ϊʵ��  δʹ��
	
		JTable jt = verifyDataJtable;
		int rowCount = jt.getRowCount();
		
		if(rowCount == 0){
			return ;
		}
		
	
		
		if(selected){
			
			for(int i=0; i<rowCount; i++){
				
				Object o = jt.getValueAt(i, 1);
				o = o==null ? new String() : o ;
				System.out.println(o.toString());
				jt.setValueAt(Util.char2Hex(o.toString()), i, 1);			
			}	
		}
		else {
			
			for(int j=0; j<rowCount; j++){
				//verifyDataJtable.setValueAt(Util.hex2Char(verifyData[i]),i,1);
			}
		
		}	
	}


	

	public void mouseWheel(JTextArea revArea, int wheelIndex, boolean autoLine) {
		// TODO ������ʵ��
		
		if(!autoLine) {
			
			String emptyStrCount = " ";
			for(int i=0; i<=wheelIndex; i++){
				emptyStrCount += " ";
			}
			
			emptyCount = emptyStrCount;
		}
		
	}

	
}

