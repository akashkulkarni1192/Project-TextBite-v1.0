import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


class MainBite {
	JFrame mainFrame = new JFrame();
	TextArea tArea;
	Clipboard cBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
	public static void main(String[] args) {
		
		MainBite biteObject = new MainBite();
		
		biteObject.setGUI();
	}

	public void setGUI() {
		
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setBackground(Color.GRAY);
		tArea = new TextArea();
		tArea.setBackground(Color.BLACK);
		tArea.setForeground(Color.WHITE);
		tArea.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
		String fileName;
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		
		MenuItem newOption = new MenuItem("New");
		MenuItem openOption = new MenuItem("Open");
		MenuItem saveOption = new MenuItem("Save");
		MenuItem exitOption = new MenuItem("Exit");
		
		MenuItem copyOption = new MenuItem("Copy");
		MenuItem cutOption = new MenuItem("Cut");
		MenuItem pasteOption = new MenuItem("Paste");
		MenuItem undoOption = new MenuItem("Undo");
		MenuItem redoOption = new MenuItem("Redo");
		
		newOption.addActionListener(new NewAction());
		openOption.addActionListener(new OpenAction());
		saveOption.addActionListener(new SaveAction());
		exitOption.addActionListener(new ExitAction());
		
		copyOption.addActionListener(new CopyAction());
		cutOption.addActionListener(new CutAction());
		pasteOption.addActionListener(new PasteAction());
		undoOption.addActionListener(new UndoAction());
		redoOption.addActionListener(new RedoAction());
		
		fileMenu.add(newOption);
		fileMenu.add(openOption);
		fileMenu.add(saveOption);
		fileMenu.add(exitOption);
		
		
		editMenu.add(copyOption);
		editMenu.add(cutOption);
		editMenu.add(pasteOption);
		editMenu.add(undoOption);
		editMenu.add(redoOption);
	
		tArea.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if (e.isPopupTrigger())
					doPop(e);
			}
			public void mouseReleased(MouseEvent e){
				if (e.isPopupTrigger())
					doPop(e);
			}
			public void doPop(MouseEvent e){
				PopupMenu pop = new PopupMenu();
				MenuItem un,doo;
				un = new MenuItem("Undo");
				doo = new MenuItem("doo");
				pop.add(un);
				pop.add(doo);
				
				pop.show(e.getComponent(),e.getX(),e.getY());
			}
		});
		
		
		
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		
		mainFrame.getContentPane().add(BorderLayout.CENTER,tArea);
		mainFrame.setMenuBar(menuBar);
		//mainFrame.getContentPane().add(BorderLayout.NORTH,menuBar);
		mainFrame.setSize(600, 500);
		mainFrame.setVisible(true);
	}
	
	class NewAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			tArea.setText(" ");
			mainFrame.setVisible(true);
		}

	}
	class OpenAction implements ActionListener{
		String fileName;
		public void actionPerformed(ActionEvent e) {
			JFileChooser selectFile = new JFileChooser();
			int returnVal = selectFile.showOpenDialog(mainFrame);
		    if(returnVal == JFileChooser.APPROVE_OPTION){
		    	fileName = selectFile.getSelectedFile().getAbsolutePath();
		    	tArea.setText(fileName);
		    }
		    
		    try {
				BufferedReader bReader = new BufferedReader(new FileReader(fileName));
				String data="";
				String line;
				try {
					while((line=bReader.readLine())!=null){
						data=data+line+"\n";
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tArea.setText(data);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
	class SaveAction implements ActionListener{
		String fileName,data;
		public void actionPerformed(ActionEvent e) {
			JFileChooser saveFile = new JFileChooser();
			int returnVal = saveFile.showSaveDialog(mainFrame);
		    if(returnVal == JFileChooser.APPROVE_OPTION){
		    	data=tArea.getText();
		    	fileName=saveFile.getSelectedFile().getAbsolutePath();
				
		    	try {
					PrintWriter writer = new PrintWriter(new FileWriter(fileName));
					writer.write(data);
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    	mainFrame.setVisible(true);
		    }
		}

	}
	
	class ExitAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	class CopyAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			String selection=tArea.getSelectedText();
			StringSelection sBuffer = new StringSelection(selection);
			cBoard.setContents(sBuffer, null);
		}

	}
	
	class CutAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			String selection=tArea.getSelectedText();
			StringSelection sBuffer = new StringSelection(selection);
			cBoard.setContents(sBuffer, null);
			tArea.replaceRange(null, tArea.getSelectionStart(), tArea.getSelectionEnd());
		}
	}
	
	class PasteAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			Transferable tBuffer = cBoard.getContents(this);
			try {
				String pasteText = (String) tBuffer.getTransferData(DataFlavor.stringFlavor);
				
				tArea.replaceRange(pasteText, tArea.getSelectionStart(), tArea.getSelectionEnd());
			} catch (UnsupportedFlavorException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class UndoAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
	
		}
	}
	
	class RedoAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
	
		}
	}
}
