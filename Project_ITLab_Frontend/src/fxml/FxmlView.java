package fxml;


import java.util.ResourceBundle;

public enum FxmlView {
    LOGIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        public String getFxmlFile() {
            return "../fxml/Login/Login.fxml";
        }
    },
    HOME {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("home.title");
        }

        @Override
        public String getFxmlFile() {
            return "../fxml/Home/Home.fxml";
        }
    },
    MANAGESESSION {
        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public String getFxmlFile() {
            return "../fxml/ManageSession/ManageSession.fxml";
        }
    },
    STATISTIC {
        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public String getFxmlFile() {
            return "../fxml/Statistic/Statistic.fxml";
        }
    },
    ANNOUNCEMENT {
        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public String getFxmlFile() {
            return "../fxml/Announcement/Announcement.fxml";
        }
    },
    EDITUSER {
        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public String getFxmlFile() {
            return "../fxml/EditUser/EditUser.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
