package views;

import assets.Colors;
import auth.Login;
import entities.Session;
import entities.User;

import javax.swing.*;

import admin.BookList;
import admin.BookReturn;
import admin.BorrowerList;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Sidebar extends JPanel {
	private Connection conn;
	private JFrame parentFrame;
	private User user;

	public Sidebar(Connection conn, JFrame parentFrame) {
		this.user = Session.getInstance().getUser();
		this.conn = conn;
		this.parentFrame = parentFrame;

		if (user != null) {
			initializeSidebar();
		} else {
			System.out.println("User session is null");
		}
	}

	private void initializeSidebar() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Colors.BASE);
		setPreferredSize(new Dimension(200, 768));

		addLogoButton();

		addButton("Book List", e -> navigateTo(BookList.class));

		if (user.getRole().equalsIgnoreCase("admin")) {
			addButton("Borrower List", e -> navigateTo(BorrowerList.class));
		}

		addButton("Book Return", e -> navigateTo(BookReturn.class));

		// Placeholder panel to push buttons to the top
		JPanel spacerPanel = new JPanel();
		spacerPanel.setBackground(Colors.BASE);
		spacerPanel.setMinimumSize(new Dimension(100, Integer.MAX_VALUE));
		add(spacerPanel);

		JButton button = new JButton("Logout");
		button.setMaximumSize(new Dimension(180, 50));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setFont(new Font("Arial", Font.BOLD, 14));

		button.setBackground(Colors.RED);
		button.setForeground(Colors.BASE);

		button.addActionListener(e -> logout());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(button);
	}

	private void logout() {
		Session.deleteInstance();
		parentFrame.dispose();

		new Login(conn);
	}

	private void addLogoButton() {
		JButton logoButton = new JButton("The Archives");
		logoButton.setMaximumSize(new Dimension(180, 50));
		logoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		logoButton.setFont(new Font("Arial", Font.BOLD, 28));
		logoButton.setBackground(Colors.BASE);
		logoButton.setForeground(Colors.TEXT);
		logoButton.setBorder(BorderFactory.createEmptyBorder());
		logoButton.setEnabled(false);

		logoButton.setMargin(new Insets(10, 10, 10, 10));

		add(logoButton);
		add(Box.createRigidArea(new Dimension(0, 10))); // Add space between logo and buttons
	}

	private void addButton(String label, ActionListener actionListener) {
		JButton button = new JButton(label);
		button.setMaximumSize(new Dimension(180, 50));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setFont(new Font("Arial", Font.BOLD, 14));

		// Check if the button label matches the current frame's title
		if (parentFrame.getTitle().equalsIgnoreCase(label)) {
			button.setBackground(Colors.BLUE);
			button.setForeground(Colors.BASE);
		} else {
			button.setBackground(Colors.BASE);
			button.setForeground(Colors.TEXT);
		}

		button.addActionListener(actionListener);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(button);
	}

	private void navigateTo(Class<? extends JFrame> frameClass) {
		SwingUtilities.invokeLater(() -> {
			try {
				JFrame frame = frameClass.getDeclaredConstructor(Connection.class).newInstance(conn);
				frame.setVisible(true);
				parentFrame.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
