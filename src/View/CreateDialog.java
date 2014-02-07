package View;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JDialog;

import Data.Notebook;
import Data.Util;


/**
 * Used to create a new {@link Notebook} object.
 * @author Hirsch Singhal
 */
public class CreateDialog extends JDialog {

	private Notebook returnValue=null;

	/**
	 * Creates new form InitialDialog
	 * @param own Parent Frame
	 */
	public CreateDialog(Frame own) {
		super(own, "Create a Notebook", true);
		initComponents();
	}

	/**
	 * Displays the dialog and waits for the user to exit the dialog.
	 * @return created Notebook.  May be null if the user selects cancel.
	 */
	public Notebook getNotebook(){
		this.setVisible(true);
		return returnValue;
	}

	private void createButtonAction() {
		SecretKeySpec key=Util.makeKey(passField.getText(), titleField.getText());
		returnValue = new Notebook(new File(titleField.getText()+".nbk"), 
				titleField.getText(), 
				authField.getText(), 
				passCheckBox.isSelected(),
				key);
		setVisible(false);
		this.dispose();
	}
	private void cancelButtonAction(){
		returnValue=null;
		setVisible(false);
		this.dispose();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	private void initComponents() {

		titleLabel = new javax.swing.JLabel("Notebook Title");
		authLabel = new javax.swing.JLabel("Author(s)");
		titleField = new javax.swing.JTextField();
		authField = new javax.swing.JTextField();
		passCheckBox = new javax.swing.JCheckBox("Encrypt?");
		passField = new javax.swing.JPasswordField();
		passLabel = new javax.swing.JLabel("Password:");
		createButton = new javax.swing.JButton("Create");
		cancelButton = new javax.swing.JButton("Cancel");
		this.getRootPane().setDefaultButton(createButton);

		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createButtonAction();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButtonAction();
			}
		});

		//setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(passLabel)
												.addGap(55, 55, 55)
												.addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(passCheckBox)
												.addGroup(layout.createSequentialGroup()
														.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(titleLabel)
																.addComponent(authLabel))
																.addGap(18, 18, 18)
																.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(authField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
																		.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
																				.addComponent(cancelButton)
																				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																				.addComponent(createButton)))
																				.addContainerGap())
				);

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {titleField, authField});

		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(titleLabel)
								.addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(authLabel)
										.addComponent(authField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(passCheckBox)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(passLabel)
												.addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(createButton)
														.addComponent(cancelButton))
														.addContainerGap())
				);

		pack();
	}// </editor-fold>                        

	// Variables declaration - do not modify                     
	private javax.swing.JButton createButton;
	private javax.swing.JButton cancelButton;
	private javax.swing.JCheckBox passCheckBox;
	private javax.swing.JLabel titleLabel;
	private javax.swing.JLabel authLabel;
	private javax.swing.JLabel passLabel;
	private javax.swing.JTextField titleField;
	private javax.swing.JTextField authField;
	private javax.swing.JPasswordField passField;
	// End of variables declaration                   
}