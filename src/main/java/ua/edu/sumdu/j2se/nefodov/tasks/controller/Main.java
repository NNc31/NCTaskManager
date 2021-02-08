package ua.edu.sumdu.j2se.nefodov.tasks.controller;

import ua.edu.sumdu.j2se.nefodov.tasks.view.View;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.loadList(controller.getFile()); // завантаження списку задач

		// автоматичне збереження задач при завершенні роботи програми
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			controller.saveList(controller.getFile());
		}));

		View view = new View(controller);
		controller.setView(view);
		controller.setTimers(); // запуск таймерів для повідомлень
		SwingUtilities.invokeLater(() -> controller.launchOperation(Operations.MAIN_MENU)); // запуск меню
	}
}
