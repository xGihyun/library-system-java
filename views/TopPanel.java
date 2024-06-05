package views;

import assets.Colors;
import entities.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class TopPanel {

	private User user;

	public TopPanel() {
		this.user = Session.getInstance().getUser();

		if (user != null) {
			createTopPanel();
		} else {
			System.out.println("User session is null");
		}
	}

	public JPanel createTopPanel() {
		JPanel userInfoPanel = new JPanel(new BorderLayout());
    userInfoPanel.setBackground(Colors.BASE);
    userInfoPanel.setBorder(new EmptyBorder(2, 10, 2, 10));

    JLabel userNameLabel = new JLabel(user.getFullName());
    userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
    userNameLabel.setForeground(Colors.TEXT);

    JLabel userAvatarLabel = new JLabel();
    ImageIcon avatarIcon;

    if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
      avatarIcon = new ImageIcon(getClass().getResource("../assets/images/avatars/" + user.getAvatarUrl()));
    } else {
      avatarIcon = new ImageIcon(getClass().getResource("../assets/images/default.jpg"));
    }

    Image img = avatarIcon.getImage();
    Image resizedImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
    avatarIcon = new ImageIcon(resizedImg);

    userAvatarLabel.setIcon(avatarIcon);

    // Adding components to the panel
    JPanel userDetailPanel = new JPanel();
    userDetailPanel.setBackground(Colors.BASE);
    userDetailPanel.add(userNameLabel);
    userDetailPanel.add(userAvatarLabel);

    userInfoPanel.add(userDetailPanel, BorderLayout.EAST);

    return userInfoPanel;
	}
}
