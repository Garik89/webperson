package ru.taipov.webperson.projectboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.taipov.webperson.projectboot.dao.PersonDAO;

import java.util.Scanner;

//import com.sun.org.apache.xerces.internal.utils.ConfigurationError;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {

        logger.info("Начало выполнения работы с базой данных");
        System.out.println("Введите идентификатор действия над базой данных: 1 - Создать, 2 - Прочитать, 3 - Обновить, 4 - Удалить");
        System.out.println();

        boolean flag = true;



        while (flag) {

        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();

        switch (action) {
            case "1":
                new PersonConsole(new PersonDAO()).createConsole();
                flag = false;
                break;

            case "2":
                new PersonConsole(new PersonDAO()).readConsole();
                flag = false;
                break;

            case "3":
                new PersonConsole(new PersonDAO()).updateConsole();
                flag = false;
                break;

            case "4":
                new PersonConsole(new PersonDAO()).deleteConsole();
                flag = false;
                break;

            default: {
                logger.error("Ошибка введения данных");
                System.out.println("Такой команды нет. Введите снова:");
            }
        }
    }


    }
}
