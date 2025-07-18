package nz.ac.auckland.se281.gui;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {

  private final ImageView imageView;
  private final int count;
  private final int columns;
  private final int offsetX;
  private final int offsetY;
  private final int width;
  private final int height;

  public SpriteAnimation(
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
    // This method is called on every frame of the animation

    final int index = Math.min((int) Math.floor(k * count), count - 1);

    final int x = ((columns - 1) - (index % columns)) * width + offsetX;

    final int y = (index / columns) * height + offsetY;

    // Set the "window" of the ImageView to show only the current frame
    imageView.setViewport(new Rectangle2D(x, y, width, height));
  }
}
