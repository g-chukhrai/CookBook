package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.Category;
import by.chuger.cookbook.model.domain.Product;
import by.chuger.cookbook.model.domain.Recipe;
import by.chuger.cookbook.utils.MessageUtils;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.MenuModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

enum ViewState {

    viewNewest, viewCategory, viewBest
}

@Component
@Named("categoryLazyMB")
@Scope("view")
public class CategoryLazyMB implements Serializable {

    @Autowired
    private Facade facade;
    private Category category;
    private Integer categoryId;
    private LazyDataModel<Recipe> recipes;
    private static final Integer MAX_STRING_LENGTH = 100;
    private static MenuModel categoryBreadModel;
    private static MenuItem homeItem;

    public CategoryLazyMB() {
        initRecipes();
    }

    private void initRecipes() {
        if (category != null) {
            final Integer catId = category.getId();
            recipes = new LazyDataModel<Recipe>() {
                @Override
                public List<Recipe> load(int first, int pageSize, String s, SortOrder sortOrder, Map<String, String> filters) {
                    return new ArrayList<Recipe>(facade.getRecipesLazy(first, pageSize, catId));
                }

            };
            recipes.setRowCount(facade.getRecipeCountInCategory(catId));
            recipes.setPageSize(10);
        }
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

    private MenuItem getHomeItem() {
        if (homeItem == null) {
            homeItem = new MenuItem();
            homeItem.setValue("Home");
            homeItem.setUrl("/");
            homeItem.setId("homeItem");
        }
        return homeItem;
    }

    public LazyDataModel<Recipe> getRecipes() {
        return recipes;
    }

    public Integer getCategoryId() {
        return categoryId;
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
        initRecipes();
    }

    public MenuModel getBreadCrumbModel() {
        return getCategoryBreadModel();
    }

    public String cutString(String string) {
        if (MessageUtils.isNotEmpty(string)) {
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
