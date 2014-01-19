package View;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import Data.Entry;
import Data.Notebook;


public class NotebookFrame extends JFrame implements ActionListener {

	private JTabbedPane entries = new JTabbedPane();
	private Notebook currentNotebook;
	private JList<Entry> entryList;


	public NotebookFrame(){
		generateToolbar();
		generateLayout();
		setListeners();
		loadNotebook();
		fillEntryList();		
		//Maximize on open and display.
		//setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		//browseNotebook();

	}

	private void loadNotebook(){
		//Custom button text
		Object[] options = {"Load", "Create"};
		int n = JOptionPane.showOptionDialog(this,
				"Would you like to load or create a notebook?",
				"Init Dialog",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		if(n==0){
			JFileChooser jfc = new JFileChooser();
			jfc.setFileFilter(new FileFilter() {
				//Only accept .nbk files
				@Override
				public String getDescription() {
					return "Just Notebooks (*.nbk)";
				}

				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					if(f.getName().endsWith(".nbk")){
						return true;
					}
					return false;
				}
			});
			int m = jfc.showOpenDialog(this);
			if(m==JFileChooser.APPROVE_OPTION){
				loadNotebook( new Notebook(jfc.getSelectedFile()));
			} else loadNotebook();
		} else if (n==1){
			//m==1, create
			CreateDialog cd = new CreateDialog(this);
			loadNotebook( cd.getNotebook());
		}
	}

	private void loadNotebook(Notebook n){
		if(checkSaveAll() && n != null){
			currentNotebook = n;
			fillEntryList();
			for (Component d : entries.getComponents()) {
				if (d instanceof EntryTab) {	
					entries.remove(d);
				}
			}
		}
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

		entryList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				Rectangle r = entryList.getCellBounds(0, entryList.getLastVisibleIndex()); 
				if (r != null && r.contains(evt.getPoint())) { //Avoid double clicking empty space 
					if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
						int index = entryList.locationToIndex(evt.getPoint());
						addTab(entryList.getModel().getElementAt(index));
					}
				}
			}			
		});
		//TODO double click to open in list;		
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


	public void fillEntryList(){
		if(currentNotebook!= null){
			DefaultListModel<Entry> listModel = new DefaultListModel<>();
			for (Entry e : currentNotebook.getEntries()){
				listModel.addElement(e);
			}
			entryList.setModel(listModel);       
		}
	}

	private void generateLayout(){
		entryList = new JList<>();
		entryList.setPreferredSize(new java.awt.Dimension(150, 100));
		JScrollPane jScrollPane2 = new JScrollPane();
		jScrollPane2.setViewportView(entryList);


		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addComponent(entries, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
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

		file.add(makeMenuItem("Load Notebook", "load notebook",javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK)));

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
			newEntry();

		} else if ("load notebook".equals(cmd)) {
			loadNotebook();

		} else if ("save all".equals(cmd)) {
			for (Component d : entries.getComponents()) {
				if (d instanceof EntryTab) {
					((EntryTab) d).save();
				}
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
			} else
				temp.actionPerformed(e);
		}
	}

	private void newEntry() {
		String entryTitle = (String) JOptionPane.showInputDialog(new Frame(), 
				"Please name the entry:", 
				"New Entry",
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				"Insert Name Here");
		if(entryTitle==null)return;
		Entry en = currentNotebook.newEntry(entryTitle);
		addTab(en);
	}
	
	private void addTab(Entry en){
		EntryTab temp = new EntryTab(en);
		entries.add(temp);
		//((DefaultListModel<Entry>)entryList.getModel()).addElement(en);
		fillEntryList();
		entries.setSelectedComponent(temp);
		int tabLocation = entries.getSelectedIndex();
		entries.setTabComponentAt(tabLocation, new ButtonTabComponent(
				entries, temp, this));
		temp.setRequestFocusEnabled(false);
		temp.setVerifyInputWhenFocusTarget(false);
	}

	public static void main(String[] args){
		new NotebookFrame();
	}
}
