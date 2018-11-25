package filemanager;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        UI ui = new UI();
        FileManager fm = new FileManager();
        ui.mainMenu();

        boolean output = false;
        while (!output) {
            ui.userInput();
            switch (ui.getCommand()) {
                case "LIST":
                case "INFO":
                case "CREATE_DIR":
                case "RENAME":
                case "COPY":
                case "MOVE":
                case "DELETE":
                    File path = new File(ui.getPath());
                    if (path.exists() && path.isAbsolute() && path.isDirectory()) {
                        switch (ui.getCommand()) {
                            case "LIST":
                                fm.List(path);
                                break;
                            case "INFO":
                                fm.Info(path);
                                break;
                            case "CREATE_DIR":
                                fm.CreateDir(path);
                                break;
                            case "RENAME":
                                fm.Rename(path);
                                break;
                            case "COPY":
                                fm.Copy(path);
                                break;
                            case "MOVE":
                                fm.Move(path);
                                break;
                            case "DELETE":
                                fm.Delete(path);
                                break;
                        }
                    } else {
                        System.out.println("Izabrana putanja ne postoji. Pokusajte ponovo.");
                    }
                    break;
                case "SHOW COMMANDS":
                    ui.mainMenu();
                    break;
                case "EXIT":
                    output = true;
                    break;
            }
        }
    }
}
