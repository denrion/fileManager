package filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

public class FileManager {

    Scanner scan = new Scanner(System.in);

    public void List(File path) {
        File[] list = path.listFiles();
        if (list != null && list.length > 0) {
            try {
                for (File element : list) {
                    System.out.println(element.getName());
                }
            } catch (Exception e) {
                System.out.println("Doslo je do greske: " + e);
            }
        } else {
            System.out.println("Folder je prazan.");
        }
    }

    public void Info(File path) {
        if (Objects.requireNonNull(path.listFiles()).length > 0) {
            File[] elements = path.listFiles();
            BasicFileAttributes bfa;
            DecimalFormat df = new DecimalFormat("#.##");

            if (elements != null) {
                for (File element : elements) {
                    try {
                        bfa = Files.readAttributes(element.toPath(), BasicFileAttributes.class);
                        FileTime ct = bfa.creationTime();
                        Instant instant = ct.toInstant();
                        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                        Instant instantLM = Instant.ofEpochMilli(element.lastModified());
                        LocalDateTime dateTimeLM = LocalDateTime.ofInstant(instantLM, ZoneId.systemDefault());
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd. MMM yyyy. HH:mm:ss");

                        System.out.printf("+----------------------------------------+------------------------------------------------------------------------------------------------------------------+\n");
                        System.out.printf("| Ime:                                   | " + element.getName() + "\n");
                        System.out.printf("+----------------------------------------+------------------------------------------------------------------------------------------------------------------|\n|");
                        System.out.printf("| Apsolutna putanja:                     | " + element.getAbsolutePath() + "\n");
                        System.out.printf("+----------------------------------------|------------------------------------------------------------------------------------------------------------------|\n");
                        if (element.length() < 1048576) {
                            System.out.printf("| Velicina:                              | " + df.format((double) element.length() / 1024) + "KB\n");
                        } else {
                            System.out.printf("| Velicina:                              | " + df.format((double) element.length() / 1024 / 1024 + "MB\n"));
                        }
                        System.out.printf("+----------------------------------------|------------------------------------------------------------------------------------------------------------------+\n");
                        System.out.printf("| Datum kreiranja:                       | " + dtf.format(dateTime) + "\n");
                        System.out.printf("+-----------------------------------------------------------------------------------------------------------------------------------------------------------+\n");
                        System.out.printf("| Datum poslednje izmene                 | " + dtf.format(dateTimeLM) + "\n");
                        System.out.printf("+----------------------------------------|------------------------------------------------------------------------------------------------------------------+\n");

                    } catch (Exception ex) {
                        System.out.println("Doslo je do greske: " + ex);
                    }
                }
            }
        } else {
            System.out.println("Folder je prazan.");
        }
    }

