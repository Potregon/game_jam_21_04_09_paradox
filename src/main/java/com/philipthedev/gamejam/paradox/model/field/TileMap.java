package com.philipthedev.gamejam.paradox.model.field;

import com.philipthedev.gamejam.paradox.ui.MainFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public final class TileMap {

    private BufferedImage baseImage = null;
    private BufferedImage[] surroundingNoBorder = new BufferedImage[12];
    private BufferedImage[] surroundingBorder = new BufferedImage[12];
    private BufferedImage[] innerBorder = new BufferedImage[4];

    public TileMap(BufferedImage rawImage) {
        baseImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        ImageObserver imageObserver = MainFrame.get();
        Graphics2D g = baseImage.createGraphics();
        //top left
        g.drawImage(rawImage, 0, 0, 16, 16, 48, 48, 64, 64, imageObserver);
        //top right
        g.drawImage(rawImage, 16, 0, 32, 16, 0, 48, 16, 64, imageObserver);
        //bottom left
        g.drawImage(rawImage, 0, 16, 16, 32, 48, 0, 64, 16, imageObserver);
        //bottom right
        g.drawImage(rawImage, 16, 16, 32, 32, 0, 0, 16, 16, imageObserver);
        g.dispose();

        //surrounding border
        surroundingBorder[0] = cut(rawImage, 64, 0, 16, 16, imageObserver);
        surroundingBorder[1] = cut(rawImage, 80, 0, 16, 16, imageObserver);
        surroundingBorder[2] = cut(rawImage, 96, 0, 16, 16, imageObserver);
        surroundingBorder[3] = cut(rawImage, 112, 0, 16, 16, imageObserver);
        surroundingBorder[4] = cut(rawImage, 112, 16, 16, 16,imageObserver);
        surroundingBorder[5] = cut(rawImage, 112, 32, 16, 16,imageObserver);
        surroundingBorder[6] = cut(rawImage, 112, 48, 16, 16,imageObserver);
        surroundingBorder[7] = cut(rawImage, 96, 48, 16, 16,imageObserver);
        surroundingBorder[8] = cut(rawImage, 80, 48, 16, 16,imageObserver);
        surroundingBorder[9] = cut(rawImage, 64, 48, 16, 16,imageObserver);
        surroundingBorder[10] = cut(rawImage, 64, 32, 16, 16,imageObserver);
        surroundingBorder[11] = cut(rawImage, 64, 16, 16, 16,imageObserver);

        //surrounding no border
        surroundingNoBorder[0] = cut(rawImage, 80, 16, 16, 16, imageObserver);
        surroundingNoBorder[1] = cut(rawImage, 48, 32, 16, 16, imageObserver);
        surroundingNoBorder[2] = cut(rawImage, 0, 32, 16, 16, imageObserver);
        surroundingNoBorder[3] = cut(rawImage, 96, 16, 16, 16, imageObserver);
        surroundingNoBorder[4] = cut(rawImage, 16, 48, 16, 16, imageObserver);
        surroundingNoBorder[5] = cut(rawImage, 16, 0, 16, 16, imageObserver);
        surroundingNoBorder[6] = cut(rawImage, 96, 32, 16, 16, imageObserver);
        surroundingNoBorder[7] = cut(rawImage, 0, 16, 16, 16, imageObserver);
        surroundingNoBorder[8] = cut(rawImage, 48, 16, 16, 16, imageObserver);
        surroundingNoBorder[9] = cut(rawImage, 80, 32, 16, 16, imageObserver);
        surroundingNoBorder[10] = cut(rawImage, 32, 0, 16, 16, imageObserver);
        surroundingNoBorder[11] = cut(rawImage, 32, 48, 16, 16, imageObserver);

        //inner border
        innerBorder[0] = cut(rawImage,16, 16, 16, 16, imageObserver);
        innerBorder[1] = cut(rawImage,32, 16, 16, 16, imageObserver);
        innerBorder[2] = cut(rawImage,32, 32, 16, 16, imageObserver);
        innerBorder[3] = cut(rawImage,16, 32, 16, 16, imageObserver);
    }

    private static BufferedImage cut(BufferedImage source, int x, int y, int width, int height, ImageObserver imageObserver) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(source, 0, 0, width, height, x, y, x + width, y + height, imageObserver);
        g.dispose();
        return bufferedImage;
    }


    public BufferedImage getTile(boolean northWest, boolean north, boolean northEast, boolean east, boolean southEast, boolean south, boolean southWest, boolean west) {
        BufferedImage bufferedImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        ImageObserver imageObserver = MainFrame.get();
        if (north) {
            g.drawImage(surroundingNoBorder[1], 16, 0, imageObserver);
            g.drawImage(surroundingNoBorder[2], 32, 0, imageObserver);
        } else {
            g.drawImage(surroundingBorder[1], 16, 0, imageObserver);
            g.drawImage(surroundingBorder[2], 32, 0, imageObserver);
        }
        if (east) {
            g.drawImage(surroundingNoBorder[4], 48, 16, imageObserver);
            g.drawImage(surroundingNoBorder[5], 48, 32, imageObserver);
        } else {
            g.drawImage(surroundingBorder[4], 48, 16, imageObserver);
            g.drawImage(surroundingBorder[5], 48, 32, imageObserver);
        }
        if (south) {
            g.drawImage(surroundingNoBorder[7], 32, 48, imageObserver);
            g.drawImage(surroundingNoBorder[8], 16, 48, imageObserver);
        } else {
            g.drawImage(surroundingBorder[7], 32, 48, imageObserver);
            g.drawImage(surroundingBorder[8], 16, 48, imageObserver);
        }
        if (west) {
            g.drawImage(surroundingNoBorder[10], 0, 32, imageObserver);
            g.drawImage(surroundingNoBorder[11], 0, 16, imageObserver);
        } else {
            g.drawImage(surroundingBorder[10], 0, 32, imageObserver);
            g.drawImage(surroundingBorder[11], 0, 16, imageObserver);
        }

        if (north) {
            if (west) {
                if (northWest) {
                    g.drawImage(surroundingNoBorder[0], 0, 0, imageObserver);
                } else {
                    g.drawImage(innerBorder[2], 0, 0, imageObserver);
                }
            } else {
                if (northWest) {
                    g.drawImage(surroundingBorder[10], 0, 0, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[10], 0, 0, imageObserver);
                }
            }
        } else {
            if (west) {
                if (northWest) {
                    g.drawImage(surroundingBorder[2], 0, 0, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[2], 0, 0, imageObserver);
                }
            } else {
                g.drawImage(surroundingBorder[0], 0, 0, imageObserver);
            }
        }

        if (north) {
            if (east) {
                if (northEast) {
                    g.drawImage(surroundingNoBorder[3], 48, 0, imageObserver);
                } else {
                    g.drawImage(innerBorder[3], 48, 0, imageObserver);
                }
            } else {
                if (northEast) {
                    g.drawImage(surroundingBorder[5], 48, 0, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[5], 48, 0, imageObserver);
                }
            }
        } else {
            if (east) {
                if (northEast) {
                    g.drawImage(surroundingBorder[1], 48, 0, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[1], 48, 0, imageObserver);
                }
            } else {
                if (northEast) {
                    g.drawImage(surroundingBorder[3], 48, 0, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[3], 48, 0, imageObserver);
                }
            }
        }

        if (south) {
            if (east) {
                if (southEast) {
                    g.drawImage(surroundingNoBorder[6], 48, 48, imageObserver);
                } else {
                    g.drawImage(innerBorder[0], 48, 48, imageObserver);
                }
            } else {
                if (southEast) {
                    g.drawImage(surroundingBorder[4], 48, 48, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[4], 48, 48, imageObserver);
                }
            }
        } else {
            if (east) {
                if (southEast) {
                    g.drawImage(surroundingBorder[8], 48, 48, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[8], 48, 48, imageObserver);
                }
            } else {
                if (southEast) {
                    g.drawImage(surroundingBorder[6], 48, 48, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[6], 48, 48, imageObserver);
                }
            }
        }

        if (south) {
            if (west) {
                if (southWest) {
                    g.drawImage(surroundingNoBorder[9], 0, 48, imageObserver);
                } else {
                    g.drawImage(innerBorder[1], 0, 48, imageObserver);
                }
            } else {
                if (southWest) {
                    g.drawImage(surroundingBorder[11], 0, 48, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[11], 0, 48, imageObserver);
                }
            }
        } else {
            if (west) {
                if (southWest) {
                    g.drawImage(surroundingBorder[7], 0, 48, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[7], 0, 48, imageObserver);
                }
            } else {
                if (southWest) {
                    g.drawImage(surroundingBorder[9], 0, 48, imageObserver);
                } else {
                    g.drawImage(surroundingBorder[9], 0, 48, imageObserver);
                }
            }
        }

        g.drawImage(baseImage, 16, 16, imageObserver);

        g.dispose();
        return bufferedImage;
    }
}
