package View;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import Data.Entry;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

public class EntryTab extends JPanel{

	private JScrollPane wys;
	private HTMLEditorPane html;
	private boolean saved = false; 
	private Entry en;
	private static Color[] editRotation = {Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GREEN, Color.RED};
	
		public EntryTab(Entry en){
			this.en = en;
			html = new HTMLEditorPane();
			JToolBar entryToolBar = (JToolBar)html.getComponent(0);
			wys = (JScrollPane)((JTabbedPane)html.getComponent(1)).getComponent(0);
			generateLayout(entryToolBar);
	        
			if(en.getHTML()!=null)html.setText(en.getHTML());
			this.setName(en.getTitle());
			setEditor(en.getEditCount()); 
			
		}
		
		private void setEditor(int editCount) {
			Color toBe;
			if(editCount == 0){
				
			}
		}

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
		
		public String getSource(){
			return html.getText();
		}
		
		public String getSourceBase64(){
			//TODO get Base64 lib
			return "STUB";
		}

		public String getTitle() {
			return this.getName();
		}

		public boolean save() {
			//if(!saved){
				en.setHTML(html.getText());
				System.out.println(html.getText());
				saved = en.saveXML();
//				setEditor(en.getEditCount());
		//	}
			return saved;
		}

		public boolean isChanged() {
			return saved;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("save entry")){
				save();
			}else{
				System.out.println(e.getActionCommand());
			}
		}
}
