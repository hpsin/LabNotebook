package View;

import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import Data.Entry;


/**
 * Allows the user to edit metadata and raw HTML of an {@link Entry}.
 * @author Hirsch Singhal
 */
public class EntryAdminDialog extends javax.swing.JDialog {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private Entry en;
	private boolean returnValue;
    /**
     * Creates new form EntryAdminDialog to edit the Entry en. 
     * @param en {@link Entry} to be edited.
     */
    public EntryAdminDialog(Entry en) {
    	this.setModal(true);
    	this.en=en;
        initComponents();
    }
    
	/**
	 * Displays the dialog and waits on the user to exit.
	 * @return User clicked OK to exit. 
	 */
	public boolean makeChoice() {
		this.setVisible(true);
		return returnValue;
	}
    
    private void initComponents() {

        OKButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        html_field = new javax.swing.JTextArea();
        titleLabel = new javax.swing.JLabel();
        serialLbl = new javax.swing.JLabel();
        timeLbl = new javax.swing.JLabel();
        title_field = new javax.swing.JTextField();
        time_field = new javax.swing.JTextField();
        serial_spinner = new javax.swing.JSpinner();
        list_scroll = new javax.swing.JScrollPane();
        time_list = new javax.swing.JList<>();
        deleteButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(500, 300));

        OKButton.setText("OK");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });

        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });
        
        jScrollPane1.setViewportView(html_field);
        html_field.setText(en.getHTML());
        html_field.setWrapStyleWord(true);

        titleLabel.setText("Entry Title");

        serialLbl.setText("Serial Number");
        serial_spinner.setValue(en.getSerial());

        timeLbl.setText("Timestamp");

        title_field.setText(en.getTitle());

        time_field.setText(en.getTimestampString());
        DefaultListModel<String> model = new DefaultListModel<String>();
        time_list.setModel(model);
        for(Date d : en.getEdits()){
        	model.addElement(sdf.format(d));
        }
        list_scroll.setViewportView(time_list);

        deleteButton.setText("Delete Edit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleLabel)
                            .addComponent(serialLbl))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(serial_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(timeLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(time_field))
                            .addComponent(title_field, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(OKButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(deleteButton, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(list_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(OKButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(titleLabel)
                            .addComponent(title_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(serialLbl)
                            .addComponent(serial_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeLbl)
                            .addComponent(time_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(list_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }                

    protected void DeleteActionPerformed(ActionEvent evt) {
    	int index = time_list.getSelectedIndex();
    	if(index>-1){
    		try {
				en.getEdits().remove(sdf.parse(((DefaultListModel<String>) time_list.getModel()).remove(index)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
	}

	private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {                                         
        en.setHTML(html_field.getText());
        en.setTitle(title_field.getText());
        en.setSerial((int)serial_spinner.getValue());
        try {
			en.setTimestamp(sdf.parse(time_field.getText()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        this.setVisible(false);
        returnValue=true;
        this.dispose();
	}                                        

    // Variables declaration - do not modify                     
    private javax.swing.JButton OKButton;
    private javax.swing.JButton deleteButton;
    private JTextArea html_field;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane list_scroll;
    private javax.swing.JLabel serialLbl;
    private javax.swing.JSpinner serial_spinner;
    private javax.swing.JLabel timeLbl;
    private javax.swing.JTextField time_field;
    private javax.swing.JList<String> time_list;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField title_field;
    // End of variables declaration                   

}
