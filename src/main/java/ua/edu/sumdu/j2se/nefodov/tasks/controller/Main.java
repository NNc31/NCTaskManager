package ua.edu.sumdu.j2se.nefodov.tasks.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.edu.sumdu.j2se.nefodov.tasks.view.NotificationView;
import ua.edu.sumdu.j2se.nefodov.tasks.view.UserView;



public class Main {

	//public static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) {

		UserController userController = new UserController();
		userController.loadList(userController.loadFile()); // завантаження списку задач

		UserView userView = new UserView(userController); // створення відображення
		userController.setView(userView);
		userController.launchOperation(UserOperations.MAIN_MENU); // запуск нового меню

		//NotificationController notificationController = new NotificationController();
		//notificationController.setTaskList(userController.getTaskList());
		//NotificationView notificationView = new NotificationView(notificationController);
		//notificationController.setView(notificationView);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                userController.saveList(userController.loadFile()); // збереження списку задач
            }
        }));
	}
}
