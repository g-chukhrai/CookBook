package by.chuger.cookbook.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;

public final class MessageUtils extends ResourceBundle {

    private static ResourceBundle resourceBundle;
    private static final String ERROR_PATTERN = "??? missing bundle message for key {0} ???";
    protected static final String BUNDLE_VAR_NAME = "msg";

    private MessageUtils() {
    }

    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

    @Override
    public Enumeration getKeys() {
        return parent.getKeys();
    }

    public static ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            resourceBundle = context.getApplication().getResourceBundle(context, BUNDLE_VAR_NAME);
        }
        return resourceBundle;
    }

    public static String getMessage(final String bundleKey) {
        String message;
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
        return (!(string == null || "".equals(string.trim())));
    }
}
