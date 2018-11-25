package filemanager;

import java.util.Scanner;

public class UI {

    private String command;
    private String path;
    private Scanner scan = new Scanner(System.in);

    public void mainMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("*************************************************************\n");
        sb.append("Dostupne komande su: \n");
        sb.append("LIST - pregled foldera\n").append("INFO - prikaz informacija o fajlovima/folderima \n").append("CREATE_DIR - kreiranje foldera\n").append("RENAME - preimenovanje fajlova/foldera\n");
        sb.append("COPY - kopiranje fajlova/foldera\n").append("MOVE - premestanje fajlova/foldera\n").append("DELETE - brisanje fajlova/foldera\n").append("SHOW COMMANDS - prikazivanje komandi koje mozete koristiti\n").append("EXIT - napustanje programa\n");
        sb.append("*************************************************************");
        System.out.println(sb);
    }

    public void userInput() {
        System.out.println("\n" + "Uneti zeljenu komandu: ");
        this.command = scan.nextLine();
        this.command = this.command.toUpperCase();
        switch (this.command) {
            case "LIST":
            case "INFO":
            case "CREATE_DIR":
            case "RENAME":
            case "COPY":
            case "MOVE":
            case "DELETE":
                System.out.println("Uneti zeljenu putanju: ");
                this.path = scan.nextLine();
                break;
            case "EXIT":
                System.out.println("Napustanje programa.");
                break;
            default:
                System.out.println("Uneta komanda nije prepoznata. Pokusajte ponovo.");
                break;
        }
    }

    public String getCommand() {
        return command;
    }

    public String getPath() {
        return path;
    }

    public Scanner getScan() {
        return scan;
    }
}
