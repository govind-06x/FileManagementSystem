import java.io.*;
import java.util.Scanner;

public class TextFileManager {

    public void Start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Choose");
            System.out.println("1. Create a File \t2. Delete a File");
            System.out.println("3. Search a File \t4. Modify File");
            System.out.println("5. Print data \t\t6. Copy File");
            System.out.println("7. Move File \t\t8. Rename File");
            System.out.println("9. Exit");
            System.out.print("Your Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 9) {
                System.out.print("Exiting system, please wait");
                exit();
                System.out.println("Thank You!");
                break;
            }

            System.out.print("Enter Path (without file): ");
            String path = sc.nextLine().trim();

            System.out.print("Enter file name with extension: ");
            String filename = sc.nextLine().trim();

            if (path.isEmpty() || filename.isEmpty()) {
                System.out.println("Path or filename cannot be empty!");
                System.out.println();
                continue;
            }
            File dir = new File(path);
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    System.out.println("Directory created: " + path);
                } else {
                    System.out.println("Failed to create directory: " + path);
                    System.out.println();
                    continue;
                }
            }
            File file = new File(path + File.separator + filename);

            try {
                switch (choice) {
                    case 1:
                        if (file.createNewFile()) {
                            System.out.println("File Created Successfully!");
                            System.out.print("Do you want to add text (y/n): ");
                            String c = sc.nextLine();
                            if (c.equalsIgnoreCase("y")) {
                                modify(file, sc, true);
                            }
                        } else {
                            System.out.println("File already exists or there was an issue creating the file.");
                        }
                        break;

                    case 2:
                        if (file.delete()) {
                            System.out.println("File deleted successfully.");
                        } else {
                            System.out.println("Failed to delete the file or file does not exist.");
                        }
                        break;

                    case 3:
                        if (isAvailable(file)) {
                            System.out.println("File Exists");
                        } else {
                            System.out.println("File Doesn't Exist");
                        }
                        break;

                    case 4:
                        if (isAvailable(file)) {
                            modify(file, sc, false);
                        } else {
                            System.out.println("File doesn't exist.");
                        }
                        break;

                    case 5:
                        if (isAvailable(file)) {
                            printdata(file);
                        } else {
                            System.out.println("File doesn't exist or error in reading data");
                        }
                        break;

                    case 6:
                        System.out.print("Enter new Path : ");
                        String newpath = sc.nextLine().trim();
                        change(path, newpath, file, true);
                        break;

                    case 7:
                        System.out.print("Enter new Path : ");
                        String newpath1 = sc.nextLine().trim();
                        change(path, newpath1, file, false);
                        break;

                    case 8:
                        System.out.print("Enter new name with extension: ");
                        String newName = sc.nextLine().trim();
                        if (newName.isEmpty()) {
                            System.out.println("New file name cannot be empty!");
                            break;
                        }
                        File renamedFile = new File(path + File.separator + newName);
                        if (file.renameTo(renamedFile)) {
                            System.out.println("File renamed successfully.");
                        } else {
                            System.out.println("Failed to rename file.");
                        }
                        break;

                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }

        sc.close();
    }

    public boolean isAvailable(File file) {
        return file.exists();
    }

    public void exit() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(".");
        }
        System.out.println();
    }

    public void modify(File file, Scanner sc, boolean isOverride) throws IOException {
        int choice;
        if (!isOverride) {
            System.out.println("Choose:\n1. Override data \t2. Add data");
            System.out.print("Your Choice :");
            choice = sc.nextInt();
            sc.nextLine();
        } else {
            choice = 1;
        }

        if (choice == 1) {
            System.out.println("Enter new data:");
            String content = sc.nextLine();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                bw.write(content);
                System.out.println("Data written to file successfully.");
            }
        } else if (choice == 2) {
            System.out.println("Enter data to append:");
            String newData = sc.nextLine();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.newLine();
                bw.write(newData);
                System.out.println("Data appended to file successfully.");
            }
        } else {
            System.out.println("Invalid choice for modification.");
        }
    }

    public void printdata(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("---- File Content ----");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("----------------------");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void change(String oldPath, String newPath, File file, boolean isCopy) throws IOException {
        File newDir = new File(newPath);
        if (!newDir.exists()) {
            newDir.mkdirs();
        }

        File newFile = new File(newPath + File.separator + file.getName());

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
        }

        if (!isCopy) {
            if (file.delete()) {
                System.out.println("File moved successfully.");
            } else {
                System.out.println("Failed to delete original file after moving.");
            }
        } else {
            System.out.println("File copied successfully.");
        }
    }
}
