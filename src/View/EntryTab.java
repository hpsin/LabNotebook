package View;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import Data.DataStorage;
import Data.Entry;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

/**
 * @author Hirsch Singhal
 *	Used to display an {@link Entry}.  Holds an {@link HTMLEditorPane} and 
 *	provides editing toolbars.
 */
public class EntryTab extends JPanel implements DataRepresentation{

	private JScrollPane wys;
	private HTMLEditorPane html;
	private boolean saved = false; 
	private Entry en;
	private static Color[] editRotation = {Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GREEN, Color.RED};
	
		/**
		 * Creates an EntryTab using an {@link Entry} object.  Also initializes
		 * the {@link HTMLEditorPane} and toolbar. First call to this method is 
		 * expensive as it spins up the entirety of {@link HTMLEditorPane}   
		 * @param en Entry to be displayed.
		 */
		public EntryTab(Entry en){
			this.en = en;
			html = new HTMLEditorPane();
			JToolBar entryToolBar = (JToolBar)html.getComponent(0);
			wys = (JScrollPane)((JTabbedPane)html.getComponent(1)).getComponent(0);
			generateLayout(entryToolBar);
	        
			if(en.getHTML()!=null)html.setText(en.getHTML());
			this.setName(en.getTitle());
//			setEditor(en.getEditCount()); 
			
		}
		
//		private void setEditor(int editCount) {
//			Color toBe;
//			if(editCount == 0){
//				
//			}
//		}

		private void generateLayout(JToolBar entryToolBar){
			javax.swing.GroupLayout entryLayout = new javax.swing.GroupLayout(this);
	        this.setLayout(entryLayout);
	        entryLayout.setHorizontalGroup(
	            entryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(entryToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	            .addComponent(wys, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
	        );
	        entryLayout.setVerticalGroup(
	            entryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(entryLayout.createSequentialGroup()
	                .addComponent(entryToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(wys, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE))
	        );
		}
		
		/**
		 * Returns the HTML represetnation of the {@link HTMLEditorPane}'s 
		 * contents. Does NOT return the text associated with the {@link Entry} 
		 * neccesarily.    
		 * @return Currently displayed HTML.
		 */
		public String getSource(){
			return html.getText();
		}
		
		/**
		 * @return The title of this {@link EntryTab}.
		 */
		public String getTitle() {
			return this.getName();
		}

		/**
		 * Forces the associated Entry to save itself using the HTML currently
		 * being displayed. Forces a write to the harddrive. 
		 * @return Success or failure of the save operation. 
		 */
		public boolean save() {
			//if(!saved){
				en.setHTML(html.getText());
				saved = en.saveXML();
//				setEditor(en.getEditCount());
		//	}
			return saved;
		}

		/**
		 * Used to track whether or not the HTML has changed since last save. 
		 * @return If this needs to be saved. 
		 */
		public boolean isChanged() {
			return saved;
		}
		
		/**
		 * Acts on certain {@link ActionEvent} commands, currently only 
		 * "save entry".  If a command is not recognized it is printed to 
		 * System.out
		 * @param e Command to be acted on.
		 */
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("save entry")){
				save();
			}else{
				System.out.println(e.getActionCommand());
			}
		}

		/**
		 * Used to get the underlying {@link Entry} object.
		 * @return Associated Entry.
		 */
		public Entry getEntry() {
			return en;
		}

		/* (non-Javadoc)
		 * @see View.DataRepresentation#getData()
		 */
		@Override
		public DataStorage getData() {
			return en;
		}
}
