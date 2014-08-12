package org.librebiz.pureport.context;

import java.awt.Graphics2D;
import java.awt.font.GraphicAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageAttribute implements Serializable {
    private static final Logger LOG
            = Logger.getLogger(ImageAttribute.class.getName());
    private static final Map<URL,BufferedImage> IMAGE_MAP
            = new WeakHashMap<URL,BufferedImage>();

    private URL url;
    private int alignment;
    private float originX;
    private float originY;
    private float width;
    private float height;

    public ImageAttribute() {
    }

    public ImageAttribute(URL file, int alignment,
            float width, float height, float originX, float originY) {
        this.url = file;
        this.alignment = alignment;
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImageAttribute other = (ImageAttribute) obj;
        if (url == null ? other.url != null : !url.equals(other.url)) {
            return false;
        }
        if (alignment != other.alignment) {
            return false;
        }
        if (originX != other.originX) {
            return false;
        }
        if (originY != other.originY) {
            return false;
        }
        if (width != other.width) {
            return false;
        }
        if (height != other.height) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37*hash + url.hashCode();
        hash = 37*hash + alignment;
        hash = 37*hash + Float.floatToIntBits(originX);
        hash = 37*hash + Float.floatToIntBits(originY);
        hash = 37*hash + Float.floatToIntBits(width);
        hash = 37*hash + Float.floatToIntBits(height);
        return hash;
    }

    public GraphicAttribute toGraphicAttribute() {
        return new GraphicAttribute(alignment) {
            private BufferedImage image = getImage();

            @Override
            public float getAscent() {
                return originY <= 0 ? 0 : originY;
            }

            @Override
            public float getDescent() {
                return height <= originY ? 0 : height-originY;
            }

            @Override
            public float getAdvance() {
                return width <= originX ? 0 : width-originX;
            }

            @Override
            public void draw(Graphics2D g2d, float x, float y) {
                g2d.drawImage(image, (int) (x-originX), (int) (y-originY),
                        (int)width, (int)height, null);
            }

            @Override
            public Rectangle2D getBounds() {
                return new Rectangle2D.Float(
                                -originX, -originY, width, height);
            }
        };
    }

    private BufferedImage getImage() {
        BufferedImage img = loadImage(url);
        if (img != null) {
            if (width <= 0) {
                width = img.getWidth(null);
            }
            if (height <= 0) {
                height = img.getHeight(null);
            }
        }
        return img;
    }

    private static BufferedImage loadImage(URL url) {
        try {
            BufferedImage result = IMAGE_MAP.get(url);
            if (result == null) {
                LOG.log(Level.WARNING, "Loading image {0}", url);
                result = ImageIO.read(url);
                IMAGE_MAP.put(url, result);
            }
            return result;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error loading image " + url, e);
            return null;
        }
    }
}
