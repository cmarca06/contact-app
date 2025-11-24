package notifications;

import utils.Toast;

public class NotificationMessage {
    private final String message;
    private final Toast.ToastType type;

    public NotificationMessage(String message, Toast.ToastType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public Toast.ToastType getType() {
        return type;
    }
}
