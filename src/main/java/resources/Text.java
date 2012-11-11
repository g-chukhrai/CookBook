package resources;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Text extends ResourceBundle {

    private static ResourceBundle resourceBundle;
    private static final String ERROR_PATTERN = "??? missing bundle message for key {0} ???";
    protected static final String BUNDLE_NAME = "Text";
    protected static final String BUNDLE_EXTENSION = "properties";
    protected static final Control UTF8_CONTROL = new UTF8Control();
    protected static final String BUNDLE_VAR_NAME = "msg";

    public Text() {
        setParent(ResourceBundle.getBundle(BUNDLE_NAME,
                FacesContext.getCurrentInstance().getViewRoot().getLocale(), UTF8_CONTROL));
    }

    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

    @Override
    public Enumeration getKeys() {
        return parent.getKeys();
    }

    protected static class UTF8Control extends Control {

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            // The below code is copied from default Control#newBundle() implementation.
            // Only the PropertyResourceBundle line is changed to read the file as UTF-8.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }

    public static ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            resourceBundle = context.getApplication().getResourceBundle(context, BUNDLE_VAR_NAME);
        }
        return resourceBundle;
    }

    public static String getMessage(final String bundleKey) {
        String message = null;
        try {
            message = getResourceBundle().getString(bundleKey);
        } catch (Exception e) {
            message = MessageFormat.format(ERROR_PATTERN, bundleKey);
        }
        return message;
    }

    public static void addError(final String componentId, final String bundleKey) {
        FacesContext.getCurrentInstance().addMessage(componentId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage(bundleKey), null));
    }

    public static void showMessage(final String title, final String description) {
        FacesMessage msg = new FacesMessage(title, description);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static boolean isNotEmpty(final String string) {
        return (string == null || "".equals(string.trim())) ? false : true;
    }
}
