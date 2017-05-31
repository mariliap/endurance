package research.mpl.backend.smart.util;

/**
 * Created by Marilia Portela on 04/02/2017.
 */
public class TimeUtil {

    public static void printExecutionTime(long estimatedTimeInMilliseconds, String name) {

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = estimatedTimeInMilliseconds / daysInMilli;
        estimatedTimeInMilliseconds = estimatedTimeInMilliseconds % daysInMilli;

        long elapsedHours = estimatedTimeInMilliseconds / hoursInMilli;
        estimatedTimeInMilliseconds = estimatedTimeInMilliseconds % hoursInMilli;

        long elapsedMinutes = estimatedTimeInMilliseconds / minutesInMilli;
        estimatedTimeInMilliseconds = estimatedTimeInMilliseconds % minutesInMilli;

        long elapsedSeconds = estimatedTimeInMilliseconds / secondsInMilli;
        estimatedTimeInMilliseconds = estimatedTimeInMilliseconds % secondsInMilli;

        long elapsedMilliSeconds = estimatedTimeInMilliseconds;

        StringBuffer strbff = new StringBuffer();
        strbff.append("\n");
        if(name != null && !name.trim().isEmpty()) {
            strbff.append("[");
            strbff.append(name);
            strbff.append("] ");
        }
        strbff.append("Total execution time: ");
        strbff.append(elapsedDays);
        strbff.append(" days, ");
        strbff.append(elapsedHours);
        strbff.append(" hours, ");
        strbff.append(elapsedMinutes);
        strbff.append(" minutes, ");
        strbff.append(elapsedSeconds);
        strbff.append(" seconds, ");
        strbff.append(elapsedMilliSeconds);
        strbff.append(" milliseconds");
        strbff.append("\n");

        System.out.println(strbff.toString());
    }
}
