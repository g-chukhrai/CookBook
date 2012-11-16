package by.chuger.cookbook.model.dao;

import by.chuger.cookbook.model.domain.*;

import java.util.Collection;

public interface Facade {

    void createCategory(final Category entity);

    void createRecipe(final Recipe entity);

    void createFeedback(final Feedback feedback);

    Category getCategoryByID(final Integer id);

    Recipe getRecipeByID(final Integer id);

    Collection<Category> getAllCategory();

    Collection<Recipe> getAllRecipe();

    void deleteRecipe(final Integer id);

    void deleteRecipeMark(final Integer recipeMarkId);

    Collection<String> getIngridientsByName(final String query);

    Collection<String> getAmountsByName(final String queryString);

    Ingridient getIngridientByName(final String ingridientName);

    Amount getAmountByName(final String amountName);

    Category getCategoryByName(final String categoryName);

    Recipe getRecipeByName(final String recipeName);

    void createUser(final UserAccount user);

    UserAccount getUserAccountByName(final String userName);

    UserAuthority getUserAuthorityById(final Integer id);

    Mark getMarkById(final Double markId);

    void saveOrUpdateRecipeMark(final RecipeMark recipeMark);

    RecipeMark getRecipeMarkByRecipeAndUser(final Integer userId, final Integer recipeId);

    Collection<Recipe> getRecipesOrderByDate();

    Collection<Recipe> getRecipesOrderByAvgMark();

    Collection<Recipe> getRecipeByStringAndIngridients(final String searchString, final Collection<String> ingridients);

    Collection<Recipe> getRecipesByUser(final Integer id);

    Collection<RecipeMark> getRecipeMarkByUser(final Integer id);

    Double getAvgMarkByRecipe(final Recipe recipeToCalculate);

    Collection<Recipe> getRecipesLazy(int first, int pageSize, int categoryId);

    int getRecipeCountInCategory(int categoryId);


}
