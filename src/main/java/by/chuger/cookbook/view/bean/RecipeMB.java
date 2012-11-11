package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.*;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.event.RateEvent;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.IOException;
import java.util.*;

@Component
@Named("recipeMB")
@Scope("view")
public class RecipeMB {

    @Autowired
    private Facade facade;
    private Category category;
    private Recipe recipe;
    private List<Product> products;
    private Collection<String> images;
    private Integer categoryId;
    private Integer recipeId;
    private MenuModel breadModel;
    private UserAccount userAccount;

    public UserAccount getUserAccount() {
        return userAccount;
    }
    private RecipeMark mark;

    public RecipeMB() {
    }

    public void init() {
        initBread();
        initUserAccount();
        initProducts();
        initMark();
        loadSubData();
    }

    private void initBread() {
        if (breadModel == null) {
            breadModel = new DefaultMenuModel();
            MenuItem menuItem = new MenuItem();
            menuItem.setValue("Home");
            menuItem.setUrl("/");
            breadModel.addMenuItem(menuItem);
            if (category != null) {
                menuItem = new MenuItem();
                menuItem.setValue(category.getName());
                menuItem.setUrl("/category/" + category.getId());
                breadModel.addMenuItem(menuItem);
            }
            if (recipe != null) {
                menuItem = new MenuItem();
                menuItem.setValue(recipe.getName());
                menuItem.setUrl("/category/" + category.getId() + "/recipe/" + recipe.getId());
                breadModel.addMenuItem(menuItem);
            }
        }
    }

    private void initUserAccount() {
        if (userAccount == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails == false) {
                userAccount = null;
            } else {
                UserDetails userDetails = (UserDetails) principal;
                userAccount = facade.getUserAccountByName(userDetails.getUsername());
            }
        }
    }

    private void initProducts() {
        if (products == null && recipe != null) {
            products = new ArrayList();
            for (Product product : recipe.getProducts()) {
                products.add(product);
            }
        }
    }

    private void initMark() {
        if (mark == null && userAccount != null && recipe != null) {
            updateMark();
        }
    }

    private void updateMark() {
        for (RecipeMark recipeMark : recipe.getRecipeMarks()) {
            if (recipeMark.getUserAccount().getId().equals(userAccount.getId())) {
                mark = recipeMark;
            }
        }
    }

    private void loadSubData() {
        if (recipe != null) {
            images = null;
            String imagesNames = recipe.getImages();
            if (imagesNames != null) {
                images = Arrays.asList(imagesNames.split(";"));
            }
        }
    }

    public Category getCategory() {
        return category;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public MenuModel getRecipeCrumbModel() {
        return breadModel;
    }

    public void handleRate(RateEvent rateEvent) {
        if (recipe != null && userAccount != null) {
            RecipeMark recipeMark = getUserRecipeMark();
            Double rating = (Double) rateEvent.getRating();
            if (recipeMark != null) {
                if (rating == null || rating.equals(new Double(0))) {
                    recipe.getRecipeMarks().remove(recipeMark);
                    facade.deleteRecipeMark(recipeMark.getId());
                    return;
                }
                Double oldValue = recipeMark.getMark().getValue();
                if (oldValue.equals(rating)) {
                    return;
                }
            } else {
                recipeMark = new RecipeMark();
                recipeMark.setRecipe(recipe);
                recipeMark.setUserAccount(userAccount);
            }

            recipeMark.setDateAdded(Calendar.getInstance().getTime());
            recipeMark.setMark(facade.getMarkById(rating));
            facade.saveOrUpdateRecipeMark(recipeMark);
        }
        updateMark();
    }

    public RecipeMark getUserRecipeMark() {
        return mark;
    }

    public List<Product> getProducts() {

        return products;
    }

    public Collection<String> getImages() {
        return images;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
        recipe = facade.getRecipeByID(recipeId);
        init();
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        category = facade.getCategoryByID(categoryId);
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void deleteRecipe(ActionEvent e) {
        try {
            facade.deleteRecipe(recipe.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/CookBook");
        } catch (IOException ex) {
        } 
    }
}
