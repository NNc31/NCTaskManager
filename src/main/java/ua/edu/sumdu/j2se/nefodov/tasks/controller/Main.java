package ua.edu.sumdu.j2se.nefodov.tasks.controller;

import ua.edu.sumdu.j2se.nefodov.tasks.view.View;

import javax.swing.*;

public class Main {

	//public static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) {

		Controller controller = new Controller();
		controller.loadList(controller.getFile()); // завантаження списку задач
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			controller.saveList(controller.getFile()); // збереження списку задач автоматично
		}));


		View view = new View(controller); // створення відображення
		controller.setView(view);
		SwingUtilities.invokeLater(() -> controller.launchOperation(Operations.MAIN_MENU)); // запуск менюf
		controller.setTimers();
	}
}
