package Application.DAO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageHandle<BufferedImage> {
    public byte[] GetImage(String link) {

        File file = null;
        if(link.trim().isEmpty())
            file = new File(getClass().getResource("/Chuacohinh.jpg").getFile());
        else
            file = new File(link);

        byte[] bFile = new byte[(int) file.length()];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bFile;
    }

    public Image ChangeByte_Image(byte[] bFile) throws IOException {

        BufferedImage img = (BufferedImage) ImageIO.read(new ByteArrayInputStream(bFile));

        Image image = SwingFXUtils.toFXImage((java.awt.image.BufferedImage) img, null);
        return image;

    }

}
