package automation.core.properties;

import static automation.util.LoggerUtil.logThrowableCause;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import automation.core.browser.BrowserName;
import automation.core.logging.MessageLogger;

/**
 * Class used to store and load {@link Properties} used in the tests
 *
 * @author alexgabor
 *
 */
public class Settings extends Properties implements TestProperties {

	private static final long serialVersionUID = -8694069003376510800L;

	private static final MessageLogger LOG = new MessageLogger(Settings.class);
	private static final String PROPERTIES_FILE = "test.properties";

	private static Settings instance = null;

	private Settings() {

	}

	public static Settings getInstance() {

		InputStream stream;

		try {

			File settingsFile = new File(PROPERTIES_FILE);

			settingsFile.createNewFile();

			stream = new FileInputStream(PROPERTIES_FILE);

			instance = new Settings();

			instance.load(stream);

			stream.close();

		} catch (Exception e) {

			LOG.error("Exception thrown when reading the Settings file: ");

			logThrowableCause(e);
		}

		return instance;
	}

	@Override
	public String getURL() {
		return this.getProperty("browser.url");
	}
	
	@Override
	public void setURL(String url) throws IOException {
		storeProperty("browser.url", url);
	}

	@Override
	public void setBrowserName(BrowserName browserName) throws Exception {

		if (browserName == null) {
			return;
		}

		storeProperty("browser.name", browserName.name());
	}

	@Override
	public BrowserName getBrowserName() {
		return BrowserName.valueOf(this.getProperty("browser.name"));
	}

	/**
	 * Stores the given property value to the settings file
	 *
	 * @param key
	 *        the property name
	 * @param value
	 *        the property value
	 * @throws IOException
	 */
	private void storeProperty(String key, String value) throws IOException {

		OutputStream output = null;

		try {

			output = new FileOutputStream(PROPERTIES_FILE);

			this.setProperty(key, value);

			this.store(output, null);

		} catch (IOException e) {

			logThrowableCause(e);

			throw e;

		} finally {

			if (output == null) {
				return;
			}

			try {
				output.close();
			} catch (IOException e) {

				logThrowableCause(e);

				throw e;
			}
		}
	}

}
