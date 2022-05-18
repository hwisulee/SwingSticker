package face_editor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent; 
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter; 

public class editor extends JFrame{
	
	Container c = getContentPane();
	
	JFileChooser jfc = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
	
	private String[] stickers = {"None", "sticker1", "sticker2", "sticker3", "sticker4"};
	private String file;
	private String[] filepaths = {
			"img/imgnull.png",
			"img/sticker1.png",
			"img/sticker2.png",
			"img/sticker3.png",
			"img/sticker4.png"
		};
	
	private JPanel ImgPanel = new JPanel();
	private JPanel SetPanel = new JPanel();
	private JLabel faceLabel = new JLabel();
	private JComboBox<String> strCombo = new JComboBox<String>(stickers);
	private JScrollPane scrollbar = new JScrollPane();
	
	private BufferedImage bgimg;
	private BufferedImage mgimg;
		
	private int temp;
	private int save = 0;
	private int overnum = 0;
	private int x, y, w, h;
	private int imgW, imgH;
	
	public editor() {
		Menu();
		Panel();
		Image();
		
		setTitle("Face editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(935, 970);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void Menu() {
		JMenuBar mb = new JMenuBar();
		
		JMenu fileMenu = new JMenu("Menu");
		
		JMenuItem fm1 = new JMenuItem("Load Image");
		JMenuItem fm2 = new JMenuItem("Save Image");
		JMenuItem fm3 = new JMenuItem("Exit");
		
		fm1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					jfc.setFileFilter(filter);
					
					if(filter != null) {
						throw new Exception();
					}
				} catch(Exception e) {
					int ret = jfc.showOpenDialog(null);
					if(ret != JFileChooser.APPROVE_OPTION) {
						JOptionPane.showMessageDialog(null, "Cancel image import", "Cancel", JOptionPane.WARNING_MESSAGE);
						setLocationRelativeTo(null);
						return;
					}
					else if(ret == JFileChooser.APPROVE_OPTION) {
						String filepath = jfc.getSelectedFile().getPath();
						file = filepath;
					}
				}
			}
		});
		fm2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(file != null) {
					save = 1;
					Image();
				}
				else {
					JOptionPane.showMessageDialog(null, "Please load the image first", "Error", JOptionPane.WARNING_MESSAGE);
					setLocationRelativeTo(null);
				}
			}
		});
		fm3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		mb.add(fileMenu);
		
		fileMenu.add(fm1);
		fileMenu.add(fm2);
		fileMenu.addSeparator();
		fileMenu.add(fm3);
		
		this.setJMenuBar(mb);
	}
	
	public void Panel() {
		c.setLayout(null);
		
		ImgPanel.setBounds(10, 10, 600, 900);

		JScrollPane scrollPane = new JScrollPane(faceLabel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		ImgPanel.add(faceLabel);
		ImgPanel.add(scrollPane);
		
		strCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(file != null) {
					JComboBox<String> cb = (JComboBox<String>)e.getSource();
					int index = cb.getSelectedIndex();
					temp = index;
					Image();
				}
				else {
					JComboBox<String> cb = (JComboBox<String>)e.getSource();
					JOptionPane.showMessageDialog(null, "Please load the image first", "Error", JOptionPane.WARNING_MESSAGE);
					setLocationRelativeTo(null);
					cb.setSelectedIndex(0);
				}
			}
		});
		
		SetPanel.setLayout(null);
		SetPanel.setBounds(620, 10, 300, 900);
		SetPanel.setBackground(Color.white);
		SetPanel.setBorder(new LineBorder(Color.black, 1, true));
		
		JLabel str_label = new JLabel("Sticker : ");
		JLabel position_label = new JLabel("Sticker's Position");
		JLabel x_label = new JLabel("X axis : ");
		JLabel y_label = new JLabel("Y axis : ");
		JLabel size_label = new JLabel("Sticker's Size");
		JLabel w_label = new JLabel("Width : ");
		JLabel h_label = new JLabel("Height : ");
		
		JTextField x_text = new JTextField("0");
		JTextField y_text = new JTextField("0");
		JTextField w_text = new JTextField("0");
		JTextField h_text = new JTextField("0");
		
		JButton p_btn = new JButton("set");
		JButton s_btn = new JButton("set");
		
		p_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(file != null) {
					x = Integer.parseInt(x_text.getText());
					y = Integer.parseInt(y_text.getText());
					
					if(x < imgW && x > 0 && y < imgH && y > 0) {
						Image();
					}
					else {
						JOptionPane.showMessageDialog(null, "You cannot specify outside the size of the image.", "Error", JOptionPane.WARNING_MESSAGE);
						setLocationRelativeTo(null);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Please load the image first", "Error", JOptionPane.WARNING_MESSAGE);
					setLocationRelativeTo(null);
					x_text.setText("0");
					y_text.setText("0");
				}
			}
		});
		
		s_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(file != null) {
					w = Integer.parseInt(w_text.getText());
					h = Integer.parseInt(h_text.getText());
					
					if(w < imgW && w > 0 && h < imgH && h > 0) {
						Image();
					}
					else if(w >= imgW || h >= imgH) {
						JOptionPane.showMessageDialog(null, "You cannot specify outside the size of the image.", "Error", JOptionPane.WARNING_MESSAGE);
						setLocationRelativeTo(null);
					}
					else {
						JOptionPane.showMessageDialog(null, "You cannot specify outside the size of the image.", "Error", JOptionPane.WARNING_MESSAGE);
						setLocationRelativeTo(null);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Please load the image first", "Error", JOptionPane.WARNING_MESSAGE);
					setLocationRelativeTo(null);
					w_text.setText("0");
					h_text.setText("0");
				}
			}
		});
		
		str_label.setBounds(10, 10, 150, 30);
		strCombo.setBounds(140, 10, 150, 30);
		
		position_label.setBounds(10, 50, 100, 30);
		x_label.setBounds(30, 78, 50, 30);
		y_label.setBounds(130, 78, 50, 30);
		x_text.setBounds(70, 85, 50, 20);
		y_text.setBounds(170, 85, 50, 20);
		p_btn.setBounds(230, 85, 60, 20);
		
		size_label.setBounds(10, 130, 100, 30);
		w_label.setBounds(30, 158, 50, 30);
		h_label.setBounds(130, 158, 50, 30);
		w_text.setBounds(70, 165, 50, 20);
		h_text.setBounds(170, 165, 50, 20);
		s_btn.setBounds(230, 165, 60, 20);
		
		SetPanel.add(str_label);
		SetPanel.add(strCombo);
		SetPanel.add(position_label);
		SetPanel.add(x_label);
		SetPanel.add(y_label);
		SetPanel.add(x_text);
		SetPanel.add(y_text);
		SetPanel.add(p_btn);
		SetPanel.add(size_label);
		SetPanel.add(w_label);
		SetPanel.add(h_label);
		SetPanel.add(w_text);
		SetPanel.add(h_text);
		SetPanel.add(s_btn);
		
		scrollPane.setViewportView(faceLabel);
		c.add(ImgPanel);
		c.add(SetPanel);
	}
	
	public void Image() {
		if(temp != 0 && file != null) {
			try {
				bgimg = ImageIO.read(new File(file));
				mgimg = ImageIO.read(new File(filepaths[temp]));
				
				if (bgimg.getWidth() > mgimg.getWidth() && bgimg.getHeight() > mgimg.getHeight()) {
					imgW = bgimg.getWidth();
					imgH = bgimg.getHeight();
					
					BufferedImage overlay = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = overlay.createGraphics();
					g.drawImage(bgimg, 0, 0, null);
					g.drawImage(mgimg,  x, y, w, h, null);
					g.dispose();
					
					faceLabel.setIcon(new ImageIcon(overlay));
					
					if(save == 1) {
						String home = System.getProperty("user.home");
						String desktop = home + "/Desktop/";
						File savepath = new File(desktop + "overlayedImg_" + overnum + ".png");
						
						try {
							boolean isExists = savepath.exists();
							
							if(isExists) {
								overnum += 1;
								File resavepath = new File(desktop + "overlayedImg_" + overnum + ".png");
							}
							else {
								ImageIO.write(overlay, "PNG", savepath);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						save = 0;
						return;
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Please load the image first", "Error", JOptionPane.WARNING_MESSAGE);
					setLocationRelativeTo(null);
				}
			} catch(IOException ie) {
				ie.printStackTrace();
			}

		}
		else {
			faceLabel.setIcon(new ImageIcon(filepaths[0]));
		}
	}

	public static void main(String[] args) {  
		new editor();
	}
}
