package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.Category;
import by.chuger.cookbook.model.domain.Recipe;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import resources.Text;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Named("sessionMB")
@Scope("session")
public class SessionMB implements Serializable {

    @Autowired
    private Facade facade;
    private static MenuModel model;
    private static final String[] months =
            new String[]{"янв", "фев", "март", "апр", "май", "июнь", "июль", "авг", "сент", "окт", "нояб", "дек"};

    public SessionMB() {
    }

    @PostConstruct
    void init() {
        model = new DefaultMenuModel();
        Collection<Category> mainCategoryList = facade.getAllCategory();
        for (Category cat : mainCategoryList) {
            MenuItem menuItem = new MenuItem();
            menuItem.setValue(cat.getName());
            menuItem.setUrl("/category/" + cat.getId());
            model.addMenuItem(menuItem);
        }
    }

    public MenuModel getModel() {
        return model;
    }

    public String convertDateMonth(Date date) {
        if (date == null) {
            return null;
        }
        int month = Integer.valueOf(new SimpleDateFormat("MM").format(date));
        if (month >= 1 && month <= 12) {
            return months[month - 1];
        }
        return null;
    }

    public String convertDateDay(Date date) {
        return date != null ? new SimpleDateFormat("dd").format(date) : "";
    }

    public String imagePath(Recipe r) {
        String path = null;
        if (r != null) {
            String imagesNames = r.getImages();
            if (Text.isNotEmpty(imagesNames)) {
                List<String> asList = Arrays.asList(imagesNames.split(";"));
                path = asList.get(0);
            }
        }
        return path;
    }

    public String dateToString(Date date) {
        return new SimpleDateFormat("dd.MM.yy").format(date);
    }
}
