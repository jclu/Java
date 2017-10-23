import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class t {

    /**
     * File Permissions Java Example using File and PosixFilePermission
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        String srcFileName = "/home/jiaclu/src.txt";
        String destFileName = "/home/jiaclu/dest.txt";
        String srcFileContent = "xxx";

        File srcFile = new File(srcFileName);
        srcFile.createNewFile();
        srcFile.setWritable(true, true);
        srcFile.setReadable(true, false);

        FileOutputStream fos = new FileOutputStream(srcFile);
        fos.write(srcFileContent.getBytes());
        fos.close();

        // Create dest file
        File destFile = new File(destFileName);
        destFile.createNewFile();

        // Rename the src file to the dest file
        //FileUtils.copyFile(srcFile, destFile);

        Path srcFilePath = Paths.get(srcFileName);
        Path destFilePath = Paths.get(destFileName);
        Files.copy(srcFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

        File file = new File("/home/jiaclu/temp.txt");

        //set application user permissions to 455
        file.setExecutable(false);
        file.setReadable(false);
        file.setWritable(true);

        //change permission to 777 for all the users
        //no option for group and others
        file.setExecutable(true, false);
        file.setReadable(true, false);
        file.setWritable(true, false);

        //using PosixFilePermission to set file permissions 777
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        //add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);

        Files.setPosixFilePermissions(Paths.get("/home/jiaclu/run.sh"), perms);
    }

}
