package View;

import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;

import Data.DataStorage;
import Data.Entry;
import Data.Notebook;
import Data.PDFWrapper;

/**
 * Provides an administrative interface for the user to alter metadata 
 * concerning the Notebook.
 * @author Hirsch Singhal
 */
public class NotebookSettings extends javax.swing.JDialog {
	private Notebook note;
	private boolean returnValue;
    /**
     * Creates new form NotebookSettings
     * @param n Notebook to be edited.
     */
    public NotebookSettings(Notebook n) {
    	this.setModal(true);
    	note=n;
        initComponents();
    }
    
    /**
     * Displays the dialog and waits for the user to make a choice. 
     * @return true if the user selects OK. 
     */
    public boolean makeChoice(){
    	this.setVisible(true);
    	return returnValue;
    }
                     
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        title_field = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        data_list = new javax.swing.JList<DataStorage>();
        deleteButton = new javax.swing.JButton();
        OKButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(325, 250));

        jLabel1.setText("Notebook Title");

        title_field.setText(note.getTitle());

        DefaultListModel<DataStorage> dlm = new DefaultListModel<DataStorage>();
        for(PDFWrapper p : note.getPDFs()){
        	dlm.addElement(p);
        }
        for(Entry e : note.getEntries()){
        	dlm.addElement(e);
        }
        data_list.setModel(dlm);
        
        jScrollPane1.setViewportView(data_list);

        OKButton.setText("OK");

        deleteButton.setText("Delete Entry/PDF");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });
        
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(title_field, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(OKButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(title_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OKButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    protected void deleteActionPerformed(ActionEvent evt) {
    	int index = data_list.getSelectedIndex();
    	if(index>-1){
    		note.removeData(((DefaultListModel<DataStorage>) data_list.getModel()).remove(index));
    	}
	}

	private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {
        note.setTitle(title_field.getText());
        this.setVisible(false);
        returnValue=true;
        this.dispose();
	}   


    // Variables declaration - do not modify                     
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton OKButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<DataStorage> data_list;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField title_field;
    // End of variables declaration                   
}

