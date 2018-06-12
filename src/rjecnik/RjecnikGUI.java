package rjecnik;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RjecnikGUI implements ActionListener{
	JFrame frame;
	JPanel northPanel;
	JButton searchButton;
	JTextField textField;
	JLabel label;
	JPanel centarPanel;
	JTextArea textArea;
	JScrollPane scrollPane;
	String stanjeRecenice;
	Map<String, String> mapaRijeci;
	public static void main(String[] args) {

		RjecnikGUI mojRjecnik = new RjecnikGUI();
		mojRjecnik.go();

	}

	public void go() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		northPanel = new JPanel();
		searchButton = new JButton("Pronaði");
		searchButton.addActionListener(this);
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(200, 24));
		label = new JLabel("Pronaði zanèenje reèenice");
		textArea = new JTextArea(26, 40);


		northPanel.add(label);
		northPanel.add(textField);
		northPanel.add(searchButton);

		//postavljanje vertikalnog scrollbara za textArea
		scrollPane = new JScrollPane(textArea);
		textArea.setLineWrap(true);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		centarPanel = new JPanel();
		centarPanel.add(scrollPane);

		frame.getContentPane().add(BorderLayout.NORTH, northPanel);
		frame.getContentPane().add(BorderLayout.CENTER, centarPanel);


		frame.setSize(500, 500);
		frame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// get input from text field
		textArea.setText(textField.getText());
		
		//map for saving words
		mapaRijeci = new HashMap<>();


		//---------BUILD XML FILE-----------------//

		//Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			//Build document
			Document document = builder.parse(new File("rjecnik.xml"));


			//Normalize the XML structure 
			document.getDocumentElement().normalize();

			//get all words
			NodeList nList = document.getElementsByTagName("rijec");

			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element wordElement = (Element) node;
					mapaRijeci.put((String) wordElement.getElementsByTagName("pojam").item(0).getTextContent(), (String) wordElement.getElementsByTagName("definicija").item(0).getTextContent());
				}
			} //end for loop for adding words in map
			
			
			//loop over map of words
			for (String name: mapaRijeci.keySet()) {
				
				String key = name.toString();
				String value = mapaRijeci.get(name);
				
				//checking if input field is empty
				if(!textField.getText().equals("")) { 
					
					//case insensitive
					String inputWord = textField.getText();
					inputWord = inputWord.substring(0, 1).toUpperCase() + inputWord.substring(1);
					
					//checking input value wit words in map
					if (inputWord.equals(key)) {
						textArea.setText(value);
						
						//if is word found then get out of loop
						break;
						
					} 
					else {
						textArea.setText("Definicija reèenice " +textField.getText() + " nije pronaðena unutar rijeènika");
					}
					
				} 
				else {
					textArea.setText("Molimo Vas upišite rijeè koja vas zanima");
				} 
			}

		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
