import javax.swing.*;
import java.awt.*;

public class Animation {
    Image[] images;
    int currentImage = 0;

    int duration;
    int delay;

    public Animation(String name, int count, int duration, String filetype)
    {
        images = new Image[count];

        this.duration = duration;

        delay = duration;

        for(int i = 0; i < images.length; i++)
        {
            images[i] = getImage(name + "_" + i + "." + filetype);
        }
    }

    public Image stillImage()
    {
        return images[1];
    }

    public Image nextImage()
    {
        delay--;

        if(delay == 0)
        {
            if( currentImage == images.length-1) {
                currentImage = 0;
            } else {
                currentImage++;
            }

            delay = duration;
        }

        return images[currentImage];
    }


    public Image getImage(String filename)
    {
        //new ImageIcon(getClass().getResource("room_art/room.png")).getImage();

        return Toolkit.getDefaultToolkit().getImage(filename);
       // return new ImageIcon(filename).getImage();
    }
}
