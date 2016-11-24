import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.Arrays.asList;

public class Main {
    private static final String AUDIO_ZIP_NAME = "audios";
    private static final String VIDEOS_ZIP_NAME = "videos";
    private static final String IMAGES_ZIP_NAME = "images";
    private static final String[] AUDIO_EXTENSIONS = new String[]{"mp3", "wav", "wma"};
    private static final String[] VIDEO_EXTENSIONS = new String[]{"avi", "mp4", "flv"};
    private static final String[] IMAGE_EXTENSIONS = new String[]{"png", "jpeg", "jpg", "gif"};

    public static void main(String[] args) {
        File directory = getDirectory();
        List<File> audios = getFilesByMask(directory, AUDIO_EXTENSIONS);
        List<File> videos = getFilesByMask(directory, VIDEO_EXTENSIONS);
        List<File> images = getFilesByMask(directory, IMAGE_EXTENSIONS);

        writeToZip(AUDIO_ZIP_NAME, audios);
        writeToZip(VIDEOS_ZIP_NAME, videos);
        writeToZip(IMAGES_ZIP_NAME, images);
    }

    private static List<File> getFilesByMask(File directory, String[] extensionsMask) {
        File[] files = directory.listFiles((file, name) -> {
            for (String ext : extensionsMask)
                if (name.endsWith(ext))
                    return true;
            return false;
        });
        return asList(files);
    }

    private static File getDirectory() {
        File directory;
        do {
            System.out.print("Input path to folder: ");
            Scanner input = new Scanner(System.in);
            directory = new File(input.nextLine());
        } while (!directory.exists() || !directory.isDirectory());
        return directory;
    }

    private static void writeToZip(String archiveName, List<File> files) {
        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(archiveName + ".zip")))) {
            for (File file : files) {
                zos.putNextEntry(new ZipEntry(file.getName()));
                zos.write(file.toString().getBytes());
                zos.closeEntry();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}