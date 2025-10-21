import java.io.*;
import java.util.Scanner;

public class TextFileManager {

    public void Start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Choose");
            System.out.println("1. Create a File \t2. Delete a File");
            System.out.println("3. Search a File \t4. Modify File");
            System.out.println("5. Print data \t\t6. Exit");
            System.out.print("Your Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 6) {
                System.out.print("Exiting System");
                exit();
                break;  
            }

            System.out.print("Enter Path (without file): ");
            String path = sc.nextLine();

            System.out.print("Enter file name with extension: ");
            String filename = sc.nextLine();

            File file = new File(path + "\\" + filename);

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

   public void printdata(File file){
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
}
