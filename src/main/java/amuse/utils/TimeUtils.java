package amuse.utils;


public final class TimeUtils {
  /**
   * Converts the time, given in seconds, to the readable String, like {hh : mm : ss}
   *
   * @param duration source duration in seconds.
   * @return formatted time as string.
   */
  public static String formatTime(final long duration) {
    int seconds = (int) (duration % 60);
    int minutes = (int) ((duration / 60) % 60);
    int hours = (int) ((duration / (60 * 60)) % 24);
    if (hours == 0)
      return String.format("%d min : %d sec", minutes, seconds);
    else
      return String.format("%d h : %d min : %d sec", hours, minutes, seconds);
  }
}
