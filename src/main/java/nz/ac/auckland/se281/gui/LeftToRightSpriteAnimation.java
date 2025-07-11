package nz.ac.auckland.se281.gui;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LeftToRightSpriteAnimation extends Transition {

  private final ImageView imageView;
  private final int count;
  private final int columns;
  private final int offsetX;
  private final int offsetY;
  private final int width;
  private final int height;

  public LeftToRightSpriteAnimation(
      ImageView imageView,
      Duration duration,
      int count,
      int columns,
      int offsetX,
      int offsetY,
      int width,
      int height) {
    this.imageView = imageView;
    this.count = count;
    this.columns = columns;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.width = width;
    this.height = height;
    setCycleDuration(duration);
    setInterpolator(Interpolator.LINEAR);
  }

  @Override
  protected void interpolate(double k) {
    final int index = Math.min((int) Math.floor(k * count), count - 1);

    // This is the standard Left-to-Right formula
    final int x = (index % columns) * width + offsetX;
    final int y = (index / columns) * height + offsetY;

    imageView.setViewport(new Rectangle2D(x, y, width, height));
  }
}
