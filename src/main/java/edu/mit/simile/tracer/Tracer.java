package edu.mit.simile.tracer;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This is a special Log4j log formatter that is capable of reacting on special log messages
 * and 'indent' the logs accordingly. This is very useful to visually inspect a debug log
 * and see what calls what. An example of logs are "> method()" and "< method()" where > and <
 * are used to indicate respectively "entering" and "exiting".
 */
public class Tracer extends Layout {

    protected static final int CONTEXT_SIZE = 25;
    protected static final long MAX_DELTA = 10000;

    protected final StringBuffer buf = new StringBuffer(256);
    protected Calendar calendar = Calendar.getInstance();
    protected long previousTime = 0;
    protected int indentation = 0;

    public void activateOptions() {
        // no options at this time
    }

    public String format(LoggingEvent event) {
        String message = event.getRenderedMessage();
        if (message == null) return "";
        if (message.length() < 2) return message;
        
        char leader = message.charAt(0);
        char secondLeader = message.charAt(1);
        if ((leader == '<') && (secondLeader == ' ') && (this.indentation > 0)) this.indentation--;

        // Reset buf
        buf.setLength(0);

        Date date = new Date();
        long now = date.getTime();
        calendar.setTime(date);

        long delta = 0;
        if (previousTime > 0) {
            delta = now - previousTime;
        }
        previousTime = now;

//        if ((previousTime == 0) || (delta > MAX_DELTA)) {
//            buf.append('\n');
//            indentation = 0; // reset indentation after a while, as we might
//            // have runaway/unmatched log entries
//        }

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) buf.append('0');
        buf.append(hour);
        buf.append(':');

        int mins = calendar.get(Calendar.MINUTE);
        if (mins < 10) buf.append('0');
        buf.append(mins);
        buf.append(':');

        int secs = calendar.get(Calendar.SECOND);
        if (secs < 10) buf.append('0');
        buf.append(secs);
        buf.append('.');

        int millis = (int) (now % 1000);
        if (millis < 100) buf.append('0');
        if (millis < 10) buf.append('0');
        buf.append(millis);

        buf.append(" [");
        String context = event.getLoggerName();
        if (context.length() < CONTEXT_SIZE) {
            buf.append(context);
            pad(buf, CONTEXT_SIZE - context.length(), ' ');
        } else {
            buf.append("..");
            buf.append(context.substring(context.length() - CONTEXT_SIZE + 2));
        }
        buf.append("] ");

        pad(buf, indentation, ' ');

        buf.append(message);

        buf.append(" (");
        buf.append(delta);
        buf.append("ms)\n");

        if ((leader == '>') && (secondLeader == ' ')) indentation++;

        return buf.toString();
    }

    private void pad(StringBuffer buffer, int pads, char padchar) {
        for (int i = 0; i < pads; i++) {
            buf.append(padchar);
        }
    }

    public boolean ignoresThrowable() {
        return true;
    }
}
