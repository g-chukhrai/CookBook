package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.Recipe;
import by.chuger.cookbook.model.domain.RecipeMark;
import by.chuger.cookbook.model.domain.UserAccount;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Named("userMB")
@Scope("view")
public class UserMB implements Serializable {

    @Autowired
    private Facade facade;
    private String username;
    private UserAccount userAccount;
    private static MenuModel breadModel;
    private Collection<Recipe> userRecipes;
    private CartesianChartModel lineChart;
    private ChartSeries marks;
    private List<RecipeMark> userMarkedRecipes;

    public CartesianChartModel getLineChart() {
        return lineChart;
    }

    public Collection<Recipe> getUserRecipes() {
        return userRecipes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        if (username != null) {
            userAccount = facade.getUserAccountByName(username);
        }
        init();
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public MenuModel getBreadModel() {
        return breadModel;
    }

    private void init() {
        initBread();
        initUserRecipes();
        initLineChart();
    }

    private void initUserRecipes() {
        if (userRecipes == null && userAccount != null) {
            Integer id = userAccount.getId();
            userRecipes = facade.getRecipesByUser(id);
        }
    }

    private void initBread() {
        if (breadModel == null) {
            breadModel = new DefaultMenuModel();
            MenuItem menuItem = new MenuItem();
            menuItem.setValue("Home");
            menuItem.setUrl("/");
            menuItem.setId("home");
            breadModel.addMenuItem(menuItem);
            menuItem = new MenuItem();
            menuItem.setValue("Пользователи");
            menuItem.setUrl("/users");
            menuItem.setId("users");
            breadModel.addMenuItem(menuItem);
            if (userAccount != null) {
                menuItem = new MenuItem();
                menuItem.setValue(userAccount.getUsername());
                menuItem.setUrl("/user/" + userAccount.getUsername());
                menuItem.setId("userAccount");
                breadModel.addMenuItem(menuItem);
            }
        }
    }

    private void initLineChart() {
        if (lineChart == null && userAccount != null) {
            Integer id = userAccount.getId();
            userMarkedRecipes = new ArrayList<RecipeMark>(facade.getRecipeMarkByUser(id));
            lineChart = new CartesianChartModel();
            marks = new ChartSeries();
            marks.setLabel("");

            for (RecipeMark rm : userMarkedRecipes) {
                marks.set(rm.getRecipe().getName(), rm.getMark().getId());
            }
            lineChart.addSeries(marks);
        }
    }

    public void showRecipeMark(ItemSelectEvent e) {
        try {
            int itemIndex = e.getItemIndex();
            RecipeMark rm = userMarkedRecipes.get(itemIndex);
            String redirectUrl = MessageFormat.format("/CookBook/category/{0}/recipe/{1}", rm.getRecipe().getCategory().getId(), rm.getRecipe().getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect(redirectUrl);
        } catch (Exception ex) {
            //TODO:LOG
        }

    }
}
