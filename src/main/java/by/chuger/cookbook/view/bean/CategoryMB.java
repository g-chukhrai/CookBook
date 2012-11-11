package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.Category;
import by.chuger.cookbook.model.domain.Product;
import by.chuger.cookbook.model.domain.Recipe;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import resources.Text;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

enum ViewType {

    viewNewest, viewBest
}

@Component
@Named("categoryMB")
@Scope("view")
public class CategoryMB implements Serializable {

    @Autowired
    private Facade facade;
    private Category category;
    private Integer categoryId;
    private Collection<Recipe> recipes;
    private ViewType state = ViewType.viewNewest;
    private static final Integer MAX_STRING_LENGTH = 100;
    private static MenuModel newestBreadModel;
    private static MenuModel categoryBreadModel;
    private static MenuModel bestBreadModel;
    private static MenuItem homeItem;

    private void initRecipes() {
//        if (state == ViewState.viewCategory) {
//            if (category != null) {
//                Collection<Recipe> recipesSet = category.getRecipes();
//                recipes = new ArrayList<Recipe>(recipesSet);
//            }
//        } else
        if (state == ViewType.viewNewest) {
            Collection<Recipe> recipesSet = facade.getRecipesOrderByDate();
            recipes = new ArrayList<Recipe>(recipesSet);
        } else if (state == ViewType.viewBest) {
            Collection<Recipe> recipesSet = facade.getRecipesOrderByAvgMark();
            recipes = new ArrayList<Recipe>(recipesSet);
        }
    }

    private MenuModel getBestBreadModel() {
        if (bestBreadModel == null) {
            bestBreadModel = new DefaultMenuModel();
            bestBreadModel.addMenuItem(getHomeItem());
            MenuItem menuItem = new MenuItem();
            menuItem.setValue(Text.getMessage("menu.best"));
            menuItem.setUrl("/best");
            menuItem.setId("best");
            bestBreadModel.addMenuItem(menuItem);
        }
        return bestBreadModel;
    }

    private MenuModel getCategoryBreadModel() {
        if (categoryBreadModel == null) {
            categoryBreadModel = new DefaultMenuModel();
            categoryBreadModel.addMenuItem(getHomeItem());
            if (category != null) {
                MenuItem menuItem = new MenuItem();
                menuItem.setValue(category.getName());
                menuItem.setUrl("/category/" + category.getId());
                menuItem.setId("category");
                categoryBreadModel.addMenuItem(menuItem);
            }
        }
        return categoryBreadModel;
    }

    private MenuModel getNewestBreadModel() {
        newestBreadModel = new DefaultMenuModel();
        newestBreadModel.addMenuItem(getHomeItem());
        MenuItem menuItem = new MenuItem();
        menuItem.setValue(Text.getMessage("menu.all"));
        menuItem.setUrl("/newest");
        menuItem.setId("newest");
        newestBreadModel.addMenuItem(menuItem);
        return newestBreadModel;
    }

    private MenuItem getHomeItem() {
        if (homeItem == null) {
            homeItem = new MenuItem();
            homeItem.setValue("Home");
            homeItem.setUrl("/");
            homeItem.setId("homeItem");
        }
        return homeItem;
    }

    public Collection<Recipe> getRecipes() {
        return recipes;
    }

    public void viewNewest() {
        state = ViewType.viewNewest;
        initRecipes();
    }

//    public void viewCategory() {
//        state = ViewState.viewCategory;
//        initRecipes();
//    }
    public void viewBest() {
        state = ViewType.viewBest;
        initRecipes();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public CategoryMB() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        category = facade.getCategoryByID(categoryId);
    }

    public MenuModel getBreadCrumbModel() {
//        if (state == ViewState.viewCategory) {
//            return getCategoryBreadModel();
//        } else
        if (state == ViewType.viewNewest) {
            return getNewestBreadModel();
        } else if (state == ViewType.viewBest) {
            return getBestBreadModel();
        }
        return null;
    }

    public String cutString(String string) {
        if (Text.isNotEmpty(string)) {
            string = string.replaceAll("<(.)+?>", "");
            string = string.replaceAll("<(\n)+?>", "");
            if (string.length() > MAX_STRING_LENGTH) {
                string = string.substring(0, MAX_STRING_LENGTH) + "...";
            }
        }
        return string;
    }

    public Collection<String> getIngridients(final Recipe recipe) {
        if (recipe != null) {
            Collection<String> ingridients = new ArrayList<String>();
            for (Product product : recipe.getProducts()) {
                String ingridient = product.getIngridient().getName();
                ingridients.add(ingridient);
            }
            return ingridients;
        }
        return null;
    }
}