    public void CreateDir(File path) {
        System.out.println("Uneti naziv novog foldera: ");
        String newFolderInput = this.scan.nextLine();

        if (newFolderInput.contains("\\") || newFolderInput.contains("/") || newFolderInput.contains(":") || newFolderInput.contains("?") || newFolderInput.contains("*") || newFolderInput.contains("<")
                || newFolderInput.contains(">") || newFolderInput.contains("|")) {
            System.out.println("Naziv foldera ne sme sadrzati sledece karaktere: \\ / : ? * < > |");
        } else {
            File newFolder = new File(path + "\\" + newFolderInput);
            try {
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                    System.out.println("Novi folder sa nazivom " + newFolder.getName() + " je uspesno kreiran.");
                } else {
                    System.out.println("Folder sa navedenim imenom vec postoji.");
                }
            } catch (Exception e) {
                System.out.println("Kreiranje foldera " + newFolder.getName() + " nije uspesno izvrseno. ");
            }
        }
    }

    public void Rename(File path) {
        System.out.println("Uneti ime fajla/foldera koji zelite da preimenujete: ");
        String oldName = this.scan.nextLine();
        File oldFile = new File(path + "\\" + oldName);

        if (oldFile.exists()) {
            try {
                System.out.println("Uneti novo ime: ");
                String newName = this.scan.nextLine();
                if (newName.contains("\\") || newName.contains("/") || newName.contains(":") || newName.contains("?") || newName.contains("*") || newName.contains("<") || newName.contains(">")
                        || newName.contains("|")) {
                    System.out.println("Naziv ne sme sadrzati sledece karaktere: \\ / : ? * < > |");
                } else {
                    File newFile = new File(path + "\\" + newName);
                    if (oldName.equals(newName)) {
                        System.out.println("Novo ime je isto kao i staro ime. ");
                    } else {
                        if (newFile.exists()) {
                            System.out.println("Fajl/Folder sa navedenim imenom vec postoji. ");
                        }
                        if (oldFile.renameTo(newFile)) {
                            System.out.println("Preimenovanje je uspesno izvrseno. ");
                        } else {
                            System.out.println("Premenovanje nije uspesno izvrseno. ");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Doslo je do greske: " + e);
            }
        } else {
            System.out.println("Fajl/Folder sa navedenim imenom ne postoji");
        }
    }

    private void copyDir(File f1, File f2) {
        if (f1.isDirectory()) {
            if (!f2.exists()) {
                f2.mkdir();
            }
            String[] fs = f1.list();
            for (String f : fs) {
                copyDir(new File(f1, f), new File(f2, f));
            }
        }
    }

    public void Copy(File path) {
        System.out.println("Uneti ime fajla/foldera koji zelite da kopirate: ");
        String originalFileName = this.scan.nextLine();
        File originalFile = new File(path + "\\" + originalFileName);

        if (originalFile.exists()) {
            try {
                if (originalFile.isFile()) {
                    System.out.println("Uneti lokaciju gde zelite da kopirate fajl: ");
                    String copyFileDest = this.scan.nextLine();
                    File destination = new File(copyFileDest);

                    File copiedFile = new File(copyFileDest + "\\" + originalFileName);

                    if (!copiedFile.exists() && destination.isDirectory()) {
                        try (FileInputStream inStream = new FileInputStream(originalFile);
                                FileOutputStream outStream = new FileOutputStream(copiedFile);) {
                            byte[] buffer = new byte[1024];
                            int lenght;

                            while ((lenght = inStream.read(buffer)) > 0) {
                                outStream.write(buffer, 0, lenght);
                            }
                            System.out.println("Fajl je uspesno kopiran. ");
                        } catch (Exception ex) {
                            System.out.println("Doslo je do greske: " + ex);
                        }
                    } else if (copiedFile.exists() && destination.isDirectory()) {
                        System.out.println("Fajl sa navedenim imenom vec postoji. ");
                    } else {
                        System.out.println("Unesena putanja ne postoji. Morate prvo kreirati folder. ");
                    }
                } else if (originalFile.isDirectory()) {
                    System.out.println("Uneti lokaciju gde zelite da kopirate folder: ");
                    String copyFileDest = this.scan.nextLine();
                    File destination = new File(copyFileDest);
                    File toCopyFile = new File(copyFileDest + "\\" + originalFileName);

                    if (!toCopyFile.exists() && destination.isDirectory()) {
                        this.copyDir(originalFile, toCopyFile);
                        System.out.println("Folder je uspesno kopiran. ");
                    } else if (toCopyFile.exists() && destination.isDirectory()) {
                        System.out.println("Folder sa navedenim imenom vec postoji");
                    } else {
                        System.out.println("Unesena putanja ne postoji. Morate prvo kreirati folder. ");
                    }
                }
            } catch (Exception e) {
                System.out.println("Doslo je do greske: " + e);
            }
        } else {
            System.out.println("Fajl/Folder sa navedenim imenom ne postoji na izabranoj putanji. ");
        }
    }

    public void Move(File path) {
        System.out.println("Uneti ime fajla/foldera koji zelite  da premestite: ");
        String toMoveFileName = this.scan.nextLine();
        File toMoveFile = new File(path + "\\" + toMoveFileName);

        if (toMoveFile.exists()) {
            try {
                if (toMoveFile.isFile() || toMoveFile.isDirectory()) {
                    System.out.println("Uneti lokaciju gde zelite da premestite fajl/folder: ");
                    String toMoveFileDest = this.scan.nextLine();
                    File destination = new File(toMoveFileDest);
                    File movedFile = new File(toMoveFileDest + "\\" + toMoveFileName);

                    if (!movedFile.exists() && destination.isDirectory()) {
                        toMoveFile.renameTo(movedFile);
                        System.out.println("Premestanje je uspesno kompetirano. ");
                    } else if (movedFile.exists() && destination.isDirectory()) {
                        System.out.println("Fajl/Folder sa navedenim imenom vec postoji.");
                    } else {
                        System.out.println("Unesena putanja ne postoji.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Doslo je do greske: " + e);
            }
        } else {
            System.out.println("Fajl/Folder sa navedenim imenom ne postoji na izabranoj lokaciji. ");
        }
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    public void Delete(File path) {
        System.out.println("Uneti ime fajla/foldera koji zelite da obrisete: ");
        String toDeleteFileName = this.scan.nextLine();
        File toDeleteFile = new File(path + "\\" + toDeleteFileName);

        if (toDeleteFile.exists()) {
            try {
                if (toDeleteFile.isFile()) {
                    toDeleteFile.delete();
                    System.out.println("Fajl je uspesno obrisan. ");
                } else if (toDeleteFile.isDirectory()) {
                    this.deleteDir(toDeleteFile);
                    System.out.println("Folder je uspesno obrisan. ");
                }
            } catch (Exception e) {
                System.out.println("Doslo je do greske: " + e);
            }
        } else {
            System.out.println(toDeleteFile.getName() + " nije moguce obrisati zato sto ne postoji. ");
        }
    }
}
