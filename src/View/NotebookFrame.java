package View;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import Data.Entry;


public class NotebookFrame extends JFrame implements ActionListener {

	private JTabbedPane entries = new JTabbedPane();
	

	
	public NotebookFrame(){
		generateToolbar();
		addPage();
		generateLayout();
		setListeners();
		
		//Maximize on open and display.
		//setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		
	}
	
	
	private void setListeners() {
		entries.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// On middle click, close.
				if (e.getButton() == 2) {
					int i = entries.indexAtLocation(e.getPoint().x,
							e.getPoint().y);
					removeTabAt(i);
				}
			}
		});
		
	}


	void removeTabAt(int i) {
		if (i >= 0) {
			EntryTab temp = ((EntryTab) entries.getComponentAt(i));
			if (checkForSave(temp)) {
				entries.remove(i);
				//refreshWorkAreas();
				repaint();
			}
		}
	}


	private boolean checkForSave(EntryTab temp) {
		if (null == temp) {
			return checkSaveAll();
		} else if (temp.isChanged()) {
			int n = JOptionPane.showConfirmDialog(new JFrame(),
					"Do you want to save " + temp.getTitle() + " before you go?",
					"Save Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
			if (n == JOptionPane.CANCEL_OPTION
					|| n == JOptionPane.CLOSED_OPTION) {
				return false;
			} else if (n == JOptionPane.NO_OPTION) {
				return true;
			} else {
				return temp.save();
			}
		} else
			return true;
	}


	private boolean checkSaveAll() {
		boolean cont = true;
		for (Component d : entries.getComponents()) {
			if (cont && d instanceof EntryTab) {
				entries.setSelectedComponent(d);
				cont = checkForSave((EntryTab) d);
			}
		}
		return cont;
	}


	private void generateLayout(){
		 javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(entries)
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(entries, javax.swing.GroupLayout.Alignment.TRAILING)
	        );

	        pack();
	}
	
	private void generateToolbar(){
		JMenu file = new JMenu("File");
		JMenuBar menuBar = new JMenuBar();
       
        file.add(makeMenuItem("New Entry", "new entry", KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)));
        
        file.add(makeMenuItem("Settings", "settings", null));
        
        file.add(makeMenuItem("View Entries", "view entries", javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK)));
        
        file.add(makeMenuItem("View Source", "view source", null));
        
        file.add(makeMenuItem("Append PDF", "append pdf", null));
        
        file.add(new JPopupMenu.Separator());
        
        file.add(makeMenuItem("Save Entry", "save entry", javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK)));
        
        file.add(makeMenuItem("Save All", "save all",javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK)));

        file.add(makeMenuItem("Load Notebook", "load notebook",javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK)));

        file.add(makeMenuItem("Quit", "quit",javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK)));

        menuBar.add(file);
        
        this.setJMenuBar(menuBar);
	}
	
	private JMenuItem makeMenuItem(String text, String command, KeyStroke k) {
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setAccelerator(k);
		menuItem.setActionCommand(command);
		menuItem.addActionListener(this);
		return menuItem;
	}
	
	public void actionPerformed(ActionEvent e) {

		EntryTab temp = (EntryTab) entries.getSelectedComponent();
		String cmd = e.getActionCommand();

		if ("new entry".equals(cmd)) {
			load(null);

//		} else if ("load".equals(cmd)) {
//			File f = Utils.loadDialog();
//			if (f != null)
//				load(f);

		} else if ("save all".equals(cmd)) {
			if (checkSaveAll()) {
				entries.removeAll();
			}
		} else if ("quit".equals(cmd)) {
			if (checkSaveAll()) {
				dispose();
			}
		} else if (temp != null) {
			if ("close".equals(cmd)) {
				if (checkForSave(temp)) {
					//temp.clear();
					entries.remove(temp);
				}

			} else if ("save as".equals(cmd)) {
				temp.saveAs();
			} else
				temp.actionPerformed(e);
		}
	}
	
	private void load(Entry f) {
//		int index = -1;
//		if (f != null) {
//			for (int i = 0; i < entries.getTabCount(); i++) {
//				if (f.getAbsolutePath().equalsIgnoreCase(
//						entries.getToolTipTextAt(i))) {
//					// If the file is already loaded, switch to it.
//					index = i;
//				}
//			}
//		}
//		if (index == -1) { // if f is null or not present
//			EntryTab temp = addPage();
//			if (f != null) {
//				temp.load(f);
//			}
//
//		} else {
//			entries.setSelectedIndex(index);
//		}
//		//refreshWorkAreas();
	}


	private EntryTab addPage() {
		EntryTab temp = new EntryTab();
		entries.add(temp);
		entries.setSelectedComponent(temp);
		int tabLocation = entries.getSelectedIndex();
		entries.setTabComponentAt(tabLocation, new ButtonTabComponent(
				entries, temp, this));
		temp.setRequestFocusEnabled(false);
		temp.setVerifyInputWhenFocusTarget(false);
		return temp;
	}


	public static void main(String[] args){
		new NotebookFrame();
	}
}
