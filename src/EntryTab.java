import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

public class EntryTab extends JPanel{

	private JScrollPane wys;
	private HTMLEditorPane html;
	private boolean saved = false; 
	
		public EntryTab(){
			
			html = new HTMLEditorPane();
			JToolBar entryToolBar = (JToolBar)html.getComponent(0);
			wys = (JScrollPane)((JTabbedPane)html.getComponent(1)).getComponent(0);
			generateLayout(entryToolBar);
	        
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
			return "STUB";
		}

		public String getTitle() {
			// TODO Auto-generated method stub
			return "TABNAME";
		}

		public boolean save() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isChanged() {
			return saved;
		}

		public void saveAs() {
			// TODO Auto-generated method stub
			
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void load(File f) {
			// TODO Auto-generated method stub
			
		}


	
}
