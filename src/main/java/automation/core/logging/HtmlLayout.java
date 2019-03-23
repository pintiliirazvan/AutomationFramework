package automation.core.logging;

import static automation.util.StringUtil.isNullOrEmpty;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.apache.logging.log4j.Level.ERROR;
import static org.apache.logging.log4j.Level.WARN;
import static org.apache.logging.log4j.core.util.Transform.escapeHtmlTags;
import static org.apache.logging.log4j.util.Strings.LINE_SEPARATOR;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.HtmlLayout.FontSize;
import org.apache.logging.log4j.core.util.Transform;
import org.apache.logging.log4j.util.Strings;

/**
 *
 * Outputs events as rows in an HTML table on an HTML page.
 * <p>
 * Appenders using this layout should have their encoding set to UTF-8 or UTF-16, otherwise events containing non ASCII
 * characters could result in corrupted log files.
 * </p>
 * 
 * @author alexgabor
 * 
 */
@Plugin(name = "HtmlLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class HtmlLayout extends AbstractStringLayout {

	public static final String DEFAULT_FONT_FAMILY = "arial,sans-serif";
	public static final String THREAD_ID_LABEL = "Thread.Id=";
	private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
	private static final String REGEXP = LINE_SEPARATOR.equals("\n") ? "\n" : LINE_SEPARATOR + "|\n";
	private static final String DEFAULT_TITLE = "Log4j Log Messages";
	private static final String DEFAULT_CONTENT_TYPE = "text/html";

	private final long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();

	private final boolean locationInfo;
	private final String title;
	private final String contentType;
	private final String font;
	private final String fontSize;
	private final String headerSize;

	private HtmlLayout(final boolean locationInfo, final String title, final String contentType, final Charset charset,
			final String font, final String fontSize, final String headerSize) {

		super(charset);

		this.locationInfo = locationInfo;
		this.title = title;
		this.contentType = addCharsetToContentType(contentType);
		this.font = font;
		this.fontSize = fontSize;
		this.headerSize = headerSize;
	}

	public String getTitle() {
		return title;
	}

	public boolean isLocationInfo() {
		return locationInfo;
	}

	/**
	 * Returns appropriate HTML headers
	 *
	 * @return the header as a byte array
	 */
	@Override
	public byte[] getHeader() {
	
		final StringBuilder sbuf = new StringBuilder();
	
		append(sbuf, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
		appendLs(sbuf, "\"http://www.w3.org/TR/html4/loose.dtd\">");
		appendLs(sbuf, "<html>");
		appendLs(sbuf, "<head>");
		append(sbuf, "<meta charset=\"");
		append(sbuf, getCharset().toString());
		appendLs(sbuf, "\"/>");
		append(sbuf, "<title>").append(title);
		appendLs(sbuf, "</title>");
		appendLs(sbuf, "<style type=\"text/css\">");
		appendLs(sbuf, "<!--");
		append(sbuf, "body, table {font-family:").append(font).append("; font-size: ");
		appendLs(sbuf, headerSize).append(";}");
		appendLs(sbuf, "th {background: #336699; color: #FFFFFF; text-align: left;}");
		appendLs(sbuf, "-->");
		appendLs(sbuf, "</style>");
		appendLs(sbuf, "</head>");
		appendLs(sbuf, "<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">");
		appendLs(sbuf, "<hr size=\"1\" noshade=\"noshade\">");
		appendLs(sbuf, "Log session start time " + new java.util.Date() + "<br>");
		appendLs(sbuf, "<br>");
		appendLs(sbuf, "<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
		appendLs(sbuf, "<tr>");
		appendLs(sbuf, "<th>Time</th>");
		appendLs(sbuf, "<th>Thread ID</th>");
		appendLs(sbuf, "<th>Level</th>");
		appendLs(sbuf, "<th>Logger</th>");
	
		if (locationInfo) {
			appendLs(sbuf, "<th>Method:Line Number</th>");
		}
	
		appendLs(sbuf, "<th>Message</th>");
		appendLs(sbuf, "</tr>");
	
		return sbuf.toString().getBytes(getCharset());
	}

	/**
	 * Returns the appropriate HTML footers
	 *
	 * @return the footer as a byte array
	 */
	@Override
	public byte[] getFooter() {
	
		final StringBuilder sbuf = new StringBuilder();
	
		appendLs(sbuf, "</table>");
		appendLs(sbuf, "<br>");
		appendLs(sbuf, "</body></html>");
	
		return getBytes(sbuf.toString());
	}

	/**
	 * @return The content type
	 */
	@Override
	public String getContentType() {
		return contentType;
	}

	/**
	 * Formats as a {@link String}
	 *
	 * @param event
	 *        the Logging Event
	 * @return a {@link String} containing the {@link LogEvent} as HTML
	 */
	@Override
	public String toSerializable(final LogEvent event) {
	
		Level level = event.getLevel();
	
		String formattedMessage = event.getMessage().getFormattedMessage();
	
		final StringBuilder sbuf = getStringBuilder();
	
		long eventTimeMillis = event.getTimeMillis();
		long minutes = MILLISECONDS.toMinutes(eventTimeMillis - jvmStartTime);
		long seconds = MILLISECONDS.toSeconds(eventTimeMillis - jvmStartTime) - MINUTES.toSeconds(minutes);
	
		String time = String.format("%d min, %d sec", minutes, seconds);
		String escapedThread = Transform.escapeHtmlTags(THREAD_ID_LABEL + event.getThreadId());
		String messageContent = markForLevel(formattedMessage, level).replaceAll(REGEXP, "<br />");
		String escapedLogger = Transform.escapeHtmlTags(event.getLoggerName());
	
		escapedLogger = isNullOrEmpty(escapedLogger) ? LoggerConfig.ROOT : escapedLogger;
	
		appendLs(sbuf, LINE_SEPARATOR + "<tr>");
	
		addCellWithTextAndTitle(sbuf, time, "Time");
	
		addCellWithTextAndTitle(sbuf, escapedThread, escapedThread + " thread");
	
		addCellWithTextAndTitle(sbuf, markForLevel(String.valueOf(level), level), "Level");
	
		addCellWithTextAndTitle(sbuf, markForLevel(escapedLogger, level), escapedThread + " logger");
	
		if (locationInfo) {
	
			final StackTraceElement element = event.getSource();
	
			List<StackTraceElement> stackTraceList = asList(currentThread().getStackTrace());
	
			int index = stackTraceList.indexOf(element);
	
			StackTraceElement traceElement = stackTraceList.get(index + 1); // get on the next level of the StackTrace to bypass MLogger wrapper
	
			int lineNumber = traceElement.getLineNumber();
	
			String methodName = traceElement.getMethodName();
			String methodAndLineNumber = markForLevel(methodName + "():" + lineNumber, level);
	
			addCellWithTextAndTitle(sbuf, methodAndLineNumber, "Method and Line Number");
		}
	
		addCellWithTextAndTitle(sbuf, messageContent, "Message");
	
		appendLs(sbuf, "</tr>");
	
		if (event.getContextStack() != null && !event.getContextStack().isEmpty()) {
	
			sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(fontSize);
			sbuf.append(";\" colspan=\"6\" ");
			sbuf.append("title=\"Nested Diagnostic Context\">");
			sbuf.append("NDC: ").append(Transform.escapeHtmlTags(event.getContextStack().toString()));
	
			appendLs(sbuf, "</td></tr>");
		}
	
		if (event.getContextData() != null && !event.getContextData().isEmpty()) {
	
			sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(fontSize);
			sbuf.append(";\" colspan=\"6\" ");
			sbuf.append("title=\"Mapped Diagnostic Context\">");
			sbuf.append("MDC: ").append(Transform.escapeHtmlTags(event.getContextData().toMap().toString()));
	
			appendLs(sbuf, "</td></tr>");
		}
	
		final Throwable throwable = event.getThrown();
	
		if (throwable != null) {
	
			sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : x-small");
			sbuf.append(";\" colspan=\"6\">");
	
			appendThrowableAsHtml(throwable, sbuf);
	
			appendLs(sbuf, "</td></tr>");
		}
	
		return sbuf.toString();
	}

	/**
	 * Appends the charset to the given content type
	 * 
	 * @param contentType
	 *        the content type
	 * @return
	 */
	private String addCharsetToContentType(final String contentType) {

		if (contentType == null) {
			return DEFAULT_CONTENT_TYPE + "; charset=" + getCharset();
		}

		return contentType.contains("charset") ? contentType : contentType + "; charset=" + getCharset();
	}

	/**
	 * Appends the given {@link Throwable} as HTML
	 * 
	 * @param throwable
	 *        the {@link Throwable} to print
	 * @param sbuf
	 *        the {@link StringBuilder}
	 */
	private static void appendThrowableAsHtml(final Throwable throwable, final StringBuilder sbuf) {

		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);

		try {
			throwable.printStackTrace(pw);
		} catch (final RuntimeException ex) {
			// Ignore the exception
		}

		pw.flush();

		final LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
		final ArrayList<String> lines = new ArrayList<>();

		try {

			String line = reader.readLine();

			while (line != null) {

				lines.add(line);
				line = reader.readLine();
			}

		} catch (final IOException ex) {

			if (ex instanceof InterruptedIOException) {
				Thread.currentThread().interrupt();
			}

			lines.add(ex.toString());
		}

		boolean first = true;

		for (final String line : lines) {

			if (!first) {
				sbuf.append(TRACE_PREFIX);
			} else {
				first = false;
			}

			sbuf.append(Transform.escapeHtmlTags(line));
			sbuf.append(Strings.LINE_SEPARATOR);
		}
	}

	/**
	 * Appends the provided {@link String} to the {@link StringBuilder}, with a line separator
	 * 
	 * @param sbuilder
	 *        the {@link StringBuilder}
	 * @param s
	 *        the {@link String} to append
	 * @return
	 */
	private static StringBuilder appendLs(final StringBuilder sbuilder, final String s) {

		sbuilder.append(s).append(Strings.LINE_SEPARATOR);

		return sbuilder;
	}

	/**
	 * Appends the provided {@link String} to the {@link StringBuilder}
	 * 
	 * @param sbuilder
	 *        the {@link StringBuilder}
	 * @param s
	 *        the {@link String} to append
	 * @return
	 */
	private static StringBuilder append(final StringBuilder sbuilder, final String s) {

		sbuilder.append(s);

		return sbuilder;
	}

	/**
	 * Builds the cell HTML marking for the given text and appends it to the given {@link StringBuffer}
	 *
	 * @param sbuf
	 *        the {@link StringBuffer} used for appending the tokens
	 * @param textInCell
	 *        the content of the cell
	 * @param title
	 *        the title of the cell
	 */
	protected void addCellWithTextAndTitle(final StringBuilder sbuf, String textInCell, String title) {
	
		String tdTag = isNullOrEmpty(title) ? "<td>" : "<td title=\"" + title + "\">";
	
		sbuf.append(tdTag);
		sbuf.append(textInCell);
	
		appendLs(sbuf, "</td>");
	}

	/**
	 * Adds to font size and color marking to the given value, according to the given {@link Level}
	 *
	 * @param level
	 *        the {@link Level} considered when deciding the font size and color
	 * @return
	 */
	protected String markForLevel(String value, Level level) {
	
		String font;
	
		if (level.equals(WARN)) {

			String fontColorOrange = "<font color=\"#daa520\" style=\"font-size : small;\"><strong>";
	
			font = fontColorOrange;

		} else if (level.isMoreSpecificThan(ERROR)) {

			String fontColorRed = "<font color=\"#CD3700\" style=\"font-size : small;\"><strong>";
	
			font = fontColorRed;
	
		} else {
			return escapeHtmlTags(value);
		}
	
		String endFontTag = font.contains("strong") ? "</font>" : "</strong></font>";
		String marked = font + escapeHtmlTags(value) + endFontTag;
	
		return marked;
	}

	/**
	 * Creates an HTML Layout
	 *
	 * @param locationInfo
	 *        if <code>true</code>, then location information will be included. The default is <code>false</code>
	 * @param title
	 *        the title to include in the file header. If none is specified the default title will be used
	 * @param contentType
	 *        the content type. Defaults to "text/html"
	 * @param charset
	 *        the character set to use. If not specified, the default will be used
	 * @param fontSize
	 *        the font size of the text
	 * @param font
	 *        the font to use for the text
	 * @return an HTML Layout
	 */
	@PluginFactory
	public static HtmlLayout createLayout(@PluginAttribute(value = "locationInfo") final boolean locationInfo,
			@PluginAttribute(value = "title", defaultString = DEFAULT_TITLE) final String title,
			@PluginAttribute("contentType") String contentType,
			@PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset,
			@PluginAttribute("fontSize") String fontSize,
			@PluginAttribute(value = "fontName", defaultString = DEFAULT_FONT_FAMILY) final String font) {

		final FontSize fs = FontSize.getFontSize(fontSize);

		fontSize = fs.getFontSize();

		final String headerSize = fs.larger().getFontSize();

		if (contentType == null) {
			contentType = DEFAULT_CONTENT_TYPE + "; charset=" + charset;
		}

		return new HtmlLayout(locationInfo, title, contentType, charset, font, fontSize, headerSize);
	}

	/**
	 * Creates an HTML Layout using the default settings
	 *
	 * @return an HTML Layout
	 */
	public static HtmlLayout createDefaultLayout() {
		return newBuilder().build();
	}

	@PluginBuilderFactory
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * Builder class for {@link HtmlLayout} logs
	 * 
	 * @author alexgabor
	 *
	 */
	public static class Builder implements org.apache.logging.log4j.core.util.Builder<HtmlLayout> {

		@PluginBuilderAttribute
		private boolean locationInfo = false;

		@PluginBuilderAttribute
		private String title = DEFAULT_TITLE;

		@PluginBuilderAttribute
		private String contentType = null; // defer default value in order to use specified charset

		@PluginBuilderAttribute
		private Charset charset = StandardCharsets.UTF_8;

		@PluginBuilderAttribute
		private FontSize fontSize = FontSize.SMALL;

		@PluginBuilderAttribute
		private String fontName = DEFAULT_FONT_FAMILY;

		private Builder() {
		}

		public Builder withLocationInfo(final boolean locationInfo) {

			this.locationInfo = locationInfo;

			return this;
		}

		public Builder withTitle(final String title) {

			this.title = title;

			return this;
		}

		public Builder withContentType(final String contentType) {

			this.contentType = contentType;

			return this;
		}

		public Builder withCharset(final Charset charset) {

			this.charset = charset;

			return this;
		}

		public Builder withFontSize(final FontSize fontSize) {

			this.fontSize = fontSize;

			return this;
		}

		public Builder withFontName(final String fontName) {

			this.fontName = fontName;

			return this;
		}

		@Override
		public HtmlLayout build() {

			if (contentType == null) {
				contentType = DEFAULT_CONTENT_TYPE + "; charset=" + charset;
			}

			return new HtmlLayout(locationInfo, title, contentType, charset, fontName, fontSize.getFontSize(), fontSize.larger().getFontSize());
		}
	}

}
